package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Gestion de la connexion côté client
 * @author Lucas Tissier, Louis Schibler
 * */
public class Client implements ClientInterface{

    /**Socket TCP côté client pour connexion serveur*/
    private Socket ClientSocket = null;
    /**Flux d'entrée*/
    private BufferedReader SocIn;
    /**Flux de sortie*/
    private PrintStream SocOut;
    /**Thread d'écoute client des messages serveur*/
    private ClientThread client;
    /**Interface graphique*/
    private ClientInterface GUI;
    /**Etat de la connexion*/
    private Boolean isConnected;

    /**
     * Initialisation des attributs.
     * @param gui Interface utilisateur client
     * */
    public Client(ClientInterface gui) {
        this.GUI = gui;
        isConnected = false;
    }

    /**
     * Conncte le client au serveur, lance le thread d'écoute des messages
     * @param ip IP serveur.
     * @param port Port d'écoute du serveur.
     * @param pseudo Pseudo du client.
     * */
    public void connect(String ip, int port, String pseudo) throws IOException {
        ClientSocket = new Socket(ip,port);
        SocIn = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
        SocOut= new PrintStream(ClientSocket.getOutputStream());
        SocOut.println(pseudo);
        client = new ClientThread(this,SocIn);
        client.start();
        isConnected = true;
        System.out.println("Connected to host : " + ip);
    }

    /**
     * Déconnecte le client.
     * */
    public void disconnect() {
        try {
            isConnected = false;
            ClientSocket.close();
        } catch(IOException e) {}
    }

    /**
     * Envoie un message au serveur.
     * @param msg Message à envoyer.
     * */
    public void sendMessage(String msg) {
        if(ClientSocket != null) {
            SocOut.println(msg);
        }
    }

    /**
     * Renvoie l'état de la connexion au serveur.
     * */
    public boolean isConnected() {
        return client != null && client.isRunning();
    }

    /**
     * Gestion de la récéption d'un message par le thread d'écoute.
     * @param msg Le message reçu depuis le serveur.
     * */
    @Override
    public void onReceiveMessage(String msg) {
        GUI.onReceiveMessage(msg);

    }

    /**
     * Méthode appelée lors de la déconnexion du processus d'écoute du serveur.
     * @param msg Le message de déconnexion.
     * */
    @Override
    public void onConnectionLost(String msg) {
        try {
            ClientSocket.close();
        } catch(Exception e) {}
        GUI.onConnectionLost(msg);
    }
}
