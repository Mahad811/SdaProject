package com.example.sda_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import com.example.sda_project.model.Inmate;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TrackingInmateController1 {

    private int guard_id;

    public void setGuard_id(int id) {this.guard_id = id;}
    public int getGuard_id() {return this.guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private TableColumn<Inmate, Integer> idColumn;

    @FXML
    private TableView<Inmate> inmateTable;

    @FXML
    private TextField inmatesearchField;

    @FXML
    private TableColumn<Inmate, String> nameColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Inmate, String> statusColumn;

    // Initialize method to set up TableView columns
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    // Method to go back to the previous menu
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

    // Method to search for an inmate by ID and display the information in the TableView
    @FXML
    void onSearchButtonClick(ActionEvent event) {
        String inmateIdText = inmatesearchField.getText();

        if (inmateIdText.isEmpty()) {
            showAlert("Error", "Please enter an inmate ID.");
            return;
        }

        try {
            int inmateId = Integer.parseInt(inmateIdText);
            MainController mainController = new MainController();
            Inmate inmate = mainController.searchInmateById(inmateId);

            if (inmate != null) {
                ObservableList<Inmate> inmateData = FXCollections.observableArrayList();
                inmateData.add(inmate);
                inmateTable.setItems(inmateData);
            } else {
                showAlert("No Results", "No inmate found with the given ID.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid ID format. Please enter a numeric ID.");
        }
    }

    // Method to fetch the inmate by ID from the database


    // Display alert messages
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
