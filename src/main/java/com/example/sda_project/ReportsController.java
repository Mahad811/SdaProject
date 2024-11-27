package com.example.sda_project;

import com.example.sda_project.model.Guard;
import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.PRMS;
import com.example.sda_project.model.Prison;
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

public class ReportsController {

    @FXML
    private GridPane general_gid;

    @FXML
    private RadioButton general_opn;

    @FXML
    private RadioButton guar_atten_opn;

    @FXML
    private Label guard_num_text;

    @FXML
    private Label guard_num_val;

    @FXML
    private Label inmate_num_val;

    @FXML
    private RadioButton inm_atte_opn;

    @FXML
    private RadioButton inm_behav_opn;

    @FXML
    private Label inmate_num_text;

    @FXML
    private Label prison_num_text;

    @FXML
    private Label prison_num_val;

    @FXML
    private ToggleGroup reports_toggle;

    @FXML
    private ComboBox<String> selec_prisons;

    @FXML
    private Label title_label;

    public PRMS prms;
    @FXML
    private TableView<Inmate> table_inmate_behav;
    @FXML
    private TableColumn<Inmate, Integer> colInmateId;
    @FXML
    private TableColumn<Inmate, String> colInmateName;
    @FXML
    private TableColumn<Inmate, Integer> colPrisonId;
    @FXML
    private TableColumn<Inmate, Double> colBehaviorRating;

    @FXML
    private TableView<Guard> table_inmate_behav1;

    @FXML
    private TableColumn<Guard, Integer> colGuardId;

    @FXML
    private TableColumn<Guard, String> colGuardName;

    @FXML
    private TableColumn<Guard, Integer> colPrisonId1;

    @FXML
    private TableColumn<Guard, Integer> colGuardDays;

    @FXML
    private TableView<Inmate> table_inmate_behav11;

    @FXML
    private TableColumn<Inmate, Integer> colGuardId1;

    @FXML
    private TableColumn<Inmate, String> colGuardName1;

    @FXML
    private TableColumn<Inmate, Integer> colPrisonId11;

    @FXML
    private TableColumn<Inmate, Integer> colGuardDays1;


    private ObservableList<Inmate> inmateList;

    private ObservableList<Guard> guardList;

    private ObservableList<Inmate> inmateList2;

    public void initialize()
    {
        prms = PRMS.getInstance();
        ObservableList<String> items = FXCollections.observableArrayList("All Prisons");
        selec_prisons.setItems(items);
        for(Prison p: prms.getPrisons())
        {
            items = selec_prisons.getItems();
            items.add("Prison " + String.valueOf(p.getPrisonID()));
        }
        prison_num_val.setText(String.valueOf(prms.getPrisons().size()));
        inmate_num_val.setText(String.valueOf(prms.getInmates().size()));
        guard_num_val.setText(String.valueOf(prms.getGuards().size()));

        colInmateId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colInmateName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrisonId.setCellValueFactory(new PropertyValueFactory<>("prisonid"));
        colBehaviorRating.setCellValueFactory(new PropertyValueFactory<>("behaviorRating"));
        inmateList = FXCollections.observableArrayList(prms.inmates);
        table_inmate_behav.setItems(inmateList);
        table_inmate_behav.setVisible(false);

        colGuardId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGuardName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrisonId1.setCellValueFactory(new PropertyValueFactory<>("prisonid"));
        colGuardDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        guardList = FXCollections.observableArrayList(prms.guards);
        table_inmate_behav1.setItems(guardList);
        table_inmate_behav1.setVisible(false);

        colGuardId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGuardName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrisonId11.setCellValueFactory(new PropertyValueFactory<>("prisonid"));
        colGuardDays1.setCellValueFactory(new PropertyValueFactory<>("days"));
        inmateList2 = FXCollections.observableArrayList(prms.inmates);
        table_inmate_behav11.setItems(inmateList2);
        table_inmate_behav11.setVisible(false);

        reports_toggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == general_opn) {
                showContent(1);
            } else if (newValue == inm_behav_opn) {
                showContent(2);
            } else if (newValue == inm_atte_opn) {
                showContent(3);
            } else if (newValue == guar_atten_opn) {
                showContent(4);
            }
        });

    }
    private void showContent(int option) {
        // Hide all panes
        general_gid.setVisible(false);
        table_inmate_behav.setVisible(false);
        table_inmate_behav1.setVisible(false);
        table_inmate_behav11.setVisible(false);

        //contentPane2.setVisible(false);
        //contentPane3.setVisible(false);
        //contentPane4.setVisible(false);

        if (option == 1) {
            general_gid.setVisible(true);
        }
        if (option == 2) {
            table_inmate_behav.setVisible(true);
        }
        if (option == 3) {
            table_inmate_behav11.setVisible(true);
        }
        if (option == 4) {
            table_inmate_behav1.setVisible(true);
        }
    }
    @FXML
    void onSelectedPrison(ActionEvent event) {

            if (selec_prisons.getValue().equals("All Prisons"))
            {
                prison_num_val.setText(String.valueOf(prms.getPrisons().size()));
                inmate_num_val.setText(String.valueOf(prms.getInmates().size()));
                guard_num_val.setText(String.valueOf(prms.getGuards().size()));
                inmateList = FXCollections.observableArrayList(prms.inmates);
                table_inmate_behav.setItems(inmateList);
                guardList = FXCollections.observableArrayList(prms.guards);
                table_inmate_behav1.setItems(guardList);
                inmateList2 = FXCollections.observableArrayList(prms.inmates);
                table_inmate_behav11.setItems(inmateList2);

            }
            else{
                int pr_id = Integer.parseInt(selec_prisons.getValue().substring(7));
                for(Prison p: prms.prisons)
                {
                    if (p.getPrisonID() == pr_id) {
                        prison_num_val.setText("1");
                        inmate_num_val.setText(String.valueOf(p.getInmates().size()));
                        guard_num_val.setText(String.valueOf(p.getGuards().size()));
                        inmateList = FXCollections.observableArrayList(p.getInmates());
                        table_inmate_behav.setItems(inmateList);
                        guardList = FXCollections.observableArrayList(p.getGuards());
                        table_inmate_behav1.setItems(guardList);
                        inmateList2 = FXCollections.observableArrayList(p.getInmates());
                        table_inmate_behav11.setItems(inmateList2);

                    }
                }

            }
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
