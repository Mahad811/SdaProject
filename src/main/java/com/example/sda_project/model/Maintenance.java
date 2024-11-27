package com.example.sda_project.model;

public class Maintenance {

    private int maintenanceId;
    private String maintenanceDate;
    private String location;
    private String urgencyLevel;
    private int guardId;
    private String details;

    // Constructor
    public Maintenance(int maintenanceId, String maintenanceDate, String location, String urgencyLevel, int guardId, String details) {
        this.maintenanceId = maintenanceId;
        this.maintenanceDate = maintenanceDate;
        this.location = location;
        this.urgencyLevel = urgencyLevel;
        this.guardId = guardId;
        this.details = details;
    }

    // Getters and Setters

    public int getMaintenanceId() {
        return maintenanceId;
    }
    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }


    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // ToString method to display the object in a readable form
    @Override
    public String toString() {
        return "Maintenance{" +
                "maintenanceId=" + maintenanceId +
                ", maintenanceDate='" + maintenanceDate + '\'' +
                ", location='" + location + '\'' +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                ", guardId=" + guardId +
                ", details='" + details + '\'' +
                '}';
    }
}
