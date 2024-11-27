package com.example.sda_project.model;

import java.util.Date;

public class Behavior {

    // Attributes
    private String type;
    private String incident;
    private String description;
    private Date date;
    private int reportingGuardId;
    private int inmateid;

    // Constructor
    public Behavior(String type, String incident, String description, Date date, int reportingGuardId, int inmateid) {
        this.type = type;
        this.incident = incident;
        this.description = description;
        this.date = date;
        this.reportingGuardId = reportingGuardId;
        this.inmateid = inmateid;
    }

    // Default constructor
    public Behavior() {
        this.type = "";
        this.incident = "";
        this.description = "";
        this.date = new Date(); // Default to current date
        this.reportingGuardId = 0;
        this.inmateid = 0;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getIncident() {
        return incident;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public int getReportingGuardID() {
        return reportingGuardId;
    }
    public int getInmateidId() {
        return inmateid;
    }
    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReportingGuardId(int reportingGuardId) {
        this.reportingGuardId = reportingGuardId;
    }
    public void setInmateidId(int inmateid) {
        this.inmateid = inmateid;
    }

    // toString method for debugging and display
    @Override
    public String toString() {
        return "Behavior{" +
                "type='" + type + '\'' +
                ", incident='" + incident + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", reportingGuardId=" + reportingGuardId +
                '}';
    }
}
