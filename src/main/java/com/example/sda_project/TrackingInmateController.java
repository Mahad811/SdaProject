package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TrackingInmateController {
    private int guard_id;

    public void setGuard_id(int id) {this.guard_id = id;}
    public int getGuard_id() {return this.guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private Button recordlocationButton;

    @FXML
    private Button viewlocationButton;


    @FXML
    void onBackButtonClick(ActionEvent event) {
        switchScene(event, "guard_menu.fxml",0);
    }

    @FXML
    void recordbuttonclicked(ActionEvent event) {
        switchScene(event, "tracking_inmate1.fxml",1);
    }

    @FXML
    void viewbuttonclicked(ActionEvent event) {
        switchScene(event, "tracking_inmate2.fxml",2);
    }

    // Utility method to load and switch to the specified FXML file
    private void switchScene(ActionEvent event, String fxmlFileName,int diff) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            if (diff == 0) {
                GuardMenu c = loader.getController();
                c.setGuard_id(guard_id);
            }
            if (diff == 1) {
                TrackingInmateController1 c = loader.getController();
                c.setGuard_id(guard_id);
            }
            if (diff == 2) {
                TrackingInmateController2 c = loader.getController();
                c.setGuard_id(guard_id);
            }
            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
