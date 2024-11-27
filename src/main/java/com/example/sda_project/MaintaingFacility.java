package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MaintaingFacility {

    private int guard_id;
    public void setGuard_id(int guard_id) {this.guard_id = guard_id;}
    public int getGuard_id() {return guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private TextField dateField;

    @FXML
    private TextArea detailsField;

    @FXML
    private TextField guardIDField;

    @FXML
    private TextField locationField;

    @FXML
    private Button submithButton;

    @FXML
    private TextField urgencyField;



    @FXML
    void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guard_menu.fxml"));
            Parent root = loader.load();
            GuardMenu c = loader.getController();
            c.setGuard_id(guard_id);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene and show the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        // Retrieve the data entered in the fields
        String date = dateField.getText().trim();
        String location = locationField.getText().trim();
        String urgencyLevel = urgencyField.getText().trim();

        String details = detailsField.getText().trim();

        // Validate the input fields
        if (date.isEmpty() || location.isEmpty() || urgencyLevel.isEmpty() || details.isEmpty()) {
            System.out.println("All fields must be filled!");
            return;
        }

        int guardId = guard_id;

        MainController mainController = new MainController();
        mainController.addMaintenance(0,date,location,urgencyLevel,guardId,details);

    }

}