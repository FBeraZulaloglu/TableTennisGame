/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Message.Message;
import Message.Message.Message_Type;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X550V
 */
public class Server {

    public static ServerSocket serverSocket;
    public static int IdClient = 0;
    public static int port = 0;
    public static NewClientListener runThread;
    public static ArrayList<Client> Clients = new ArrayList<>();

    public static void Start(int openport) {
        try {
            Server.port = openport;
            Server.serverSocket = new ServerSocket(Server.port);

            Server.runThread = new NewClientListener();
            Server.runThread.start();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Send(Client cl, Message msg) {

        try {
            cl.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void FindValidEnemy(Client c) {
        for (Client client : Clients) {
            if (c != client && client.opponent == null) {
                c.opponent = client;
                client.opponent = c;

                SendOpponentInfo(c);
                SendOpponentInfo(client);
            }
        }
    }

    // Sending the opponent name to the user
    private static void SendOpponentInfo(Client cl) {
        Message msg = new Message(Message_Type.OpponentFound);
        msg.content = cl.opponent.name;
        Send(cl, msg);
    }

    //Sending the location of paddle to the opponent
    public static void PaddleUp(Client cl,Message msg) {
        Message up = msg;
        Send(cl.opponent, up);
    }

    public static void PaddleDown(Client cl,Message msg) {
        Message down = msg;
        Send(cl.opponent, down);
    }

    public static void PaddleStopped(Client cl,Message msg) {
        Message up = msg;
        Send(cl.opponent, up);
    }

    static void ScoreChanged(Client cl,Message msg) {
        System.out.println("Score changed");
        Message score = msg;
        Send(cl.opponent, score);
    }

    public static void ExitServer(Client cl) throws IOException {
        cl.soket.close();
        cl.sOutput.flush();
        cl.sOutput.close();
        cl.sInput.close();
    }

}
