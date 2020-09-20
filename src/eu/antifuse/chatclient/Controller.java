package eu.antifuse.chatclient;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.net.InetSocketAddress;

public class Controller extends Application {

    private Client client;

    @FXML
    private TextArea outArea;

    @FXML
    private TextField address;

    @FXML
    private TextField message;

    @FXML
    private TextField name;

    @FXML
    private void handleSend(javafx.scene.input.MouseEvent event) {
        this.sendMessage();
    }

    @FXML
    private void handleEnter(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            this.sendMessage();
        }
    }

    @FXML
    private void handleConnect(javafx.scene.input.MouseEvent event) {
        if (client != null) client.disconnect();
        client = new Client();
        String addrStr = address.getText();
        String ip;
        int port;
        if (addrStr.split(":").length > 1) {
            ip = addrStr.split(":")[0];
            port = Integer.parseInt(addrStr.split(":")[1]);
        } else {
            ip = addrStr;
            port = 1000;
        }
        client.setUsername(name.getText());
        client.setGui(this);
        client.connect(new InetSocketAddress(ip, port));
    }

    private void sendMessage() {
        client.sendMessage(message.getText());
        message.clear();
    }

    public void showMessage(String message) {
        this.outArea.appendText("\n" + message);
    }

    FXMLLoader loader;
    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("clientgui.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Chatclient");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (this.client != null) this.client.disconnect();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void showError(String error) {
        this.outArea.appendText("\n" + error);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
