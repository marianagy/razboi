package com.razboi.client.view;


import com.razboi.client.ui_utils.ClientController;
import com.razboi.client.ui_utils.Utils;
import com.razboi.razboi.networking.utils.ServerException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginView {
    private static final Logger logger = LogManager.getLogger();
    BorderPane pane;
    TextField usernameBox, passwordBox;
    static String username;
    ClientController clientController;

    public LoginView(ClientController controller) {
        this.clientController = controller;
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setCenter(userLogin());
    }

    public BorderPane getView() {
        return pane;
    }

    public GridPane userLogin() {
        GridPane grid = Utils.initWindow("Login");

        Label usrLabel = new Label("Username:");
        grid.add(usrLabel, 0, 2);
        usernameBox = new TextField();
        grid.add(usernameBox, 1, 2);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);
        passwordBox = new PasswordField();
        grid.add(passwordBox, 1, 3);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                login(event);

            }
        });
        hbBtn.getChildren().addAll(loginBtn);
        grid.add(hbBtn, 0, 6, 2, 1);

        return grid;
    }

    private void login(ActionEvent event) {
        username = usernameBox.getText();
        String password = passwordBox.getText();
        try {
            clientController.login(username, password);
            Utils.showDialog("Login reusit", "Hello", Alert.AlertType.INFORMATION);
            //GeneralController generalController = new GeneralController(username, clientController.getServer());
            Scene primaryScene = ((Node) (event.getSource())).getScene();
            Pane root = new StartGameView(this.clientController).getView();
            Stage stage = new Stage();
            stage.setTitle("Main View");
            stage.setScene(new Scene(root, 1200, 600));
            stage.show();
            //Hide this current window (if this is what you want)
            primaryScene.getWindow().hide();

        } catch (ServerException e) {
            Utils.showDialog("Login nereusit: " + e.getMessage(), "Eroare", Alert.AlertType.ERROR);
        }

    }


}
