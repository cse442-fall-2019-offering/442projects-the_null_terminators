package edu.buffalo.cse442.nullterminators;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalDateTime;

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
        _dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        _dayLabel.setLayoutY(15);
        TranslateTransition moveLabel = new TranslateTransition();
        moveLabel.setDuration(Duration.seconds(9));
        moveLabel.setToX(700);
        moveLabel.setAutoReverse(true);
        moveLabel.setCycleCount(Animation.INDEFINITE);
        moveLabel.setNode(_dayLabel);
        moveLabel.play();

        update();
        this.getChildren().addAll(_dayLabel);
    }

    public void update() {
        _dayLabel.setText(_when.getMonth().getValue() + "/" + _when.getDayOfMonth()
                            + " - Some meeting..");

    }
}
