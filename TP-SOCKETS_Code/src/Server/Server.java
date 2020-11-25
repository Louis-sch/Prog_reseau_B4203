/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package Server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

/**
 * Gestion de la connexion côté serveur
 * @author Lucas Tissier, Louis Schibler
 * */
public class Server implements ServerInterface{
	/**Thread d'écoute des connexions client*/
	private ConnectionListenerThread listener;
	/**Liste de thread de connection client*/
	private LinkedList<ClientThread> clients;

	/**
	 * Lance le serveur
	 * @param port Port de lancement du serveur
	 * */
      public void startServer(int port) {
		  try {
			  clients = new LinkedList<ClientThread>();
			  listener = new ConnectionListenerThread(port,this);
			  listener.start();
			  System.out.println("Server started on port : "+port);
		  } catch (IOException e) {
			  System.err.println("Error while starting server :" + e);
		  }

	  }

	/**
	 * Lancement du thread, écoute des messages entrants provenant du client et envoie à l'interface serveur pour transmission
	 * @param msg Message à transmettre
	 * */
	public void writeHistory(String msg) {
		try {
			File logFile = new File("serverhistory.log");
			PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile, true), true);
			logWriter.append(msg + "\n");
			logWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error : logs are not available");
		}

	}

	/**
	 * Récupère l'historique du serveur
	 * */
	public String getHistory() {
		String log = new String();
		try {
			File logFile = new File("serverhistory.log");
			BufferedReader logReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
			String line;
			while((line = logReader.readLine()) != null) {
				log += (line + "\n");
			}
			logReader.close();
		} catch (IOException e) {
		}
		if(log.equals("\n")) {
			log = new String();
		}
		return log;
	}

	/**
	 * Gère l'arrivée d'un nouveau client, envoie des messages issus de l'historique serveur
	 * @param client Thread d'écoute du client
	 * */
	@Override
	public void onClientAccepted(ClientThread client) {
      	client.sendMessage(getHistory());
		for(ClientThread c : clients) {
			c.sendMessage(client + " has joined the chat");
		}
		clients.add(client);

	}

	/**
	 * Gère la déconnexion d'un client, tue le thread d'écoute client
	 * @param client Thread d'écoute du client
	 * */
	@Override
	public void onClientDisconnected(ClientThread client) {
		clients.remove(client);
		client.killThread();
			for(ClientThread c : clients) {
				c.sendMessage(client + " has left the chat");
		}
			System.out.println(client + " has left the chat");
	}

	/**
	 * Envoie un message au client, écrit le message dans l'historique
	 * @param client Thread d'écoute du client
	 * @param msg Lessage à envoyer
	 * */
	@Override
	public void onClientMessage(ClientThread client, String msg) {
			for(ClientThread c : clients) {
				c.sendMessage(client + " : " + msg);
			}
		writeHistory(client + " : " + msg);
			System.out.println(msg);
	}

}

  
