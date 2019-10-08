package edu.buffalo.cse442.nullterminators;

import java.sql.*;
import java.lang.String;
import java.util.ArrayList;

public class Database {

    // Opens a connection to the database, creating it if it doesn't exist,
    // and creates 'events' table if it doesn't exist
    // RETURNS: Connection
    public static Connection openConnection() {
        String url = "jdbc:sqlite:calendar.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql = "CREATE TABLE IF NOT EXISTS events (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    name text NOT NULL,\n"
                + "    datetime datetime,\n"
                + "    description text\n"
                + ");";
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    // Adds an event to the database
    // Takes parameters:
    // Event Name, Event Date/Time, Event Description (may be blank)
    // Date/Time should be in the following format: YYYY-MM-DD HH:MM (24-hour time)
    // RETURNS: -1 upon error, unique id of added event otherwise
    public static int addEvent(String name, String datetime, String description) {
        Connection conn = openConnection();
        String sql = "INSERT INTO events(name, datetime, description) VALUES(?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, datetime);
            statement.setString(3, description);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return getLatestEventID();
    }

    public static int getLatestEventID() {
        Connection conn = openConnection();
        int id = -1;

        String sql = "SELECT id FROM events ORDER BY id DESC LIMIT 1";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            id = rs.getInt("id");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    // Queries database for events between given date/time range
    // Date/time must be in following format: YYYY-MM-DD HH:MM
    // RETURNS: ArrayList<String[]>
    // Array items: Unique ID, Event Name, Event Date/Time, Event Description
    public static ArrayList<String[]> getEvents(String startDate, String endDate) {
        Connection conn = openConnection();
        ArrayList<String[]> retVal = new ArrayList<>();

        String sql = "SELECT * FROM events WHERE datetime >= ? AND datetime <= ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String[] row = new String[4];
                row[0] = Integer.toString(rs.getInt("id"));
                row[1] = rs.getString("name");
                row[2] = rs.getString("datetime");
                row[3] = rs.getString("description");
                retVal.add(row);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Removes event from the database by corresponding unique ID
    // Takes parameter: Unique ID of event
    // RETURNS: false on error, true otherwise
    public static Boolean deleteEvent(int id) {
        Connection conn = openConnection();

        String sql = "DELETE FROM events WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // Deletes all events from the database
    // BE CAREFUL
    public static void clearDatabase() {
        Connection conn = openConnection();

        String sql = "DELETE FROM events";

        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
