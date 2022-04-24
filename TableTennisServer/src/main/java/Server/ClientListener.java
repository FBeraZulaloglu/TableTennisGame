/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Message.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X550V
 */
public class ClientListener extends Thread {

    Client TheClient;

    ClientListener(Client TheClient) {
        this.TheClient = TheClient;
    }

    @Override
    public void run() {
        while (TheClient.soket.isConnected()) {
            try {
                try {
                    if (TheClient.soket.isClosed()) {
                        System.out.println(TheClient.name+" is exited");
                        break;
                    } else {
                        Message msg = (Message) TheClient.sInput.readObject();

                        switch (msg.type) {
                            case JoinServer:
                                TheClient.name = msg.content.toString();
                                System.out.println("User " + TheClient.name + " has joined the server...");
                                Server.FindValidEnemy(TheClient);
                                break;
                            case PaddleUp:
                                Server.PaddleUp(TheClient,msg);
                                break;
                            case PaddleDown:
                                Server.PaddleDown(TheClient,msg);
                                break;
                            case PaddleStopped:
                                Server.PaddleStopped(TheClient,msg);
                                break;
                            case ScoreChanged:
                                Server.ScoreChanged(TheClient,msg);
                                break;
                            case ExitServer:
                                System.out.println("Exit Server");
                                Server.ExitServer(TheClient);
                                break;
                                
                        }

                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                Server.Clients.remove(TheClient);
            }
        }
    }
}
