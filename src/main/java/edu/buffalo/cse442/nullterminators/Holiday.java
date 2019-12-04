package edu.buffalo.cse442.nullterminators;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Holiday {
    private File _hFile;
    private String _HOLIDAY_MASTER;

    public Holiday() {
        try {
            InputStream in = getClass().getResourceAsStream("holidays.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            _HOLIDAY_MASTER = reader.lines().collect(Collectors.joining());
        }

        catch (Exception FileNotFoundException) {
            System.out.println("The chosen file does not exist");
        }
    }


    public ArrayList<String> getHolidays() {
        int nameSkipper = 0;

        ArrayList<String> allHolidays = new ArrayList<String>();

        int firstIndex = _HOLIDAY_MASTER.indexOf("\"name\": ") + 9;
        String newHString = _HOLIDAY_MASTER.substring(firstIndex);
        int lastIndex = firstIndex + newHString.indexOf(',') - 1;

        String eventName = _HOLIDAY_MASTER.substring(firstIndex, lastIndex);
        allHolidays.add(eventName);

        while (newHString.contains("uuid")) {
            firstIndex = newHString.indexOf("\"name\": ") + 9;
            newHString = newHString.substring(firstIndex);
            lastIndex = newHString.indexOf(',') - 1;
            eventName = newHString.substring(0, lastIndex);
            if (nameSkipper >= 2) {
                allHolidays.add(eventName);
                nameSkipper = 0;
                continue;
            }
            nameSkipper += 1;
            newHString = newHString.substring(lastIndex);
        }

        return allHolidays;
    }


    public ArrayList<String> getDates() {
        int dateSkipper = 0;
        String year = "2019";
        ArrayList<String> allDates = new ArrayList<String>();

        int dateFirstIndex = _HOLIDAY_MASTER.indexOf("\"date\": ") + 9;
        String newDString = _HOLIDAY_MASTER.substring(dateFirstIndex);
        int dateLastIndex = dateFirstIndex + newDString.indexOf(',') - 1;

        String date = _HOLIDAY_MASTER.substring(dateFirstIndex, dateLastIndex);
        allDates.add(date);

        while (newDString.contains("uuid")) {
            dateFirstIndex = newDString.indexOf("\"date\": ") + 9;
            newDString = newDString.substring(dateFirstIndex);
            dateLastIndex = newDString.indexOf(',') - 1;
            date = newDString.substring(0, dateLastIndex);
            if (dateSkipper >= 1) {
                allDates.add(date);
                dateSkipper = 0;
                continue;
            }
            dateSkipper += 1;
            newDString = newDString.substring(dateLastIndex);
        }

        for (int i = 0; i < allDates.size(); ++i) {
            String fixedDate = year.concat(allDates.get(i).substring(4, allDates.get(i).length()));
            allDates.set(i, fixedDate.concat(" 00:00"));
        }

        return allDates;
    }
}
