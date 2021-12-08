package BackEnd;

import java.util.ArrayList;

public class Algorithms {
    public Long human;
    public Long agent;
    public int humanScore;
    public int agentScore;
    public board manipulation;
    private final int maxDepth;
    private boolean pruning;

    public Algorithms(int maxDepth, boolean pruning) {
        this.maxDepth = maxDepth;
        manipulation = new board();
        human = 0L;
        agent = 0L;
        this.pruning = pruning;
    }

    public int minimax() {
        int col;
        if (pruning) {
            Integer alpha = Integer.MIN_VALUE;
            Integer beta = Integer.MAX_VALUE;
            col = minimaxPruning(alpha, alpha, beta, beta, this.human, this.agent, maxDepth, true)[2];
        } else
            col = minimax(this.human, this.agent, maxDepth, true)[2];
        System.out.println(col);
        updateAgent(col);
        return 6 - col;
    }

    ArrayList<Integer> getNext(Long board) {
        ArrayList<Integer> cols = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (manipulation.isValid(i, board)) {
                cols.add(i);
            }
        }
        return cols;
    }

    private int[] getHeuristic(Long human, Long agent) {
        Long avaHuman = 4398046511103L, avaAgent = 4398046511103L;
        avaAgent = avaAgent ^ human;
        avaHuman = avaHuman ^ agent;
        // Long emptyAgent = avaAgent & ~(agent) , emptyHuman = avaHuman & ~(human) ;
        return new int[] { manipulation.getScore(agent) - manipulation.getScore(human),
                manipulation.getScore(avaAgent) - manipulation.getScore(avaHuman) };
    }

    private int[] minimax(Long human, Long agent, int depth, boolean maximizingPlayer) {
        ArrayList<Integer> cols = getNext(agent | human);
        int maxDiff, maxScore, col = 0;
        Long board = human | agent;
        int[] arr;
        if (cols.size() == 0 || depth == 0) {
            int[] score = getHeuristic(human, agent);
            return new int[] { score[0], score[1], 0 };
        }
        if (maximizingPlayer) { // max
            maxDiff = -1000;
            maxScore = -1000;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimax(human, manipulation.addSpecific(board, agent, cols.get(i)), depth - 1, false);
                if ((arr[0] > maxScore) || (arr[0] == maxScore && maxDiff < arr[1])) {
                    maxScore = arr[0];
                    maxDiff = arr[1];
                    col = cols.get(i);
                }
            }
        } else { // min
            maxDiff = 1000;
            maxScore = 1000;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimax(manipulation.addSpecific(board, human, cols.get(i)), agent, depth - 1, true);
                if ((arr[0] < maxScore) || (arr[0] == maxScore && maxDiff > arr[1])) {
                    maxScore = arr[0];
                    maxDiff = arr[1];
                    col = cols.get(i);
                }
            }
        }
        return new int[] { maxDiff, maxScore, col };
    }

    private int[] minimaxPruning(Integer alpha0, Integer alpha1, Integer beta0, Integer beta1, Long human, Long agent,
            int depth, boolean maximizingPlayer) {
        ArrayList<Integer> cols = getNext(agent | human);
        int maxDiff, maxScore, col = 0;
        Long board = human | agent;
        int[] arr;
        if (cols.size() == 0 || depth == 0) {
            int[] score = getHeuristic(human, agent);
            return new int[] { score[0], score[1], 0 };
        }
        if (maximizingPlayer) { // max
            maxDiff = -1000;
            maxScore = -1000;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimaxPruning(alpha0, alpha1, beta0, beta1, human,
                        manipulation.addSpecific(board, agent, cols.get(i)), depth - 1, false);
                if ((arr[0] > maxScore) || (arr[0] == maxScore && maxDiff < arr[1])) {
                    maxScore = arr[0];
                    maxDiff = arr[1];
                    col = cols.get(i);
                }
                if (alpha0 < maxScore || (alpha0 == maxScore && alpha1 < maxDiff)) {
                    alpha0 = maxScore;
                    alpha1 = maxDiff;
                }
                if (alpha0 > beta0 || (alpha0 == beta0 && alpha1 >= beta1))
                    break;
            }
        } else { // min
            maxDiff = 1000;
            maxScore = 1000;
            for (int i = 0; i < cols.size(); i++) {
                arr = minimaxPruning(alpha0, alpha1, beta0, beta1, manipulation.addSpecific(board, human, cols.get(i)),
                        agent, depth - 1, true);
                if ((arr[0] < maxScore) || (arr[0] == maxScore && maxDiff > arr[1])) {
                    maxScore = arr[0];
                    maxDiff = arr[1];
                    col = cols.get(i);
                }
                if (beta0 > maxScore || (beta0 == maxScore && beta1 > maxDiff)) {
                    beta0 = maxScore;
                    beta1 = maxDiff;
                }
                if (alpha0 > beta0 || (alpha0 == beta0 && alpha1 >= beta1))
                    break;
            }
        }
        return new int[] { maxDiff, maxScore, col };
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
        // System.out.println(col);
        int row = manipulation.getRowOrder(human | agent, col);
        this.human = human | (1L << (row * 7 + col));
        this.humanScore = manipulation.getScore(human);
    }

    /**
     * Set the agent move after applying minimax algorithm
     * 
     * @param col column to insert the agent pile in
     */
    public void updateAgent(int col) {
        if (!manipulation.isValid(col, human | agent)) {
            System.out.println("Not Valid");
        }
        int row = manipulation.getRowOrder(human | agent, col);
        agent = agent | (1L << (row * 7 + col));
        this.agentScore = manipulation.getScore(agent);
    }

}
