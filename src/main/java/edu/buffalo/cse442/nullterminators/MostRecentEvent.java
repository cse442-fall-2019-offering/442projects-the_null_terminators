package edu.buffalo.cse442.nullterminators;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class that creates the GUI for showing the Most Recent Event in the toolbar section
 */
public class MostRecentEvent extends Pane {

        private LocalDateTime _when;
        private Label _dayLabel;

    public MostRecentEvent() {
        super();
        _when = LocalDateTime.now();
        HBox.setHgrow(this, Priority.SOMETIMES);
        _dayLabel = new Label();
//        _dayLabel.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        _dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        _dayLabel.setLayoutY(15);
        TranslateTransition moveLabel = new TranslateTransition();
        moveLabel.setDuration(Duration.seconds(9));
        moveLabel.setToX(700);
        moveLabel.setAutoReverse(true);
        moveLabel.setCycleCount(Animation.INDEFINITE);
        moveLabel.setNode(_dayLabel);
        moveLabel.play();
        this.getChildren().addAll(_dayLabel);

        Updater u = new Updater();
        Timer t = new Timer();
        t.schedule(u, 100, 100);
    }

    public void update() {
        String[] e = Database.getLatestEvent();
        for (String s : e) {
            if (s == null) {
                _dayLabel.setText("You have no upcoming events!");
                return;
            }
        }
//        int id = Integer.parseInt(e[0]);
        String name = e[1];
//        String details = e[3];
        /*
        int hour = Integer.parseInt(e[2].substring(11,13));
        int minute = Integer.parseInt(e[2].substring(14,16));

        LocalDateTime when = LocalDateTime.of(year, month, day, hour, minute);

        */
        _dayLabel.setText(e[2].substring(5,7) + "/" + e[2].substring(8,10) + " - " + name);
    }

    private class Updater extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //update();
                }
            });
        }
    }
}
