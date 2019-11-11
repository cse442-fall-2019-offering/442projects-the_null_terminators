package edu.buffalo.cse442.nullterminators;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TagEditorPopup {
    final double width = 398.0;
    private Stage _stage;
    private Event _event;
    private Bounds _loc;
    private ArrayList<Tag> _tagsAvaliable;

    public TagEditorPopup(Event evt) {
        _tagsAvaliable = new ArrayList<>();
        _event = evt;
        setup();
    }

    public void show() {
        _stage.setIconified(false);
        _stage.show();
    }

    public void hide() {
        _stage.setIconified(true);
    }

    public void close() {
        _stage.close();
    }

    public void setBounds(Bounds bnd) {
        _loc = bnd;
        _stage.setX(_loc.getMaxX() - width);
        _stage.setY(_loc.getMaxY());
    }

    private void setup() {
        _stage = new Stage();
        _stage.setWidth(width);
        _stage.setResizable(false);
        _stage.initStyle(StageStyle.UNDECORATED);

        _stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                // TODO: something with linking it to the "Repeating.." button in EventWindow
            }
        });

        for(String[] tag : Database.getTags()) {
            _tagsAvaliable.add(new Tag(Integer.parseInt(tag[0]), tag[1], tag[2]));
        }

        VBox outerFrame = new VBox();
        outerFrame.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        outerFrame.setPadding(new Insets(20, 20, 20,20));
        outerFrame.setSpacing(8);
        outerFrame.setAlignment(Pos.CENTER);

        HBox tagContainer = new HBox();
        tagContainer.setAlignment(Pos.CENTER);
        tagContainer.setSpacing(5);
        ToggleGroup group = new ToggleGroup();
        for (Tag t : _tagsAvaliable) {
            RadioButton rb = new RadioButton(t.getName());
            rb.setToggleGroup(group);
            // TODO: add listeners to date checkboxes
            tagContainer.getChildren().add(rb);
        }

        Button confirm = new Button("Add Tags");
        confirm.setOnAction(e -> {
            for(Node node : tagContainer.getChildren()) {
                if(node instanceof RadioButton) {
                    if(((RadioButton) node).selectedProperty().get()) {
                        for(Tag tag : _tagsAvaliable) {
                            if(tag.getName().equals(((RadioButton) node).getText())) {
                                _event.setTag(tag);
                            }
                        }
                    }
                }
            }
            _stage.close();
        });

        outerFrame.getChildren().addAll(tagContainer, confirm);

        _stage.setScene(new Scene(outerFrame));
    }
}
