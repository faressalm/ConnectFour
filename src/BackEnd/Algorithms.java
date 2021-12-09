package BackEnd;


import java.util.ArrayList;
import java.util.Arrays;


public class Algorithms {
    public Long human;
    public Long agent;
    public int humanScore;
    public int agentScore;
    public board manipulation;
    private final int maxDepth;
    private boolean pruning;
    private String[] tree;

    public Algorithms(int maxDepth, boolean pruning) {
        this.maxDepth = maxDepth;
        manipulation = new board();
        human = 0L;
        agent = 0L;
        this.pruning = pruning;
        tree = new String[maxDepth + 1];
        Arrays.fill(tree, "");
    }

    public int minimax() {
        int col;
        tree = new String[maxDepth + 1];
        Arrays.fill(tree, "");
        if (pruning) {
            Integer alpha = Integer.MIN_VALUE;
            Integer beta = Integer.MAX_VALUE;
            col = minimaxPruning(alpha, beta, this.human, this.agent, 0, true)[1];
        } else
            col = minimax(this.human, this.agent, 0, true)[1];
        updateAgent(col);
        return 6 - col;
    }

    ArrayList<Integer> getNext(Long board) {
        ArrayList<Integer> cols = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            if (manipulation.isValid(i, board)) {
                cols.add(i);
            }
        }
        return cols;
    }

    private int count(Long player, int piles) {
        int cnt = 0;
        Boolean[][] arr = manipulation.get2D(player);
        int adj = 0;
        int[] diagP = new int[12], diagN = new int[12];
        Arrays.fill(diagN, 0);
        Arrays.fill(diagP, 0);
        /* Get Rows counts  && set Diagonals counts */
        for (int i = 0; i < 6; i++) {
            adj = 0;
            for (int j = 0; j < 7; j++) {
                if (arr[i][j]) {
                    diagN[i + j]++;
                    diagP[5 + (j - i)]++;
                    adj++;
                    if (diagN[i + j] == piles) cnt++;
                    if (diagP[5 + (j - i)] == piles) cnt++;
                    if (adj == piles) cnt++;
                } else {
                    diagN[i + j] = 0;
                    diagP[5 + (j - i)] = 0;
                    adj = 0;

                }
            }
        }
        /*  Get Cols */
        for (int i = 0; i < 7; i++) {
            adj = 0;
            for (int j = 0; j < 6; j++) {
                if (!arr[j][i]) adj = 0;
                else {
                    adj++;
                    if (adj == piles) cnt++;
                }
            }
        }
        return cnt;
    }

    private int destroy(Long player) {
        player = (~(player)) & (4398046511103L);
        return 69 - manipulation.getScore(player);
    }

    private int getHeuristic(Long human, Long agent) {
        int agentFeatures = 0, humanFeatures = 0;
        int value = 10;
        int humanCnt, agentCnt;
        for (int piles = 2; piles < 8; piles++) {
            humanCnt = count(human, piles);
            agentCnt = count(agent, piles);
            humanFeatures += value * humanCnt;
            agentFeatures += value * agentCnt;
            value *= 10;
        }
        agentFeatures += destroy(agent) * 1000;
        humanFeatures += destroy(human) * 1000;
        return agentFeatures - humanFeatures;
    }

    private int[] minimax(Long human, Long agent, int depth, boolean maximizingPlayer) {
        ArrayList<Integer> cols = getNext(agent | human);
        int features, col = 0;
        Long board = human | agent;
        int[] arr;
        if (cols.size() == 0 || depth == maxDepth) {
            features = getHeuristic(human, agent);
            return new int[]{features, 0};
        }
        if (maximizingPlayer) { // max
            features = Integer.MIN_VALUE;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimax(human, manipulation.addSpecific(board, agent, cols.get(i)), depth + 1, false);
                if (arr[0] > features) {
                    features = arr[0];
                    col = cols.get(i);
                }
                updateTree(depth, cols.get(i), arr[0]);
            }
        } else { // min
            features = Integer.MAX_VALUE;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimax(manipulation.addSpecific(board, human, cols.get(i)), agent, depth + 1, true);
                if (arr[0] < features) {
                    features = arr[0];
                    col = cols.get(i);
                }
                updateTree(depth, cols.get(i), arr[0]);
            }
        }
        if (depth == 0) tree[depth] += "( " + col + " , " + features + " )";
        return new int[]{features, col};
    }

    private int[] minimaxPruning(Integer alpha, Integer beta, Long human, Long agent, int depth, boolean maximizingPlayer) {
        ArrayList<Integer> cols = getNext(agent | human);
        int features, col = 0;
        Long board = human | agent;
        int[] arr;
        if (cols.size() == 0 || depth == maxDepth) {
            features = getHeuristic(human, agent);
            return new int[]{features, 0};
        }
        if (maximizingPlayer) { // max
            features = Integer.MIN_VALUE;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimaxPruning(alpha, beta, human, manipulation.addSpecific(board, agent, cols.get(i)), depth + 1, false);
                if (arr[0] > features) {
                    features = arr[0];
                    col = cols.get(i);
                }
                updateTree(depth, cols.get(i), arr[0]);
                alpha = Math.max(alpha, arr[0]);
                if (alpha >= beta) break;
            }
        } else { // min
            features = Integer.MAX_VALUE;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimaxPruning(alpha, beta, manipulation.addSpecific(board, human, cols.get(i)), agent, depth + 1, true);
                if (arr[0] < features) {
                    features = arr[0];
                    col = cols.get(i);
                }
                updateTree(depth, cols.get(i), arr[0]);
                beta = Math.min(beta, arr[0]);
                if (alpha >= beta) break;
            }
        }
        if (depth == 0) tree[depth] += "( " + col + " , " + features + " )";
        return new int[]{features, col};
    }

    /**
     * Set the human move
     *
     * @param col column to insert the human pile in.
     */
    public void updateHuman(int col) {
        col = 6 - col;
        if (!manipulation.isValid(col, human | agent)) {
            System.out.println("Not Valid");
        }
        //System.out.println(col);
        int row = manipulation.getRowOrder(human | agent, col);
        this.human = human | (1L << (row * 7 + col));
        this.humanScore = manipulation.getScore(human);
    }

    /**
     * Set the agent move after applying minimax algorithm
     *
     * @param col column to insert the agent pile in
     */
    private void updateAgent(int col) {
        if (!manipulation.isValid(col, human | agent)) {
            System.out.println("Not Valid");
        }
        int row = manipulation.getRowOrder(human | agent, col);
        agent = agent | (1L << (row * 7 + col));
        this.agentScore = manipulation.getScore(agent);
    }

    /**
     * Display the MiniMax tree
     */
    public void DisplayTree() {
        System.out.println("***************************  Tree   *****************************************");
        for (int i = 0; i < tree.length; i++) {
            if (!tree[i].contains("(")) break;
            System.out.println("*********************************************************");
            System.out.println(i % 2 == 0 ? "MAX" : " MIN");
            System.out.println(tree[i]);
        }
    }

    private void updateTree(int depth, int col, int features) {
        tree[depth + 1] += "( " + col + " , " + features + " )";
        //if(depth == 0 ) tree[depth + 1] += " || " ;
        if (depth != maxDepth - 1) tree[depth + 2] += " || ";
    }

    /**
     * @return True if game is over,false otherwise
     */
    public boolean gameOver() {
        if ((human | agent) == 4398046511103L) return true;
        return false;
    }
}
