
import java.util.ArrayList;


public class State {
    public int[][] board;  //the usable squares on the board  0 is open, 1 is player 1, -1 is player 2, K is 1 or -1 * 2

    public State(){
        board = new int[8][8];
        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                if(b < 3 && (b + a) % 2 == 0){
                    board[a][b] = 1;
                }
                else if(b > 4 && (b + a)%2 == 0){
                    board[a][b] = -1;
                }
                else{
                    board[a][b] = 0;
                }
            }
        }
    }

    public State copy(){
        State Clone = new State();
        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                Clone.board[a][b] = board[a][b];
            }
        }
        return Clone;
    }

    /**
     * creates a State from an existing one
     * @param state
     * @param peiceToMove
     * @param newPlace
     */
    public State(State state, int A, int B, int code){
        board = new int[8][8];
        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                board[a][b] = state.board[a][b];  //copy it over
            }
        }
        int value = board[A][B];
        board[A][B] = 0;
        switch(code){
            case(0): A --;
                     B ++; break; //step right up
            case(1): A ++;
                     B ++; break; //step right down
            case(2): //jump right up
                     board[A-1][B+1] = 0;  
                     A -= 2;
                     B += 2; break;
            case(3): //jump right down
                     board[A+1][B+1] = 0;
                     A += 2;
                     B += 2; break;
            case(4): A --;
                     B --; break; //step right up
            case(5): A ++;
                     B --; break; //step right down
            case(6): //jump right up
                     board[A-1][B-1] = 0;
                     A -= 2;
                     B -= 2;  break;
            case(7): //jump right down
                     board[A+1][B-1] = 0;
                     A += 2;
                     B -= 2;  break;
            default: break;
        }
        board[A][B] = value;
        if((B == 0 && value == -1) || (B == 7 && value == 1)){  //assign piece value as a king
            board[A][B] *= 2;
        }
    }

    /**
     * returns true if this is an ending position
     * @return
     */
    public boolean gameOver(boolean player1){
        return getMoves(player1).isEmpty();
    }



    /**
     * returns true if the peice at a,b can legally jump
     * @param a
     * @param b
     * @return
     */
    public boolean canJump(int a, int b){
        int peiceType = board[a][b];
        if((peiceType > 0 || peiceType == -2) && (jumpOK(a, b, a-1,b+1) || jumpOK(a, b, a+1,b+1))){  //check jumps in the positive direction
            return true;
        }
        if((peiceType < 0 || peiceType == 2) &&(jumpOK(a, b, a-1,b-1) || jumpOK(a, b, a+1,b-1))){  //check jumps in the negative direction
            return true;
        }
        return false;
    }

    /**
     * returns true if the peice at a,b can legally step
     * @param a
     * @param b
     * @return
     */
    public boolean canStep(int a, int b){
        int peiceType = board[a][b];
        if((peiceType > 0 || peiceType == -2) && (open(a-1,b+1) || open(a+1,b+1))){  //check steps in the positive direction
            return true;
        }
        if((peiceType < 0 || peiceType == 2) &&(open(a-1,b-1) || open(a+1,b-1))){  //check steps in the negative direction
            return true;
        }
        return false;
    }

    // returns the list of states with a possible legal move
    public ArrayList <State> getMoves(boolean player1){
        ArrayList <State> moves = new ArrayList <State> ();

        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                if((player1 && board[a][b] > 0) || (!player1 && board[a][b] < 0)){
                    moves.addAll(getJumpMoves(a, b, board[a][b]));
                }
            }
        }

        if(moves.isEmpty()){  //only add step moves if there are no killing moves
            for(int a = 0; a < board.length; a++){
                for(int b = 0; b < board[0].length; b++){
                    if(player1 && board[a][b] > 0){
                        if(open(a-1, b+1)){  //move right and up is open
                            moves.add(new State(this, a, b, 0));
                        }
                        if(open(a+1, b+1)){  //move right and down is open
                            moves.add(new State(this, a, b, 1));
                        }
                        if(board[a][b] == 2){
                            if(open(a-1, b-1)){  //move left and up is open
                                moves.add(new State(this, a, b, 4));
                            }
                            if(open(a+1, b-1)){  //move leftt and down is open
                                moves.add(new State(this, a, b, 5));
                            }
                        }
                    }
                    if(!player1 && board[a][b] < 0){
                        if(open(a-1, b-1)){  //move left and up is open
                            moves.add(new State(this, a, b, 4));
                        }
                        if(open(a+1, b-1)){  //move leftt and down is open
                            moves.add(new State(this, a, b, 5));
                        }
                        if(board[a][b] == -2){
                            if(open(a-1, b+1)){  //move right and up is open
                                moves.add(new State(this, a, b, 0));
                            }
                            if(open(a+1, b+1)){  //move right and down is open
                                moves.add(new State(this, a, b, 1));
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    /**
     * returns the jump moves for the given piece
     * @param a
     * @param b
     * @return
     */
    public ArrayList <State> getJumpMoves(int a, int b, int peiceType){
        ArrayList <State> moves = new ArrayList <State> ();
        if(peiceType > 0 || peiceType == -2){
            if(jumpOK(a, b, a-1,b+1)){
                State newState = new State(this, a, b, 2);
                //this ensures that as many jumps as possible are made
                ArrayList <State> derivativeJumps = newState.getJumpMoves(a-2,b+2,peiceType);
                if(derivativeJumps.isEmpty()){
                    moves.add(newState);
                }
                else{
                    moves.addAll(derivativeJumps);
                }
            }
            if(jumpOK(a, b, a+1,b+1)){
                State newBP = new State(this, a, b, 3);
                ArrayList <State> derivativeJumps = newBP.getJumpMoves(a+2,b+2,peiceType);
                if(derivativeJumps.isEmpty()){
                    moves.add(newBP);
                }
                else{
                    moves.addAll(derivativeJumps);
                }
//                moves.add(newBP);
//                moves.addAll(newBP.getJumpMoves(a+2,b+2,peiceType));  //add all the jump moves from this jump move
            }
        }
        if(peiceType < 0 || peiceType == 2){
            if(jumpOK(a, b, a-1,b-1)){
                State newBP = new State(this, a, b, 6);
                ArrayList <State> derivativeJumps = newBP.getJumpMoves(a-2,b-2,peiceType);
                if(derivativeJumps.isEmpty()){
                    moves.add(newBP);
                }
                else{
                    moves.addAll(derivativeJumps);
                }
//                moves.add(newBP);
//                moves.addAll(newBP.getJumpMoves(a-2,b-2,peiceType));  //add all the jump moves from this jump move
            }
            if(jumpOK(a, b, a+1,b-1)){
                State newBP = new State(this, a, b, 7);
                ArrayList <State> derivativeJumps = newBP.getJumpMoves(a+2,b-2,peiceType);
                if(derivativeJumps.isEmpty()){
                    moves.add(newBP);
                }
                else{
                    moves.addAll(derivativeJumps);
                }
//                moves.add(newBP);
//                moves.addAll(newBP.getJumpMoves(a+2,b-2, peiceType));  //add all the jump moves from this jump move
            }
        }

        return moves;
    }

    /**
     * returns a list of coordinates the given peice can step to
     * NOTE: this does not check if the peice has a legal jump
     * @return
     */
    public ArrayList <int[]> getStepMoves(int a, int b){
        ArrayList <int[]> stepMoves = new ArrayList <int[]> ();
        int peiceType = board[a][b];
        if(peiceType > 0 || peiceType == -2){
            if(open(a-1,b+1)){
                stepMoves.add(new int[]{a-1,b+1});
            }
            if(open(a+1,b+1)){
                stepMoves.add(new int[]{a+1,b+1});
            }
        }
        if(peiceType < 0 || peiceType == 2){
            if(open(a-1,b-1)){
                stepMoves.add(new int[]{a-1,b-1});
            }
            if(open(a+1,b-1)){
                stepMoves.add(new int[]{a+1,b-1});
            }
        }
        return stepMoves;
    }

    /**
     * returns a list of coordinates the given peice can step to
     * NOTE: this does not check if the peice has a legal jump
     * @return
     */
    public ArrayList <int[]> getJumpMoves(int a, int b){
        ArrayList <int[]> jumpMoves = new ArrayList <int[]> ();
        int peiceType = board[a][b];
        if(peiceType > 0 || peiceType == -2){  //if it needs to move forward
            if(jumpOK(a, b, a-1,b+1)){
                jumpMoves.add(new int[]{a-2,b+2});
            }
            if(jumpOK(a, b, a+1,b+1)){
                jumpMoves.add(new int[]{a+2,b+2});
            }
        }
        if(peiceType < 0 || peiceType == 2){
            if(jumpOK(a, b, a-1,b-1)){
                jumpMoves.add(new int[]{a-2,b-2});
            }
            if(jumpOK(a, b, a+1,b-1)){
                jumpMoves.add(new int[]{a+2,b-2});
            }
        }
        return jumpMoves;
    }

    /**
     * returns if the given player has available jumps
     * @return
     */
    public boolean jumpsAvailable(boolean player1){
        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                if(((player1 && board[a][b] > 0) || (!player1 && board[a][b] < 0)) && (canJump(a,b))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns true if you can jump from a1, b1, over a2, b2
     * @param a
     * @param b
     * @return
     */
    public boolean jumpOK(int a1, int b1, int a2, int b2){
        return open(2*a2 - a1, 2*b2 - b1) && inBounds(a2, b2) && (Math.signum(board[a2][b2]) == -Math.signum(board[a1][b1]));
    }

    /**
     * true if the given coordinates are open
     * @param a
     * @param b
     * @return
     */
    public boolean open(int a, int b){
        return inBounds(a,b) && board[a][b] == 0;
    }

    public boolean inBounds(int a, int b){
        return a >= 0 && a < board.length && b >= 0 && b < board[0].length;
    }



    public double boardValue(){

        double value = 0;
        int values[][] = {{4, 4, 4, 4, 4, 4, 4, 4},
                          {4, 3, 3, 3, 3, 3, 3, 3},
                          {4, 3, 2, 2, 2, 2, 2, 2},
                          {4, 3, 2, 1, 1, 2, 3, 4},
                          {4, 3, 2, 1, 1, 2, 3, 4},
                          {4, 3, 2, 2, 2, 2, 2, 2},
                          {4, 3, 3, 3, 3, 3, 3, 3},
                          {4, 4, 4, 4, 4, 4, 4, 4}};
        for(int a = 0; a < board.length; a++){
            for(int b = 0; b < board[0].length; b++){
                value += board[a][b] * values[a][b];
            }
        }
        return value;
    }


}
