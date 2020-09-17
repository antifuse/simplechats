package eu.antifuse.simplechats.client;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    private final Socket socket;
    private String username;
    private PrintWriter write;
    private ClientReader reader;
    private Controller gui;

    public Client() {
        this.socket = new Socket();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void connect(SocketAddress ip) {
        try {
            this.socket.connect(ip);
            this.reader = new ClientReader(this.socket,this);
            this.reader.setGui(this.gui);
            this.reader.start();
            this.write = new PrintWriter(this.socket.getOutputStream(), true);
            this.write.println("UN " + username);
            System.out.println("UN " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (!this.socket.isConnected()) return;
        this.write.println("DC");
        try {
            this.write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String content) {
        if (content == null || this.username == null) return;
        if (content.split(" ")[0].equals("/list")) this.write.println("LS");
        else if (content.split(" ")[0].equals("/name")) {
            if (content.split(" ").length < 2) return;
            this.write.println("UN " + content.split(" ")[1]);
        } else {
            System.out.println("SN " + content);
            this.write.println("SN " + content);
        }
    }

    public ClientReader getReader() {
        return reader;
    }

    public void setGui(Controller gui) {
        this.gui = gui;
    }
}
