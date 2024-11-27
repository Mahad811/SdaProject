package com.example.sda_project;
import com.example.sda_project.db.*;
import com.example.sda_project.model.CourtCase;

import com.example.sda_project.model.PRMS;
import javafx.collections.*;
import javafx.fxml.*;

import java.sql.*;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SentenceController {

    public PRMS prms;

    @FXML
    private Label blue_sects;

    @FXML
    private Label title_label;

    @FXML
    private Button enter_sent_btn;

    @FXML
    private Button back_sent_btn;
    @FXML
    private Button enter_btn;

    @FXML
    private RadioButton recent_cases;

    @FXML
    private RadioButton search_inmates;

    @FXML
    private VBox sent_detail_box;

    @FXML
    private ToggleGroup sent_man_group;

    @FXML
    private TableView<CourtCase> tableview_cases;

    @FXML
    private TextField title_field;

    @FXML
    private Label toggle_text;
    @FXML
    private TableColumn<CourtCase, Integer> col_caseId;
    @FXML
    private TableColumn<CourtCase, String> col_caseName;
    @FXML
    private TableColumn<CourtCase, Date> col_caseDate;
    @FXML
    private TableColumn<CourtCase, Integer> col_inmateId;
    @FXML
    private TableColumn<CourtCase, String> col_inmateName;

    @FXML
    private ComboBox<String> sent_type;

    @FXML
    private DatePicker sent_date;

    @FXML
    private CheckBox parole;

    @FXML
    public void initialize() {
        // Initially, hide the TableView
        tableview_cases.setVisible(true);
        sent_detail_box.setVisible(false);

        // Listen for changes in the ToggleGroup (RadioButton selection)
        sent_man_group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == recent_cases) {
                tableview_cases.setVisible(true);  // Show the TableView when the first RadioButton is selected
                toggle_text.setText("Enter Inmate ID:");
                toggle_text.setTextAlignment(TextAlignment.RIGHT);
            } else if (newToggle == search_inmates) {
                tableview_cases.setVisible(false); // Hide the TableView when the second RadioButton is selected
                toggle_text.setText("Search Inmate ID:");
                toggle_text.setTextAlignment(TextAlignment.LEFT);
            }
        });
        col_caseId.setCellValueFactory(new PropertyValueFactory<>("caseid"));
        col_caseName.setCellValueFactory(new PropertyValueFactory<>("casename"));
        col_caseDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_inmateId.setCellValueFactory(new PropertyValueFactory<>("inmateid"));
        col_inmateName.setCellValueFactory(new PropertyValueFactory<>("inmatename"));

        loadCourtCasesFromDatabase();
        prms = PRMS.getInstance();
        prms.display();


    }
    private ObservableList<CourtCase> courtCases =  FXCollections.observableArrayList();
    ;
    private void loadCourtCasesFromDatabase() {
        List<CourtCase> courtCaseList = DataBaseHelper.getCourtCases(); // Replace with actual DB fetch logic

        for (CourtCase courtCase : courtCaseList) {
            System.out.println(courtCase);
        }
        // Convert List to ObservableList
        courtCases.setAll(courtCaseList);

        // Set the items in the TableView
        tableview_cases.setItems(courtCases);
    }
    @FXML
    void btnEnterClick(ActionEvent event) {
        int inmateid = 0;
        try {
            inmateid = Integer.parseInt(title_field.getText());
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter valid number.");
            alert.showAndWait();
            return;
        }

        if (!DataBaseHelper.checkInmateExistence(inmateid)){ Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Inmate does not exist.");
            alert.showAndWait();
            return;}


        tableview_cases.setVisible(false);
        recent_cases.setVisible(false);
        search_inmates.setVisible(false);
        blue_sects.setVisible(false);
        title_field.setVisible(false);
        enter_btn.setVisible(false);
        sent_detail_box.setVisible(true);



        Stage mainWindow = (Stage) title_field.getScene().getWindow();
        String title = title_field.getText();
        mainWindow.setTitle(title);

    }

    @FXML
    void backSentClick(ActionEvent event) {
        tableview_cases.setVisible(true);
        recent_cases.setVisible(true);
        search_inmates.setVisible(true);
        blue_sects.setVisible(true);
        title_field.setVisible(true);
        enter_btn.setVisible(true);
        sent_detail_box.setVisible(false);

        sent_type.getSelectionModel().clearSelection();
        sent_date.setValue(null); // This will set it to null (no date selected)
        parole.setSelected(false);
        title_field.clear();
    }

    @FXML
    void enterSentClick(ActionEvent event) {
        if (title_field.getText().equals("") || sent_date.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter all fields correctly.");
            alert.showAndWait();
            return;}
        else {
            String type = sent_type.getValue();  // Get the selected type from ComboBox
            Date date = java.sql.Date.valueOf(sent_date.getValue());  // Convert LocalDate to java.sql.Date
            boolean paroleAllowed = parole.isSelected();  // Get whether parole is allowed
            int inmateid = Integer.parseInt(title_field.getText());  // Get inmate ID from TextField
            MainController mainController = new MainController();
            mainController.addSentence(date, type, paroleAllowed, inmateid);
        }

        tableview_cases.setVisible(true);
        recent_cases.setVisible(true);
        search_inmates.setVisible(true);
        blue_sects.setVisible(true);
        title_field.setVisible(true);
        enter_btn.setVisible(true);

        sent_detail_box.setVisible(false);


        sent_type.getSelectionModel().clearSelection();
        sent_date.setValue(null); // This will set it to null (no date selected)
        parole.setSelected(false);
        title_field.clear();

        prms.display();

    }
    @FXML
    void onReturnButton(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene and show the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

