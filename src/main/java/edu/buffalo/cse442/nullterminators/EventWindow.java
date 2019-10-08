package edu.buffalo.cse442.nullterminators;

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

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventWindow {

    private String title;
    private String details;
    private LocalDateTime when;

    public EventWindow() {
        new EventWindow(LocalDateTime.now(), "", "");
    }
    public EventWindow(LocalDateTime time, String tit, String det) {
        title = tit;
        details = det;
        when = time;

        Stage stage = new Stage();

        //set initial window layout
        VBox frame = new VBox();
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(7.5);

        TextArea eventTitle = new TextArea(title);                  // event title
        eventTitle.setMinSize(300, 30);
        eventTitle.setMaxSize(300, 30);
        eventTitle.setWrapText(true);

        TextArea eventDes = new TextArea(details);                  // event description
        eventDes.setMinSize(400, 200);
        eventDes.setMaxSize(400, 200);
        eventDes.setWrapText(true);


        HBox pickDateTime = new HBox();                             // picking date
        DatePicker date = new DatePicker(when.toLocalDate());
        date.setMaxWidth(100);
        // TODO: Set date prompt to date picked on node? Somehow?
        date.setValue(LocalDateTime.now().toLocalDate());

        ComboBox<String> hours = new ComboBox();                    // pick what hour event is
        hours.setEditable(true);
        hours.setMaxWidth(50);
        hours.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        if (when.getMinute() > 12) {
            hours.setValue(Integer.toString(when.getHour() - 12));
        }
        else {
            hours.setValue(Integer.toString(when.getHour()));
        }
        hours.setMinWidth(40);

        Label colon = new Label(" : ");

        ComboBox<String> minutes = new ComboBox();                  // picking minute of event
        minutes.setEditable(true);
        minutes.setMaxWidth(55);
        minutes.getItems().addAll("00", "15", "30", "45");
        if (when.getMinute() < 10) {
            minutes.setValue("0" + when.getMinute());
        }
        else {
            minutes.setValue(Integer.toString(when.getMinute()));
        }
        minutes.setMinWidth(40);

        Pane spacer1 = new Pane();
        spacer1.setMinWidth(10);
        Pane spacer2 = new Pane();
        spacer2.setMinWidth(10);

        AtomicBoolean is_am = new AtomicBoolean(when.getHour() < 12);    // fancy am/pm button stuff

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

        Button addEvent = new Button("Add Event");

        //sets 'enter' keystroke to click button
        addEvent.isDefaultButton();

        //set button to just close the Event Editor for now
        addEvent.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                setValues(eventTitle, eventDes, date, Integer.valueOf(hours.getValue().toString()), Integer.valueOf(minutes.getValue().toString()));
                stage.close();
            }
        });

        addEvent.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    setValues(eventTitle, eventDes, date, Integer.valueOf(hours.getValue().toString()), Integer.valueOf(minutes.getValue().toString()));
                    stage.close();
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

    private void setValues(TextArea tits, TextArea dets, DatePicker day, int hours, int minutes) {
        title = tits.getText();
        details = dets.getText();
        when = LocalDateTime.of(day.getValue().getYear(), day.getValue().getMonthValue(), day.getValue().getDayOfMonth(), hours, minutes);
        consoleTest();
    }

    private void consoleTest() {
        System.out.println("EVENT DATE: " + getWhen());
        System.out.println("EVENT TITLE: " + getTitle());
        System.out.println("EVENT DETAILS: " + getDetails());
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getWhen() {
        return when;
    }
}
