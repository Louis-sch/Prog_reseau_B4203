package multicast;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Thread d'écoute d'un groupe multicast
 * @author Lucas Tissier, Louis Schibler
 * */
public class MulticastThread extends Thread {

    /**Socket Multicast abonnée*/
    private MulticastSocket socket;
    /**Adresse du groupe*/
    private InetAddress groupAddr;
    /**port du groupe*/
    private int groupPort;

    /**
     * Initialisation des attributs.
     * @param s Socket Multicast abonné au groupe défini par (addr, port).
     * @param addr Adresse du groupe multicast.
     * @param port port du groupe multicast.
     * */
    public MulticastThread(MulticastSocket s, InetAddress addr, int port) {
        socket = s;
        groupAddr = addr;
        groupPort = port;
    }

    /**
     * Tue le listener thread
     * */
    public synchronized void kill() {
        try {
            socket.leaveGroup(groupAddr);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description textuelle du groupe auquel le processus est abonné.
     * @return Une définition du groupe auquel le processus est abonné : groupAddr et groupPort.
     * */
    public String toString() {
        return groupAddr.toString() + " : " + groupPort;
    }

    /**
     * Boucle principale du processus.
     * Ecoute le groupe et notifie le listener chaque fois qu'un message est reçu, ou quand la connexion est rompue.
     * */
    public void run() {
        try {
            for(;;) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(buffer, 0, packet.getLength());
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        kill();
        System.out.println("You have left the group");
    }

}
