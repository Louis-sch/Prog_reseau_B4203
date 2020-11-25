package gui.client;

import client.Client;
import gui.widgets.MainPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SettingsPage extends JFrame {

    protected JButton connexion;
    protected JTextField ip;
    protected JTextField port;
    protected JTextField username;
    protected Client client;

    public SettingsPage(Client c) throws IOException {

        client = c;

        setTitle("Settings");
        setSize(200, 350);
        setLocation(900,150);
        setResizable(false);
        setLayout(new BorderLayout());

        Container mainContainer = this.getContentPane();

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(null);
        mainContainer.add(main);


        JLabel informationUser = new JLabel("User information");
        informationUser.setFont(informationUser.getFont().deriveFont(informationUser.getFont().getStyle() | Font.BOLD));
        informationUser.setBounds(24,10,150,40);
        main.add(informationUser);

        // Username

        JLabel usernameL = new JLabel("Username");
        usernameL.setBounds(35,40,100,40);
        main.add(usernameL);
        username = new JTextField();
        username.setBounds(30,70,140,28);
        main.add(username);

        JLabel informationNetwork = new JLabel("Network information");
        informationNetwork.setFont(informationNetwork.getFont().deriveFont(informationNetwork.getFont().getStyle() | Font.BOLD));
        informationNetwork.setBounds(24,100,150,40);
        main.add(informationNetwork);

        // Ip

        JLabel ipIndication = new JLabel("Server's IP");
        ipIndication.setBounds(35,140,100,40);
        main.add(ipIndication);
        ip = new JTextField();
        ip.setBounds(30,170,140,28);
        main.add(ip);

        // Port
        JLabel portIndication = new JLabel("Port");
        portIndication.setBounds(35,200,100,40);
        main.add(portIndication);
        port = new JTextField();
        port.setBounds(30,230,140,28);
        main.add(port);

        //Boutons de connexion et déconnexion

        connexion = new JButton("Connection");
        connexion.setBounds(10,280,180,28);
        main.add(connexion);




    }
}
