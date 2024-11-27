package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TrackingInmateController2 {

    private int guard_id;

    public void setGuard_id(int id) {this.guard_id = id;}
    public int getGuard_id() {return this.guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> inmateDropdown;

    @FXML
    private TextField inmatesearchField;

    @FXML
    private Button saveButton;

    @FXML
    private Button searchButton;

    // Initialize the ComboBox with the predefined location options
    @FXML
    public void initialize() {
        inmateDropdown.getItems().addAll("Cell", "Cafeteria", "Gym", "Library", "Yard");
        inmateDropdown.setDisable(true); // Disable the dropdown initially until search is done
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        try{
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

    // This method will search the inmate by ID and show available locations in the dropdown
    @FXML
    void onSearchButtonClick(ActionEvent event)     {
        String inmateIdText = inmatesearchField.getText();

        if (inmateIdText.isEmpty()) {
            showAlert("Error", "Please enter an inmate ID.");
            return;
        }

        try {
            int inmateId = Integer.parseInt(inmateIdText);
            MainController mainController = new MainController();
            String inmateName = mainController.searchInmateNameById(inmateId);

            if (inmateName != null) {
                // If inmate found, enable the dropdown for location selection
                inmateDropdown.setDisable(false);
                showAlert("Inmate Found", "Inmate: " + inmateName + " found. Select a location.");
            } else {
                showAlert("No Results", "No inmate found with the given ID.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a numeric ID.");
        }
    }


    // When the save button is clicked, the location will be updated in the database
    @FXML
    void onSaveButtonClick(ActionEvent event) {
        String selectedLocation = inmateDropdown.getValue();
        String inmateIdText = inmatesearchField.getText();

        if (selectedLocation == null) {
            showAlert("Error", "Please select a location.");
            return;
        }

        if (inmateIdText.isEmpty()) {
            showAlert("Error", "Please enter an inmate ID.");
            return;
        }

        try {
            int inmateId = Integer.parseInt(inmateIdText);
            MainController mainController = new MainController();
            mainController.updateInmateLocation(inmateId, selectedLocation);
            showAlert("Success", "Inmate's location updated successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a numeric ID.");
        }
    }

    // Display alert messages
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
