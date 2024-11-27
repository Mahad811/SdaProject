package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ReportIncident {

    private int guard_id;
    public void setGuard_id(int guard_id) {this.guard_id = guard_id;}
    public int getGuard_id() {return guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private TextField dateOfOccurrenceField;

    @FXML
    private TextField guardIDField;

    @FXML
    private TextArea incidentDetailsField;

    @FXML
    private TextField inmateIDField;

    @FXML
    private TextField inmateNameField;

    @FXML
    private TextField locationField;

    @FXML
    private Button submitButton;

    @FXML
    void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guard_menu.fxml"));
            Parent root = loader.load();
            GuardMenu c = loader.getController();
            c.setGuard_id(guard_id);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSubmitButtonClick(ActionEvent event) throws SQLException {
        // Retrieve the data entered in the fields
        String date = dateOfOccurrenceField.getText().trim();
        String inmateName = inmateNameField.getText().trim();
        String location = locationField.getText().trim();
        String details = incidentDetailsField.getText().trim();

        if (date.isEmpty() || inmateName.isEmpty() || location.isEmpty() || details.isEmpty()) {
            showAlert(AlertType.ERROR, "Missing Information", "All fields must be filled!");
            return;
        }

        int inmateId;
        int guardId;

        try {
            inmateId = Integer.parseInt(inmateIDField.getText().trim());
            guardId = guard_id;
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Inmate ID and Guard ID must be numeric.");
            return;
        }


        MainController mainController = new MainController();

        if (mainController.addIncident(date, inmateId, inmateName, guardId, location, details)) {

            showAlert(AlertType.INFORMATION, "Success", "Incident record successfully added!");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to add incident record.");
        }
    }




    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
