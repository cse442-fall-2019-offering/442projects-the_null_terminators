package edu.buffalo.cse442.nullterminators;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class DateNode extends AnchorPane {

    private LocalDate _date;
    private ArrayList<Event> _events;

    private Label _dateView;
    private Button _addEvent;

    private VBox _storage;

    DateNode(Node... children) {
        super(children);
        _dateView = new Label();
        _dateView.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        _addEvent = new Button("+");
        _addEvent.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        _addEvent.setTextFill(Color.DEEPSKYBLUE);
        _addEvent.setStyle("-fx-background-color: transparent; -fx-padding: 0 3 0 3;");
        _addEvent.setOnAction(e -> {
                new EventWindow(_date.atTime(4,20), "", "");
        });

        _addEvent.setOpacity(0.0);
        this.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {       // button shows when hovering over this DateNode's box
            if (show) {
                _addEvent.setOpacity(1.0);
            } else {
                _addEvent.setOpacity(0.0);
            }
        });

        _storage = new VBox();                           // vbox that holds date, add button, and events
        this.setTopAnchor(_storage, 0.0);
        this.setBottomAnchor(_storage, 0.0);
        this.setRightAnchor(_storage, 0.0);
        this.setLeftAnchor(_storage, 0.0);

        HBox date = new HBox();
        Pane spacer1 = new Pane();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        date.getChildren().addAll(_dateView, spacer1, _addEvent);

        _storage.getChildren().add(date);
        this.getChildren().add(_storage);
    }

    /**
     * function that gets and returns date of current DateNode
     * @return
     */
    public LocalDate getDate() {
        return _date;
    }

    /**
     * sets function that draws the day number on the current grid - sets colors for current days, and days passed
     * @param setDate date to be drawn onto the grid
     */
    void setDate(LocalDate setDate) {
        _date = setDate;
        if (setDate.isBefore(LocalDate.now())) {    // set color for previous days
            BackgroundFill color = new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY);
            this.setBackground(new Background(color));
        }
        else if (setDate.compareTo(LocalDate.now()) == 0) {     // set color for current day
            BackgroundFill color = new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            this.setBackground(new Background(color));
        }
        else {
            // TODO: THEMEING - set color for future days
//            BackgroundFill color = new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY);
//            this.setBackground(new Background(color));
        }
        _dateView.setText(" " + _date.getDayOfMonth());
    }

    /**
     * function for formatting a time value to a string
     * @param time time to be formatted
     * @return a string in normal time format
     */
    private String formatTime(LocalTime time) {
        int unf_hr = time.getHour();
        int unf_min = time.getMinute();
        String hours = Integer.toString(unf_hr);
        String minutes = Integer.toString(unf_min);

        if (unf_hr > 12) {
            hours = "" + (unf_hr - 12);
        }
        if (unf_min < 10) {
            minutes = "0" + unf_min;
        }
        return hours + ":" + minutes;
    }

    /**
     * function that draws all events from ArrayList _events to this node
     */
    private void drawEvents() {
        clearEvents();
        fetchEvents();
        for (Event e : _events) {
            // TODO: add multiple events IN ORDER
            Button adding = new Button(formatTime(e.getTime()) + " - " + e.getTitle());
            HBox.setHgrow(adding, Priority.ALWAYS);
            adding.setPadding(new Insets(0, 0, 0, 0));
            adding.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            adding.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            adding.setOnAction(evt -> {
                new EventWindow(LocalDateTime.of(e.getDate(), e.getTime()), e.getTitle(), e.getDetails());
            });

            adding.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
                if (show) {
                    adding.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    adding.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });
            _storage.getChildren().add(adding);
        }
    }

    /**
     * function that fetches events from the database for current day
     */
    public void fetchEvents() {
        // TODO: get events from database?

    }

    /**
     * clears the events from the Node GUI and clears list of events
     */
    private void clearEvents() {
        _storage.getChildren().remove(1, _storage.getChildren().size()-1);
        _events.clear();
    }
}
