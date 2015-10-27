import java.util.ArrayList;


public class CheckersTreeNodes {
	
    public ArrayList <CheckersTreeNodes> children;  //possible moves
    public State state;  //state of the board
    public double alpha = Double.MIN_VALUE;
    public double beta  = Double.MAX_VALUE;

    public CheckersTreeNodes(State state){
        children = new ArrayList <CheckersTreeNodes> ();
        this.state = state;
    }

    /**
     * moves through the tree until it reaches the end, then it generates a new tree of possible moves
     * @param player1  //who is in control at this level
     */
    public void generateSubTree(boolean player1){
        for(CheckersTreeNodes GTN : children){
            GTN.generateSubTree(!player1);
        }

        if(children.isEmpty()){
            ArrayList <State> moves = state.getMoves(player1);
            for(State b : moves){
                children.add(new CheckersTreeNodes(b));  //add them all to the list
            }
        }
    }


     // returns the value best option for computer
    
    public double minimax(boolean player1){
    	double option = player1? Double.MIN_VALUE : Double.MAX_VALUE; //this will be the choice the minimax algorithm will choose
        if(children.isEmpty()){  
            return state.boardValue();
        }

        else if(player1){  //if player 1, choose maximum value choice
            for(CheckersTreeNodes GTN : children){  //cycle through all of the possible states
                option = Math.max(option, GTN.minimax(!player1));
            }
            return option;
        }
        else{   //if it is player 2, we minimize the score
            for(CheckersTreeNodes GTN : children){  //cycle through all of the possible resulting moves
                option = Math.min(option, GTN.minimax(!player1));
            }
            return option;
        }
    }
    
    public double alphabeta(boolean player1){
    	if (Checkers.alphabeta){
    		double option = player1? Double.MIN_VALUE : Double.MAX_VALUE; //this will be the choice the minimax algorithm will choose
    		if(children.isEmpty()){ 
    			if (player1)
    				alpha = state.boardValue();
    			else 
    				beta = state.boardValue();
    			return state.boardValue();
    		}

    		else if(player1){  //if player 1, choose maximum value choice
    			for(CheckersTreeNodes GTN : children){
    				if (GTN.alphabeta(player1) < beta)
    					option = Math.max(option, GTN.alphabeta(!player1)); //we will use minimax to get to the next value
    			}
    			return option;
    		}
    		else{   //if it is player 2, we minimize the score
    			for(CheckersTreeNodes GTN : children){  //cycle through all of the possible resulting moves
    				if (GTN.alphabeta(!player1)> alpha)
    					option = Math.min(option, GTN.alphabeta(!player1));
    			}
    			return option;
    		}
    	}
    	else
    		return minimax(player1);
    }
    public CheckersTreeNodes getMove(boolean player1){
        if(children.isEmpty()){
            System.out.println("NO POSSIBLE MOVES");
            return null;
        }

        CheckersTreeNodes best = null;
        if(player1){  //if we are player1, we want to maximize the board score
            double a = Double.MIN_VALUE; //a here stands for alpha
            for(CheckersTreeNodes GTN : children){  //cycle through all of the possible resulting moves
                double value = GTN.alphabeta(!player1);  //get the value of the taking the other move
                if(best == null || value > a){
                    a = value;
                    best = GTN;
                }
            }
        }
        else{  //if we are not player1, we want to minimize the board score
            double a = Double.MAX_VALUE; // a here stands for beta
            for(CheckersTreeNodes GTN : children){  //cycle through all of the possible resulting moves
                double value = GTN.alphabeta(!player1);  //get the value of the taking the other move
                if(best == null || value < a){
                    a = value;
                    best = GTN;
                }
            }
        }

        return best;
    }
    
    
    
}
