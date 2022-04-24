package PingPong;

import Client.Client;
import Message.Message;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * process key input from the keyboard
 *
 * @author Zayed
 *
 */
public class KeyInput extends KeyAdapter {

    private Paddle lp; // left paddle
    public static boolean lup = false; // lup = left up (up1 in video)
    public static boolean ldown = false;
    public static boolean rup = false; // lup = left up (up1 in video)
    public static boolean rdown = false;

    private MainMenu newMenu;

    /**
     * constructor
     *
     * @param p1 - left paddle is the first player's paddle
     * @param menu - MainMenu before game starts
     */
    public KeyInput(Paddle p1, MainMenu menu) {
        newMenu = menu;
        lp = p1;

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // if click the user name text part you can start typing your user name
        // the name can not be longer than 8 characters
        if (newMenu.userNameTxtContent.length() < 8) {
            if (newMenu.active) {
                newMenu.userNameTxtContent += e.getKeyChar();
            }
        }
        int key = key = e.getKeyCode();

        if (key == KeyEvent.VK_BACK_SPACE) {
            newMenu.userNameTxtContent = newMenu.userNameTxtContent.substring(0, newMenu.userNameTxtContent.length() - 2);
        }
        if (key == KeyEvent.VK_ENTER) {
            if (newMenu.userNameTxtContent.length() > 3) {
                MainMenu.active = false;
            }
        }
        // exit
        if (key == KeyEvent.VK_ESCAPE) {
            Client.Stop();
            System.exit(1);
        }

        if (Client.socket != null) {

            if (key == KeyEvent.VK_W) {
                lp.switchDirections(-1);
                lup = true;
                // Send location of paddle
                Message msg = new Message(Message.Message_Type.PaddleUp);
                msg.content = "Up";
                Client.Send(msg);
            }
            if (key == KeyEvent.VK_S) {
                lp.switchDirections(1);
                ldown = true;

                Message msg = new Message(Message.Message_Type.PaddleDown);
                msg.content = "Down";
                Client.Send(msg);
            }
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = 0;
        if (Client.socket != null)
            key = e.getKeyCode();
        

        if (key == KeyEvent.VK_W) {
            lup = false;

            Message msg = new Message(Message.Message_Type.PaddleStopped);
            msg.content = "Stopped";
            Client.Send(msg);
        }
        if (key == KeyEvent.VK_S) {
            ldown = false;

            Message msg = new Message(Message.Message_Type.PaddleStopped);
            msg.content = "Stopped";
            Client.Send(msg);
        }

        // to prevent lag
        if (!lup && !ldown) {
            lp.stop();
        }
    }

}
