package com.example.sda_project;

import com.example.sda_project.db.DataBaseHelper;
import com.example.sda_project.model.PRMS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TransferInmate {

    @FXML private TextField inmateIDField;
    @FXML private TextField inmateNameField;
    @FXML private TextField currentPrisonField;
    @FXML private ComboBox<String> destinationPrisonComboBox;
    @FXML private DatePicker transferDatePicker;
    @FXML private TextArea notesField;
    public PRMS prms; // Reference to the PRMS singleton

    @FXML
    private void initialize() {
        prms = PRMS.getInstance();
        prms.display();
        List<String> prisonNames = DataBaseHelper.fetchPrisons();
        destinationPrisonComboBox.getItems().addAll(prisonNames);
    }
    @FXML
    private void onTransferButtonClick(ActionEvent event) {
        String inmateID = inmateIDField.getText();
        String currentPrison = currentPrisonField.getText();
        String destinationPrison = destinationPrisonComboBox.getValue();
        String transferDate = transferDatePicker.getValue() != null ? transferDatePicker.getValue().toString() : "";
        String notes = notesField.getText();

        if (inmateID.isEmpty() || currentPrison.isEmpty() || destinationPrison == null || transferDate.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        try {
            MainController mainController = new MainController();
            mainController.transferInmate(inmateID,destinationPrison,transferDate,currentPrison);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
        }
    }

    // Action for Cancel Button
    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        // Close the current page or navigate back to the admin menu
        // This could be implemented by closing the window or switching scenes
    }

    // Method to show error message
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show success message
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        try {
            // Load the previous scene (replace with the scene you want to go back to)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            // Get the current stage (window)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle error (could log or show an alert)
        }
    }
}
