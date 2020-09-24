package eu.antifuse.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader extends Thread {
    private Controller gui;

    private BufferedReader reader;
    private Socket socket;
    private Client client;

    public ClientReader(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println(response);
                if (response.startsWith("+MSG")) {
                    this.gui.showMessage(client.dEscape(response.substring(5)));
                } else if (response.startsWith("-FAIL")) {
                    this.gui.showError(response.substring(7));
                }  else if (response.startsWith("+NICK")) {
                    String nicks = response.substring(6);
                    this.gui.showMessage(nicks.split(":").length + " Clients online:");
                    for (String nick: nicks.split(":")) this.gui.showMessage(nick);
                }

            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }

    public void setGui(Controller gui) {
        this.gui = gui;
    }
}
