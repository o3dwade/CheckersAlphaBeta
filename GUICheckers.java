
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GUICheckers extends JPanel implements KeyListener, FocusListener, MouseMotionListener, MouseListener{

    public long sleepTime;
    public JFrame myJFrame;

    public Checkers myCheckers;

    public GUICheckers(long sleepTime, int width, int height, Checkers myCheckers){
        myJFrame = new JFrame("Checkers");
        myJFrame.setSize(width,height);
        myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJFrame.add(this);  //add the viewer (itself) into the frame
        myJFrame.setVisible(true);

        myJFrame.addFocusListener(this);
        myJFrame.addKeyListener(this);
        myJFrame.addMouseMotionListener(this);
        myJFrame.addMouseListener(this);
        myJFrame.requestFocus();  //get focus
        this.sleepTime = sleepTime;
        this.myCheckers = myCheckers;
    }

    @Override
    public void paintComponent(Graphics g){
        setBackground(Color.WHITE);
        super.paintComponent(g);

        if(sleepTime >= 0 && myCheckers != null){
            double screenDimensions[] = {getWidth(), getHeight()};
            for(int a = 0; a < myCheckers.gameState.board.length; a++){
                for(int b = 0; b < myCheckers.gameState.board[0].length; b++){
                    if(a == 0){
                        g.setColor(Color.BLACK);
                        g.drawString(new String(new char[]{(char)(b + 48)}), 80, b*50 + 225);
                        g.drawString(new String(new char[]{(char)(b + 48)}), b*50 + 120, 180);
                    }
                    g.setColor((((a + b)%2 == 0) ? Color.WHITE : Color.BLACK));
                    g.fillRect(a*50+100,b*50+200,50,50);
                    switch(myCheckers.gameState.board[a][b]){
                        case(-2): g.setColor(Color.RED); g.drawString("K", a*50+120, b*50+220);
                        case(-1): g.setColor(new Color(0,0,0,180)); g.fillOval(a*50+100, b*50+200,50,50); break;
                        case(2):  g.setColor(Color.BLACK); g.drawString("K", a*50+120, b*50+220);
                        case(1):  g.setColor(new Color(255,0,0,180)); g.fillOval(a*50+100, b*50+200,50,50); break;
                        default: break;
                    }
                }
            }
            g.drawString("" + sleepTime, 0, 10);
            
        }

        repaint();

        try{Thread.sleep(sleepTime);}catch(Exception e){}
    }

    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        //they pressed a key
        if(e.getKeyCode() == KeyEvent.VK_W){  //pressed the W key
            sleepTime ++;  //increment the sleep time
        }
        else if(e.getKeyCode() == KeyEvent.VK_S){  //pressed the S key
            sleepTime --;
        }
    }
    public void keyReleased(KeyEvent e){}
    public void focusLost(FocusEvent e){}
    public void focusGained(FocusEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        if(myCheckers.turn){
            myCheckers.RED.onMousePressed(e);
        }
        else{
            myCheckers.BLACK.onMousePressed(e);
        }
    }
    public void mouseReleased(MouseEvent e){
        if(myCheckers.turn){
            myCheckers.RED.onMouseReleased(e);
        }
        else{
            myCheckers.BLACK.onMouseReleased(e);
        }
    }
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}
}
