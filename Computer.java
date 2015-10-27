
import java.awt.event.*;

public class Computer implements Player{

    public CheckersTreeNodes myTree;  //the futuristic structure
    public boolean player1;  //if the computer is player 1
    int depth;

    public Computer(State state, int Depth, boolean player1){
        myTree = new CheckersTreeNodes(state);
        this.player1 = player1;
        this.depth = Depth;
        while(Depth > 0){  //predict future moves
            myTree.generateSubTree(true);  //start it off with the first player's
            Depth --;
        }
    }

    @Override
    public State getMove(){

//        try{Thread.sleep(3000);}catch(Exception e){} //wait for click
        return myTree.getMove(player1).state;  //tell it to become the next node down in its minimax list
    }

    @Override
    public void update(State state, boolean player1){
        
        String currentBoard = state.toString();
        boolean switched = false;
        int a = 0;
        while(a < myTree.children.size() && !switched){
            if((myTree.children.get(a).state.toString()).equals(currentBoard)){  //if we found a match
                myTree = myTree.children.get(a);
                switched = true;
            }
            a++;
        }

        if(!switched){  //if we can't find it in the tree (weird, this shouldn't happen)
//            System.out.println("Could not find move in the Game Tree, creating new tree structure COMP");
//            System.out.println("PRINTING WHAT I THOUGHT THE MOVES WERE");
//            for(CheckersTreeNodes GTN : myTree.children){
//                System.out.println(GTN.state);
//            }
            myTree = new CheckersTreeNodes(state);
            for(int b = 0; b < depth - 1; b++){
                myTree.generateSubTree(!player1);
            }
        }
        myTree.generateSubTree(!player1);
    }

    public void onMousePressed(MouseEvent e){
    }
    public void onMouseReleased(MouseEvent e){}
}
