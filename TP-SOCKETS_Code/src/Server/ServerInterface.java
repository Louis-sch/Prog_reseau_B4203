package Server;

public interface ServerInterface {
    /**
     * Méthode à appeler lors de la connexion du client.
     * @param client Le thread d'ecoute client.
     * */
    public void onClientAccepted(ClientThread client);

    /**
     * Méthode à appeler lors de la déconnection du client.
     * @param client Le thread d'ecoute client.
     * */
    public void onClientDisconnected(ClientThread client);

    /**
     * Méthode à appeler lors de la réception d'un message par le client.
     * @param client Le thread d'ecoute client.
     * @param msg Le message à transmettre
     * */
    public void onClientMessage(ClientThread client, String msg);

    /**
     * Méthode à appeler lorsqu'un événement particulier se produit et que l'on souhaite notifier le serveur.
     * @param report Le message décrivant l'événement.
     * */

}
