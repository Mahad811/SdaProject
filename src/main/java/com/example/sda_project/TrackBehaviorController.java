package com.example.sda_project;

import com.example.sda_project.model.Behavior;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;

public class TrackBehaviorController {

    public PRMS prms;
    public int loggedin_guardid;
    public void setLoggedin_guardid(int loggedin_guardid) {this.loggedin_guardid = loggedin_guardid;}

    public int getLoggedin_guardid() {
        return loggedin_guardid;
    }

    @FXML
    private Button addnew_btn;

    @FXML
    private TableView<Behavior> behav_view;

    @FXML
    private Label blue_sects;

    @FXML
    private Label blue_sects2;

    @FXML
    private Button enter_btn;

    @FXML
    private ComboBox<String> new_behav;

    @FXML
    private DatePicker new_date;

    @FXML
    private TextField new_desc;

    @FXML
    private TextField new_incident;

    @FXML
    private RadioButton search_inmates;

    @FXML
    private ToggleGroup sent_man_group;


    @FXML
    private TextField title_field;

    @FXML
    private Label title_label;

    @FXML
    private Label toggle_text;

    @FXML
    private Button track_rtn_btn;

    @FXML
    private RadioButton view_imates;

    @FXML
    private TableView<Inmate> tableview_inmates;

    @FXML
    private TableColumn<Inmate, Integer> col_inmateId;

    @FXML
    private TableColumn<Inmate, String> col_inmateName;

    @FXML
    private TableColumn<Inmate, Integer> col_inmateAge;

    @FXML
    private TableColumn<Inmate, String> col_inmateGender;

    private ObservableList<Inmate> inmateList;

    @FXML
    private TableColumn<Behavior, String> col_behavior; // Column for Behavior type

    @FXML
    private TableColumn<Behavior, String> col_incident; // Column for Incident

    @FXML
    private TableColumn<Behavior, String> col_description; // Column for Description

    @FXML
    private TableColumn<Behavior, String> col_date; // Column for Date

    @FXML
    private TableColumn<Behavior, Integer> col_reportingGuardId; // Column for Reporting Guard ID

    @FXML
    public void initialize() {
        // Initially, hide the TableView
        tableview_inmates.setVisible(true);
        behav_view.setVisible(false);
        blue_sects2.setVisible(false);
        new_behav.setVisible(false);
        new_date.setVisible(false);
        new_desc.setVisible(false);
        new_incident.setVisible(false);
        addnew_btn.setVisible(false);

        new_behav.getItems().addAll("Bad", "Good");

        // Listen for changes in the ToggleGroup (RadioButton selection)
        sent_man_group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == view_imates) {
                tableview_inmates.setVisible(true);  // Show the TableView when the first RadioButton is selected
                toggle_text.setText("Enter Inmate ID:");
                toggle_text.setTextAlignment(TextAlignment.RIGHT);
            } else if (newToggle == search_inmates) {
                tableview_inmates.setVisible(false); // Hide the TableView when the second RadioButton is selected
                toggle_text.setText("Search Inmate ID:");
                toggle_text.setTextAlignment(TextAlignment.LEFT);
            }
        });


        prms = PRMS.getInstance();
        prms.display();

        loadInmates();


    }
    public void loadInmates() {
        col_inmateId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_inmateName.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_inmateAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_inmateGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        inmateList = FXCollections.observableArrayList(prms.inmates);

        // Set the data into the TableView
        tableview_inmates.setItems(inmateList);
    }
    public void loadBehaviors() {
        // Set up the TableColumn mappings
        col_behavior.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_incident.setCellValueFactory(new PropertyValueFactory<>("incident"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_reportingGuardId.setCellValueFactory(new PropertyValueFactory<>("reportingGuardID"));

        // Create an ObservableList of Behavior objects
        List<Behavior> b;
        for(Inmate i: prms.inmates){
            if (i.getId() == Integer.parseInt(title_field.getText())){
                b = i.getBehaviors();

                ObservableList<Behavior> behaviorList = FXCollections.observableArrayList(b);

                // Set the data into the TableView
                behav_view.setItems(behaviorList);
            }

        }
    }

    @FXML
    void trackRtnClick(ActionEvent event) {
        System.out.println(loggedin_guardid);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("guard_menu.fxml"));
            Parent root = loader.load();
            GuardMenu c = loader.getController();
            c.setGuard_id(loggedin_guardid);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        boolean found = false;
        for(Inmate i : prms.inmates){
            if (i.getId() == inmateid){ found = true; break; }
        }
        if (!found){ Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Inmate does not exist.");
            alert.showAndWait();
        return;}
        tableview_inmates.setVisible(false);
        view_imates.setVisible(false);
        search_inmates.setVisible(false);
        blue_sects.setVisible(false);
        title_field.setVisible(false);
        enter_btn.setVisible(false);
        behav_view.setVisible(true);
        blue_sects2.setVisible(true);
        new_behav.setVisible(true);
        new_date.setVisible(true);
        new_desc.setVisible(true);
        new_incident.setVisible(true);
        addnew_btn.setVisible(true);
        title_label.setText("Inmate Behaviors");
        track_rtn_btn.setOnAction(e -> {
            tableview_inmates.setVisible(true);
            view_imates.setVisible(true);
            search_inmates.setVisible(true);
            blue_sects.setVisible(true);
            title_field.setVisible(true);
            enter_btn.setVisible(true);
            blue_sects2.setVisible(false);
            new_behav.setVisible(false);
            new_date.setVisible(false);
            new_desc.setVisible(false);
            new_incident.setVisible(false);
            addnew_btn.setVisible(false);
            behav_view.setVisible(false);
            title_label.setText("Track Inmate Behavior");
            track_rtn_btn.setOnAction(this::trackRtnClick);
        });

        loadBehaviors();

    }
    @FXML
    void addEnterClick(ActionEvent event) {
        try {
            if ( new_date.getValue() == null || new_behav.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
                alert.setTitle("Error");
                alert.setContentText("Enter all fields correctly.");
                alert.showAndWait();
                return;
            }
            // Extract the values from the TextFields
            String type = new_behav.getValue().trim();
            String incident = new_incident.getText().trim();
            String description = new_desc.getText().trim();
            Date date = java.sql.Date.valueOf(new_date.getValue());  // Convert LocalDate to java.sql.Date
            int inmateid = Integer.parseInt(title_field.getText().trim());

            // Validate that none of the required fields are empty
            if (type.isEmpty() || incident.isEmpty())  {
                Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
                alert.setTitle("Error");
                alert.setContentText("Enter all fields correctly.");
                alert.showAndWait();
                return;
            }
            MainController mainController = new MainController();
            mainController.addBehavior(type,incident,description,date,loggedin_guardid,inmateid);

            // Create the Behavior object
            System.out.println(loggedin_guardid);


            loadInmates();

            new_behav.setValue(null);
            new_incident.clear();
            new_desc.clear();
            new_date.setValue(null);
            loadBehaviors();
            // Show success message
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
