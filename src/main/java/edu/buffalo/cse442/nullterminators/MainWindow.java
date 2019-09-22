package edu.buffalo.cse442.nullterminators;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class MainWindow {

    @FXML private CalendarNode calendarGrid;
    @FXML private DateDisplay dateDisplayController;
    @FXML private Button weekViewButton;
    @FXML private Button monthViewButton;
    @FXML private Button dayViewButton;

    public MainWindow() {
    }

    @FXML
    void initialize() {
        dateDisplayController.updateDateText(LocalDate.now());

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
