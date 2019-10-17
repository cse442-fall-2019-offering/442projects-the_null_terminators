package edu.buffalo.cse442.nullterminators;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * class for the GUI that shows/hides the days of the week (above the grid, below the [month] [year] toolbar)
 */
public class DotwNode extends AnchorPane {
    private String[] _days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private String[] _daysShort = new String[]{"SUN", "MON", "TUES", "WED", "THURS", "FRI", "SAT"};      // if grid gets smaller and full length names don't fit, not used yet..
    private GridPane _dotw;

    public DotwNode() {
        this.setMinHeight(25);
        this.setMaxHeight(25);
        this.setMinWidth(700);

        _dotw = new GridPane();
        _dotw.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setTopAnchor(_dotw, 0.0);
        this.setBottomAnchor(_dotw, 0.0);
        this.setRightAnchor(_dotw, 0.0);
        this.setLeftAnchor(_dotw, 0.0);

        for (int j = 0; j < 7; ++j) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(Double.valueOf(1.0/7.0) * 100);
            col.setHalignment(HPos.CENTER);
            _dotw.getColumnConstraints().add(col);
        }

        this.getChildren().add(_dotw);
    }

    /**
     * function that shows THIS anchor pane - shows SMTWTF at the top
     */
    public void show() {
        _dotw.getChildren().clear();
        for (int i = 0; i < 7; ++i) {
            _dotw.addColumn(i, format_name(_days[i]));
        }
    }

    /**
     * function that hides the days of the week - used in the day view
     */
    public void hide() {
        _dotw.getChildren().clear();
    }

    /**
     * helper function that formats each day of the week's font
     * @param name day of the week - monday, tuesday, wednesday...
     * @return label with formatted string in it
     */
    private Label format_name(String name) {
        Label ret = new Label(name);
        ret.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        ret.setAlignment(Pos.CENTER);

        return ret;
    }
}

