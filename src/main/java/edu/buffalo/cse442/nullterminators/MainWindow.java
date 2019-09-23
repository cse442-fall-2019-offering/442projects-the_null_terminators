package edu.buffalo.cse442.nullterminators;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class MainWindow {

    @FXML private CalendarNode calendarGrid;
    @FXML private DateDisplay dateDisplayController;
    @FXML private Button month_before;
    @FXML private Button today;
    @FXML private Button month_after;
    @FXML private Button weekViewButton;
    @FXML private Button monthViewButton;
    @FXML private Button dayViewButton;

    public MainWindow() {
    }

    @FXML
    void initialize() {
        dateDisplayController.updateDateText(LocalDate.now());
        month_before.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calendarGrid.setOffset(calendarGrid.getOffset().minusMonths(1));
                calendarGrid.changeView(CalendarNode.VIEW.MONTH);
                dateDisplayController.updateDateText(calendarGrid.getOffset());
            }
        });
        today.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calendarGrid.setOffset(LocalDate.now());
                calendarGrid.changeView(CalendarNode.VIEW.MONTH);
                dateDisplayController.updateDateText(calendarGrid.getOffset());
            }
        });
        month_after.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calendarGrid.setOffset(calendarGrid.getOffset().plusMonths(1));
                calendarGrid.changeView(CalendarNode.VIEW.MONTH);
                dateDisplayController.updateDateText(calendarGrid.getOffset());
            }
        });

        weekViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                calendarGrid.changeView(CalendarNode.VIEW.WEEK);
            }
        });
        monthViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                calendarGrid.changeView(CalendarNode.VIEW.MONTH);
            }
        });
        dayViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                calendarGrid.changeView(CalendarNode.VIEW.DAY);
            }
        });
    }
}
