package edu.buffalo.cse442.nullterminators;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDateTime;

public class RecurrentEvents extends Stage {
    final double width = 398.0;
    private Event _event = new Event(-1, "", "", LocalDateTime.now());
    private Bounds _loc;

    public RecurrentEvents(Event evt) {
        _event = evt;
        setup();
    }

    public void setBounds(Bounds bnd) {
        _loc = bnd;
        this.setX(_loc.getMaxX() - width);
        this.setY(_loc.getMaxY());
    }

    private void setup() {
        this.setWidth(width);
        this.setResizable(false);
        this.initStyle(StageStyle.UNDECORATED);
        this.setOpacity(.95);

        VBox outerFrame = new VBox();
        outerFrame.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        outerFrame.setPadding(new Insets(20, 20, 20,20));
        outerFrame.setSpacing(8);
        outerFrame.setAlignment(Pos.CENTER);

        HBox freqFrame = new HBox();
        freqFrame.setAlignment(Pos.CENTER);
        freqFrame.setSpacing(5);
        DatePicker from = new DatePicker();
        from.setMaxWidth(105);
        from.setValue(_event.getDate());
        DatePicker until = new DatePicker();
        until.setMaxWidth(105);
        until.setValue(_event.getDate());
        freqFrame.getChildren().addAll(new Label("From "), from, new Label(" until "), until);

        HBox dateFrame = new HBox();
        dateFrame.setAlignment(Pos.CENTER);
        dateFrame.setSpacing(5);
        final String[] dotw = new String[]{"SUN", "MON", "TUES", "WED", "THURS", "FRI", "SAT"};
        for (String s : dotw) {
            CheckBox cb = new CheckBox(s);
            cb.setIndeterminate(false);
            // TODO: add listeners to date checkboxes
            dateFrame.getChildren().add(cb);
        }

        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            // TODO: add listener to confirm button, sends values to the opened event window
            this.close();
        });

        outerFrame.getChildren().addAll(freqFrame, dateFrame, confirm);

        this.setScene(new Scene(outerFrame));
    }
}
