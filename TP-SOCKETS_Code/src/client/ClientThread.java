package client;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Thread d'écoute du serveur.
 * @author Lucas Tissier, Louis Schibler
 * */
public class ClientThread extends Thread{

    /**Interface de connexion client*/
    private ClientInterface clientInterface;
    /**Flux d'entrée depuis le serveur*/
    private BufferedReader SocIn;
    /**Etat du thread*/
    private boolean running = false;

    /**
     * Initialisation des attributs.
     * @param ci Interface connexion client
     * @param br Flux d'entrée
     * */
    public ClientThread(ClientInterface ci, BufferedReader br) {
        this.clientInterface = ci;
        this.SocIn = br;
    }

    /**
     * Tue le thread.
     * */
    public void kill() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Lance le thread, écoute les messages entrants
     * */
    public void run() {
        running = true;
        try {
            String msg;
            while((msg = SocIn.readLine()) != null) {
                clientInterface.onReceiveMessage(msg);
            }
            clientInterface.onConnectionLost("Connection lost...");
        } catch (IOException e) {
            clientInterface.onConnectionLost("Client disconnected");
        }
        running = false;
    }

}
