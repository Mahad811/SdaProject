package com.example.sda_project;

import com.example.sda_project.model.Inmate;
import com.example.sda_project.model.Guard;
import com.example.sda_project.model.PRMS;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

public class MainController {
    private PRMS prms = PRMS.getInstance();
    public void addNewInmate(String name,int ageText,String gender, String transferStatus, String location, String prisonname)
    {
        prms.addNewInmateUsecase(name,ageText,gender,transferStatus,location,prisonname);
    }

    public Inmate searchInmate(String name)
    {
        return prms.searchInmate(name);
    }

    public void checkInmateELigibility(String inmateIdText){
        prms.checkInmateELigibility(inmateIdText);
    }

    public void addCommunityService(String inmateIdText, String selectedCommunity, String selectedPerformance,int guard_id)
    {
        prms.addCommunityService(inmateIdText,selectedCommunity,selectedPerformance,guard_id);
    }

    public void addMaintenance(int maintenanceId, String maintenanceDate, String location, String urgencyLevel, int guardId, String details)
    {
        prms.addMaintenance(maintenanceId,maintenanceDate,location,urgencyLevel,guardId,details);
    }

    public void addTreatment(int counsellorId, int inmateId, String type, String description)
    {
        prms.addTreatment(counsellorId,inmateId,type,description);
    }

    public boolean addVisitation(int inmateID, String visitorName, LocalDateTime visitDate) throws SQLException {
        return prms.addVisitation(inmateID,visitorName,visitDate);
    }

    public boolean addIncident(String incidentDate, int inmateId, String inmateName, int guardId, String location, String details) throws SQLException {
        return prms.addIncident(incidentDate,inmateId,inmateName,guardId,location,details);
    }
    public void addSentence(Date date , String type, boolean paroleAllowed, int inmateid)
    {
        prms.addSentence(date,type,paroleAllowed,inmateid);
    }
    public void addBehavior(String type, String incident, String description, Date date, int reportingGuardId, int inmateid)
    {
        prms.addBehavior(type, incident, description, date, reportingGuardId, inmateid);
    }
    public boolean addAttendenceRecord(String guardID, String inmateID, LocalDateTime shiftStart, String attendanceType) throws SQLException {
        return prms.addAttendenceRecord(guardID,inmateID,shiftStart,attendanceType);

    }
    public Inmate searchInmateById(int inmateId)
    {
        return prms.searchInmateById(inmateId);
    }
    public String searchInmateNameById(int inmateId)
    {
        return prms.searchInmateNameById(inmateId);
    }
    public void updateInmateLocation(int inmateId, String location)
    {
        prms.updateInmateLocation(inmateId,location);
    }
    public boolean transferGuard(Guard selectedGuard,String selectedPrison,String trasferDate) throws SQLException {
        return prms.transferGuard(selectedGuard,selectedPrison,trasferDate);
    }
    public boolean transferInmate(String selectedInmate,String selectedPrison,String trasferDate,String currentPrison) throws SQLException {
        return prms.transferInmate(selectedInmate,selectedPrison,trasferDate,currentPrison);
    }

}
