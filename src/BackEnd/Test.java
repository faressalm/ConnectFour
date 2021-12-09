package BackEnd;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {


        Algorithms b = new Algorithms(5, true);
        Long s, e;
        s = System.currentTimeMillis();
       b.updateHuman(3);
        b.minimax() ;
        b.updateHuman(4);
        b.minimax() ;
        b.updateHuman(2);
        b.minimax() ;
        b.updateHuman(3);
        b.minimax() ;
        b.updateHuman(5);
        b.minimax() ;
        b.updateHuman(6);
        b.minimax() ;
        b.updateHuman(2);
        b.minimax() ;
        b.updateHuman(4);
        b.minimax() ;
        b.updateHuman(0);
        b.minimax() ;
        b.updateHuman(6);
        b.minimax() ;
        b.updateHuman(2);
        b.minimax() ;
        b.updateHuman(2);
        b.minimax() ;
        b.updateHuman(4);
        b.minimax() ;
        b.updateHuman(4);
        b.minimax() ;
        b.updateHuman(0);
        b.minimax() ;
        b.updateHuman(6);
        b.minimax() ;
        b.updateHuman(1);
        b.minimax() ;
        b.updateHuman(1);
        b.minimax() ;
        b.updateHuman(5);
        b.minimax() ;
        b.updateHuman(5);
        b.minimax() ;
        b.DisplayTree();
        b.updateHuman(1);
        b.minimax() ;
        b.DisplayTree();

     System.out.println( "human : " + b.humanScore  );
        System.out.println( "agent : "  + b.agentScore  );
        e = System.currentTimeMillis();
        //System.out.println(e - s);
        //b.updateHuman();
        /*System.out.println("human " + b.humanScore);
        System.out.println("agent " + b.agentScore);*/
        Long avg = 0L ;
        //for(int i = 0 ; i < 100 ; i++ ) {
         /*Algorithms b = new Algorithms(5, false);
         Long s, e;
         s = System.currentTimeMillis();
         b.updateHuman(3);
         b.minimax();
         b.updateHuman(2);
         b.minimax();
         b.updateHuman(4);
         b.minimax();
         b.updateHuman(3);
         b.minimax();
         b.updateHuman(1);
         b.minimax();
         b.updateHuman(5);
         b.minimax();
         b.updateHuman(6);
         b.minimax();
         b.updateHuman(4);
         b.minimax();
         b.updateHuman(5);
         b.minimax();
         b.updateHuman(0);
         b.minimax();
         b.updateHuman(2);
         b.minimax();
         b.updateHuman(2);
         b.minimax();
         b.updateHuman(2);
         b.minimax();
         b.updateHuman(4);
         b.minimax();
         b.updateHuman(0);
         b.minimax();
         b.updateHuman(1);
         b.minimax();
         b.updateHuman(1);
         b.minimax();
         b.updateHuman(1);
         b.minimax();
         b.updateHuman(2);
         b.minimax();
         b.updateHuman(6);
         b.minimax();
         b.updateHuman(6);
         b.minimax();
         e = System.currentTimeMillis();
        // avg += e - s ;
         System.out.println("human " + b.humanScore);
         System.out.println("agent " + b.agentScore);*/
        //}
     /*avg/=100 ;
        System.out.println("**************************************\n" + avg);*/
       /* System.out.println( Arrays.deepToString(b.manipulation.get2D( b.agent)))    ;
        System.out.println( Arrays.deepToString(b.manipulation.get2D(b.human )))    ;
        System.out.println( Arrays.deepToString(b.manipulation.get2D(b.human | b.agent)))    ; */

    }
}
