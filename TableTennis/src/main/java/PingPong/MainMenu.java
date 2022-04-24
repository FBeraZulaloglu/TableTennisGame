package PingPong;

import Client.Client;
import Message.Message;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * the main menu at the start of the game
 *
 * @author Zayed
 *
 */
public class MainMenu extends MouseAdapter {
    
    private final int PORT_NUMBER = 5000;
    
    public static boolean active; // true if main menu is displaying

    // Play button
    private Rectangle playBtn;
    private String playTxt = "Play";
    private boolean pHighlight = false; // true if the mouse hovered over the rectangle

    // Quit button
    private Rectangle quitBtn;
    private String quitTxt = "Quit";
    private boolean qHighlight = false;

    //User Name Label
    private Rectangle userNameLbl;
    private String userNameLblTxt = "User Name:";
    private boolean lHighlight = false;

    //User Name Label
    private Rectangle userNameTxt;
    public String userNameTxtContent = "";
    private boolean tHighlight = false;

    private boolean getUserName = false;
    private Font font;


    /**
     * constructor
     *
     * @param game - the Game object
     */
    public MainMenu(Game game) {

        active = true;
        game.start();

        // position and dimensions of each button
        int x, y, w, h;

        w = 400;
        h = 100;

        y = Game.HEIGHT / 2 + h / 2;

        int x_first = Game.WIDTH / 4 - w / 2;
        playBtn = new Rectangle(x_first, y, w, h);

        int x_second = Game.WIDTH * 3 / 4 - w / 2;
        quitBtn = new Rectangle(x_second, y, w, h);
        // just change y position
        y = Game.HEIGHT / 2 - h;
        userNameLbl = new Rectangle(x_first, y, w, h);

        userNameTxt = new Rectangle(x_second, y, w, h);
        
        font = new Font("Roboto", Font.PLAIN, 50);

    }

    /**
     * Draw buttons and name input in Main Menu
     *
     * @param g - Graphics object used to draw everything
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(font);

        // draw buttons
        Color buttonColor = new Color(36, 99, 6);
        Color highLightColor = new Color(184, 209, 171);
        Color labelColor = new Color(27, 71, 5);
        
        g.setColor(buttonColor);
        if (pHighlight) {
            g.setColor(highLightColor);
        }
        g2d.fill(playBtn);

        g.setColor(buttonColor);
        if (qHighlight) {
            g.setColor(highLightColor);
        }
        g2d.fill(quitBtn);
        // draw labels
        g.setColor(labelColor);
        g2d.fill(userNameLbl);

        g.setColor(labelColor);
        g2d.fill(userNameTxt);

        // draw borders
        g.setColor(Color.white);
        g2d.draw(playBtn);
        g2d.draw(quitBtn);
        g2d.draw(userNameLbl);
        g2d.draw(userNameTxt);

        // draw text in buttons
        int strWidth, strHeight;

        // Play Button text
        strWidth = g.getFontMetrics(font).stringWidth(playTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.green);
        g.drawString(playTxt, (int) (playBtn.getX() + playBtn.getWidth() / 2 - strWidth / 2),
                (int) (playBtn.getY() + playBtn.getHeight() / 2 + strHeight / 4));

        // Quit Button text
        strWidth = g.getFontMetrics(font).stringWidth(quitTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.red);
        g.drawString(quitTxt, (int) (quitBtn.getX() + quitBtn.getWidth() / 2 - strWidth / 2),
                (int) (quitBtn.getY() + quitBtn.getHeight() / 2 + strHeight / 4));

        // User Name Label text
        strWidth = g.getFontMetrics(font).stringWidth(userNameLblTxt);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.WHITE);
        g.drawString(userNameLblTxt, (int) (userNameLbl.getX() + userNameLbl.getWidth() / 2 - strWidth / 2),
                (int) (userNameLbl.getY() + userNameLbl.getHeight() / 2 + strHeight / 4));

        // User Name Text Content
        strWidth = g.getFontMetrics(font).stringWidth(userNameTxtContent);
        strHeight = g.getFontMetrics(font).getHeight();

        g.setColor(Color.WHITE);
        g.drawString(userNameTxtContent, (int) (userNameTxt.getX() + userNameTxt.getWidth() / 2 - strWidth / 2),
                (int) (userNameTxt.getY() + userNameTxt.getHeight() / 2 + strHeight / 4));

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Point p = e.getPoint();

        if (playBtn.contains(p)) {
            active = false;
            Client.Start("127.0.0.1", PORT_NUMBER);
            Message msg = new Message(Message.Message_Type.JoinServer);
            msg.content = userNameTxtContent;
            Client.Send(msg);
        } else if (quitBtn.contains(p)) {
            System.exit(0);
        } else if (userNameTxt.contains(p)) {
            getUserName = true;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        Point p = e.getPoint();

        // determine if mouse is hovering inside one of the buttons
        pHighlight = playBtn.contains(p);
        qHighlight = quitBtn.contains(p);

    }
    

}
