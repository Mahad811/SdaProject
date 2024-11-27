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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PrisonAdminLoginController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField adminIdField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button nextButton;
    @FXML
    private Button signUpButton;
    public PRMS prms; // Reference to the PRMS singleton

    @FXML
    public void initialize() {
        prms = PRMS.getInstance(); // Link to the existing PRMS instance
        prms.display();
    }

    // This method is called when the "Next" button is clicked
    @FXML
    private void onNextButtonClick(ActionEvent event) {
        String name = nameField.getText();
        String password = passwordField.getText();  // Add password field here

        // Check if any field is empty
        if (name.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please fill in all fields.");
        } else {
            // Check if the admin exists in the database with the provided credentials
            boolean isValidAdmin = DataBaseHelper.checkAdminLogin(name, password);
            if (isValidAdmin) {
                // Navigate to Admin Menu if credentials are valid
                navigateToAdminMenu(event);
            } else {
                showAlert(AlertType.ERROR, "Login Error", "Admin not found. Please check your credentials.");
            }
        }
    }


    // Helper method to load the Admin Menu page
    private void navigateToAdminMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Unable to load Admin Menu.");
        }
    }

    // This method will be triggered when the Back button is clicked
    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        try {
            // Load the main page (the first page)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginmenu-view.fxml")); // Update with correct FXML path
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Handle the exception if the FXML fails to load
        }
    }

    // Helper method to show an alert with the given type, title, and message
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
