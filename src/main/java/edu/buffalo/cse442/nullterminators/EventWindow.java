package edu.buffalo.cse442.nullterminators;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EventWindow {


    public void createEEWindow() {

        Stage stage = new Stage();

        //set initial window layout
        GridPane eeGrid = new GridPane();

        //sets default vertical gap between rows
        eeGrid.setVgap(20);

        TextArea eventTitle = new TextArea();
        eventTitle.setMinSize(300, 30);
        eventTitle.setMaxSize(300, 30);
        eventTitle.setWrapText(true);

        TextArea eventDes = new TextArea();
        eventDes.setMinSize(400, 200);
        eventDes.setMaxSize(400, 200);
        eventDes.setWrapText(true);


        Button addEvent = new Button("Add Event");

        //sets 'enter' keystroke to click button
        addEvent.isDefaultButton();

        //set button to just close the Event Editor for now
        addEvent.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {stage.close();}
        });

        addEvent.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {stage.close();}
            }
        });

        Label titleLabel = new Label("Event Title");
        Label descLabel = new Label("Description");

        titleLabel.setStyle("-fx-font: 18 arial");
        descLabel.setStyle("-fx-font: 18 arial");


        //lay out all the items on the grid
        eeGrid.add(titleLabel, 0, 1);
        eeGrid.add(eventTitle, 0 , 2);

        eeGrid.add(descLabel, 0, 5);
        eeGrid.add(eventDes, 0, 6);

        eeGrid.add(addEvent, 0, 8);


        //center all the items
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        GridPane.setHalignment(descLabel, HPos.CENTER);
        GridPane.setHalignment(addEvent, HPos.CENTER);
        GridPane.setHalignment(eventTitle, HPos.CENTER);
        GridPane.setHalignment(eventDes, HPos.CENTER);

        //add empty first and last row for better looking spacing
        eeGrid.addRow(9, new Label(""));
        eeGrid.addRow(1, new Label(""));

        stage.setTitle("Event Editor");
        stage.setScene(new Scene(eeGrid));

        stage.show();
    }

}
