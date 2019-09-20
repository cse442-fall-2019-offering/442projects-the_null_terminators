package edu.buffalo.cse442.nullterminators;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class MainWindow {

    @FXML private CalendarNode calendarGrid;
    @FXML private DateDisplay dateDisplayController;

    public MainWindow() {
    }

    @FXML
    void initialize() {
        dateDisplayController.updateDateText(LocalDate.now());
    }
}
