package com.example.sda_project.model;

import com.example.sda_project.db.DataBaseHelper;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PRMS {

    public List<PrisonAdmin> admins;
    public List<Guard> guards;
    public List<Inmate> inmates;
    public List<Prison> prisons;
    public List<CourtCase> cases;
    public List<AttendanceRecord> attendanceRecords;
    public List<Visitation> visitations;
    public List<Transfer> transfers;
    public List<Maintenance> maintenanceRecords = new ArrayList<>();; // New List for Maintenance
    public List<Incident> incidentRecords = new ArrayList<>();
    public List<Behavior> behaviorRecords = new ArrayList<>();
    public List<Sentence> sentences = new ArrayList<>();



    private static PRMS instance;  // The single instance of the PRMS class
    public static PRMS getInstance() {
        if (instance == null) {
            instance = new PRMS();
        }
        return instance;
    }
    // Database URL and credentials
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;" // SQL Server URL
            + "databaseName=SDA_Assignment;encrypt=true;trustServerCertificate=true";

    private static final String USER = "HASANWAQAR";
    private static final String PASS = "Hasan_SQL";

    // Constructor that reads data from the database
    public PRMS() {
        admins = new ArrayList<>();
        guards = new ArrayList<>();
        inmates = new ArrayList<>();
        prisons = new ArrayList<>();
        cases = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        visitations = new ArrayList<>();
        transfers = new ArrayList<>();


        // Load data from the database
        loadDataFromDatabase();
    }

    public void set_sentence(int inmateid, Sentence sent)
    {
        int pid = 0;
        for(Inmate i : inmates){
            if (i.getId() == inmateid) {
                i.setSentence(sent);
                pid = i.getPrisonID();
            }
        }
        if (pid == 0)
            return;
        for(Prison p : prisons){
            if (pid == p.getPrisonID() && pid !=0){
                p.set_sentence(inmateid,sent);
            }
        }

    }
    public void set_behavior(int inmateid, Behavior behav)
    {
        int pid = 0;
        for(Inmate i : inmates){
            if (i.getId() == inmateid) {
                i.addBehavior(behav);
                pid = i.getPrisonID();
            }
        }
        behaviorRecords.add(behav);


    }
    public void set_treatment(TreatmentPlan trtpln)
    {
        for(Inmate i : inmates){
            if (i.getId() == trtpln.getInmateId()) {
                for(TreatmentPlan t: i.getTreatmentPlans()){
                    t.setActive(false);
                }
                i.addTreatmentPlan(trtpln);

            }
        }

    }

    // Method to load data from the database and populate lists
    private void loadDataFromDatabase() {
        // Establish connection to the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Query for PrisonAdmin
            String adminQuery = "SELECT * FROM PrisonAdmin";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(adminQuery)) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    admins.add(new PrisonAdmin(name, password));
                }
            }

            // Query for Guard
            String guardQuery = "SELECT * FROM Guard";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(guardQuery)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    int prisonID = rs.getInt("prisonID");
                    String password = rs.getString("password");
                    guards.add(new Guard(name, id, age, gender, prisonID, password));
                }
            }

            // Query for Inmate
            String inmateQuery = "SELECT * FROM Inmate";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(inmateQuery)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    int prisonID = rs.getInt("prisonID");
                    String loc = rs.getString("location");
                    String transtat = rs.getString("TransferStatus");
                    boolean elig = rs.getBoolean("eligibilty");
                    inmates.add(new Inmate(name, id, age, gender, prisonID, loc,transtat,elig));
                }
            }

            // Query for Prison
            String prisonQuery = "SELECT * FROM Prison";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(prisonQuery)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String location = rs.getString("location");
                    prisons.add(new Prison(id, name, location,guards,inmates));
                }
            }

            String query = "SELECT * FROM CourtCase";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                // Iterate through the result set and create CourtCase objects
                while (rs.next()) {
                    int caseid = rs.getInt("caseid");
                    String casename = rs.getString("casename");
                    Date date = rs.getDate("date");
                    int inmateid = rs.getInt("inmateid");
                    String inmatename = rs.getString("inmatename");

                    // Create a CourtCase object and add it to the list
                    CourtCase courtCase = new CourtCase(caseid, casename, date, inmateid, inmatename);
                    cases.add(courtCase);
                }
            }
            String sentenceQuery = "SELECT * FROM Sentence";

            // Retrieve the sentences
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sentenceQuery)) {
                while (rs.next()) {
                    // Extract data from the result set
                    Date date = rs.getDate("date"); // Get the date
                    String type = rs.getString("type"); // Get the sentence type
                    boolean paroleAllowed = rs.getBoolean("paroleAllowed"); // Get parole eligibility
                    int inmateid = rs.getInt("inmateid"); // Get the inmate ID
                    Sentence s = new Sentence(date, type, paroleAllowed, inmateid);
                    sentences.add(s);
                    for(Inmate i : inmates){
                        if (i.getId() == inmateid) {
                            i.setSentence(s);
                        }
                    }
                }
            }
            String behaviorQuery = "SELECT * FROM Behavior";

