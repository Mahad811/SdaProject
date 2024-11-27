package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class IntroController {

    @FXML
    private Button startButton;

    @FXML
    private void handleStartButton(ActionEvent event) {
        try {


            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("loginmenu-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
