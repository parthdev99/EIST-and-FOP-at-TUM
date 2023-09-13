package de.tum.in.ase.fop;

public class Main {
    public static void main(String[] args){
       // Player player1=new Player("parth");
        //Player player2=new Player("parth2");
        Player[] players=new Player[2];
        players[0]=new Player("parth");
        players[1]=new Player("parth2");
        ScrabbleBoard s1=new ScrabbleBoard(6,6);
        StandardLetterBag l1=new StandardLetterBag();
        Scrabble sb1=new Scrabble(l1,s1,players);
        sb1.play();

        /*
        ScrabbleBoard board = new ScrabbleBoard(10, 10);
        board.print();
        board.layWordAt(1, 1, "Java", true, player1);
        board.print();
        board.layWordAt(2, 1, "Artemis", false, player2);
        board.print();
        */
       /* p1.printHand();
        String sss1=InputReader.readString("enter");
        s1.print();
        System.out.println(s1.layWordAt(
                2,1,sss1,false,p1
        ));
        s1.print();
        p1.refillHand(l1);
        p1.printHand();
        String sss2=InputReader.readString("enter");
        int c=InputReader.readInt("enter C");
        int r=InputReader.readInt("enter r");
        System.out.println(s1.layWordAt(
                c,r,sss2,true,p1
        ));
        s1.print();*/



    }
}
