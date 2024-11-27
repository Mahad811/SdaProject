package com.example.sda_project;

import com.example.sda_project.model.Behavior;
import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.PRMS;
import com.example.sda_project.model.TreatmentPlan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.List;

public class CounsellingController {

    public PRMS prms;
    @FXML
    private Button entr_btn;

    @FXML
    private GridPane grid_inmate;

    @FXML
    private Button rtn_btn;

    @FXML
    private TextField searchd_inmate;

    @FXML
    private TextField inmateid_field;

    @FXML
    private TextField inmateage_field;

    @FXML
    private TextField inmategender_field;

    @FXML
    private TextField inmatename_field;

    @FXML
    private TextField inmatesentdate_field;

    @FXML
    private TextField inmatesenttype_field;

    public int tempinmateid;

    @FXML
    private TableView<Behavior> table_behav;
    @FXML
    private TableColumn<Behavior, String> col_behav;
    @FXML
    private TableColumn<Behavior, String> col_incident;
    @FXML
    private TableColumn<Behavior, Date> col_date;
    @FXML
    private Label toggle_text;

    @FXML
    private Button treat_btn;

    @FXML
    private TableView<TreatmentPlan> table_treat;
    @FXML
    private TableColumn<TreatmentPlan, Integer> col_inmateId;
    @FXML
    private TableColumn<TreatmentPlan, Integer> col_counsellorId;
    @FXML
    private TableColumn<TreatmentPlan, String> col_type;
    @FXML
    private TableColumn<TreatmentPlan, String> col_description;
    @FXML
    private TableColumn<TreatmentPlan, Boolean> col_activeStatus;


    public void initialize() {
        prms = PRMS.getInstance();
        prms.display();
    }
    @FXML
    void btnEnterClick(ActionEvent event) {
        int inmateid = 0;
        try {
            inmateid = Integer.parseInt(searchd_inmate.getText());
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter valid number.");
            alert.showAndWait();
            return;
        }
        boolean found = false;
        Inmate in = null;
        for(Inmate i : prms.inmates){
            if (i.getId() == inmateid){ in = i; found = true; break; }
        }
        if (!found){ Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Inmate does not exist.");
            alert.showAndWait();
            return;}
        tempinmateid = inmateid;
        inmateid_field.setText(String.valueOf(in.getId()));
        inmatename_field.setText(in.getName());
        inmateage_field.setText(String.valueOf(in.getAge()));
        inmategender_field.setText(in.getGender());
        inmatesenttype_field.setText(in.getSentence().getType());
        inmatesentdate_field.setText(String.valueOf(in.getSentence().getDate()));

        col_behav.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_incident.setCellValueFactory(new PropertyValueFactory<>("incident"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Assuming you have a list of behaviors
        ObservableList<Behavior> behaviorList = FXCollections.observableArrayList(in.getBehaviors());

        // Set the items in the TableView
        table_behav.setItems(behaviorList);

    }

    @FXML
    void rtn_btnClick(ActionEvent event) {
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

    public void loadTreatmentPlans() {
        // Set up the TableColumn mappings
        col_inmateId.setCellValueFactory(new PropertyValueFactory<>("inmateId"));
        col_counsellorId.setCellValueFactory(new PropertyValueFactory<>("counsellorId"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_activeStatus.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Find the selected inmate and load their treatment plans
        for (Inmate inmate : prms.inmates) {
            if (inmate.getId() == tempinmateid) {
                List<TreatmentPlan> treatmentPlans = inmate.getTreatmentPlans(); // Ensure you have a getter in the Inmate class for this
                ObservableList<TreatmentPlan> treatmentPlanList = FXCollections.observableArrayList(treatmentPlans);

                // Set the data into the TableView
                table_treat.setItems(treatmentPlanList);
            }
        }
    }

    @FXML
    void btnTreatClick(ActionEvent event) {
        int inmateid = 0;
        try {
            inmateid = Integer.parseInt(searchd_inmate.getText());
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

        try{
            tempinmateid = inmateid;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("treatmentplans-view.fxml"));
            Parent root = loader.load();
            CounsellingController controller = loader.getController();
            controller.setTempinmateid(inmateid);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set the new scene and show the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setTempinmateid(int someInteger) {
        this.tempinmateid = someInteger;
        loadTreatmentPlans();

    }
    @FXML
    private Button addnew_btn;

    @FXML
    private TextField new_coun_id;

    @FXML
    private TextField new_desc;

    @FXML
    private TextField new_type;

    @FXML
    private Button rtn2_btn;


    @FXML
    void btnAddClick(ActionEvent event) {
        int inmateid = tempinmateid;
        if (inmateid == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter valid number.");
            alert.showAndWait();
            return;
        }
        int counsellorId = 0;
        try {
            counsellorId = Integer.parseInt(new_coun_id.getText().trim()); // Parse counsellor ID;
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter valid counsellor id.");
            alert.showAndWait();
            return;
        }
        if (counsellorId == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter valid counsellor id.");
            alert.showAndWait();
            return;
        }

        String type = new_type.getText().trim();                          // Get type
        String description = new_desc.getText().trim();                   // Get description

        if (type.equals("") || description.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // You can change AlertType to ERROR, WARNING, CONFIRMATION, etc.
            alert.setTitle("Error");
            alert.setContentText("Enter all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new TreatmentPlan object
        new_coun_id.setText(null);
        new_desc.setText(null);
        new_type.setText(null);
        MainController mainController = new MainController();
        mainController.addTreatment(counsellorId,inmateid,description,type);

        loadTreatmentPlans();
    }
    @FXML
    void rtn2_btnClick(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("counselling-view.fxml"));
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
