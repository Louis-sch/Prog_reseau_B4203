package multicast;

import Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MulticastMain {

    public static void main(String args[]) throws IOException {
        String msg = null;
        String pseudo = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String address = null;
        int port = 0;

        while(pseudo == null) {
            System.out.println("Enter pseudo : ");
            try {
                pseudo = stdIn.readLine();
            } catch (IOException e) {
                System.out.println("Error, try again");
            }
        }

        while(address == null) {
            System.out.println("Enter address : ");
            try {
                address = stdIn.readLine();
            } catch (IOException e) {
                System.out.println("Error, try again");
            }
        }

        while(port == 0) {
            System.out.println("Enter port : ");
            try {
                port = Integer.parseInt(stdIn.readLine());
            } catch (IOException e) {
                System.out.println("Error, try again");
            }
        }

        MulticastClient mutlicast = new MulticastClient(pseudo);
        try {
            mutlicast.join(address,port);
        } catch (IOException e) {
            System.out.println("Couldn't join group");
            return;
        }

        while (true) {
            msg=stdIn.readLine();
            if (msg.equals(".")) break;
            mutlicast.send(msg);
        }
        stdIn.close();

    }


}
