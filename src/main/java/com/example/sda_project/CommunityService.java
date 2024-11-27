package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CommunityService {


    private int guard_id;
    public void setGuard_id(int guard_id) {this.guard_id = guard_id;}
    public int getGuard_id() {return guard_id;}

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> communityDropdown;

    @FXML
    private TextField inmatesearchField;

    @FXML
    private ComboBox<String> performanceDropdown;

    @FXML
    private Button searchButton1;

    @FXML
    private Button submitButton;

    @FXML
    private Label eligibilityStatusLabel;

    private static final String URL =  "jdbc:sqlserver://localhost:1433;" // SQL Server URL
            + "databaseName=SDA_Assignment;encrypt=true;trustServerCertificate=true";

    private static final String USER = "HASANWAQAR";
    private static final String PASSWORD = "Hasan_SQL";

    // Initialize dropdowns with default values
    @FXML
    public void initialize() {
        communityDropdown.getItems().addAll(
                "Cleaning Public Parks",
                "Assisting in Community Kitchens",
                "Street Cleaning",
                "Helping in Old Age Homes"
        );

        performanceDropdown.getItems().addAll("Good", "Bad");
    }

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
        String inmateIdText = inmatesearchField.getText().trim();

        if (inmateIdText.isEmpty()) {
            showAlert("Input Error", "Please enter an inmate ID.");
            return;
        }
        MainController mainController = new MainController();
        mainController.checkInmateELigibility(inmateIdText);

    }

    @FXML
    void onSubmitButtonClick(ActionEvent event) {
        String inmateIdText = inmatesearchField.getText().trim();
        String selectedCommunity = communityDropdown.getValue();
        String selectedPerformance = performanceDropdown.getValue();

        if (inmateIdText.isEmpty()) {
            showAlert("Input Error", "Please enter an inmate ID.");
            return;
        }

        if (selectedCommunity == null || selectedPerformance == null) {
            showAlert("Input Error", "Please select both a community service and performance level.");
            return;
        }
        MainController mainController = new MainController();
        mainController.addCommunityService(inmateIdText, selectedCommunity, selectedPerformance,guard_id);
    }


    // Utility method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
