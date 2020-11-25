package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Gestion de la connexion multicast
 * @author Lucas Tissier, Louis Schibler
 * */
public class MulticastClient {

    /**Port de connexion au groupe multicast*/
    private int groupPort;
    /**Adresse de connexion au groupe mutlicast*/
    private String groupAddress;
    /**Peuso utilisateur*/
    private String pseudo;

    /**
     * Initialisation des attributs.
     * @param pseudo Nom de l'utilisateur
     * */
    public MulticastClient(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Envoie un message sous forme de paquets de bytes à toutes les sockets abonnées au groupe
     * @param msg Message à envoyer
     * */
    public void send(String msg) {
        msg = pseudo + " : " +msg;
        MulticastSocket socket = null;
        try {
            InetAddress groupAddr = InetAddress.getByName(groupAddress);
            socket = new MulticastSocket(groupPort);
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, groupAddr, groupPort);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(socket != null) {
            socket.close();
        }
    }

    /**
     * Abonne la socket à un groupe
     * @param address Adresse du groupe mutlicast
     * @param port Port du groupe multicast
     * */
    public void join(String address, int port) throws IOException {
        this.groupAddress = address;
        this.groupPort = port;

        InetAddress groupAddr = InetAddress.getByName(address);
        MulticastSocket socket = new MulticastSocket(port);
        socket.joinGroup(groupAddr);
        MulticastThread l = new MulticastThread(socket, groupAddr, port);
        l.start();
        System.out.println("You have joined group " + groupAddr + " (port " + port + ") under the name " + pseudo);


    }


}
