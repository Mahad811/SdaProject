package com.example.sda_project.model;

import java.time.LocalDateTime;

public class AttendanceRecord {
    private String recordID;
    private String guardID;
    private String inmateID;
    private LocalDateTime Attendance;
    private String attendanceType; // "Guard" or "Inmate"

    // Constructor
    public AttendanceRecord(String recordID, String guardID, String inmateID, LocalDateTime shiftStart, String attendanceType) {
        this.recordID = recordID;
        this.guardID = guardID;
        this.inmateID = inmateID;
        this.Attendance = shiftStart;
        this.attendanceType = attendanceType;
    }

    // Getters and Setters
    // Getter and setter for recordID
    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public String getGuardID() {
        return guardID;
    }

    public void setGuardID(String guardID) {
        this.guardID = guardID;
    }

    public String  getInmateID() {
        return inmateID;
    }

    public void setInmateID(String inmateID) {
        this.inmateID = inmateID;
    }

    public LocalDateTime getShiftStart() {
        return Attendance;
    }

    public void setShiftStart(LocalDateTime shiftStart) {
        this.Attendance = shiftStart;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "recordID='" + recordID + '\'' +
                ", guardID='" + guardID + '\'' +
                ", inmateID='" + inmateID + '\'' +
                ", Attendance=" + (Attendance != null ? Attendance.toString() : "null") +
                ", attendanceType='" + attendanceType + '\'' +
                '}';
    }

}
