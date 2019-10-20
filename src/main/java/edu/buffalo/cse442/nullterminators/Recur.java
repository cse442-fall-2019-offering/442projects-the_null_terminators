package edu.buffalo.cse442.nullterminators;

import java.time.LocalDate;

public class Recur {

    private LocalDate _from;
    private LocalDate _until;
    private int[] _daysSet = new int[]{0, 0, 0, 0, 0, 0 ,0};

    public Recur(LocalDate from, LocalDate until, int[] set) {
        _from = from;
        _until = until;
        _daysSet = set;
    }
}
