package com.example.sda_project;

import com.example.sda_project.model.PRMS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class AdminMenu {
    public PRMS prms; // Reference to the PRMS singleton
    @FXML
    private Button analyzingButton;

    @FXML
    private Button backButton;

    @FXML
    private Button counsellingButton;

    @FXML
    private Button inmateProfilingButton;

    @FXML
    private Button manageVisitationButton;

    @FXML
    private Button sentenceManagemenetButton;

    @FXML
    private Button timeAttendanceButton;

    @FXML
    private Button transferGuardButton;

    @FXML
    private Button transferInmateButton;

    @FXML
    public void initialize() {
        prms = PRMS.getInstance(); // Link to the existing PRMS instance
        //prms.display();
    }

    // This method is called when the "Check Inmate Profiling" button is clicked
    @FXML
    private void onInmateProfilingClick(ActionEvent event) {
        System.out.println("Inmate Profiling clicked");
        // Navigate to the Inmate Profiling page (example)
        navigateToPage(event, "check-inmate-profile.fxml");
    }

    // This method is called when the "Track Time and Attendance" button is clicked
    @FXML
    private void onTimeAttendanceClick(ActionEvent event) {
        System.out.println("Time and Attendance clicked");
        // Navigate to the Time & Attendance page (example)
        navigateToPage(event, "track-time-and-attendance.fxml");
    }

    // This method is called when the "Manage Visitation" button is clicked
    @FXML
    private void onManageVisitationClick(ActionEvent event) {
        System.out.println("Manage Visitation clicked");
        // Navigate to the Visitation Management page (example)
        navigateToPage(event, "manage-visitation.fxml");
    }

    // This method is called when the "Back" button is clicked
    @FXML
    private void onBackButtonClick(ActionEvent event) {
        System.out.println("Back clicked");
        // Navigate back to the login page
        navigateToPage(event, "loginmenu-view.fxml");
    }
    @FXML
    private void onTransferGuardClick(ActionEvent event) {
        System.out.println("Transfer Guard clicked");
        // Navigate to the Transfer Guard page (example)
        navigateToPage(event, "transfer-guards.fxml");
    }

    // Helper method to load and display a new page
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        try {
            // Load the FXML file for the target page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root,600,400);

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTransferInmateClick(ActionEvent actionEvent) {
        System.out.println("Transfer Inmate clicked");
        // Navigate to the Visitation Management page (example)
        navigateToPage(actionEvent, "transfer-inmate.fxml");
    }
    @FXML
    void onSetenceManagemetClick(ActionEvent actionEvent) {
        System.out.println("Sentence Management clicked");
        navigateToPage(actionEvent, "sentence-view.fxml");
    }
    @FXML
    void onAnalyzingClick(ActionEvent actionEvent) {
        System.out.println("Analyzing and Reporting clicked");
        navigateToPage(actionEvent, "reports-view.fxml");
    }

    @FXML
    void onCounsellingClick(ActionEvent actionEvent) {
        System.out.println("Counselling and Rehabilitation clicked");
        navigateToPage(actionEvent, "counselling-view.fxml");
    }
}
