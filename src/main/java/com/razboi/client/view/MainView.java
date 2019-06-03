package com.razboi.client.view;

import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.ui_utils.Utils;
import com.razboi.razboi.networking.utils.ServerException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainView {
    BorderPane pane;
    ClientController clientController;

    private ObservableList<String> loggedInUsers;
    public MainView(ClientController clientController) {
        this.clientController = clientController;
        this.loggedInUsers = clientController.getLoggedInUsers();
        System.out.println(this.loggedInUsers);
        this.loggedInUsers.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                System.out.println("List changed");
                System.out.println(loggedInUsers);
            }
        });
        initView();

    }

    private void initView() {
        pane = new BorderPane();
        pane.setCenter(initPane());
//        pane.setCenter();
        pane.setLeft(makeUserList());
        //pane.setLeft(createScore());
        //controlButtons();
    }


    private GridPane makeUserList() {

        GridPane grid = Utils.initWindow("Logged In users");
        ListView<String> list = new ListView<String>();
        list.setItems(loggedInUsers);
        Label listLabel = new Label();
        listLabel.setText("Logged in users: ");

//        grid.add(listLabel,0,0);
        grid.add(list, 0, 1);
        return grid;
    }


    private GridPane initPane() {
        GridPane grid = Utils.initWindow("sa vedem");

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logout(event);
                //Utils.showDialog("Helloooo Logout","logout", Alert.AlertType.CONFIRMATION);
            }
        });
        grid.add(logoutBtn, 0, 6, 2, 1);
        return grid;
    }

    private void logout(ActionEvent event) {
        try {
            clientController.logout();
            Utils.showDialog("Logout reusit", "Logout", Alert.AlertType.INFORMATION);

            Scene primaryScene = ((Node) (event.getSource())).getScene();
            Pane root = new LoginView(clientController).getView();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 800, 500));
            stage.show();
            // Hide this current window (if this is what you want)
            primaryScene.getWindow().hide();

        } catch (ServerException e) {
            Utils.showDialog("Logout neresit: " + e.getMessage(), "Logout", Alert.AlertType.CONFIRMATION);
        }

    }

    public BorderPane getView() {
        return pane;
    }
}
