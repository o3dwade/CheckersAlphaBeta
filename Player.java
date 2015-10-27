
import java.awt.event.*;


public abstract interface Player {
    public State getMove();  //returns the next state
    public void update(State BP, boolean player1);
    public void onMousePressed(MouseEvent e);
    public void onMouseReleased(MouseEvent e);
}
