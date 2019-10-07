package edu.buffalo.cse442.nullterminators;

import java.sql.*;
import java.lang.String;

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
}
