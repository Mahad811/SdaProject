package com.example.sda_project.model;

import java.time.LocalDateTime;

public class Visitation {
    private int visitationID;
    private int inmateID;
    private String visitorName;
    private LocalDateTime visitDate;

    public Visitation(int visitationID, int inmateID, String visitorName, LocalDateTime visitDate) {
        this.visitationID = visitationID;
        this.inmateID = inmateID;
        this.visitorName = visitorName;
        this.visitDate = visitDate;
    }

    // Getters and setters
    public int getVisitationID() {
        return visitationID;
    }

    public void setVisitationID(int visitationID) {
        this.visitationID = visitationID;
    }

    public int getInmateID() {
        return inmateID;
    }

    public void setInmateID(int inmateID) {
        this.inmateID = inmateID;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }
    @Override
    public String toString() {
        return "Visitation{" +
                "visitationID=" + visitationID +
                ", inmateID=" + inmateID +
                ", visitorName='" + visitorName + '\'' +
                ", visitDate=" + visitDate +
                '}';
    }

}
