package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread d'écoute serveur pour les connexions entrantes des clients
 * @author Lucas Tissier, Louis Schibler
 * */
public class ConnectionListenerThread extends Thread{
    /**Socket d'écoute serveur pour connexions clients*/
    private ServerSocket listenSocket;
    /**Interface serveur*/
    private ServerInterface connectionInterface;

    /**
     * Initialisation des attributs.
     * @param port port de connexion au serveur
     * @param si Interface serveur
     * */
    public ConnectionListenerThread(int port, ServerInterface si) throws IOException {
        listenSocket = new ServerSocket(port);
        this.connectionInterface = si;
    }

    /**
     * Lancement du thread, écoute des connexions entrantes
     * */
    public void run() {
        try {
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new ClientThread(clientSocket, connectionInterface).start();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
            }

        } catch (IOException e) {
            System.out.println("Server down...");
        }

    }
}