

import java.util.ArrayList;


public class Checkers {

    public GUICheckers myView;
    public State gameState;
    public Player RED;
    public Player BLACK;
    public boolean turn;  //true if RED is active player
    public static boolean alphabeta = true;

    public Checkers(){
        ArrayList <String> records = new ArrayList <String> ();

        turn = true;
        gameState = new State();
        myView = new GUICheckers(10, 600, 700, this);
        RED = new Computer(gameState, 3, turn);
//        RED = new Human(gameState, turn);
        BLACK = new Computer(gameState, 1, !turn);
//        BLACK = new Human(gameState, !turn);

        
        while(!gameState.gameOver(turn)){ //&& !records.contains(gameState.toString())){
            records.add(gameState.toString());
            gameState = (turn ? RED.getMove() : BLACK.getMove());
            RED.update(gameState, turn);
            BLACK.update(gameState, turn);
            turn = !turn;

        }
        System.out.println("GAME OVER");
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to Checkers");

        Checkers myCheckers = new Checkers();
    }
}
