package com.razboi.client.view;

import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.ui_utils.Utils;
import com.razboi.razboi.networking.utils.ServerException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartGameView {

    BorderPane pane;
    ClientController clientController;

    private ObservableList<String> loggedInUsers;
    private BooleanProperty gameSrated;

    Button startGameBtn;

    public StartGameView(ClientController clientController) {
        this.clientController = clientController;
        this.gameSrated=clientController.isGameStarted();

        this.gameSrated.addListener((observable, oldValue, newValue) ->  {
            if(newValue){
                //Scene primaryScene = ((Node) (event.getSource())).getScene();
                System.out.println("Event listener fired");
                Platform.runLater(
                        () -> {
                            Scene primaryScene = ((Node) (startGameBtn)).getScene();
                            Pane root = new MainView(this.clientController).getView();
                            Stage stage = new Stage();
                            stage.setTitle("Razboi");
                            stage.setScene(new Scene(root, 1200, 600));
                            stage.show();
                            primaryScene.getWindow().hide();
                        }
                );

            }
        });

        initView();

    }

    private void initView() {
        pane = new BorderPane();
        pane.setCenter(initPane());
//        pane.setCenter();
//        pane.setLeft(makeUserList());
        //pane.setLeft(createScore());
        //controlButtons();
    }


    private GridPane initPane() {
        GridPane grid = Utils.initWindow("Welcome"+clientController.getUserDTO().getUsername());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logout(event);
                //Utils.showDialog("Helloooo Logout","logout", Alert.AlertType.CONFIRMATION);
            }
        });
        grid.add(logoutBtn, 0, 6, 2, 1);

        startGameBtn = new Button("Start Game");
        startGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                startGame(event);

            }
        });
        grid.add(startGameBtn, 0, 5, 2, 1);
        return grid;
    }

    /**
     * When startgame putton is pressed, MainView is opened and clientController.startGame() si called
     * @param event
     */
    private void startGame(ActionEvent event) {
        try {
           // clientController.addPlayer(LoginView.username);
            clientController.startGame();
            Utils.showDialog("Start", "Hello", Alert.AlertType.INFORMATION);
            //GeneralController generalController = new GeneralController(username, clientController.getServer());
            Scene primaryScene = ((Node) (event.getSource())).getScene();
            Pane root = new MainView(this.clientController).getView();
            Stage stage = new Stage();
            stage.setTitle("Razboi");
            stage.setScene(new Scene(root, 1200, 600));
            stage.show();
            //Hide this current window (if this is what you want)
            primaryScene.getWindow().hide();

        } catch (ServerException e) {
            Utils.showDialog("Game failed to start: " + e.getMessage(), "Eroare", Alert.AlertType.ERROR);
        }
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
