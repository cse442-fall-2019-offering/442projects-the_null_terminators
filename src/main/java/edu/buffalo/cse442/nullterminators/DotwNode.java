package edu.buffalo.cse442.nullterminators;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DotwNode extends AnchorPane {
    String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String[] days_shrt = new String[]{"SUN", "MON", "TUES", "WED", "THURS", "FRI", "SAT"};
    private GridPane dotw;
    // TODO: need to finish so the DOTW won't be there when switching to day view.
    public DotwNode() {
        this.setMinHeight(25);
        this.setMaxHeight(25);
        this.setMinWidth(700);

        dotw = new GridPane();
        dotw.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        this.setTopAnchor(dotw, 0.0);
        this.setBottomAnchor(dotw, 0.0);
        this.setRightAnchor(dotw, 0.0);
        this.setLeftAnchor(dotw, 0.0);

        for (int j = 0; j < 7; ++j) {
            ColumnConstraints c_add = new ColumnConstraints();
            c_add.setPercentWidth(Double.valueOf(1.0/7.0) * 100);
            c_add.setHalignment(HPos.CENTER);
            dotw.getColumnConstraints().add(c_add);
        }

        this.getChildren().add(dotw);
    }

    private Label format_name(String name) {
        Label ret = new Label(name);
        ret.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        ret.setAlignment(Pos.CENTER);

        return ret;
    }

    public void show() {
        dotw.getChildren().clear();
        for (int i = 0; i < 7; ++i) {
            dotw.addColumn(i, format_name(days[i]));
        }
    }

    public void clear() {
        dotw.getChildren().clear();
    }
}