// Retrieve the behaviors
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(behaviorQuery)) {
                while (rs.next()) {
                    // Extract data from the result set
                    String type = rs.getString("type");  // Behavior type
                    String incident = rs.getString("incident");  // Incident description
                    String description = rs.getString("description");  // Detailed description
                    Date date = rs.getDate("date");  // Behavior date
                    int reportingGuardID = rs.getInt("reportingGuardID");  // Reporting guard ID
                    int inmateid = rs.getInt("inmateID");  // Inmate ID (foreign key to Inmate)

                    // Create a new Behavior object
                    Behavior behavior = new Behavior(type, incident, description, date, reportingGuardID,inmateid);
                    this.behaviorRecords.add(behavior);
                    // Find the corresponding inmate and add the behavior to their behavior list
                    for (Inmate i : inmates) {
                        if (i.getId() == inmateid) {
                            i.addBehavior(behavior);
                        }
                    }
                }
            }
            String treatmentPlanQuery = "SELECT * FROM TreatmentPlan";

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(treatmentPlanQuery)) {
                while (rs.next()) {
                    // Extract data from the result set
                    int counsellorId = rs.getInt("counsellorid"); // Counsellor ID
                    int inmateId = rs.getInt("inmateid");         // Inmate ID (foreign key to Inmate)
                    String type = rs.getString("type");          // Type of treatment
                    String description = rs.getString("description"); // Treatment description

                    // Create a new TreatmentPlan object
                    TreatmentPlan treatmentPlan = new TreatmentPlan(counsellorId, inmateId, type, description);

                    // Find the corresponding inmate and add the treatment plan to their list
                    for (Inmate inmate : inmates) {
                        if (inmate.getId() == inmateId) {
                            if (inmate.getTreatmentPlans() == null) {
                                inmate.setTreatmentPlans(new ArrayList<>());
                            }
                            for(TreatmentPlan t: inmate.getTreatmentPlans()){
                                t.setActive(false);
                            }
                            inmate.addTreatmentPlan(treatmentPlan);
                        }
                    }
                }
            }
            String attendanceQuery = "SELECT * FROM AttendanceRecords";

