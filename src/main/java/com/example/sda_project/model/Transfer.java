package com.example.sda_project.model;

import java.time.LocalDateTime;

public class Transfer {
    private int transferID;
    private int entityID; // Could be either GuardID or InmateID
    private Prison fromPrison;
    private Prison toPrison;
    private LocalDateTime date;
    private String type;

    // Constructors
    public Transfer(int transferID, int entityID, Prison fromPrison, Prison toPrison, LocalDateTime date,String type) {
        this.transferID = transferID;
        this.entityID = entityID;
        this.fromPrison = fromPrison;
        this.toPrison = toPrison;
        this.type = type;
        this.date = date;
    }

    // Getters and Setters
    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public Prison getFromPrison() {
        return fromPrison;
    }

    public void setFromPrison(Prison fromPrison) {
        this.fromPrison = fromPrison;
    }

    public Prison getToPrison() {
        return toPrison;
    }

    public void setToPrison(Prison toPrison) {
        this.toPrison = toPrison;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Transfer{" +
                "transferID=" + transferID +
                ", entityID=" + entityID +
                ", fromPrison=" + fromPrison.getName() +  // Assuming getName() method is defined in the Prison class
                ", toPrison=" + toPrison.getName() +      // Assuming getName() method is defined in the Prison class
                ", date=" + date +
                ", type=" + type +
                '}';
    }

}

