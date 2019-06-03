package com.razboi.client;


import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.view.LoginView;
import com.razboi.razboi.networking.ServerObjProxy;
import com.razboi.razboi.networking.utils.IServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppMain extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    static BorderPane getView(ClientController controller) {
        try {


            LoginView loginView = new LoginView(controller);

            return loginView.getView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(new FileInputStream("D:\\Facultate\\Sem II\\MPP\\razboii\\src\\main\\resources\\client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("triathlon.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("triathlon.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IServer server = new ServerObjProxy(serverIP, serverPort);
        ClientController ctrl = new ClientController(server);
        primaryStage.setTitle("Triathlon Application");
        BorderPane pane = getView(ctrl);
        Scene scene = new Scene(pane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
