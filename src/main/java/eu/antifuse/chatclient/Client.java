package eu.antifuse.chatclient;

import java.io.IOException;
import java.io.PrintWriter;
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
            this.write.println("NAME:" + escape(username));
            System.out.println("NAME:" + escape(username));
        } catch (IOException e) {
            e.printStackTrace();
            this.gui.showError("Es kann keine Verbindung hergestellt werden.");
        }
    }

    public void disconnect() {
        if (!this.socket.isConnected()) return;
        try {
            this.write.close();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String content) {
        if (content == null || this.username == null) return;
        if (content.split(" ")[0].equals("/list")) this.write.println("NICK");
        else if (content.split(" ")[0].equals("/name")) {
            if (content.split(" ").length < 2) return;
            this.write.println("NAME:" + escape(content.split(" ")[1]));
        } else {
            System.out.println("ALL:" + escape(content));
            this.write.println("ALL:" + escape(content));
        }
    }

    public ClientReader getReader() {
        return reader;
    }

    public void setGui(Controller gui) {
        this.gui = gui;
    }

    public String escape(String input) {
        return input.replace("&","&a").replace(":","&c");
    }

    public String dEscape (String input) {
        return input.replace("&c",":").replace("&a","&");
    }
}
