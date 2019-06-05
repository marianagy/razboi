package com.razboi.client.view;

import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.ui_utils.Utils;
import com.razboi.razboi.networking.utils.ServerException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainView {
    BorderPane pane;
    ClientController clientController;

    // fx controls
    Label choiceLabel;
    Label opponentLabel;
    Button startButton;

    //board constants

    private static final int BUTTON_PADDING = 20;
    private static final int NUM_BUTTON_LINES = 3;
    private static final int BUTTONS_PER_LINE = 3;

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
        pane.setRight(initPane());
        pane.setCenter(initGameBoard());
        //pane.setLeft(makeUserList());
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
        GridPane grid = Utils.initWindow("Welcome "+clientController.getUserDTO().getUsername());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logout(event);

            }
        });
        startButton = new Button("Start");
        startButton.setDisable(true);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                gameStart(event);

            }
        });

        grid.add(logoutBtn, 0, 2, 2, 1);
        grid.add(startButton, 0, 1, 2, 1);
        return grid;
    }

    private void gameStart(ActionEvent event){
        // obtin numele adversarului
        this.clientController.gameStart(choiceLabel.getText());
        // obtin pozitia adversarului
        //opponentLabel.setText("Your Opponent: test");
        System.out.println("Game start button pushed");
    }
    private GridPane initGameBoard(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(BUTTON_PADDING));
        grid.setHgap(BUTTON_PADDING);
        grid.setVgap(BUTTON_PADDING);

        for (int r = 0; r < NUM_BUTTON_LINES; r++) {
            for (int c = 0; c < BUTTONS_PER_LINE; c++) {
                int number = NUM_BUTTON_LINES * r + c;
                Button button = new Button(String.valueOf(number));
                grid.add(button, c, r);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        //logout(event);
                        choiceLabel.setText(String.valueOf(number));
                        startButton.setDisable(false);
                        System.out.println("Position "+String.valueOf(number)+" has been chosen");
                    }
                });
            }
        }

        Label instructions = new Label();
        instructions.setText("Alege pozitia si apasa butonul de start");
        Label choice_text = new Label();
        choice_text.setText("Pozitia aleasa: ");
        choiceLabel = new Label();

        grid.add(instructions,0,NUM_BUTTON_LINES+1,5,1);
        grid.add(choice_text,0,NUM_BUTTON_LINES+2,4,1);
        grid.add(choiceLabel,5,NUM_BUTTON_LINES+2,1,1);

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
