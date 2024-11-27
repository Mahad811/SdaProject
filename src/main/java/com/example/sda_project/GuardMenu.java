package com.example.sda_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GuardMenu {

    private int guard_id;

    public void setGuard_id(int id) {this.guard_id = id;}
    public int getGuard_id() {return this.guard_id;}
    @FXML
    private Button backButton;

    @FXML
    private Button maintaingfacitiltybutton;

    @FXML
    private Button reportingincident;

    @FXML
    private Button trackcommunityservicebutton;

    @FXML
    private Button trackingInmatebutton;

    @FXML
    private Button trackinmatebehaviorbutton;

    @FXML
    void onBackButtonClick(ActionEvent event) {
        try {
            // Load the FXML file for the target page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginmenu-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onmaintainingfacilityclick(ActionEvent event) {
        System.out.println(guard_id);

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("maintaing_facility.fxml"));
            Parent root = loader.load();
            MaintaingFacility c = loader.getController();
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


    @FXML
    void onreportingincidentclick(ActionEvent event) {
        System.out.println(guard_id);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("report_incident.fxml"));
            Parent root = loader.load();
            ReportIncident c = loader.getController();
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

    @FXML
    void ontrackcommunityserviceclick(ActionEvent event) {
        System.out.println(guard_id);

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("community_service.fxml"));
            Parent root = loader.load();
            CommunityService c = loader.getController();
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

    @FXML
    void ontrackinginmateclick(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tracking_inmate.fxml"));
            Parent root = loader.load();
            TrackingInmateController c = loader.getController();
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
    @FXML
    void ontrackinmatebehaviorclick(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("trackbehavior-view.fxml"));
            Parent root = loader.load();
            TrackBehaviorController c = loader.getController();
            c.setLoggedin_guardid(guard_id);
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
