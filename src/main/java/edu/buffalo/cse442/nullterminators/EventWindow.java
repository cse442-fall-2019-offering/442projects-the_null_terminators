package edu.buffalo.cse442.nullterminators;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventWindow {

    private String _title;
    private String _details;
    private LocalDateTime _when = LocalDateTime.now();

    /**
     * constructor for initializing event window
     * @param time time at which the event is held, if available
     * @param title title of the event, if available
     * @param det details for the event
     */
    public EventWindow(LocalDateTime time, String title, String det) {
        _when = time;
        _title = title;
        _details = det;

        setup();
    }

    /**
     * constructor for adding new events, opens up fresh event editor window
     */
    public EventWindow() {
        new EventWindow(_when, _title, _details);
        setup();
    }

    /**
     * function that sets up and opens an event editor window
     */
    public void setup() {
        Stage stage = new Stage();

        VBox frame = new VBox();                                    // set up layout
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(8);

        Button addEvent = new Button("Add Event");

        TextArea eventTitle = new TextArea(_title);                  // event title
        eventTitle.setMinSize(300, 30);
        eventTitle.setMaxSize(300, 30);
        eventTitle.setWrapText(true);

        TextArea eventDes = new TextArea(_details);                  // event description
        eventDes.setMinSize(400, 200);
        eventDes.setMaxSize(400, 200);
        eventDes.setWrapText(true);


        HBox pickDateTime = new HBox();                              // picking date
        DatePicker date = new DatePicker(_when.toLocalDate());
        date.setMaxWidth(105);
        date.setValue(_when.toLocalDate());

        ComboBox<String> hours = new ComboBox();                     // pick what hour event is
        hours.setMinWidth(55);
        hours.setMaxWidth(55);
        hours.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        if (_when.getHour() > 12) {
            hours.setValue(Integer.toString(_when.getHour() - 12));
        }
        else if (_when.getHour() == 0) {
            hours.setValue("12");
        }
        else {
            hours.setValue(Integer.toString(_when.getHour()));
        }

        hours.setStyle("-fx-font: 11 arial");
        hours.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px");
        hours.setEditable(true);
        input_validation(hours, true, addEvent);

        Label colon = new Label(" : ");

        ComboBox<String> minutes = new ComboBox();                  // picking minute of event
        minutes.setMinWidth(55);
        minutes.setMaxWidth(55);
        minutes.getItems().addAll("00", "15", "30", "45");
        if (_when.getMinute() < 10) {
            minutes.setValue("0" + _when.getMinute());
        }
        else {
            minutes.setValue(Integer.toString(_when.getMinute()));
        }

        minutes.setStyle("-fx-font: 11 arial");
        minutes.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px");
        minutes.setEditable(true);
        input_validation(minutes, false, addEvent);

        Pane spacer1 = new Pane();
        spacer1.setMinWidth(10);
        Pane spacer2 = new Pane();
        spacer2.setMinWidth(10);

        AtomicBoolean is_am = new AtomicBoolean(_when.getHour() < 12);    // fancy am/pm button stuff

        Button am_button = new Button("AM");
        am_button.setMinSize(25,15);
        am_button.setMaxSize(25,15);
        am_button.setPadding(new Insets(0,0,0,0));
        am_button.setStyle("-fx-border-color: black; -fx-border-width: .5px 0px .5px .5px");


        Button pm_button = new Button("PM");
        pm_button.setMinSize(25,15);
        pm_button.setMaxSize(25,15);
        pm_button.setPadding(new Insets(0,0,0,0));
        pm_button.setStyle("-fx-border-color: black; -fx-border-width: .5px .5px .5px 0px");

        Background vis = new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        Background inv = new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY));
        if (is_am.get()) {
            am_button.setBackground(vis);
            am_button.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            pm_button.setBackground(inv);
            pm_button.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        }
        else {
            pm_button.setBackground(vis);
            pm_button.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            am_button.setBackground(inv);
            am_button.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        }

        am_button.setOnAction(e -> {
            is_am.set(true);
            am_button.setBackground(vis);
            am_button.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            pm_button.setBackground(inv);
            pm_button.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        });
        pm_button.setOnAction(e-> {
            is_am.set(false);
            pm_button.setBackground(vis);
            pm_button.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            am_button.setBackground(inv);
            am_button.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        });
        pickDateTime.getChildren().addAll(date, spacer1, hours, colon, minutes, spacer2, am_button, pm_button);
        pickDateTime.setAlignment(Pos.CENTER);

        //sets 'enter' keystroke to click button
        addEvent.isDefaultButton();

        //set button to just close the Event Editor for now
        addEvent.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                setValues(eventTitle, eventDes, date, hours.getValue(), minutes.getValue(), stage);
            }
        });

        addEvent.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER && !addEvent.isDisabled()) {
                    setValues(eventTitle, eventDes, date, hours.getValue(), minutes.getValue(), stage);
                }
            }
        });

        Label titleLabel = new Label("Event Title");
        Label descLabel = new Label("Description");

        titleLabel.setStyle("-fx-font: 18 arial");
        descLabel.setStyle("-fx-font: 18 arial");

        frame.getChildren().addAll(titleLabel, eventTitle, pickDateTime, descLabel, eventDes, addEvent);

        stage.setTitle("Event Editor");
        stage.setScene(new Scene(frame));

        stage.show();
    }

    /**
     * checks input for hours and minute combo box
     * @param box combo box (hours/minutes) that's to be visually changed
     * @param isHours is hours or minutes
     * @param addEventButton link to add_event button that's to be greyed out if the input is invalid
     */
    private void input_validation(ComboBox box, boolean isHours, Button addEventButton) {
        TextField editor = box.getEditor();
        editor.setOnKeyTyped(e -> {
            char val = e.getCharacter().charAt(0);
            if (editor.getText().length() > 2 || (int) val < 48 || (int) val > 57) {                   // is a number, not > 2 chars
                editor.setText(editor.getText(0, Integer.max(editor.getText().length()-1, 0)));
                editor.requestFocus();
                editor.end();
                if (!addEventButton.isDisabled()) {
                    Timeline wait = new Timeline(
                            new KeyFrame(Duration.ZERO, evt -> box.setStyle("-fx-border-color: red; -fx-border-width: 1px 1px 1px 1px")),
                            new KeyFrame(Duration.seconds(.5), evt -> box.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px"))
                    );
                    wait.play();
                }
            }
            int p = 999;
            if (editor.getText().length() > 0) {
                p = Integer.parseInt(editor.getText());
            }
            if (isHours) {
                if (p < 1 || p > 12) {
                    addEventButton.setDisable(true);
                    box.setStyle("-fx-border-color: red; -fx-border-width: 1px 1px 1px 1px");
                }
                else {
                    addEventButton.setDisable(false);
                    box.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px");
                }
            }
            else {
                if (p < 0 || p > 59) {
                    addEventButton.setDisable(true);
                    box.setStyle("-fx-border-color: red; -fx-border-width: 1px 1px 1px 1px");
                }
                else {
                    addEventButton.setDisable(false);
                    box.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px");
                }
            }
        });
    }

    /**
     *
     * @param t title of event
     * @param d details of event
     * @param day day of which the event is on
     * @param hours hour of the event
     * @param minutes minute of the event
     * @param stage stage for the event window
     */
    private void setValues(TextArea t, TextArea d, DatePicker day, String hours, String minutes, Stage stage) {
        _title = t.getText();
        _details = d.getText();
        int h = 4, m = 20;      // default time, can be anything

        h = Integer.parseInt(hours);                // ensure is integer
        m = Integer.parseInt(minutes);
        try {
            _when = LocalDateTime.of(day.getValue().getYear(), day.getValue().getMonthValue(), day.getValue().getDayOfMonth(), h, m);        // ensure is valid time
            stage.close();
        } catch (DateTimeException dte) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Format!!");
            alert.setHeaderText(null);
            alert.setContentText("Hours must be between 1 and 12, and minutes must be between 0 and 60!");
            alert.setResizable(true);
            alert.onShownProperty().addListener(e -> {
                Platform.runLater(() -> alert.setResizable(false));
            });
            alert.showAndWait();
        }

        consoleTest();
    }


    /**
     * for debugging
     */
    private void consoleTest() {
        System.out.println("EVENT DATE: " + _when);
        System.out.println("EVENT TITLE: " + _title);
        System.out.println("EVENT DETAILS: " + _details);
    }
}
