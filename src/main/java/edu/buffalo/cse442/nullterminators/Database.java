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
                + "    description text,\n"
                + "    recur text,\n"
                + "    notification datetime,\n"
                + "    tag int\n"
                + ");";
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "CREATE TABLE IF NOT EXISTS tags (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    name text NOT NULL,\n"
                + "    color text\n"
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
    // Event Name, Event Date/Time, Event Description (may be blank), Days to recur (may be blank), Date/Time of notification (may be blank), Tag (-1 if none)
    // Date/Times should be in the following format: YYYY-MM-DD HH:MM (24-hour time)
    // RETURNS: -1 upon error, unique id of added event otherwise
    public static int addEvent(String name, String datetime, String description, String recur, String notification, int tag) {
        Connection conn = openConnection();
        String sql = "INSERT INTO events(name, datetime, description, recur, notification, tag) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, datetime);
            statement.setString(3, description);
            statement.setString(4, recur);
            statement.setString(5, notification);
            statement.setInt(6, tag);
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

    // Get latest event added to the database
    // RETURNS: String[]
    // Array items: Unique ID, Event Name, Event Date/Time, Event Description, Notification time, Tag
    public static String[] getLatestEvent() {
        Connection conn = openConnection();
        String[] retVal = new String[6];

        String sql = "SELECT * FROM events ORDER BY id DESC LIMIT 1";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                retVal[0] = Integer.toString(rs.getInt("id"));
                retVal[1] = rs.getString("name");
                retVal[2] = rs.getString("datetime");
                retVal[3] = rs.getString("description");
                retVal[4] = rs.getString("notification");
                retVal[5] = Integer.toString(rs.getInt("tag"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Get event with the soonest notification
    // RETURNS: String[]
    // Array items: Unique ID, Event Name, Event Date/Time, Event Description, Notification time, Tag
    public static String[] getNextNotification() {
        Connection conn = openConnection();
        String[] retVal = new String[5];

        String sql = "SELECT * FROM events ORDER BY datetime(notification) DESC LIMIT 1";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                retVal[0] = Integer.toString(rs.getInt("id"));
                retVal[1] = rs.getString("name");
                retVal[2] = rs.getString("datetime");
                retVal[3] = rs.getString("description");
                retVal[4] = rs.getString("notification");
                retVal[5] = Integer.toString(rs.getInt("tag"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Queries database for events between given date/time range
    // Date/time must be in following format: YYYY-MM-DD HH:MM
    // RETURNS: ArrayList<String[]>
    // Array items: Unique ID, Event Name, Event Date/Time, Event Description, Notification Date/Time, Event Tag
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
                String[] row = new String[6];
                row[0] = Integer.toString(rs.getInt("id"));
                row[1] = rs.getString("name");
                row[2] = rs.getString("datetime");
                row[3] = rs.getString("description");
                row[4] = rs.getString("notification");
                row[5] = Integer.toString(rs.getInt("tag"));
                retVal.add(row);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Queries database for events with given tag
    // RETURNS: ArrayList<String[]>
    // Array items: Unique ID, Event Name, Event Date/Time, Event Description, Recurring Days, Notification Date/Time, Event Tag
    public static ArrayList<String[]> getEventsByTag(int tag) {
        Connection conn = openConnection();
        ArrayList<String[]> retVal = new ArrayList<>();

        String sql = "SELECT * FROM events WHERE tag = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, tag);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String[] row = new String[7];
                row[0] = Integer.toString(rs.getInt("id"));
                row[1] = rs.getString("name");
                row[2] = rs.getString("datetime");
                row[3] = rs.getString("description");
                row[4] = rs.getString("recur");
                row[5] = rs.getString("notification");
                row[6] = Integer.toString(rs.getInt("tag"));
                retVal.add(row);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Queries database for all events with recurrences
    // RETURNS: ArrayList<String[]>
    // Array items: Unique ID, Event Name, Event Description, Recurring Days, Tag
    public static ArrayList<String[]> getRecurrentEvents() {
        Connection conn = openConnection();
        ArrayList<String[]> retVal = new ArrayList<>();

        String sql = "SELECT * FROM events WHERE recur IS NOT null AND recur != ''";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String[] row = new String[5];
                row[0] = Integer.toString(rs.getInt("id"));
                row[1] = rs.getString("name");
                row[2] = rs.getString("description");
                row[3] = rs.getString("recur");
                row[4] = Integer.toString(rs.getInt("tag"));
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

    public static int getLatestTagID() {
        Connection conn = openConnection();
        int id = -1;

        String sql = "SELECT id FROM tags ORDER BY id DESC LIMIT 1";
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

    // Adds an event to the database
    // Takes parameters:
    // Tag Name, Tag Color (hex value)
    // Color should be in the following format: #FFFFFF
    // RETURNS: -1 upon error, unique id of added tag otherwise
    public static int addTag(String name, String color) {
        Connection conn = openConnection();
        String sql = "INSERT INTO tags(name, color) VALUES(?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, color);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return getLatestTagID();
    }

    // Queries database for all tags
    // RETURNS: ArrayList<String[]>
    // Array items: Unique ID, Tag Name, Tag Color
    public static ArrayList<String[]> getTags() {
        Connection conn = openConnection();
        ArrayList<String[]> retVal = new ArrayList<>();

        String sql = "SELECT * FROM tags";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String[] row = new String[3];
                row[0] = Integer.toString(rs.getInt("id"));
                row[1] = rs.getString("name");
                row[2] = rs.getString("color");
                retVal.add(row);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retVal;
    }

    // Removes tag from the database by corresponding unique ID
    // Takes parameter: Unique ID of tag
    // RETURNS: false on error, true otherwise
    public static Boolean deleteTag(int id) {
        Connection conn = openConnection();

        String sql = "DELETE FROM tags WHERE id = ?";
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