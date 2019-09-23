package edu.buffalo.cse442.nullterminators;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDisplay {

    @FXML private Text dateDisplayText;

    public DateDisplay() {

    }

    public void updateDateText(LocalDate date) {
        String month = date.format(DateTimeFormatter.ofPattern("LLLL yyyy"));

        // will do later if my heart is feelin it
        /*
        switch(month) {
            case "January":

        }

        */
        dateDisplayText.setText(month);
    }

    @FXML
    void initialize() {

    }
}
