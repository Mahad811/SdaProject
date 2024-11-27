package com.example.sda_project.model;


import java.util.ArrayList;
import java.util.List;

public class Guard {
    private int id;
    private int age;
    private String gender;
    private int prisonid;
    private String password;
    private List<AttendanceRecord> attendanceRecords;
    private List<Transfer> transfers; // List of transfers for the inmate
    private String name;

    // Constructor
    public Guard(String name, int id, int age, String gender,int prisonid, String password) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.prisonid = prisonid;
        this.password = password;
        attendanceRecords = new ArrayList<>();
        transfers = new ArrayList<>();
    }
    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Getter and Setter for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter and Setter for prisonid
    public int getPrisonid() {
        return prisonid;
    }

    public void setPrisonid(int prisonid) {
        this.prisonid = prisonid;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for attendanceRecords
    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    // Setter for attendanceRecords
    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    // Add individual attendance record (optional convenience method)
    public void addAttendanceRecord(AttendanceRecord attendanceRecord) {
        if (this.attendanceRecords == null) {
            this.attendanceRecords = new ArrayList<>();
        }
        this.attendanceRecords.add(attendanceRecord);
    }
    public int getDays() {
        return attendanceRecords.size();
    }

    // Getter for transfers
    public List<Transfer> getTransfers() {
        return transfers;
    }

    // Setter for transfers
    public void setTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    // Convenience method to add a single transfer
    public void addTransfer(Transfer transfer) {
        if (this.transfers == null) {
            this.transfers = new ArrayList<>();
        }
        this.transfers.add(transfer);
    }
    // toString method
    @Override
    public String toString() {
        return "Guard{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", attendance=" + (attendanceRecords != null ? attendanceRecords : "No attendance recorded") +  // Includes the Behaviors list
                ", transfers=" + (transfers != null ? transfers : "No transfers recorded") +  // Includes the Behaviors list
                '}';
    }
}
