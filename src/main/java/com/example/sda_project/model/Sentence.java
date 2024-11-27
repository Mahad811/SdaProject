package com.example.sda_project.model;

import java.util.Date;

public class Sentence {

    // Public attributes
    private Date date;              // Date when the sentence was given
    private String type;            // Type of sentence (e.g., "Life", "Fixed term", etc.)
    private boolean paroleAllowed;  // Whether parole is allowed
    private int inmateid;           // ID of the inmate receiving the sentence

    public Sentence() {
        this.date = new Date();  // Default to current date
        this.type = "Life";      // Default sentence type
        this.paroleAllowed = true; // Default parole allowed
        this.inmateid = 0;       // Default inmate ID
    }
    // Constructor
    public Sentence(Date date , String type, boolean paroleAllowed, int inmateid) {
        this.date = date;
        this.type = type;
        this.paroleAllowed = paroleAllowed;
        this.inmateid = inmateid;
    }

    // toString method to display the sentence information
    @Override
    public String toString() {
        return "Sentence{" +
                "date=" + date +
                ", type='" + type + '\'' +
                ", paroleAllowed=" + paroleAllowed +
                ", inmateid=" + inmateid +
                '}';
    }
    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getParoleAllowed() {
        return paroleAllowed;
    }

    public void setParoleAllowed(boolean paroleAllowed) {
        this.paroleAllowed = paroleAllowed;
    }

    public int getInmateId() {
        return inmateid;
    }

    public void setInmateId(int inmateId) {
        this.inmateid = inmateId;
    }

}
