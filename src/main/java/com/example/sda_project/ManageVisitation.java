package com.example.sda_project;

import com.example.sda_project.db.DataBaseHelper;
import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.Visitation;
import com.example.sda_project.model.PRMS;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ManageVisitation {

    @FXML
    private ComboBox<String> inmateDropdown;

    @FXML
    private ComboBox<String> prisonDropdown;

    @FXML
    private ComboBox<String> visitorDropdown;

    @FXML
    private DatePicker visitDatePicker;

    @FXML
    private Label rulesViolationLabel;

    @FXML
    private TableView<Visitation> visitationTable;

    @FXML
    private TableColumn<Visitation, String> inmateColumn;

    @FXML
    private TableColumn<Visitation, String> visitorColumn;

    @FXML
    private TableColumn<Visitation, String> visitTimeColumn;

    @FXML
    private Button confirmVisitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    private DataBaseHelper databaseUtil = new DataBaseHelper();
    public PRMS prms; // Reference to the PRMS singleton

    @FXML
    public void initialize() {
        prms = PRMS.getInstance();
        prms.display();
        // Fetch inmates and populate the dropdown
        List<Inmate> inmates = databaseUtil.fetchInmates();
        ObservableList<String> inmateNames = FXCollections.observableArrayList();

        // Populate the ObservableList with names
        for (Inmate inmate : inmates) {
            inmateNames.add(inmate.getName()); // Assuming Inmate has a getName() method
        }
        inmateDropdown.getItems().clear();
        inmateDropdown.getItems().addAll(inmateNames);

        // Fetch prisons from the database
        List<String> prisons = databaseUtil.fetchPrisons();
        ObservableList<String> observablePrisons = FXCollections.observableArrayList(prisons);
        prisonDropdown.getItems().clear();
        prisonDropdown.getItems().addAll(observablePrisons);

        // Static visitors for demonstration purposes
        visitorDropdown.getItems().addAll("Visitor 1", "Visitor 2", "Visitor 3");

        // Set up visitation table columns
        inmateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getInmateID())));

        visitorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVisitorName()));

        visitTimeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVisitDate().toString()));

        // Load existing visitations into the table
        loadVisitations();
    }

    @FXML
    private void onConfirmVisit() throws SQLException {
        String selectedInmatee = inmateDropdown.getSelectionModel().getSelectedItem();
        String selectedPrison = prisonDropdown.getSelectionModel().getSelectedItem();
        String selectedVisitor = visitorDropdown.getSelectionModel().getSelectedItem();
        LocalDate visitDate = visitDatePicker.getValue();

        Inmate selectedInmate = prms.inmates.getFirst();
        for(Inmate i: prms.getInmates())
        {
            if (i.getName().equals(selectedInmatee))
            {
                selectedInmate = i;
            }

        }
        if (selectedInmate == null || selectedPrison == null || selectedVisitor == null || visitDate == null) {
            rulesViolationLabel.setText("Please select all fields.");
            rulesViolationLabel.setVisible(true);
            return;
        }

        // Check if the inmate's name and prison match the selected values
        if (!databaseUtil.isInmateInPrison(selectedInmate.getName(), selectedPrison)) {
            rulesViolationLabel.setText("Inmate does not belong to the selected prison with the correct name.");
            rulesViolationLabel.setVisible(true);
            return;
        }

        LocalDateTime visitDateTime = visitDate.atStartOfDay();

        // Check if the visitation slot is available
        if (!databaseUtil.isVisitationSlotAvailable(selectedInmate.getInmateID(), selectedVisitor, visitDateTime)) {
            rulesViolationLabel.setText("Visitation slot already booked for this visitor and inmate on the selected date.");
            rulesViolationLabel.setVisible(true);
            return;
        }

        MainController mainController = new MainController();

        if (mainController.addVisitation(selectedInmate.getInmateID(), selectedVisitor, visitDateTime)) {
            inmateDropdown.getSelectionModel().clearSelection();
            prisonDropdown.getSelectionModel().clearSelection();
            visitorDropdown.getSelectionModel().clearSelection();
            visitDatePicker.setValue(null);
            rulesViolationLabel.setVisible(false);
            loadVisitations();
        } else {
            rulesViolationLabel.setText("Error adding visitation.");
            rulesViolationLabel.setVisible(true);
        }
    }

    @FXML
    private void onCancel() {
        inmateDropdown.getSelectionModel().clearSelection();
        prisonDropdown.getSelectionModel().clearSelection();
        visitorDropdown.getSelectionModel().clearSelection();
        visitDatePicker.setValue(null);
        rulesViolationLabel.setVisible(false);
    }

    @FXML
    private void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load the main menu.");
        }
    }

    private void loadVisitations() {
        List<Visitation> visitations = databaseUtil.fetchVisitations();
        visitationTable.getItems().clear();
        visitationTable.getItems().addAll(visitations);
        prms.display();
    }
}
