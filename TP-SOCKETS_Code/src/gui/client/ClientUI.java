package gui.client;

import client.Client;
import client.ClientInterface;
import gui.widgets.MainPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class ClientUI extends JFrame implements ClientInterface, ActionListener {


    private JTextArea msgArea;


    private JButton settings;

    private JButton clear;

    private JTextField msgRedaction;

    private JButton send;

    private Client client;

    private SettingsPage settingPage;

    public ClientUI() throws IOException {
        client = new Client(this);
        settingPage = new SettingsPage(client);

        setTitle("Chat'If");
        setSize(353, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Container mainContainer = this.getContentPane();

        MainPanel main = new MainPanel();
        mainContainer.add(main);



        // History of messages
        msgArea = new JTextArea();
        msgArea.setBounds(34,80,281,520);
        msgArea.setText("");
        main.add(msgArea);

        //Bouton pour ouvrir les réglages

        settings = new JButton();
        try {
            Image img = ImageIO.read(new File("src/gui/widgets/settings.png"));
            settings.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        settings.setBounds(285,45,32,32);
        settings.addActionListener(this);
        main.add(settings);

        //Bouton pour clear le tchat

        clear = new JButton();
        try {
            Image img = ImageIO.read(new File("src/gui/widgets/clear.png"));
            clear.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        clear.setBounds(28,45,32,32);
        clear.addActionListener(this);
        main.add(clear);

        //Zone pour taper le message

        msgRedaction = new JTextField();
        msgRedaction.setBounds(28,610,243,32);
        msgRedaction.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(client.isConnected() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!msgRedaction.getText().isEmpty()) {
                        client.sendMessage(msgRedaction.getText());
                        msgRedaction.setText("");
                    }
                }
            }
            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
        });
        main.add(msgRedaction);


        send = new JButton();
        try {
            Image img = ImageIO.read(new File("src/gui/widgets/send.png"));
            send.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        send.setBounds(275,610,42,32);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( client.isConnected() && !msgRedaction.getText().isEmpty()) {
                    client.sendMessage(msgRedaction.getText());
                    msgRedaction.setText("");
                }
            }
        });
        main.add(send);

        settingPage.connexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                if (!client.isConnected() && settingPage.connexion.getText().equals("Connection") && !settingPage.username.getText().isEmpty()) {
                    try {
                        client.connect(settingPage.ip.getText(), Integer.valueOf(settingPage.port.getText()), settingPage.username.getText());
                        write("Connected to " + settingPage.ip.getText() + " on port " + Integer.valueOf(settingPage.port.getText()));
                        settingPage.connexion.setText("Disconnect");
                        settingPage.ip.setEditable(false);
                        settingPage.port.setEditable(false);
                        settingPage.username.setEditable(false);
                        msgRedaction.requestFocusInWindow();
                    } catch (IOException ex) {
                        write("Error : could not connect to remote host " + settingPage.ip.getText() + " on port " + Integer.valueOf(settingPage.port.getText()));
                    } catch (NumberFormatException ex) {
                        write("Error : you must provide a correct ip address and port...");
                    }
                } else if (client.isConnected() && settingPage.connexion.getText().equals("Disconnect")) {
                    client.disconnect();
                    settingPage.connexion.setText("Connection");
                    settingPage.ip.setEditable(true);
                    settingPage.port.setEditable(true);
                    settingPage.username.setEditable(true);
                }
            }
        });

    }

    /**
     * Ecrit une nouvelle ligne dans la zone des messages.
     * @param msg Message � �crire dans la zone d'affichage des messages.
     * */
    public void write(String msg) {

            while(msg.endsWith("\n")) {
                msg = msg.substring(0, msg.length()-1);
            }
            if(!msg.isEmpty()) {
                msgArea.append(msg + "\n");
                msgArea.setCaretPosition(msgArea.getDocument().getLength());
            }
    }



    /**
     * Efface le contenu de la zone d'affichage des messages.
     * */
    public void clear() {
            msgArea.setText("");
    }

    /**
     * Appelé à la récéption d'un messsage provenant du serveur
     * Ecrit le message dans la zone d'affichage des messages.
     * @param msg Le message re�u.
     * */
    @Override
    public void onReceiveMessage(String msg) {
        write(msg);
    }

    /**
     * Appelé lors de la déconnection du client
     * Maj des contrôles de l'interface et affichage d'un message de déconnexion.
     * @param msg Message de deconnexion.
     * */
    @Override
    public void onConnectionLost(String msg) {
        //connect.setText("Connection");
        //serverIP.setEditable(true);
        //serverPort.setEditable(true);
        //pseudoField.setEditable(true);
        write(msg);
    }

    public static void main(String[] args) throws IOException {
        new ClientUI().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == settings) {
            if (this.settingPage.isVisible() == false) {
                this.settingPage.setVisible(true);
            } else
                this.settingPage.setVisible(false);
        }
        if (source == clear) {
            clear();
        }
    }
}
