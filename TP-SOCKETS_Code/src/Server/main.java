package Server;

public class main {

    public static void main(String args[]){

        if(args.length == 1){
            int port = Integer.parseInt(args[0]);
            Server server = new Server();
            server.startServer(port);
        } else {
            System.out.println("Use : java -jar MultiThread.jar [port]");
        }

    }
}
