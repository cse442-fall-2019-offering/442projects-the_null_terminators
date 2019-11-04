package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SideBar extends Stage {

    private Bounds _loc;

    public SideBar() {
        setup();
    }

    public void setBounds(Bounds bnd) {
        _loc = bnd;
        this.setX(_loc.getMaxX() - 200);
        this.setY(_loc.getMaxY());

    }

    private void setup() {
        this.setResizable(false);
        this.initStyle(StageStyle.UNDECORATED);
        Pane foo = new Pane();
        foo.setMaxSize(200, 500);
        foo.setMinSize(200, 500);

        this.setScene(new Scene(foo));
    }
}
