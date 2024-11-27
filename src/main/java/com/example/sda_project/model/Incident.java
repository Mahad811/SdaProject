package com.example.sda_project.model;

public class Incident {
    private int incidentId;        // Auto-incremented ID
    private String incidentDate;   // Date of the incident
    private int inmateId;          // ID of the inmate involved
    private String inmateName;     // Name of the inmate
    private int guardId;           // ID of the guard present
    private String location;       // Location of the incident
    private String details;        // Details of the incident

    // Constructor
    public Incident(int incidentId, String incidentDate, int inmateId, String inmateName, int guardId, String location, String details) {
        this.incidentId = incidentId;
        this.incidentDate = incidentDate;
        this.inmateId = inmateId;
        this.inmateName = inmateName;
        this.guardId = guardId;
        this.location = location;
        this.details = details;
    }

    // Getters and Setters
    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public int getInmateId() {
        return inmateId;
    }

    public void setInmateId(int inmateId) {
        this.inmateId = inmateId;
    }

    public String getInmateName() {
        return inmateName;
    }

    public void setInmateName(String inmateName) {
        this.inmateName = inmateName;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
