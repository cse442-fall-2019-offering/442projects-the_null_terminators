package edu.buffalo.cse442.nullterminators;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EventWindow {

    private String title;
    private String details;
    private LocalDate when;

    public void createEEWindow() {

        Stage stage = new Stage();

        //set initial window layout
        VBox frame = new VBox();
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);

        //sets default vertical gap between rows
        frame.setSpacing(7.5);

        TextArea eventTitle = new TextArea();
        eventTitle.setMinSize(300, 30);
        eventTitle.setMaxSize(300, 30);
        eventTitle.setWrapText(true);

        TextArea eventDes = new TextArea();
        eventDes.setMinSize(400, 200);
        eventDes.setMaxSize(400, 200);
        eventDes.setWrapText(true);

        DatePicker date = new DatePicker();
        // TODO: Set date prompt to date picked on node? Somehow?
        // Temporarily set as CURRENT day
        date.setValue(LocalDate.now());

        Button addEvent = new Button("Add Event");

        //sets 'enter' keystroke to click button
        addEvent.isDefaultButton();

        //set button to just close the Event Editor for now
        addEvent.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                setValues(eventTitle, eventDes, date);
                stage.close();
            }
        });

        addEvent.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    setValues(eventTitle, eventDes, date);
                    stage.close();
                }
            }
        });

        Label titleLabel = new Label("Event Title");
        Label descLabel = new Label("Description");

        titleLabel.setStyle("-fx-font: 18 arial");
        descLabel.setStyle("-fx-font: 18 arial");

        frame.getChildren().addAll(titleLabel, eventTitle, date, descLabel, eventDes, addEvent);

        stage.setTitle("Event Editor");
        stage.setScene(new Scene(frame));

        stage.show();
    }

    private void setValues(TextArea tits, TextArea dets, DatePicker day) {
        title = tits.getText();
        details = dets.getText();
        when = day.getValue();
        consoleTest();
    }

    private void consoleTest() {
        System.out.println("EVENT DATE: " + when);
        System.out.println("EVENT TITLE: " + title);
        System.out.println("EVENT DETAILS: " + details);
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getWhen() {
        return when;
    }
}
