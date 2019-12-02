package edu.buffalo.cse442.nullterminators;

import java.time.LocalDate;
import java.util.Random;

public class Recur {

    private int _id;
    private LocalDate _from;
    private LocalDate _until;
    private String _dotw;       // UMTWRFS

    public Recur() {
        _dotw = "0000000"; // String of 0s and 1s
    }

    public void setFrom(LocalDate f) {
        _from = f;
    }
    public void setUntil(LocalDate u) {
        _until = u;
    }
    public void setDOTW(String s) { _dotw = s; }

    public LocalDate getFrom() {
        return _from;
    }
    public LocalDate getUntil() {
        return _until;
    }
    public String getDOTW() { return _dotw; }

    public String toString() {
        String ret = "";
        Random rand = new Random();
        _id = rand.nextInt();
        ret = _from + "/" + _until + "/" + _dotw + "/" + _id;
        return ret;
    }


}
