package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

/**
 * class used for importing the UB calendar
 *
 * NOT YET IMPLEMENTED
 */
public class importUBCal {

    private String parseable;

    public importUBCal() {

    }

    /**
     * sets up UB calendar import gui
     */
    public void window() {
        Stage stage = new Stage();
        stage.setTitle("Import schedule from MyUB...");
        VBox frame = new VBox();
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);

        Label howto = new Label("Click on the button below and sign into UB's HUB.");

        Pane spacer1 = new Pane();
        spacer1.setMinHeight(10.0);

        Button openWeb = new Button("Open MyUB..");
        openWeb.setOnAction(e -> {
           Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop(): null;
           if (desktop != null && desktop.isSupported((Desktop.Action.BROWSE))) {
               try {
                   URL boooofalo = new URL("https://www.myub.buffalo.edu");
                   desktop.browse(boooofalo.toURI());
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
           }
        });

        Pane spacer2 = new Pane();
        spacer2.setMinHeight(20.0);

        TextArea input = new TextArea();
        input.setPromptText("Input your formatted UB calendar here!");
        input.setMinSize(400, 200);

        Pane spacer3 = new Pane();
        spacer3.setMinHeight(20.0);

        Button confirm = new Button("Confirm!");
        //TODO: confirm action may change dependent on whether or not we get API access/ find a more efficient method in accessing the UB calendar
        confirm.setOnAction(e -> {
            parseable = input.getText();
            System.out.println(parseable);
            stage.close();
        });

        frame.getChildren().addAll(howto, spacer1, openWeb, spacer2, input, spacer3, confirm);

        Scene scene = new Scene(frame);
        stage.setScene(scene);
        stage.show();
    }

    public String getString() {
        return parseable;
    }
}