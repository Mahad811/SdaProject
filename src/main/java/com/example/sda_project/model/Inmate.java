package com.example.sda_project.model;


import com.example.sda_project.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inmate {
    private String name;
    private int id;
    private int age;
    private String gender;
    private int prisonid;
    private Sentence sentence;
    private String location;
    private boolean eligibility = true;
    private String transferStatus;

    private List<TreatmentPlan> treatmentPlans;
    private List<AttendanceRecord> attendanceRecords;
    private List<Visitation> visitations; // List of visitations
    private List<Transfer> transfers; // List of transfers for the inmate
    private List<Behavior> behaviors;

    // Constructor
    public Inmate(String name, int id, int age, String gender,int prisonid, String location,String transferStatus,boolean eligibility) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.prisonid = prisonid;
        sentence = new Sentence();
        behaviors = new ArrayList<Behavior>();
        treatmentPlans = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        this.location = location;
        this.transferStatus = transferStatus;
        this.eligibility = eligibility;

    }


    public int getInmateID() {
        return id;
    }
    public void setEligibility(boolean eligibility) {this.eligibility = eligibility;}
    public boolean getEligibility() {return eligibility;}
    public int getId() {
        return id;
    }

    public void setInmateID(int inmateID) {
        this.id = inmateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getGender() {
        return gender;
    }

    public int getPrisonid() {
        return prisonid;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public int getDays() {
        return attendanceRecords.size();
    }
    // toString method
    @Override
    public String toString() {
        return "Inmate{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", prisonid=" + prisonid +
                ", location=" + location +
                ", transferStatus=" + transferStatus +
                ", eligibility=" + eligibility +
                ", sentence=" + sentence +  // Includes the Sentence object
                ", behaviors=" + (behaviors != null ? behaviors : "No behaviors recorded") +  // Includes the Behaviors list
                ", treatments=" + (treatmentPlans != null ? treatmentPlans : "No treatents recorded") +  // Includes the Behaviors list
                ", attendance=" + (attendanceRecords != null ? attendanceRecords : "No attendance recorded") +  // Includes the Behaviors list
                ", visitations=" + (visitations != null ? visitations : "No visitations recorded") +  // Includes the Behaviors list
                ", transfers=" + (transfers != null ? transfers : "No trasfers recorded") +  // Includes the Behaviors list
                '}';
    }

    public double getBehaviorRating()
    {
        int good = 0;
        if (behaviors.isEmpty())
            return 0;
        for(Behavior behavior : behaviors)
        {
            if (behavior.getType().equals("Good"))
            {
                good += 1;
            }
        }
        return Math.round((double) good * 5 / behaviors.size() * 10) / 10.0;

    }
    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrisonID() {
        return prisonid;
    }

    public void setPrisonID(int prisonID) {
        this.prisonid = prisonID;
    }

    public Sentence getSetence(){return this.sentence;}

    public void setSentence(Sentence sentence){
        this.sentence = sentence;
    }

    // Getter and Setter for TreatmentPlans
    public List<TreatmentPlan> getTreatmentPlans() {
        return treatmentPlans;
    }

    public void setTreatmentPlans(List<TreatmentPlan> treatmentPlans) {
        this.treatmentPlans = treatmentPlans;
    }

    public void addTreatmentPlan(TreatmentPlan treatmentPlan) {
        if (this.treatmentPlans == null) {
            this.treatmentPlans = new ArrayList<>();
        }
        this.treatmentPlans.add(treatmentPlan);
    }

    // Getter and Setter for AttendanceRecords
    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    public void addAttendanceRecord(AttendanceRecord attendanceRecord) {
        if (this.attendanceRecords == null) {
            this.attendanceRecords = new ArrayList<>();
        }
        this.attendanceRecords.add(attendanceRecord);
    }

    // Getter and Setter for Visitations
    public List<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(List<Visitation> visitations) {
        this.visitations = visitations;
    }

    public void addVisitation(Visitation visitation) {
        if (this.visitations == null) {
            this.visitations = new ArrayList<>();
        }
        this.visitations.add(visitation);
    }

    // Getter and Setter for Transfers
    public List<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    public void addTransfer(Transfer transfer) {
        if (this.transfers == null) {
            this.transfers = new ArrayList<>();
        }
        this.transfers.add(transfer);
    }

    // Getter and Setter for Behaviors
    public List<Behavior> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void addBehavior(Behavior behavior) {
        if (this.behaviors == null) {
            this.behaviors = new ArrayList<>();
        }
        this.behaviors.add(behavior);
        // Add your logic to save this Behavior object to the database or list

    }

}
