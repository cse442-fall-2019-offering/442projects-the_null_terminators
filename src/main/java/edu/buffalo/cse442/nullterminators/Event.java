package edu.buffalo.cse442.nullterminators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Event {
    private int _id;     // event id
    private String _title;
    private String _details;
    private LocalDate _date;
    private LocalTime _time;

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

    // TODO: sort
}
