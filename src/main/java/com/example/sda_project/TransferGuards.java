package com.example.sda_project;

import com.example.sda_project.db.DataBaseHelper;
import com.example.sda_project.model.Guard;
import com.example.sda_project.model.PRMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TransferGuards {

    @FXML
    private TableView<Guard> guardTable;

    @FXML
    private TableColumn<Guard, Integer> guardIdColumn;

    @FXML
    private TableColumn<Guard, String> nameColumn;

    @FXML
    private TableColumn<Guard, Integer> ageColumn;

    @FXML
    private TableColumn<Guard, String> genderColumn;

    @FXML
    private TableColumn<Guard, Integer> prisonIdColumn;

    @FXML
    private ComboBox<String> destinationPrisonComboBox;

    @FXML
    private DatePicker transferDatePicker;

    @FXML
    private Button confirmTransferButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private ObservableList<Guard> guardData = FXCollections.observableArrayList();
    public PRMS prms;
    // Initialize the controller
    public void initialize() {
        prms = PRMS.getInstance();
        prms.display();
        try {
            // Load all guards into the table
            List<Guard> guards = DataBaseHelper.fetchGuards();
            guardData.setAll(guards);
            guardTable.setItems(guardData);

            // Set up the table columns for Guard properties using getters
            guardIdColumn.setCellValueFactory(cellData ->
                    new javafx.beans.binding.ObjectBinding<Integer>() {
                        @Override
                        protected Integer computeValue() {
                            return cellData.getValue().getId();
                        }
                    });
            nameColumn.setCellValueFactory(cellData ->
                    new javafx.beans.binding.StringBinding() {
                        @Override
                        protected String computeValue() {
                            return cellData.getValue().getName();
                        }
                    });
            ageColumn.setCellValueFactory(cellData ->
                    new javafx.beans.binding.ObjectBinding<Integer>() {
                        @Override
                        protected Integer computeValue() {
                            return cellData.getValue().getAge();
                        }
                    });
            genderColumn.setCellValueFactory(cellData ->
                    new javafx.beans.binding.StringBinding() {
                        @Override
                        protected String computeValue() {
                            return cellData.getValue().getGender();
                        }
                    });
            prisonIdColumn.setCellValueFactory(cellData ->
                    new javafx.beans.binding.ObjectBinding<Integer>() {
                        @Override
                        protected Integer computeValue() {
                            return cellData.getValue().getPrisonid();
                        }
                    });

            // Load destination prisons into the ComboBox
            List<String> prisons = DataBaseHelper.fetchPrisons();
            destinationPrisonComboBox.setItems(FXCollections.observableArrayList(prisons));

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exception
        }
    }

    @FXML
    private void onConfirmTransferClick() {
        Guard selectedGuard = guardTable.getSelectionModel().getSelectedItem();
        String selectedPrison = destinationPrisonComboBox.getValue();
        String transferDate = transferDatePicker.getValue().toString();

        if (selectedGuard != null && selectedPrison != null && !transferDate.isEmpty()) {
            try {
                MainController mainController = new MainController();
                boolean b = mainController.transferGuard(selectedGuard, selectedPrison, transferDate);
                guardTable.refresh();

            } catch (SQLException e) {
                e.printStackTrace();  // Handle any SQL errors
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating the database.");
                alert.showAndWait();
            }
        } else {
            // Handle the case where no guard is selected or invalid data is entered
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText(null);
            alert.setContentText("Please select a guard, destination prison, and transfer date.");
            alert.showAndWait();
        }
    }


    // Cancel the transfer action
    @FXML
    public void onCancelClick(ActionEvent actionEvent) {
        // Clear transfer details if needed
        guardTable.getSelectionModel().clearSelection();
        destinationPrisonComboBox.getSelectionModel().clearSelection();
        transferDatePicker.getEditor().clear();
        System.out.println("Transfer details cleared.");
    }

    // Back to the main menu
    public void onBackButtonClick(ActionEvent actionEvent) {
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
            System.out.println("Unable to load the main menu.");
        }
    }
}
