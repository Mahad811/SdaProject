package com.example.sda_project.db;

import com.example.sda_project.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {

    // Database connection details
    private static final String URL =  "jdbc:sqlserver://localhost:1433;" // SQL Server URL
            + "databaseName=SDA_Assignment;encrypt=true;trustServerCertificate=true";

    private static final String USER = "HASANWAQAR";
    private static final String PASSWORD = "Hasan_SQL";

    // Method to get a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to fetch all court cases from the database
    public static List<CourtCase> getCourtCases() {
        List<CourtCase> courtCases = new ArrayList<>();

        String query = "SELECT * FROM courtcase";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Loop through the result set and create CourtCase objects
            while (resultSet.next()) {
                int caseId = resultSet.getInt("caseid");
                String caseName = resultSet.getString("casename");
                Date caseDate = resultSet.getDate("date");
                int inmateId = resultSet.getInt("inmateid");
                String inmateName = resultSet.getString("inmatename");
                // Create a CourtCase object and add it to the list
                CourtCase courtCase = new CourtCase(caseId, caseName, caseDate , inmateId, inmateName);
                courtCases.add(courtCase);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exception properly in real-world applications
        }

        return courtCases;
    }

    // Other database operations can be added here (insert, update, delete, etc.)
    public static void insertSentence(Sentence sentence) {
        // SQL Query to insert a Sentence record
        String sql = "INSERT INTO Sentence (date, type, paroleAllowed, inmateid) VALUES (?, ?, ?, ?)";

        // Establish connection and execute query
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters in the PreparedStatement
            statement.setDate(1, new java.sql.Date(sentence.getDate().getTime()));  // Convert java.util.Date to java.sql.Date
            statement.setString(2, sentence.getType());
            statement.setBoolean(3, sentence.getParoleAllowed());
            statement.setInt(4, sentence.getInmateId());

            // Execute the insert statement
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("A new sentence was inserted successfully!");
            } else {
                System.out.println("Failed to insert the sentence.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBehavior(Behavior behavior) {
        String sql = "INSERT INTO Behavior (type, incident, description, date, reportingGuardID,inmateID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the parameters
            pstmt.setString(1, behavior.getType());
            pstmt.setString(2, behavior.getIncident());
            pstmt.setString(3, behavior.getDescription());
            pstmt.setDate(4, new java.sql.Date(behavior.getDate().getTime())); // Convert java.util.Date to java.sql.Date
            pstmt.setInt(5, behavior.getReportingGuardID());
            pstmt.setInt(6, behavior.getInmateidId());

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertTreatmentPlan(TreatmentPlan treatmentPlan) {
        // Define the SQL queries
        String deactivateQuery = "UPDATE TreatmentPlan SET active = ? WHERE inmateid = ?";
        String insertQuery = "INSERT INTO TreatmentPlan (counsellorid, inmateid, type, description, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            // Start a transaction
            connection.setAutoCommit(false);

            // Step 1: Deactivate other treatment plans for the same inmate
            try (PreparedStatement deactivateStmt = connection.prepareStatement(deactivateQuery)) {
                deactivateStmt.setBoolean(1, false); // Set active to false
                deactivateStmt.setInt(2, treatmentPlan.getInmateId()); // Specify the inmate ID
                deactivateStmt.executeUpdate();
            }

            // Step 2: Insert the new treatment plan as active
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, treatmentPlan.getCounsellorId()); // Counsellor ID
                insertStmt.setInt(2, treatmentPlan.getInmateId());     // Inmate ID
                insertStmt.setString(3, treatmentPlan.getType());     // Type
                insertStmt.setString(4, treatmentPlan.getDescription()); // Description
                insertStmt.setBoolean(5, true); // Set the new treatment plan as active
                insertStmt.executeUpdate();
            }

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Inmate searchInmate(String query) {
        String sqlQuery;
        if (query.matches("\\d+")) { // If the query is numeric (ID search)
            sqlQuery = "SELECT * FROM Inmate WHERE id = ?";
        } else { // If the query is a name search
            sqlQuery = "SELECT * FROM Inmate WHERE name LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            if (query.matches("\\d+")) {
                pstmt.setInt(1, Integer.parseInt(query)); // Search by InmateID
            } else {
                pstmt.setString(1, "%" + query + "%"); // Search by Name (partial match)
            }

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Create an Inmate object from the ResultSet and return it
                int inmateID = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String transferStatus = rs.getString("TransferStatus");
                String location = rs.getString("location");
                int prisonID = rs.getInt("prisonID");  // Fetch the PrisonID
                boolean elig = rs.getBoolean("eligibilty");

                return new Inmate(name,inmateID,age, gender, prisonID, location, transferStatus,elig    );
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle error
        }

        return null; // No inmate found
    }
    public static int getLastInsertedTransferId() throws SQLException {
        Connection conn = null;
        int lastId = -1; // Default value if no transfers exist

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query to fetch the last inserted transfer ID
            String query = "SELECT MAX(id) AS last_id FROM transfer";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastId = rs.getInt("last_id"); // Retrieve the last inserted ID
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                conn.close();
            }
        }

        return lastId;
    }

    public static Prison getPrisonById(int prisonId, List<Guard> allGuards, List<Inmate> allInmates) throws SQLException {
        // Get the connection to the database
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        String query = "SELECT * FROM prison WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, prisonId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve the data from the result set
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");

                // Create the Prison object
                return new Prison(id, name, location, allGuards, allInmates);
            } else {
                return null;  // Return null if no prison is found
            }
        } finally {
            // Close the connection
            if (conn != null) {
                conn.close();
            }
        }
    }
    public String getInmateIDByName(String name) throws SQLException {
        String query = "SELECT id FROM Inmate WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null;
    }

    public static boolean addInmate(Inmate inmate) {
        String query = "INSERT INTO Inmate (name, age, gender, TransferStatus, location, prisonID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create a PreparedStatement using the connection
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                // Setting values from Inmate object into the query
                preparedStatement.setString(1, inmate.getName());
                preparedStatement.setInt(2, inmate.getAge());
                preparedStatement.setString(3, inmate.getGender());
                preparedStatement.setString(4, inmate.getTransferStatus());
                preparedStatement.setString(5, inmate.getLocation());
                preparedStatement.setInt(6, inmate.getPrisonID()); // Set the PrisonID as int

                // Executes the query and checks if rows were affected
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;  // Return true if insert was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print any SQL exceptions for debugging
            return false; // Return false if there's an issue
        }
    }
    public static int getLastIdOfIncident() throws SQLException {
        Connection conn = null;
        int lastId = -1; // Default value if no visitations exist

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query to fetch the last inserted visitation ID
            String query = "SELECT MAX(incident_id) AS last_id FROM Incident";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastId = rs.getInt("last_id"); // Retrieve the last inserted ID
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                conn.close();
            }
        }

        return lastId;
    }
    public static int getLastIdOfVisitation() throws SQLException {
        Connection conn = null;
        int lastId = -1; // Default value if no visitations exist

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query to fetch the last inserted visitation ID
            String query = "SELECT MAX(VisitationID) AS last_id FROM Visitation";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastId = rs.getInt("last_id"); // Retrieve the last inserted ID
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                conn.close();
            }
        }

        return lastId;
    }
    public static int getLastIdOfAttendance() throws SQLException {
        Connection conn = null;
        int lastId = -1; // Default value if no visitations exist

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query to fetch the last inserted visitation ID
            String query = "SELECT MAX(RecordID) AS last_id FROM AttendanceRecords";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    lastId = rs.getInt("last_id"); // Retrieve the last inserted ID
                }
            }
        } finally {
            // Ensure the connection is closed
            if (conn != null) {
                conn.close();
            }
        }

        return lastId;
    }

    public static int getPrisonIdByName(String prisonName) throws SQLException {
        String query = "SELECT id FROM Prison WHERE name = ?";
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, prisonName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id"); // Return the PrisonID if found
            }
        }
        return -1; // Return -1 if no prison with the specified name is found
    }



    // Method to check if the admin exists based on the provided ID, name, and password
    public static boolean checkAdminLogin(  String name, String password) {
        String query = "SELECT * FROM PrisonAdmin WHERE name = ? AND password COLLATE SQL_Latin1_General_CP1_CS_AS = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, password);  // Add password check here
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a result is returned, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of error
        }
    }

    public static boolean checkGuardLogin(  String name, String password) {
        String query = "SELECT * FROM Guard WHERE name = ? AND password COLLATE SQL_Latin1_General_CP1_CS_AS = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, password);  // Add password check here
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a result is returned, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of error
        }
    }
    public static boolean checkInmateExistence(int inmateId) {
        String query = "SELECT * FROM Inmate WHERE id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, inmateId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a result is returned, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of error
        }
    }
    // Method to add a new admin to the database (if needed in the future)
    public static boolean addAdmin(String name, String password) {
        String query = "INSERT INTO PrisonAdmin (name, password) VALUES (?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, password);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if one row is inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of error
        }
    }

    public static List<Inmate> fetchInmates() {
        List<Inmate> inmates = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Inmate"; // Assuming you have an "Inmate" table
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int inmateID = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String transferStatus = rs.getString("TransferStatus");
                    String location = rs.getString("location");
                    int prisonID = rs.getInt("prisonID"); // Fetch the PrisonID
                    boolean elig = rs.getBoolean("eligibilty");
                    // Create an Inmate object and include PrisonID
                    Inmate inmate = new Inmate(name, inmateID,age, gender, prisonID, location, transferStatus,elig);
                    inmates.add(inmate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inmates;
    }


    public static boolean addVisitation(Visitation visitation) {
        // First, ensure the inmate belongs to the correct prison
        int inmateID = visitation.getInmateID();
        String checkPrisonQuery = "SELECT prisonID FROM Inmate WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Check if the InmateID matches a valid prisonID
            try (PreparedStatement checkPrisonStmt = conn.prepareStatement(checkPrisonQuery)) {
                checkPrisonStmt.setInt(1, inmateID);
                ResultSet rs = checkPrisonStmt.executeQuery();

                if (rs.next()) {
                    int inmatePrisonID = rs.getInt("prisonID");

                    // Now check if the visitation is allowed for that specific prison
                    String sql = "INSERT INTO Visitation (InmateID, VisitorName, VisitDate) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, inmateID);  // InmateID as int
                    pstmt.setString(2, visitation.getVisitorName()); // VisitorName as String
                    pstmt.setTimestamp(3, Timestamp.valueOf(visitation.getVisitDate())); // VisitDate as Timestamp

                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if visitation could not be added or inmate not found
    }
    public static boolean updateInmatePrisonID(Inmate inmate) {
        String query = "UPDATE Inmate SET prisonID = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, inmate.getPrisonID());
            preparedStatement.setInt(2, inmate.getInmateID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful

        } catch (SQLException e) {
            e.printStackTrace(); // Log any exceptions
            return false;
        }
    }

    public static String getInmateCurrentPrison(int inmateID) throws SQLException {
        // Query to get the PrisonID of the inmate
        String query = "SELECT prisonId FROM Inmate WHERE id = ?";
        String currentPrison = "";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, inmateID); // Use inmateID to find their PrisonID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int prisonID = rs.getInt("prisonID");

                // Now, use the PrisonID to get the prison name from the Prisons table
                currentPrison = getPrisonNameByID(prisonID);
            }
        }
        return currentPrison;
    }

    // Helper method to get the prison name by PrisonID
    private static String getPrisonNameByID(int prisonID) throws SQLException {
        String query = "SELECT name FROM Prison WHERE id = ?";
        String prisonName = "";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, prisonID); // Fetch the name of the prison with this ID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                prisonName = rs.getString("name"); // Adjust column name if needed
            }
        }
        return prisonName;
    }


    // Fetch all prisons
    public static List<String> fetchPrisons() {
        List<String> prisons = new ArrayList<>();
        // Database query to fetch prisons
        String query = "SELECT DISTINCT name FROM Prison";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                prisons.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prisons;
    }
    public boolean isInmateInPrison(String inmateName, String prisonName) {
        String query = "SELECT P.name FROM Prison P " +
                "JOIN Inmate I ON P.id = I.prisonId " +
                "WHERE I.name = ? AND P.name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the inmate name and prison name as parameters
            statement.setString(1, inmateName);  // Set inmate name parameter
            statement.setString(2, prisonName);  // Set prison name parameter

            ResultSet resultSet = statement.executeQuery();

            // Returns true if the inmate's name and the selected prison name match
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static List<Guard> fetchGuards() throws SQLException {
        List<Guard> guards = new ArrayList<>();
        String query = "SELECT * FROM Guard";  // Assuming you have a 'Guards' table

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int guardId = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age"); // Fetch Age
                String gender = rs.getString("gender"); // Fetch Gender
                int prisonId = rs.getInt("prisonid"); // Fetch PrisonID
                String password = rs.getString("password"); // Fetch Password

                // Create a Guard object and add it to the list
                Guard guard = new Guard(name, guardId, age, gender, prisonId, password);
                guards.add(guard);
            }
        }
        return guards;
    }


    public static boolean updateGuardPrison(int guardId, int newPrisonId) throws SQLException {
        String query = "UPDATE Guard SET prisonID = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newPrisonId);
            stmt.setInt(2, guardId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public static boolean addAttendanceRecord(AttendanceRecord record) {
        String query = "INSERT INTO AttendanceRecords (GuardID, InmateID, Attendance, AttendanceType) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Prepare statement for inserting the attendance record
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, record.getGuardID());  // Guard ID or null if inmate
                pstmt.setString(2, record.getInmateID()); // Inmate ID or null if guard
                pstmt.setTimestamp(3, Timestamp.valueOf(record.getShiftStart())); // Attendance time
                pstmt.setString(4, record.getAttendanceType()); // "Guard" or "Inmate"

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;  // Return true if insertion was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if an error occurred
    }
    public String getGuardIDByName(String name) throws SQLException {
        String query = "SELECT id FROM Guard WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null;
    }
    public List<AttendanceRecord> fetchAttendanceRecords() throws SQLException {
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        String sql = "SELECT GuardID, InmateID, Attendance, AttendanceType FROM AttendanceRecords";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String guardID = resultSet.getString("GuardID");
                String inmateID = resultSet.getString("InmateID");
                LocalDateTime attendance = resultSet.getTimestamp("Attendance").toLocalDateTime();
                String attendanceType = resultSet.getString("AttendanceType");

                AttendanceRecord record = new AttendanceRecord(null,guardID, inmateID, attendance, attendanceType);
                attendanceRecords.add(record);
            }
        }

        return attendanceRecords;
    }
    // Method to fetch visitation records from the database (with visitationID)
    public List<Visitation> fetchVisitations() {
        List<Visitation> visitations = new ArrayList<>();
        String query = "SELECT * FROM Visitation";  // Assuming you have a 'Visitation' table

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Extract data from the result set
                int visitationID = resultSet.getInt("VisitationID");  // Fetch visitationID
                int inmateID = resultSet.getInt("InmateID");
                String visitorName = resultSet.getString("VisitorName");
                Timestamp visitTime = resultSet.getTimestamp("VisitDate");

                // Ensure visitTime is not null before converting to LocalDateTime
                LocalDateTime visitDate = (visitTime != null) ? visitTime.toLocalDateTime() : null;

                // Create a new Visitation object using the fetched attributes
                Visitation visitation = new Visitation(visitationID, inmateID, visitorName, visitDate);
                visitations.add(visitation);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle exceptions (you can log this)
        }

        return visitations;
    }
    public boolean isVisitationSlotAvailable(int inmateID, String visitorName, LocalDateTime visitDate) {
        String query = "SELECT COUNT(*) FROM Visitation V " +
                "INNER JOIN Inmate I ON V.InmateID = I.id " +
                "WHERE I.id = ? AND V.VisitorName = ? AND V.VisitDate = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, inmateID);
            preparedStatement.setString(2, visitorName);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(visitDate)); // Convert LocalDateTime to Timestamp

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                // Return true if no existing visitation conflicts are found
                return count == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Default to no conflict
    }
    public static boolean transferGuard(int guardID, int newPrisonID, LocalDateTime transferDate) throws SQLException {
        String transferQuery = "INSERT INTO Transfer (entityID, fromPrisonID, toPrisonID, transferDate, type) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(transferQuery)) {
            // Assuming guard's current prison ID is fetched before calling this function
            int fromPrisonID = getGuardPrisonID(guardID);

            // Set parameters for the transfer query
            stmt.setInt(1, guardID);            // Entity ID (GuardID)
            stmt.setInt(2, fromPrisonID);       // Current prison ID
            stmt.setInt(3, newPrisonID);        // New prison ID
            stmt.setTimestamp(4, Timestamp.valueOf(transferDate));  // Transfer date
            stmt.setString(5, "Guard");
            // Execute the insert query and check if successful
            int rowsAffected = stmt.executeUpdate();
            String upd_query = "UPDATE Guard set prisonid = " + String.valueOf(newPrisonID) + " where id = " + String.valueOf(guardID);
            PreparedStatement stmt2 = conn.prepareStatement(upd_query);
            int r = stmt2.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private static int getGuardPrisonID(int guardID) throws SQLException {
        String query = "SELECT prisonID FROM Guard WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guardID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("prisonID");
                } else {
                    throw new SQLException("Guard not found with ID: " + guardID);
                }
            }
        }
    }

    public static boolean transferInmate(int inmateID, int newPrisonID, LocalDateTime transferDate) throws SQLException {
        String transferQuery = "INSERT INTO Transfer (entityID, fromPrisonID, toPrisonID, transferDate, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(transferQuery)) {
            // Assuming inmate's current prison ID is fetched before calling this function
            int fromPrisonID = getInmatePrisonID(inmateID);

            // Set parameters for the transfer query
            stmt.setInt(1, inmateID);           // Entity ID (InmateID)
            stmt.setInt(2, fromPrisonID);       // Current prison ID
            stmt.setInt(3, newPrisonID);        // New prison ID
            stmt.setTimestamp(4, Timestamp.valueOf(transferDate));  // Transfer date
            stmt.setString(5, "Inmate");
            // Execute the insert query and check if successful
            int rowsAffected = stmt.executeUpdate();
            String upd_query = "UPDATE Inmate set prisonid = " + String.valueOf(newPrisonID) + " where id = " + String.valueOf(inmateID);
            PreparedStatement stmt2 = conn.prepareStatement(upd_query);
            int r = stmt2.executeUpdate();

            return rowsAffected > 0;
        }
    }

    private static int getInmatePrisonID(int inmateID) throws SQLException {
        String query = "SELECT prisonId FROM Inmate WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, inmateID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("prisonId");
                } else {
                    throw new SQLException("Inmate not found with ID: " + inmateID);
                }
            }
        }
    }


    public static boolean insertMaintenanceIntoDatabase(Maintenance maintenance) {

        // Use RETURN_GENERATED_KEYS to fetch the auto-generated ID
        String query = "INSERT INTO Maintenance (maintenance_date, location, urgency_level, guard_id, details) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, maintenance.getMaintenanceDate());
            preparedStatement.setString(2, maintenance.getLocation());
            preparedStatement.setString(3, maintenance.getUrgencyLevel());
            preparedStatement.setInt(4, maintenance.getGuardId());
            preparedStatement.setString(5, maintenance.getDetails());

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated ID
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1); // Fetch the generated ID
                        maintenance.setMaintenanceId(generatedId); // Update the Maintenance object
                        System.out.println("Generated Maintenance ID: " + generatedId);
                    }
                }
                return true;
            } else {
                return false; // No rows affected
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static Inmate getInmateById(int inmateId) {
        String query = "SELECT id, name, location,transferStatus,eligibilty FROM Inmate WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, inmateId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                String tran = resultSet.getString("transferStatus");
                Boolean elig = resultSet.getBoolean("eligibilty");
                // Return an Inmate object with the fetched data
                return new Inmate(name, inmateId, 0, "", 0,location,tran,elig); // Age, Gender, Prison ID are not fetched, set placeholders
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
        }
        return null;
    }

    // Fetch the inmate's name by ID from the database (you can add other fields if needed)
    public static String getInmateNameById(int inmateId) {
        String query = "SELECT name FROM Inmate WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, inmateId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
        }
        return null;
    }

    // Update the inmate's location in the database
    public static void updateInmateLocation(int inmateId, String location) {
        String query = "UPDATE Inmate SET location = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, location);
            preparedStatement.setInt(2, inmateId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                showAlert("Error", "Inmate with ID " + inmateId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating the inmate's location.");
        }
    }
    public static boolean insertIncidentIntoDatabase(Incident incident) {

        // Use RETURN_GENERATED_KEYS to fetch the auto-generated ID
        String query = "INSERT INTO Incident (incident_date, inmate_id, inmate_name, guard_id, location, details) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, incident.getIncidentDate());
            preparedStatement.setInt(2, incident.getInmateId());
            preparedStatement.setString(3, incident.getInmateName());
            preparedStatement.setInt(4, incident.getGuardId());
            preparedStatement.setString(5, incident.getLocation());
            preparedStatement.setString(6, incident.getDetails());

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated ID
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        incident.setIncidentId(generatedId); // Update the Incident object with the generated ID
                        System.out.println("Generated Incident ID: " + generatedId);
                    }
                }
                return true;
            } else {
                return false; // No rows affected
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // Display alert messages
    private static void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static void checkInmateEligibility(String  inmateIdText)
    {
        try {
            int inmateId = Integer.parseInt(inmateIdText);

            // Check eligibility from the database
            try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);) {
                String query = "SELECT name, eligibilty FROM Inmate WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, inmateId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String name = resultSet.getString("name");
                            boolean eligibility = resultSet.getBoolean("eligibilty");

                            String eligibilityMessage = eligibility
                                    ? "Inmate " + name + " is eligible for community service."
                                    : "Inmate " + name + " is NOT eligible for community service.";
                            showAlert("Eligibility Check", eligibilityMessage);
                        } else {
                            showAlert("Not Found", "No inmate found with ID: " + inmateId);
                        }
                    }
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid numeric ID.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
        }
    }

    public static int addCommunityService(int inmateId, String selectedCommunity, String selectedPerformance,int guard_id)
    {
        // Insert the community service record
        try (Connection connection = getConnection()) {
            String insertQuery = "INSERT INTO Behavior (type, incident, description, date, reportingGuardId, inmateid) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, selectedPerformance);
                statement.setString(2, "Performed Community Service");
                statement.setString(3, selectedCommunity);
                statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                System.out.println(guard_id);
                statement.setInt(5, guard_id); // Hardcoded guard ID for now
                statement.setInt(6, inmateId);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    // Retrieve the generated ID for the Behavior record
                    int behaviorId = -1;
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            behaviorId = generatedKeys.getInt(1);
                        }
                    }
                    showAlert("Success", "Community service record successfully added for inmate ID: " + inmateId);
                    return behaviorId;


                } else {
                    showAlert("Failure", "Failed to add community service record. Please try again.");
                    return -1;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
            return -1;
        }

    }
    public static boolean isInmateEligible(int inmateId) {
        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)) {
            String query = "SELECT eligibilty FROM Inmate WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, inmateId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean("eligibilty");
                    } else {
                        showAlert("Not Found", "No inmate found with ID: " + inmateId);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while checking eligibility.");
            return false;
        }
    }
    public static void updateInmateEligibility(int inmateId, boolean eligibility) {
        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)) {
            String updateQuery = "UPDATE Inmate SET eligibilty = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setBoolean(1, eligibility);
                statement.setInt(2, inmateId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert("Eligibility Update", "Inmate eligibility updated successfully.");
                } else {
                    showAlert("Eligibility Update Error", "Failed to update inmate eligibility.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating eligibility.");
        }
    }

}
