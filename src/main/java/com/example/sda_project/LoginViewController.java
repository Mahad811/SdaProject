package com.example.sda_project;

import com.example.sda_project.model.PRMS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    // Example for Inmate Button
    @FXML
    private Button inmateButton;

    // Example for Guard Button
    @FXML
    private Button guardButton;

    // Example for Admin Button
    @FXML
    private Button adminButton;

    // Example for ERT Leader Button
    @FXML
    private Button ertButton;

    // Example for Counselor Button
    @FXML
    private Button counselorButton;
    public PRMS prms; // Reference to the PRMS singleton

    @FXML
    public void initialize() {
        prms = PRMS.getInstance(); // Link to the existing PRMS instance
        //prms.display();
    }
    // This method will be triggered when the Inmate button is clicked
    @FXML
    protected void onInmateButtonClick() {
        // Logic to open Inmate login page or other actions
        System.out.println("Inmate button clicked!");
    }

    // This method will be triggered when the Guard button is clicked
    @FXML
    protected void onGuardButtonClick(ActionEvent event) {
        // Logic to open Guard login page or other actions
        System.out.println("Guard button clicked!");
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guard_login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) from the event source (the button)
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene and show the stage
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception if the FXML fails to load
        }
    }

    // This method will be triggered when the Admin button is clicked
    @FXML
    protected void onAdminButtonClick(ActionEvent event) {
        // This logic is triggered when the "Prison Administrator" button is clicked

        System.out.println("Admin button clicked!");

        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("prison_administrator_login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) from the event source (the button)
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene and show the stage
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception if the FXML fails to load
        }
    }

    // This method will be triggered when the ERT Leader button is clicked
    @FXML
    protected void onERTButtonClick() {
        // Logic to open ERT Leader page or other actions
        System.out.println("ERT Leader button clicked!");
    }

    // This method will be triggered when the Counselor button is clicked
    @FXML
    protected void onCounselorButtonClick() {
        // Logic to open Counselor page or other actions
        System.out.println("Counselor button clicked!");
    }
}
