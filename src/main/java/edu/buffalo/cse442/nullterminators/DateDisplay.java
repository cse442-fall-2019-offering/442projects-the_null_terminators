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
        dateDisplayText.setText(date.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
    }

    @FXML
    void initialize() {

    }
}
