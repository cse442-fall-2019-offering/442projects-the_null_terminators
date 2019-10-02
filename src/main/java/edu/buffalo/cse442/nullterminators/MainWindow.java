package edu.buffalo.cse442.nullterminators;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;

public class MainWindow {
    @FXML private DotwNode dotw;
    @FXML private CalendarNode calendarGrid;
    @FXML private DateDisplay dateDisplayController;
    @FXML private Button before;
    @FXML private Button today;
    @FXML private Button after;
    /*
    @FXML private Button weekViewButton;
    @FXML private Button monthViewButton;
    @FXML private Button dayViewButton;
     */

    @FXML private MenuItem RESET;


    @FXML private MenuItem weekViewButton;
    @FXML private MenuItem monthViewButton;
    @FXML private MenuItem dayViewButton;
    @FXML private MenuItem ubImport;
    @FXML private MenuItem importCal;

    private LocalDate date = LocalDate.now();
    private CalendarNode.VIEW view = CalendarNode.VIEW.MONTH;


    public MainWindow() {

    }

    @FXML
    void initialize() {
        dotw.show();
        dateDisplayController.updateDateText(LocalDate.now());

        before.setOnAction(e -> {
            switchHelper(-1);
            dateDisplayController.updateDateText(date);
            calendarGrid.change(view, date);
        });

        today.setOnAction(e -> {
            date = LocalDate.now();
            dateDisplayController.updateDateText(date);
            calendarGrid.change(view, date);
        });

        after.setOnAction(e -> {
            switchHelper(1);
            dateDisplayController.updateDateText(date);
            calendarGrid.change(view, date);
        });

        weekViewButton.setOnAction(e -> {
            dotw.show();
            view = CalendarNode.VIEW.WEEK;
            calendarGrid.change(view, date);
        });
        monthViewButton.setOnAction(e -> {
            dotw.show();
            view = CalendarNode.VIEW.MONTH;
            calendarGrid.change(view, date);
        });
        dayViewButton.setOnAction(e -> {
            dotw.clear();
            view = CalendarNode.VIEW.DAY;
            calendarGrid.change(view, date);
        });

        ubImport.setOnAction(e -> {
            new importUBCal().window();
        });

        RESET.setOnAction(e -> {
            System.out.println(LocalDate.now().getDayOfWeek().getValue() + " - " + LocalDate.now().getDayOfWeek());
        });
        importCal.setOnAction(e -> {
            new importCalendars().window();
        });

    }

    /** if dir = -1:
     *      go backward
     *  if dir =  1:
     *      go forward
     */
    private void switchHelper(int dir) {
        switch(view) {
            case MONTH: {
                if (dir == 1) {
//                    long daysTillNextMonth = date.until(date.plusMonths(1), ChronoUnit.DAYS);
//                    date = date.plusDays(daysTillNextMonth);
                    date = date.plusMonths(1);
                } else {
//                    long daysTillLastMonth = date.minusMonths(1).until(date, ChronoUnit.DAYS);
//                    date = date.minusDays(daysTillLastMonth);
                    date = date.minusMonths(1);
                }}
            case WEEK: {
                if (dir == 1) {
                    date = date.plusWeeks(1);
                } else {
                    date = date.minusWeeks(1);
                }}
            case DAY: {
                if (dir == 1) {
                    date = date.plusDays(1);
                } else {
                    date = date.minusDays(1);
                }}
        }
    }
}

