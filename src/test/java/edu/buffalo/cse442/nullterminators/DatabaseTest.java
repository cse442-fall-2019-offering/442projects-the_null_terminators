package edu.buffalo.cse442.nullterminators;

import org.junit.*;
import java.sql.Connection;
import java.util.ArrayList;

public class DatabaseTest {
    @BeforeClass
    public static void testConnect() {
        System.out.println("Test Connection");
        Connection conn = Database.openConnection();
        Assert.assertNotNull(conn);
    }

    @Test
    public void testAddEvent() {
        System.out.println("Test Adding");
        Assert.assertNotEquals(Database.addEvent(
                "Test Event A", "2019-10-05 22:00", "This is one cool event"), -1);
    }

    @Test
    public void testAddEventNoDescription() {
        System.out.println("Test Adding Empty");
        Assert.assertNotEquals(Database.addEvent(
                "Test Event E", "2019-10-09 22:00", ""), -1);
    }

    @Test
    public void testGetEvents() {
        System.out.println("Test Getting");
        Database.addEvent("Test Event B", "2019-10-06 22:00", "You should see this event");
        Database.addEvent("Test Event C", "2019-10-07 22:00", "You should also see this one");
        Database.addEvent("Test Event D", "2019-10-08 22:00", "But you won't see this one");
        ArrayList<String[]> events = Database.getEvents("2019-10-06 00:00", "2019-10-08 00:00");
        Assert.assertFalse(events.isEmpty());
        events.forEach(event -> System.out.println("ID: "+event[0]+" Event: "+event[1]+" Date: "+event[2]+" Description: "+event[3]));
    }

    @Test
    public void testDeleteEvent() {
        System.out.println("Test Deleting");
        Database.addEvent("Test Event F", "2019-10-09 22:00", "This will be deleted");
        int id = Database.getLatestEventID();
        Database.deleteEvent(id);
        ArrayList<String[]> events = Database.getEvents("2019-10-09 21:00", "2019-10-09 23:00");
        Assert.assertTrue(events.isEmpty());
    }

    @AfterClass
    public static void testClearTable() {
        System.out.println("Test Clearing DB");
        Database.clearDatabase();
        ArrayList<String[]> events = Database.getEvents("1970-01-01 00:00", "2999-12-31 23:59");
        Assert.assertTrue(events.isEmpty());
    }
}