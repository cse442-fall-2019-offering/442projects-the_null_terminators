package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class SideBar extends Stage {

    private Bounds _loc;
    private ArrayList<Tag> tags;

    public SideBar() {
        setup();
    }

    public void setBounds(Bounds bnd) {
        _loc = bnd;
        this.setX(_loc.getMaxX() - 200);
        this.setY(_loc.getMaxY());

    }

    private void setup() {
        tags = new ArrayList<>();
        this.setResizable(false);
        this.initStyle(StageStyle.UNDECORATED);
        Pane foo = new Pane();
        foo.setMaxSize(200, 500);
        foo.setMinSize(200, 500);

        this.setScene(new Scene(foo));

        for(String[] tag : Database.getTags()) {
            tags.add(new Tag(Integer.parseInt(tag[0]), tag[1], tag[2]));
        }

        VBox tagContainer = new VBox();
        tagContainer.setAlignment(Pos.CENTER);
        tagContainer.setSpacing(5);
        CheckBox group = new CheckBox();
        for (Tag t : tags) {
            CheckBox cb = new CheckBox(t.getName());
            cb.selectedProperty().setValue(true);
            cb.setBackground(new Background(new BackgroundFill(t.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
            cb.allowIndeterminateProperty().setValue(false);
            // TODO: add listeners to date checkboxes
            tagContainer.getChildren().add(cb);
        }

        foo.getChildren().addAll(tagContainer);
    }
}
