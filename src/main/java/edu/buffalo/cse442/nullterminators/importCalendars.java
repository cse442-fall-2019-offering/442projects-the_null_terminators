package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * class used to import calendars from external sources
 *
 * NOT YET IMPLEMENTED
 */
public class importCalendars {

    public importCalendars() {

    }

    /**
     * sets up GUI for calendar imports
     */
    public void window() {
        Stage stage = new Stage();
//        stage.setTitle("Import calendars..");
        stage.setTitle("--- TEMPORARY ---");
        stage.setMinHeight(300);
        stage.setMinWidth(300);
        VBox frame = new VBox();
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);

        Label howto = new Label("Select which calendars you'd like to see!");

        Pane spacer1 = new Pane();
        spacer1.setMinHeight(10.0);

        Pane spacer2 = new Pane();
        spacer2.setMinHeight(20.0);

        Button confirm = new Button("Confirm!");
        confirm.setOnAction(e -> {
            // TODO: fetch calendars and apply them to the current one.
            stage.close();
        });

        frame.getChildren().addAll(howto, spacer1);
        for (int i = 0; i < 7; ++i) {
            CheckBox cal = new CheckBox("OPTION" + i);
            frame.getChildren().add(cal);
        }

        frame.getChildren().addAll(spacer2, confirm);

        Scene scene = new Scene(frame);
        stage.setScene(scene);
        stage.show();
    }
}
