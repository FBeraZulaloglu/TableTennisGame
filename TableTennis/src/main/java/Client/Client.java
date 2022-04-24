package Client;

import PingPong.Game;

import java.net.Socket;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import Message.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author INSECT
 */
// serverdan gelecek mesajları dinleyen thread
class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client.socket.isConnected()) {
            try {
                //blocking until message has recieved
                Message received = (Message) (Client.sInput.readObject());
                
                switch (received.type) {
                    case OpponentFound:
                        Game.rivalNameTxt = "Player 2: " + received.content.toString();
                        Client.isOpponentFound = true;
                        break;
                    case PaddleUp:
                        Game.rightPaddle.switchDirections(-1);
                        break;
                    case PaddleDown:
                        Game.rightPaddle.switchDirections(1);
                        break;
                    case PaddleStopped:
                        Game.rightPaddle.stop();
                        break;
                    case ScoreChanged:
                        Game.rightPaddle.score = Integer.parseInt(received.content.toString());
                        break;
                }

            } catch (IOException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            }
        }

    }
}

public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenServer;

    public static boolean isOpponentFound;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Connected to Server");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenServer = new Listen();
            Client.listenServer.start();

            //ilk mesaj olarak isim gönderiyorum
            Message msg = new Message(Message.Message_Type.OpponentFound);
            msg.content = Game.gamerNameTxt;
            Client.Send(msg);
            System.out.println("Gamer :" + Game.gamerNameTxt);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenServer.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
