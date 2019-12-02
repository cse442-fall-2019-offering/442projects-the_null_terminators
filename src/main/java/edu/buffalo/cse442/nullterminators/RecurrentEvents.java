package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecurrentEvents extends Stage {
    final double width = 398.0;
    private Bounds _loc;

    private Event _evt;
    private ArrayList<CheckBox> _cbArray = new ArrayList<>();
    private DatePicker _from;
    private DatePicker _until;

    public RecurrentEvents(Event evt) {
        _evt = evt;
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
        _from = new DatePicker();
        _from.setMaxWidth(105);
        _from.setValue(_evt.getDate());
        _until = new DatePicker();
        _until.setMaxWidth(105);
        _until.setValue(_evt.getDate());
        freqFrame.getChildren().addAll(new Label("From "), _from, new Label(" until "), _until);

        HBox dateFrame = new HBox();
        dateFrame.setAlignment(Pos.CENTER);
        dateFrame.setSpacing(5);
        final String[] dotw = new String[]{"SUN", "MON", "TUES", "WED", "THURS", "FRI", "SAT"};
        for (String s : dotw) {
            CheckBox cb = new CheckBox(s);
            cb.setIndeterminate(false);
            _cbArray.add(cb);
            dateFrame.getChildren().add(cb);
        }

        Button reset = new Button("RESET");
        reset.setTextFill(Color.RED);
        reset.setStyle("-fx-background-color: transparent;");
        reset.setOnAction(e -> {
            for (CheckBox cb : _cbArray) {
                cb.setSelected(false);
            }
            _from.setValue(_evt.getDate());
            _until.setValue(_evt.getDate());
        });


        HBox resetFrame = new HBox();
        resetFrame.setAlignment(Pos.CENTER_RIGHT);
        resetFrame.getChildren().add(reset);

        outerFrame.getChildren().addAll(freqFrame, dateFrame, resetFrame);

        this.setScene(new Scene(outerFrame));
    }

    public Recur getRecur() {
        Recur ret = new Recur();

        ret.setFrom(_from.getValue());
        ret.setUntil(_until.getValue());
        String dotw = "";
        for (CheckBox cb : _cbArray) {
            if (cb.isSelected()) {
                dotw = dotw + "1";
            }
            else {
                dotw = dotw + "0";
            }
        }
        ret.setDOTW(dotw);
        return ret;
    }
}
