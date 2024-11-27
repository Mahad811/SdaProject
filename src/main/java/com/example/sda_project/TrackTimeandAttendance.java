package com.example.sda_project;

import com.example.sda_project.db.DataBaseHelper;
import com.example.sda_project.model.AttendanceRecord;
import com.example.sda_project.model.Guard;
import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.PRMS;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.stream.Collectors;

public class TrackTimeandAttendance {

    @FXML
    private RadioButton guardRadioButton;
    @FXML
    private RadioButton inmateRadioButton;
    @FXML
    private ComboBox<String> nameComboBox;
    @FXML
    private DatePicker attendanceDatePicker;
    @FXML
    private TableView<AttendanceRecord> attendanceTable;
    @FXML
    private Button saveButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button backButton;
    @FXML
    private TableColumn<AttendanceRecord, String> guardIdColumn;
    @FXML
    private TableColumn<AttendanceRecord, String> inmateIdColumn;
    @FXML
    private TableColumn<AttendanceRecord, LocalDateTime> attendanceDateColumn;
    @FXML
    private TableColumn<AttendanceRecord, String> attendanceTypeColumn;


    private DataBaseHelper database; // Instance of the Database class to interact with DB
    public PRMS prms;
    @FXML
    private void initialize() {
        prms = PRMS.getInstance();
        prms.display();
        database = new DataBaseHelper(); // Initialize the database connection
        onGuardSelected(); // Default to Guard Attendance

        // Initialize TableView columns
        guardIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuardID()));
        inmateIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInmateID()));
        attendanceDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getShiftStart()));
        attendanceTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAttendanceType()));

        // Fetch attendance records and populate the TableView
        fetchAttendanceRecords();
    }

    private void fetchAttendanceRecords() {
        try {
            // Fetch attendance records from the database
            List<AttendanceRecord> records = database.fetchAttendanceRecords();

            // Create an ObservableList from the list of attendance records
            ObservableList<AttendanceRecord> attendanceList = FXCollections.observableArrayList(records);

            // Set the items in the TableView
            attendanceTable.setItems(attendanceList);
        } catch (SQLException e) {
            System.out.println("Error fetching attendance records: " + e.getMessage());
        }
    }
    @FXML
    private void onGuardSelected() {
        System.out.println("Fetching guards from database...");
        inmateRadioButton.setSelected(false);

        nameComboBox.getItems().clear(); // Clear existing items
        try {
            // Fetch guards from the database
            List<Guard> guards = DataBaseHelper.fetchGuards();

            // Extract guard names using streams
            List<String> guardNames = guards.stream()
                    .map(Guard::getName) // Get the name property
                    .collect(Collectors.toList());

            // Populate the ComboBox with guard names
            nameComboBox.getItems().addAll(guardNames);
        } catch (SQLException e) {
            System.out.println("Error fetching guards: " + e.getMessage());
        }
    }

    @FXML
    private void onInmateSelected() {
        System.out.println("Fetching inmates from database...");
        guardRadioButton.setSelected(false);
        nameComboBox.getItems().clear(); // Clear existing items
        try {
            // Fetch inmates from the database
            List<Inmate> inmates = DataBaseHelper.fetchInmates();

            // Extract inmate names using streams
            List<String> inmateNames = inmates.stream()
                    .map(Inmate::getName) // Get the name property
                    .collect(Collectors.toList());

            // Populate the ComboBox with inmate names
            nameComboBox.getItems().addAll(inmateNames);
        } catch (Exception e) {
            System.out.println("Error fetching inmates: " + e.getMessage());
        }
    }
    @FXML
    private void onSaveButtonClick() {
        try {
            // Get the selected name and attendance type
            String selectedName = nameComboBox.getValue();
            if (selectedName == null || attendanceDatePicker.getValue() == null) {
                System.out.println("Please select a name and date.");
                return;
            }

            boolean isGuard = guardRadioButton.isSelected();
            String attendanceType = isGuard ? "Guard" : "Inmate";

            // Prepare GuardID or InmateID based on the selection
            String guardID = null;
            String inmateID = null;

            // Only try to fetch GuardID if Guard is selected
            if (isGuard) {
                guardID = database.getGuardIDByName(selectedName); // Fetch as String directly
            } else {
                // Only try to fetch InmateID if Inmate is selected
                inmateID = database.getInmateIDByName(selectedName); // Fetch as String directly
            }

            // Convert the date to a timestamp
            LocalDate selectedDate = attendanceDatePicker.getValue();
            LocalDateTime shiftStart = selectedDate.atStartOfDay();
            MainController mainController = new MainController();
            if(mainController.addAttendenceRecord(guardID,inmateID,shiftStart,attendanceType))
            {
                fetchAttendanceRecords();
            }
            // Create AttendanceRecord with a null String for recordID (it will be auto-generated)

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving attendance: " + e.getMessage());
        }
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
            System.out.println("Unable to load the main menu: " + e.getMessage());
        }
    }
}
