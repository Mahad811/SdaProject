package com.example.sda_project;

import com.example.sda_project.db.DataBaseHelper;
import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.PRMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class AddInmate {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField transferStatusField;
    @FXML private TextField locationField;
    @FXML private Button saveButton;
    @FXML private ComboBox<String> prisonComboBox;  // ComboBox for prison name
    public PRMS prms; // Reference to the PRMS singleton
    @FXML
    private ListView<String> inmatesListView;


    @FXML
    public void initialize() {
        prms = PRMS.getInstance(); // Link to the existing PRMS instance
        List<String> prisons = DataBaseHelper.fetchPrisons();
        ObservableList<String> observablePrisons = FXCollections.observableArrayList(prisons);
        prisonComboBox.getItems().clear();
        prisonComboBox.getItems().addAll(observablePrisons);

        //prms.display();
    }
    @FXML
    private void onSaveButtonClick(ActionEvent event) {
        // Retrieve the input data
        String name = nameField.getText();
        String ageText = ageField.getText();
        String gender = genderComboBox.getValue();
        String transferStatus = transferStatusField.getText();
        String location = locationField.getText();
        String prisonName = prisonComboBox.getValue();  // Get the selected prison name

        // Input validation
        if (name.isEmpty() || ageText.isEmpty() || gender == null || gender.isEmpty() || location.isEmpty() || prisonName == null || prisonName.isEmpty()) {
            // Show error message if any required field is missing
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        // Parse age
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            // Show error if age is not a valid integer
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Age");
            alert.setContentText("Age must be a valid number.");
            alert.showAndWait();
            return;
        }

        MainController mainController = new MainController();
        mainController.addNewInmate(name,age,gender,transferStatus,location,prisonName);

        // Optionally, if you're using a ListView or TableView to display inmates, update it here
        updateInmateListView();

    }

    private void updateInmateListView() {
        // Assuming you have a ListView to display inmates in the UI
        ObservableList<String> inmateNames = FXCollections.observableArrayList();
        for (Inmate inmate : prms.getInmates()) {
            inmateNames.add(inmate.getName());  // Assuming you want to display inmate names
        }
        inmatesListView.setItems(inmateNames);  // Update the ListView
    }

    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        // Logic for canceling the operation, if needed
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            // Load the previous scene (replace with the scene you want to go back to)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            // Get the current stage (window)
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle error (could log or show an alert)
        }
    }
}
