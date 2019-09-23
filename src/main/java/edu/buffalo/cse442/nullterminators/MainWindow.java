package edu.buffalo.cse442.nullterminators;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainWindow {

    @FXML private CalendarNode calendarGrid;
    @FXML private DateDisplay dateDisplayController;
    @FXML private Button month_before;
    @FXML private Button today;
    @FXML private Button month_after;

    private LocalDate date = LocalDate.now();

    public MainWindow() {
    }

    @FXML
    void initialize() {
        dateDisplayController.updateDateText(LocalDate.now());
        month_before.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long daysTillLastMonth = date.minusMonths(1).until(date, ChronoUnit.DAYS);
                date = date.minusDays(daysTillLastMonth);
                dateDisplayController.updateDateText(date);
                calendarGrid.changeMonths(date);
            }
        });
        today.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                date = LocalDate.now();
                dateDisplayController.updateDateText(date);
                calendarGrid.changeMonths(date);
            }
        });
        month_after.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long daysTillNextMonth = date.until(date.plusMonths(1), ChronoUnit.DAYS);
                date = date.plusDays(daysTillNextMonth);
                dateDisplayController.updateDateText(date);
                calendarGrid.changeMonths(date);
            }
        });
    }
}
