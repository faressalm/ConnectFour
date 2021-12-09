package BackEnd;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class board {
    /**
     * Check if inserting a tile into a column is a valid or not.
     * @param col col to be checked
     * @return true if it is valid, false otherwise
     */
    public boolean  isValid(int col, Long board ){
        int mask = 5 * 7 + col ;
        board >>= mask ;
        if(    ( 1L & board ) == 1L   ){
            return  false ;
        }
        return true ;
    }

    /**
     * Get the order of the row to be filled
     * @param board the state of the board
     * @param col col to be inserted in
     * @return  the row
     */
    public int getRowOrder(Long board , int col ){
        for(int i = 0 ; i < 6 ; i ++){
            if(   (  (1L << (i * 7 + col ) ) & board ) == 0L ) return  i ;
        }
        return 0 ;
    }
    /**
     *
     * @param board board of the game (human | agent )
     * @param player player to add move to
     * @param col col to insert pile in
     * @return an updated board for the player
     */
    public  Long addSpecific(Long board, Long player  , int col  ){
        if(!isValid( col , board) ) {
            System.out.println("Not Valid");
        }
        int row = getRowOrder(board , col ) ;
        player = player | (1L << (row * 7 + col ) ) ;
        return player  ;
    }
    public Long remove(Long board , int col , int row ){
        /* Assume  row is correct and adjusts before */
        board = board &  ~(  (1L << ((row * 7) + col)  ) )  ;
        return board ;
    }
    /*
    long board        | (1) at bit  << ( row[col] * 7 ) + col
                                     row[col]++;
                     6543210
                   5 0000000
                   4 0000000
                   3 0000000  >  / 4
                   2 0000000
                   1 0000100
                   0 1000001        N://(i+j)    P:\\( 5 + j - i )

                   1000001

         board = (agent | human )
                   0
                   7
                   14
                   21
                   28
                   35
     */
    /**
     * get 2D array from the board
     * @param board board of the player/game
     * @return Boolean 2D array of the set positions as true
     */
    public Boolean[][] get2D(Long board ){
        Boolean[][] arr = new Boolean[6][7] ;
        for(int i = 5 ; i >= 0 ; i--) {
            for (int j = 6 ; j >= 0; j--) {
                arr[i][j] = (board % 2L != 0L);
                board /= 2L;
            }
        }
        return arr ;
    }

    /**
     * @param board board of the agent / human.
     * @return score of the agent / human.
     */
    int getScore(Long board ){
        int score = 0 ;
        Boolean [][] arr = get2D(board) ;
        int cnt = 0 , best ;
        int [] diagP = new int[12],diagN = new int [12];
        Arrays.fill(diagN , 0 );
        Arrays.fill(diagP , 0 );
        /* Get Rows Score  && set Diagonals ones*/
        Set<Integer> diagfilledN  = new HashSet<Integer>(), diagfilledP = new HashSet<Integer>() ;
        for(int i = 0 ; i < 6 ; i ++){
            best = cnt = 0;
            for(int j = 0 ; j  < 7 ; j ++){
                if(arr[i][j] ){
                    if(!diagfilledN.contains(i+j))
                        diagN[i+j]++ ;
                    if(!diagfilledP.contains( 5 + j - i ) )
                        diagP[5 + (j - i)]++ ;
                }
                else {
                    if( diagN[i+j] < 3  ){
                        diagN[i+j] = 0 ;
                    }
                    else {
                        diagfilledN.add(i + j) ;
                    }
                    if( diagP[5 + (j - i)] < 3  ){
                        diagP[5 + (j - i)] = 0 ;
                    }
                    else {
                        diagfilledP.add(5 + (j - i)) ;
                    }
                }
                if(!arr[i][j]) cnt = 0 ;
                else cnt++ ;
                best = Math.max(cnt , best) ;
            }
            if ( best > 3 ) score += (best - 4 + 1 ) ;
        }
        /*  Get Cols */
        for(int i = 0 ; i < 7 ; i ++){
            best = cnt = 0;
            for(int j = 0 ; j  < 6 ; j ++){
                if(!arr[j][i]) cnt = 0 ;
                else cnt++ ;
                best = Math.max(cnt , best) ;
            }
            if ( best > 3 ) score += (best - 4 + 1 ) ;
        }
        /*   Get Diagonals  */
        for(int i = 0 ; i < 12 ; i ++) {
            score += Math.max(0, diagN[i] - 4 + 1);
            score += Math.max(0, diagP[i] - 4 + 1);
        }
        return score ;
    }

}
