package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Theme {

    private boolean _darkMode;
    private Color _highlight;

    public Theme() {
        _darkMode = false;
        _highlight = Color.CORNFLOWERBLUE;
    }

    public void openThemeEditor() {
        Stage stage = new Stage();
        stage.setMinWidth(200);
        stage.setMinHeight(200);
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(50);

        // color picker
        ColorPicker colors = new ColorPicker(Color.CORNFLOWERBLUE);


        // dark vs light mode
        HBox darkNLight = new HBox();
        darkNLight.setAlignment(Pos.CENTER);
        Button dark = new Button("DARK");

        Button light = new Button("LIGHT");

        darkNLight.getChildren().addAll(light, dark);
        frame.getChildren().addAll(colors, darkNLight);

        stage.setTitle("Theme Editor");
        stage.setScene(new Scene(frame));
        stage.show();
    }

    public Color getHighlight() {
        return _highlight;
    }
}
