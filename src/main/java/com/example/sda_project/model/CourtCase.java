package com.example.sda_project.model;

import java.util.Date;

public class CourtCase {

    private int caseid;
    private String casename;
    private Date date;  // Java Date for storing the court case date
    private int inmateid;
    private String inmatename;

    // Constructor
    public CourtCase(int caseid, String casename, Date date, int inmateid, String inmatename) {
        this.caseid = caseid;
        this.casename = casename;
        this.date = date;
        this.inmateid = inmateid;
        this.inmatename = inmatename;
    }
    public int getCaseid() {
        return caseid;
    }

    public String getCasename() {
        return casename;
    }

    public Date getDate() {
        return date;
    }

    public int getInmateid() {
        return inmateid;
    }

    public String getInmatename() {
        return inmatename;
    }
    // toString method for display purposes
    @Override
    public String toString() {
        return "CourtCase{" +
                "caseid=" + caseid +
                ", casename='" + casename + '\'' +
                ", date=" + date +
                ", inmateid=" + inmateid +
                ", inmatename='" + inmatename + '\'' +
                '}';
    }

    // Display function
    public void display() {
        System.out.println(this.toString());
    }
}
