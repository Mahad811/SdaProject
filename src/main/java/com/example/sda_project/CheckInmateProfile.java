package com.example.sda_project;

import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.PRMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckInmateProfile {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Inmate> inmateTable;

    @FXML
    private Button searchButton, addInmateButton, backButton;

    private ObservableList<Inmate> inmateData = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Inmate, Integer> idColumn; // Changed to Integer to match inmateID type
    @FXML
    private TableColumn<Inmate, String> nameColumn;
    @FXML
    private TableColumn<Inmate, Integer> AgeColumn;
    @FXML
    private TableColumn<Inmate, String> GenderColumn;
    public PRMS prms; // Reference to the PRMS singleton
    @FXML
    public void initialize() {
        prms = PRMS.getInstance();
        //prms.display();
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("inmateID")); // Corrected the field name to "inmateID"
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        GenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Load initial data (mock or fetched from database)
        inmateTable.setItems(inmateData);
    }

    @FXML
    public void onSearchButtonClick() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            showAlert("Search Error", "Please enter a name or ID to search.");
            return;
        }

        // Search the inmate in the database using the provided name or ID
        MainController mainController = new MainController();
        Inmate foundInmate = mainController.searchInmate(query);
        System.out.println("Found inmate: " + foundInmate);

        if (foundInmate != null) {
            inmateData.clear();  // Clear existing data
            inmateData.add(foundInmate);  // Add the found inmate's details
        } else {
            showAlert("No Results", "No inmate found with the provided details.");
        }
    }

    @FXML
    // This method is triggered when the "Add New Inmate" button is clicked
    public void onAddInmateButtonClick() {
        try {
            // Load the "Add New Inmate" FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-inmate.fxml"));
            Parent root = loader.load();
            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root, 600, 400);

            // Get the current stage (window) and set the scene to show the "Add New Inmate" screen
            Stage stage = (Stage) addInmateButton.getScene().getWindow(); // Assuming you have a reference to the button
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println(e);
            // Handle error if the FXML file is not found or there's an issue with loading it
            showAlert("Error", "Failed to load the Add New Inmate screen.");
        }
    }


    @FXML
    public void onBackButtonClick() {
        try {
            // Load the main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            // Get the current stage and set the new scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);

            stage.setTitle("Main Menu"); // Update the title if necessary
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load the main menu.");
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