// Retrieve the attendance records
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(attendanceQuery)) {
                while (rs.next()) {
                    // Extract data from the result set
                    int recordId = rs.getInt("RecordID"); // Get the Record ID
                    int guardId = rs.getInt("GuardID"); // Get the Guard ID
                    int inmateId = rs.getInt("InmateID"); // Get the Inmate ID
                    Timestamp attendance = rs.getTimestamp("Attendance"); // Get the Attendance timestamp
                    LocalDateTime attendanceDateTime = attendance != null ? attendance.toLocalDateTime() : null;
                    String attendanceType = rs.getString("AttendanceType"); // Get the Attendance type

                    // Create a new AttendanceRecord object
                    AttendanceRecord attendanceRecord = new AttendanceRecord(
                            String.valueOf(recordId), String.valueOf(guardId), String.valueOf(inmateId), attendanceDateTime, attendanceType
                    );
                    attendanceRecords.add(attendanceRecord);
                    // Find the corresponding inmate and add the attendance record to their list
                    for (Inmate i : inmates) {
                        if (i.getId() == inmateId && attendanceRecord.getInmateID() != null) {
                            i.addAttendanceRecord(attendanceRecord);
                        }
                    }
                    for (Guard g: guards) {
                        if (g.getId() == guardId && attendanceRecord.getGuardID() != null) {

                            g.addAttendanceRecord(attendanceRecord);
                        }
                    }
                }
            }
            String visitationQuery = "SELECT * FROM Visitation";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(visitationQuery)) {
                while (rs.next()) {
                    int visitationID = rs.getInt("visitationID");
                    int inmateID = rs.getInt("inmateID");  // Changed to int to match the constructor
                    String visitorName = rs.getString("visitorName");
                    Date visitationDate = rs.getDate("VisitDate");  // Get visitation date as Date

                    // Convert Date to LocalDateTime (assuming visitationDate is in SQL Date format)
                    LocalDateTime visitDate = visitationDate.toLocalDate().atStartOfDay();

                    Visitation vst = new Visitation(visitationID, inmateID, visitorName, visitDate);
                    // Add the Visitation to the list
                    visitations.add(vst);
                    for (Inmate i : inmates) {
                        if (i.getId() == inmateID) {
                            i.addVisitation(vst);
                        }
                    }
                }
            }
            // Query for Transfers
            String transferQuery = "SELECT * FROM Transfer";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(transferQuery)) {
                while (rs.next()) {
                    // Fetch Transfer data
                    int transferID = rs.getInt("id");
                    int entityID = rs.getInt("entityID");  // Either GuardID or InmateID
                    int fromPrisonID = rs.getInt("fromPrisonID");
                    int toPrisonID = rs.getInt("toPrisonID");
                    Date transferDate = rs.getDate("transferDate");
                    String type = rs.getString("type");

                    // Convert Date to LocalDateTime
                    LocalDateTime transferDateTime = transferDate.toLocalDate().atStartOfDay(); // If transferDate is SQL Date
                    // If it's a SQL Timestamp, you can use:
                    // LocalDateTime transferDateTime = rs.getTimestamp("transferDate").toLocalDateTime();

                    // Retrieve Prison objects based on fromPrisonID and toPrisonID
                    Prison fromPrison = getPrisonById(fromPrisonID);  // Assuming a method to fetch Prison object
                    Prison toPrison = getPrisonById(toPrisonID);      // Assuming a method to fetch Prison object

                    // Create Transfer object and add to the list
                    Transfer transfer = new Transfer(transferID, entityID, fromPrison, toPrison, transferDateTime,type);
                    transfers.add(transfer);  // Assuming 'transfers' is the list where you store Transfer objects
                    for(Inmate i : inmates) {
                        if (i.getId() == entityID && type.equals("Inmate")) {
                            i.addTransfer(transfer);
                        }
                    }
                    for(Guard g : guards) {
                        if (g.getId() == entityID&& type.equals("Guard")) {
                            g.addTransfer(transfer);
                        }
                    }
                }
            }
            String incidentQuery = "SELECT * FROM Incident";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(incidentQuery)) {

                // Iterate through the result set and create Incident objects
                while (rs.next()) {
                    int incidentId = rs.getInt("incident_id");
                    String incidentDate = rs.getString("incident_date"); // You can convert this to Date if needed
                    int inmateId = rs.getInt("inmate_id");
                    String inmateName = rs.getString("inmate_name");
                    int guardId = rs.getInt("guard_id");
                    String location = rs.getString("location");
                    String details = rs.getString("details");

                    // Create an Incident object and add it to the list
                    Incident incident = new Incident(incidentId, incidentDate, inmateId, inmateName, guardId, location, details);
                    incidentRecords.add(incident); // Assuming incidents is a list of Incident objects
                }
            }
            String maintenanceQuery = "SELECT * FROM Maintenance";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(maintenanceQuery)) {

                // Iterate through the result set and create Maintenance objects
                while (rs.next()) {
                    int maintenanceId = rs.getInt("maintenance_id");
                    String maintenanceDate = rs.getString("maintenance_date"); // You can convert this to Date if needed
                    String location = rs.getString("location");
                    String urgencyLevel = rs.getString("urgency_level");
                    int guardId = rs.getInt("guard_id");
                    String details = rs.getString("details");

                    // Create a Maintenance object and add it to the list
                    Maintenance maintenance = new Maintenance(maintenanceId, maintenanceDate, location, urgencyLevel, guardId, details);
                    maintenanceRecords.add(maintenance); // Assuming maintenances is a list of Maintenance objects
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Prison getPrisonById(int fromPrisonID) {
        for (Prison prison : prisons) {
            if (prison.getPrisonID() == fromPrisonID) {
                return prison;
            }
        }
        return null;
    }
    public void addvisitation(Visitation visitation) {
        visitations.add(visitation);
        display();
    }
    // Getter methods to access the lists
    public List<PrisonAdmin> getAdmins() {
        return admins;
    }
    public List<Guard> getGuards() {
        return guards;
    }
    public List<Inmate> getInmates() {
        return inmates;
    }
    public List<Prison> getPrisons() {
        return prisons;
    }
    public List<CourtCase> getCases() {
        return cases;
    }
    public void displayAttendanceRecords() {
        System.out.println("Attendance Records:");
        for (AttendanceRecord record : attendanceRecords) {
            System.out.println(record);
        }
    }

    public void displayVisitations() {
        System.out.println("Visitations:");
        for (Visitation visitation : visitations) {
            System.out.println(visitation);
        }
    }

    public void displayTransfers() {
        System.out.println("Transfers:");
        for (Transfer transfer : transfers) {
            System.out.println(transfer);
        }
    }
    public void displayAdmins() {
        System.out.println("Prison Admins:");
        for (PrisonAdmin admin : admins) {
            System.out.println(admin);
        }
    }
    public void displayPrisons() {
        System.out.println("Prisons:");
        for (Prison prison : prisons) {
            System.out.println(prison);
        }
    }
    public void displayGuards() {
        System.out.println("Guards:");
        for (Guard guard : guards) {
            System.out.println(guard);
        }
    }
    public void displayInmates() {
        System.out.println("Inmates:");
        for (Inmate inmate : inmates) {
            System.out.println(inmate);
        }
    }
    public void displayCases() {
        System.out.println("Cases:");
        for (CourtCase c : cases) {
            System.out.println(c);
        }
    }
    public void display() {
        displayInmates();
        displayGuards();
        displayPrisons();
        displayAttendanceRecords();
        displayVisitations();
        displayTransfers();
        displayIncidents();
        displayBehaviorRecords();
        displayMaintenanceRecords();
        displayCases();
        displaySentences();

    }
    public void addInmate(Inmate inmate) {
        inmates.add(inmate);
        display(); // Refresh the terminal output after adding the inmate
    }

    public void addAttendanceRecord(AttendanceRecord record) {
        attendanceRecords.add(record);
        display();
    }
    public void addTransfer(Transfer transfer) {
        transfers.add(transfer);
        display();
    }
    public void displayMaintenanceRecords() {
        System.out.println("Current Maintenance Records in PRMS:");
        if (maintenanceRecords.isEmpty()) {
            System.out.println("No maintenance records found.");
        } else {
            for (Maintenance maintenance : maintenanceRecords) {
                System.out.println(
                        "ID: " + maintenance.getMaintenanceId() +
                                ", Date: " + maintenance.getMaintenanceDate() +
                                ", Location: " + maintenance.getLocation() +
                                ", Urgency: " + maintenance.getUrgencyLevel() +
                                ", Guard ID: " + maintenance.getGuardId() +
                                ", Details: " + maintenance.getDetails()
                );

            }
        }
    }

    public void displayIncidents() {
        System.out.println("Current Incident Records in PRMS:");
        if (incidentRecords.isEmpty()) {
            System.out.println("No incident records found.");
        } else {
            for (Incident incident : incidentRecords) {
                System.out.println(
                        "ID: " + incident.getIncidentId() +
                                ", Date: " + incident.getIncidentDate() +
                                ", Inmate ID: " + incident.getInmateId() +
                                ", Inmate Name: " + incident.getInmateName() +
                                ", Guard ID: " + incident.getGuardId() +
                                ", Location: " + incident.getLocation() +
                                ", Details: " + incident.getDetails()
                );
            }
        }
    }
    public void displaySentences()
    {
        System.out.println("Current Sentences in PRMS:" + sentences);
    }
    public void displayBehaviorRecords() {
        if (behaviorRecords.isEmpty()) {
            System.out.println("No behavior records found.");
        } else {
            System.out.println("Behavior Records:");
            for (Behavior behavior : behaviorRecords) {

                System.out.println("Type: " + behavior.getType());
                System.out.println("Incident: " + behavior.getIncident());
                System.out.println("Description: " + behavior.getDescription());
                System.out.println("Date: " + behavior.getDate());
                System.out.println("Reporting Guard ID: " + behavior.getReportingGuardID());
                System.out.println("Inmate ID: " + behavior.getInmateidId());
                System.out.println("----------------------------------------");
            }
        }
    }


    //============================================================================================================
    //============================================================================================================
    //============================================================================================================
    //============================================================================================================
    //============================================================================================================
    //============================================================================================================
    //============================================================================================================
    //============================================================================================================




    public void addNewInmateUsecase(String name,int age,String gender, String transferStatus, String location, String prisonname)
    {
        int prisonId;
        try {
            prisonId = DataBaseHelper.getPrisonIdByName(prisonname);
            if (prisonId == -1) {
                // Show error if the prison name does not correspond to a valid prisonId
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Prison");
                alert.setContentText("The selected prison is invalid.");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while fetching the prison ID.");
            alert.showAndWait();
            return;
        }

        // Create Inmate object, now including the prisonId
        Inmate inmate = new Inmate(name, 0, age, gender, prisonId, location, transferStatus,true);  // Pass prisonId to the Inmate object

        // Call the addInmate method from the database utility class to add the inmate to the database
        boolean success = DataBaseHelper.addInmate(inmate);

        // Show the result of the operation
        if (success) {
            // Show success message if the inmate is added successfully
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inmate Added");
            alert.setHeaderText(null);
            alert.setContentText("Inmate successfully added.");
            alert.showAndWait();

            // After successful addition, update PRMS and display in terminal
            addInmate(inmate);  // Update PRMS with the new inmate
            //prms.display();  // Refresh terminal output (show updated inmates)


        } else {
            // Show failure message if the insertion failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to Add Inmate");
            alert.setContentText("An error occurred while adding the inmate. Please try again.");
            alert.showAndWait();
        }
    }

    public Inmate searchInmate(String name)
    {
        // Search the inmate in the database using the provided name or ID
        Inmate foundInmate = DataBaseHelper.searchInmate(name);
        System.out.println("Found inmate: " + foundInmate);

        return foundInmate;
    }
    public void checkInmateELigibility(String inmateIdText){
        DataBaseHelper.checkInmateEligibility(inmateIdText);
    }
    public void addCommunityService(String inmateIdText, String selectedCommunity, String selectedPerformance,int guard_id)
    {

        try {
            int inmateId = Integer.parseInt(inmateIdText);

            // Check if the inmate is eligible for community service
            if (!DataBaseHelper.isInmateEligible(inmateId)) {
                showAlert("Eligibility Error", "Inmate is not eligible for community service.");
                return;
            }

            // If the performance is "Bad", update inmate's eligibility to false
            if ("Bad".equals(selectedPerformance)) {
                DataBaseHelper.updateInmateEligibility(inmateId, false);
            }
            for(Inmate i : inmates)
            {
                if (i.getId() == inmateId) i.setEligibility(false);
            }

            if (DataBaseHelper.addCommunityService(inmateId,selectedCommunity,selectedPerformance,guard_id) > -1) {
                behaviorRecords.add(new Behavior(
                        selectedPerformance,
                        "Performed Community Service",
                        selectedCommunity,
                        new java.sql.Date(System.currentTimeMillis()), // Use `Date` for the constructor
                        guard_id, // Reporting guard ID
                        inmateId
                ));
                displayBehaviorRecords();
            }


        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric ID.");
        }

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void addMaintenance(int maintenanceId, String date, String location, String urgencyLevel, int guardId, String details)
    {
        // Create a Maintenance object
        Maintenance maintenance = new Maintenance(0, date, location, urgencyLevel, guardId, details);

        // Insert into the database
        if (DataBaseHelper.insertMaintenanceIntoDatabase(maintenance)) {
            System.out.println("Maintenance record successfully added!");

            // Add the record to the PRMS system
            maintenanceRecords.add(maintenance); // Add to in-memory list
            System.out.println("Maintenance record added to PRMS system!");
            displayMaintenanceRecords();
        } else {
            System.out.println("Failed to add maintenance record.");
        }
    }
    public void addTreatment(int counsellorId, int inmateid, String type, String description)
    {

        TreatmentPlan treatmentPlan = new TreatmentPlan(counsellorId,inmateid, type, description);

        set_treatment(treatmentPlan);
        display();
        DataBaseHelper.insertTreatmentPlan(treatmentPlan);

    }
    public boolean addVisitation(int inmateID, String visitorName, LocalDateTime visitDate) throws SQLException {
        Visitation visitation = new Visitation(DataBaseHelper.getLastIdOfVisitation() + 1, inmateID, visitorName,visitDate);
        if (DataBaseHelper.addVisitation(visitation))
        {
            addvisitation(visitation);
            return true;
        }
        else return false;

    }
    public boolean addIncident(String incidentDate, int inmateId, String inmateName, int guardId, String location, String details) throws SQLException {
        Incident incident = new Incident(DataBaseHelper.getLastIdOfIncident()+1, incidentDate, inmateId, inmateName, guardId, location, details);
        if (DataBaseHelper.insertIncidentIntoDatabase(incident))
        {
            incidentRecords.add(incident);
            displayIncidents();
            return true;
        }
        else return false;
    }
    public void addSentence(java.util.Date date , String type, boolean paroleAllowed, int inmateid)
    {
        Sentence sentence = new Sentence(date, type, paroleAllowed, inmateid);
        set_sentence(inmateid, sentence);
        DataBaseHelper.insertSentence(sentence);


    }
    public void addBehavior(String type, String incident, String description, java.util.Date date, int reportingGuardId, int inmateid)
    {

        Behavior behavior = new Behavior(type, incident, description, date, reportingGuardId,inmateid);

        System.out.println(inmateid);
        set_behavior(inmateid,behavior);
        DataBaseHelper.insertBehavior(behavior);
        display();

    }
    public boolean addAttendenceRecord(String guardID, String inmateID, LocalDateTime shiftStart, String attendanceType) throws SQLException {
        AttendanceRecord record = new AttendanceRecord(
                String.valueOf(DataBaseHelper.getLastIdOfAttendance()+1),  // No need to manually assign RecordID (it will be auto-generated by DB)
                guardID,  // GuardID as String (null if no guard is selected)
                inmateID, // InmateID as String (null if no inmate is selected)
                shiftStart,
                attendanceType
        );

        if (DataBaseHelper.addAttendanceRecord(record)) {
            // If successful, add to PRMS attendance list
            addAttendanceRecord(record);  // Add to the PRMS attendance list
            System.out.println("Attendance record saved successfully.");
            attendanceRecords.add(record);
            return true;
        } else {
            System.out.println("Failed to save attendance record.");
            return false;
        }
    }
    public Inmate searchInmateById(int inmateId)
    {
        return DataBaseHelper.getInmateById(inmateId);
    }
    public String searchInmateNameById(int inmateId)
    {
        return DataBaseHelper.getInmateNameById(inmateId);
    }
    public void updateInmateLocation(int inmateId, String location)
    {
        for(Inmate i: inmates)
        {
            if (i.getId() == inmateId)
                i.setLocation(location);
        }
        DataBaseHelper.updateInmateLocation(inmateId, location);
    }
    public boolean transferGuard(Guard selectedGuard,String selectedPrison,String transferDate) throws SQLException {
        int currentPrisonId = selectedGuard.getPrisonid();

        // Fetch the current and new prison objects using the updated method
        // Fetch the 'from' prison (the current prison of the guard)
        Prison fromPrison = DataBaseHelper.getPrisonById(currentPrisonId, getGuards(), getInmates());

        // Fetch the new prison id based on the selected destination prison
        int newPrisonId = DataBaseHelper.getPrisonIdByName(selectedPrison);

        // Fetch the 'to' prison (the destination prison)
        Prison toPrison = DataBaseHelper.getPrisonById(newPrisonId, getGuards(), getInmates());

        // Check if the current prison is the same as the selected destination
        if (fromPrison.getPrisonID() ==  toPrison.getPrisonID()) {
            // If the prison is the same, show a message to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Transfer Needed");
            alert.setHeaderText(null);
            alert.setContentText("The guard is already assigned to the selected prison.");
            alert.showAndWait();
            return false; // Exit the method if no transfer is needed
        }

        // Proceed with the guard's transfer
        LocalDateTime transferDateTime = LocalDateTime.parse(transferDate + "T00:00:00");
        boolean transferSuccessful = DataBaseHelper.transferGuard(selectedGuard.getId(), newPrisonId, transferDateTime);

        if (transferSuccessful) {
            // Show success popup after the transfer is complete
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transfer Successful");
            alert.setHeaderText(null);
            alert.setContentText("Transfer confirmed for guard " + selectedGuard.getName());
            alert.showAndWait();

            // Create a Transfer object to add to the PRMS list
            Transfer transfer = new Transfer(
                    DataBaseHelper.getLastInsertedTransferId(),
                    selectedGuard.getId(),
                    fromPrison,
                    toPrison,
                    transferDateTime,"Guard"
            );

            // Call the addTransfer method in PRMS to update the list
            addTransfer(transfer);  // Add the transfer to the PRMS singleton
            for(Prison p: prisons)
            {
                if (p.getPrisonID() == selectedGuard.getPrisonid())
                    p.removeGuard(selectedGuard);
                if (p.getPrisonID() == newPrisonId)
                    p.addGuard(selectedGuard);
            }
            // Optionally, update the table data
            selectedGuard.setPrisonid(newPrisonId);
            display();
            ; // Refresh the table to show the updated data
            return true;
        } else {
            // Handle failure if the transfer didn't succeed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Transfer Failed");
            alert.setHeaderText(null);
            alert.setContentText("Transfer failed. Please try again.");
            alert.showAndWait();
            return false;
        }
    }
    public boolean transferInmate(String inmateID,String destinationPrison,String transferDate,String currentPrison) throws SQLException {
        int destinationPrisonID = DataBaseHelper.getPrisonIdByName(destinationPrison);

        if (destinationPrisonID == -1) {
            showError("Destination prison does not exist.");
            return false;
        }

        Inmate inmate = DataBaseHelper.searchInmate(inmateID);
        if (inmate == null) {
            showError("Inmate not found.");
            return false;
        }

        String inmateCurrentPrison = DataBaseHelper.getInmateCurrentPrison(Integer.parseInt(inmateID));

        if (!inmateCurrentPrison.equals(currentPrison)) {

            showError("Current prison does not match the inmate's actual prison." + inmateCurrentPrison + currentPrison);
            return false;
        }

        if (inmateCurrentPrison.equals(destinationPrison)) {
            showError("Inmate is already in the selected destination prison.");
            return false;
        }

        // Pass appropriate lists for allGuards and allInmates
        List<Guard> allGuards = getGuards();
        List<Inmate> allInmates = getInmates();

        Prison currentPrisonObj = DataBaseHelper.getPrisonById(inmate.getPrisonID(), allGuards, allInmates);
        Prison destinationPrisonObj = DataBaseHelper.getPrisonById(destinationPrisonID, allGuards, allInmates);

        LocalDateTime transferDateTime = LocalDateTime.parse(transferDate + "T00:00:00");
        boolean success = DataBaseHelper.transferInmate(Integer.parseInt(inmateID), destinationPrisonID, transferDateTime);

        if (success) {
            Transfer transfer = new Transfer(
                    DataBaseHelper.getLastInsertedTransferId(),
                    inmate.getInmateID(),
                    currentPrisonObj,
                    destinationPrisonObj,
                    transferDateTime, "Inmate"
            );
            PRMS.getInstance().addTransfer(transfer);

            inmate.setPrisonID(destinationPrisonID);

            showInfo("Transfer Successful", "The inmate has been successfully transferred.");
        return true;
        } else {
            showError("Transfer Failed");
            return false;
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show success message
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
