package client;

/**
 * Interface pour la connexion d'un client avec le serveur
 * @author Lucas Tissier, Louis Schibler
 * */
public interface ClientInterface {

    public void onReceiveMessage(String msg);

    public void onConnectionLost(String msg);
}
