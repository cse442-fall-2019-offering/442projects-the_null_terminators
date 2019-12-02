package edu.buffalo.cse442.nullterminators;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Comparable {
    private int _id;     // event id
    private String _title;
    private String _details;
    private LocalDate _date;
    private LocalTime _time;
    private Tag _tag;

    /**
     * Event object to be placed into a DateNode
     * @param id -for database use-
     * @param title title of event
     * @param details details of event
     * @param when LocalDateTime, when the event is
     */
    public Event(int id, String title, String details, LocalDateTime when, Tag tag) {
        _id = id;
        _title = title;
        _details = details;
        _date = when.toLocalDate();
        _time = when.toLocalTime();
        _tag = tag;
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

    public Tag getTag() { return _tag; }

    public void setTag(Tag tagToAdd) {
        _tag = tagToAdd;
    }

    @Override
    public int compareTo(Object o) {
        LocalDateTime curr = LocalDateTime.of(this.getDate(), this.getTime());
        LocalDateTime obj = LocalDateTime.of(((Event) o).getDate(), ((Event) o).getTime());
        return curr.compareTo(obj);
    }
}
