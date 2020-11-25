/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package Server;

import java.io.*;
import java.net.*;

/**
 * Thread d'écoute serveur pour les messages d'un client
 * @author Lucas Tissier, Louis Schibler
 * */
public class ClientThread
	extends Thread {
	/**Socket TCP côté serveur pour connexion client*/
	private Socket clientSocket;
	/**Flux de sortie vers le client*/
	private PrintWriter SocOut;
	/**Flux d'entrée*/
	private BufferedReader SocIn;
	/**Interface serveur*/
	private ServerInterface connexionInterface;
	/**pseudo utilisateur*/
	private String pseudo;

	/**
	 * Initialisation des attributs.
	 * @param s Socket client côté serveur
	 * @param si Interface serveur
	 * */
	ClientThread(Socket s, ServerInterface si) {
		this.clientSocket = s;
		this.connexionInterface = si;
		this.SocIn = null;
	}

	/**
	 * Lancement du thread, écoute des messages entrants provenant du client et envoie à l'interface serveur pour transmission
	 * */
	public void run() {
    	  try {
    		SocOut = new PrintWriter(clientSocket.getOutputStream(), true);
    		SocIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		pseudo = SocIn.readLine();
    	} catch (Exception e) {
			  try {
				  System.err.println("Error in ClientThread:" + e);
				  clientSocket.close();
			  } catch (IOException e2) {}

        }
		//Si la connection réussit
		connexionInterface.onClientAccepted(this);
		try {
			String msg;
			while((msg = SocIn.readLine()) != null) {
				connexionInterface.onClientMessage(this, msg);
			}
		} catch(IOException e) {}
		connexionInterface.onClientDisconnected(this);
		killThread();
       }

	/**
	 * Tue le thread d'écoute
	 * */
	public void killThread() {
		try {
			clientSocket.close();
		} catch (IOException e) {}
	}

	/**
	 * Envoie d'un message au client
	 * @param msg Le message à envoyer
	 * */
	public void sendMessage(String msg) {
		SocOut.println(msg);
	}

	/**
	 * Affichage du pseudo
	 * */
	@Override
	public String toString() {
		return pseudo;
	}
}

  
