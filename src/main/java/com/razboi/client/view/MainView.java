package com.razboi.client.view;

import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.ui_utils.Utils;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.entity.Player;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainView {
    BorderPane pane;
    ClientController clientController;

    // fx controls
    Label choiceLabel;
    Label opponentLabel;
    Button startButton;
    Label currentTurnLabel;
    TextField yourWord;
    TextField letterChoice;

    List<Button> planeButtons = new ArrayList<>();
    //board constants

    private static final int BUTTON_PADDING = 20;
    private static final int NUM_BUTTON_LINES = 3;
    private static final int BUTTONS_PER_LINE = 3;

    private ObservableList<Player> loggedInUsers;
    private StringProperty opponent;
    private BooleanProperty currentTurn;
    private TableView tableView;

    public MainView(ClientController clientController) {
        this.clientController = clientController;
        this.loggedInUsers = clientController.getLoggedInUsers();
        System.out.println(this.loggedInUsers);

        tableView = new TableView();

        TableColumn<String, Player> column1 = new TableColumn<>("Nume Utilizator");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<String, Player> column2 = new TableColumn<>("Cuvant ales: ");
        column2.setCellValueFactory(new PropertyValueFactory<>("chosenWordTransformed"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);


        this.loggedInUsers.addListener((ListChangeListener.Change<? extends Player> change) -> {
//            @Override
//            public void onChanged(Change<? extends Player> c) {
//
//            }
            System.out.println("this fired");
            change.next();
            System.out.println("List changed");
            System.out.println(loggedInUsers);
            Platform.runLater(
                    () -> {


                        pane.setCenter(initPlayers());
                        pane.setRight(initGameBoard());
//                        loggedInUsers.forEach(tableView.getItems()::add);
                        tableView.setItems(loggedInUsers);

                        tableView.refresh();
                    });
            if (change.wasUpdated() || change.wasPermutated() || change.wasReplaced()) {

            }


        });
        this.opponent = clientController.getOpponentName();
        this.opponent.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Opponent arrived...");
                Platform.runLater(
                        () -> {
                            //opponentLabel.setText("Your opponent: " + newValue);
                            disableButtons();
                        });
            }
        });

        this.currentTurn = clientController.isCurrentTurn();

        this.currentTurn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {

                Platform.runLater(
                        () -> {

                            currentTurnLabel.setText("It's your turn");
                            enableButtons();
                        });

            } else {
                Platform.runLater(
                        () -> {

                            currentTurnLabel.setText("It's your opponents turn");
                            disableButtons();
                        });

            }
        });
        initView();

    }

    private void disableButtons() {

        planeButtons.forEach(button -> button.setDisable(true));
    }

    private void enableButtons() {

        planeButtons.forEach(button -> button.setDisable(false));
    }

    private void initView() {
        pane = new BorderPane();
        pane.setLeft(initPane());
        //pane.setCenter(initGameBoard());
        //pane.setLeft(makeUserList());
        //pane.setLeft(createScore());
        //controlButtons();
    }


    private GridPane makeUserList() {

        GridPane grid = Utils.initWindow("Logged In users");
        ListView<String> list = new ListView<String>();
        //list.setItems(loggedInUsers);
        Label listLabel = new Label();
        listLabel.setText("Logged in users: ");

//        grid.add(listLabel,0,0);
        grid.add(list, 0, 1);
        return grid;
    }


    private GridPane initPlayers() {
        GridPane grid = Utils.initWindow("Players: ");
        grid.add(tableView, 0, 1);
        return grid;
    }

    private GridPane initPane() {
        GridPane grid = Utils.initWindow("Bine ai venit " + clientController.getUserDTO().getUsername());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                logout(event);

            }
        });
        startButton = new Button("Start");
        //startButton.setDisable(true);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                gameStart(event);

            }
        });

        grid.add(logoutBtn, 0, 2, 2, 1);
        grid.add(startButton, 0, 1, 2, 1);
        return grid;
    }

    private void gameStart(ActionEvent event) {
        // obtin numele adversarului
        this.clientController.gameStart();
        // obtin pozitia adversarului
        //opponentLabel.setText("Your Opponent: test");
        System.out.println("Game start button pushed");
    }

    private GridPane initGameBoard() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(BUTTON_PADDING));
        grid.setHgap(BUTTON_PADDING);
        grid.setVgap(BUTTON_PADDING);


//        Label instructions = new Label();
//        instructions.setText("Alege pozitia si apasa butonul de start");
//        Label choice_text = new Label();
//        choice_text.setText("Pozitia aleasa: ");
//        choiceLabel = new Label();
//
//        opponentLabel = new Label();
//        currentTurnLabel = new Label();
//        currentTurnLabel.setText("It's your opponents turn");
//        grid.add(instructions, 0, NUM_BUTTON_LINES + 1, 5, 1);
//        grid.add(choice_text, 0, NUM_BUTTON_LINES + 2, 4, 1);
//        grid.add(choiceLabel, 5, NUM_BUTTON_LINES + 2, 1, 1);
//        grid.add(opponentLabel, 0, NUM_BUTTON_LINES + 3, 5, 1);
//        grid.add(currentTurnLabel, 0, NUM_BUTTON_LINES + 4, 5, 1);


        Label ywInstruction = new Label();
        ywInstruction.setText("Alege cuvantul: ");
        yourWord = new TextField();

        Label lcInstruction = new Label();
        lcInstruction.setText("Cand iti vine randul, alege: ");
        letterChoice = new TextField();

        Button lcOk = new Button();
        lcOk.setText("Submit Letter");
        lcOk.setDisable(true);
        Button ywOk = new Button();
        ywOk.setText("Submit word");
        ywOk.setOnAction(event -> {
            if (yourWord.getText().equals("")) {
                System.out.println("Enter a word");
                //add a bubble
            } else {
                ywOk.setDisable(true);
                try {
                    this.clientController.submitWord(yourWord.getText());
                } catch (ServerException e) {
                    e.printStackTrace();
                }
            }

        });


        grid.add(tableView, 0, 0, 5, 1);
        grid.add(ywInstruction, 0, 1);
        grid.add(yourWord, 0, 2);
        grid.add(ywOk, 1, 2);
        grid.add(lcInstruction, 0, 3);
        grid.add(letterChoice, 0, 3);
        grid.add(lcOk, 1, 3);
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
            Utils.showDialog("Logout nereusit: " + e.getMessage(), "Logout", Alert.AlertType.CONFIRMATION);
        }

    }

    public BorderPane getView() {
        return pane;
    }
}
