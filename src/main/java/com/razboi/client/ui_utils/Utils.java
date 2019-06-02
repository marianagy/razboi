package com.razboi.client.ui_utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Utils {
    public static GridPane initWindow(String sceneTitleText) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        Text scenetitle = new Text(sceneTitleText);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        scenetitle.setFill(Color.BLUE);
        grid.add(scenetitle, 0, 0, 2, 1);
        return grid;
    }

    public static void showDialog(String text, String title, Alert.AlertType type) {
        Alert message = new Alert(type);
        message.setTitle(title);
        message.setContentText(text);
        message.showAndWait();
    }
}
