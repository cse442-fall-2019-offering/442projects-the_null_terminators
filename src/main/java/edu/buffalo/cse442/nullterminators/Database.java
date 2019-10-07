package edu.buffalo.cse442.nullterminators;

import java.sql.*;
import java.lang.String;
import java.util.ArrayList;

public class Database {

    private static Connection openConnection() {
        String url = "jdbc:sqlite:calendar.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initialize() {
        Connection conn = openConnection();

        String sql = "CREATE TABLE IF NOT EXISTS events (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    name text NOT NULL,\n"
                + "    time datetime,\n"
                + "    description text\n"
                + ");";
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Boolean addEvent(String name, String datetime, String description) {
        Connection conn = openConnection();

        String sql = "INSERT INTO events(name, datetime, description) VALUES(?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, datetime);
            statement.setString(3, description);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
