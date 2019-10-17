package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * helper class that creates the AM/PM buttons in the EventEditor window (used in EventWindow.java)
 */
public class AMandPM extends HBox {

    private Button _am;
    private Button _pm;

    private AtomicBoolean _isAM;

    public AMandPM(boolean isAM) {
        _isAM = new AtomicBoolean(isAM);
        this.setAlignment(Pos.CENTER);
        this.setMinSize(50, 30);
        this.setMaxSize(50,30);

        setUpGraphic();
        setUpHandlers();

        this.getChildren().addAll(_am, _pm);
    }

    /**
     * function that returns whether time is currently set to AM or PM
     * @return boolean, true if AM, false if PM
     */
    public boolean isAM() {
        return _isAM.get();
    }


    /**
     * function that sets up the visual representation of the AM/PM buttons
     */
    private void setUpGraphic() {
        _am = new Button("AM");
        _am.setMaxSize(25,15);
        _am.setMinSize(25,15);
        _am.setStyle("-fx-border-color: black; -fx-border-width: .5px 0px .5px .5px; -fx-padding: 0 0 0 0;");

        _pm = new Button("PM");
        _pm.setMaxSize(25,15);
        _pm.setMinSize(25,15);
        _pm.setStyle("-fx-border-color: black; -fx-border-width: .5px .5px .5px 0px; -fx-padding: 0 0 0 0;");

        Background vis = new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        Background inv = new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY));

        if (_isAM.get()) {
            _am.setBackground(vis);
            _am.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            _pm.setBackground(inv);
            _pm.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        }
        else {
            _pm.setBackground(vis);
            _pm.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            _am.setBackground(inv);
            _am.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        }
    }

    /**
     * helper function that sets up that action listeners for the AM/PM buttons
     */
    private void setUpHandlers() {
        Background vis = new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        Background inv = new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY));

        _am.setOnAction(e -> {
            _isAM.set(true);
            _am.setBackground(vis);
            _am.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            _pm.setBackground(inv);
            _pm.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        });
        _pm.setOnAction(e-> {
            _isAM.set(false);
            _pm.setBackground(vis);
            _pm.setFont(Font.font("Arial", FontWeight.BOLD,10.5));
            _am.setBackground(inv);
            _am.setFont(Font.font("Arial", FontWeight.LIGHT,10));
        });
    }
}
