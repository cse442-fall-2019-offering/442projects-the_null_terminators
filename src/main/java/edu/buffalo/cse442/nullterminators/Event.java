package edu.buffalo.cse442.nullterminators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Event implements Comparable {
    private int _id;     // event id
    private String _title;
    private String _details;
    private LocalDate _date;
    private LocalTime _time;

    /**
     * Event object to be placed into a DateNode
     * @param id -for database use-
     * @param title title of event
     * @param details details of event
     * @param when LocalDateTime, when the event is
     */
    public Event(int id, String title, String details, LocalDateTime when) {
        _id = id;
        _title = title;
        _details = details;
        _date = when.toLocalDate();
        _time = when.toLocalTime();
    }

    public int getID() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDetails() {
        return _details;
    }

    public LocalDate getDate() {
        return _date;
    }

    public LocalTime getTime() {
        return _time;
    }

    @Override
    public int compareTo(Object o) {
        LocalDateTime curr = LocalDateTime.of(this.getDate(), this.getTime());
        LocalDateTime obj = LocalDateTime.of(((Event) o).getDate(), ((Event) o).getTime());
        return curr.compareTo(obj);
    }
}
