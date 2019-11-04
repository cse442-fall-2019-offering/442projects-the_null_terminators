package edu.buffalo.cse442.nullterminators;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EventTags extends Stage {

    private ObservableMap<String, Color> _tags = FXCollections.observableHashMap();

    private ListView<String> _lv = new ListView();
    private ColorPicker _cp = new ColorPicker();
    private TextArea _name = new TextArea();

    public EventTags() {

        setup();
    }

    private void setup() {
        // general stage setup
        this.setTitle("Tag Editor");
        this.setResizable(false);
        this.setWidth(400);
        this.setHeight(300);

        HBox frame = new HBox();
        frame.setSpacing(20);
        frame.setPadding(new Insets(10, 10, 10, 10));

        setupList();
        frame.getChildren().addAll(_lv, editTags());

        // show stage

        this.setScene(new Scene(frame));
        this.show();
    }

    private void setupList() {
        _lv = new ListView<>();
        VBox.setVgrow(_lv, Priority.ALWAYS);
        _lv.setMinWidth(150);

        refresh();
        _lv.getSelectionModel().selectedItemProperty().addListener((obs, old, curr) -> {
            _name.setText(curr);
            _cp.setValue(_tags.get(curr));
        });
    }

    private VBox editTags() {
        VBox vb = new VBox();
        vb.setSpacing(15);

        _name.setPromptText("Tag name..");
        _name.setMinHeight(25);
        _name.setMaxHeight(25);

        HBox confirmRow = new HBox();
        Button delete = new Button("Delete");
        delete.setStyle("-fx-background-color: transparent;");
        delete.setTextFill(Color.RED);
        Button confirm = new Button("Add Tag");
        Pane confirmSpacer = new Pane();
        HBox.setHgrow(confirmSpacer, Priority.ALWAYS);
        confirmRow.getChildren().addAll(delete, confirmSpacer, confirm);

        delete.setOnAction(e -> {
            // TODO: delete from database
            _tags.remove(_name.getText());      // possibly won't to call this, dependent on implementation of refresh()
            refresh();
        });

        confirm.setOnAction(e -> {
            String name = _name.getText();
            Color color = _cp.getValue();
            System.out.println("SETTING NEW TAG..");
            System.out.println("     TAG NAME: " + name);
            System.out.println("     COLOR:    " + color);

            // TODO: temporary, just for visuals. should later add to database instead
            if (!_tags.containsKey(name)) {            // does not exist
                _tags.put(name, color);
            }
            else {
                _tags.remove(name);
                _tags.put(name, color);
            }
            refresh();
//            this.close();
        });

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label title = new Label("---- Editor ----");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        vb.getChildren().addAll(title, _name, _cp, spacer, confirmRow);
        return vb;
    }

    /**
     * function that retrieves tags from the database and refreshes the ListView, _lv
     */
    private void refresh() {
        ObservableList vals = FXCollections.observableArrayList();
        // TODO: get tags from database, add it to vals
        vals.addAll(_tags.keySet());
        _lv.setItems(vals);
    }

}
