package edu.buffalo.cse442.nullterminators;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class EventWindow {

    private DateNode _parent;
    private Event _event = new Event(-1, "", "", LocalDateTime.now());
    private boolean _isNew;



    /**
     * constructor for initializing event window
     */
    public EventWindow(Event event, DateNode parent, boolean isNew) {
        _event = event;
        _parent = parent;
        _isNew = isNew;

        setup();
    }

    /**
     * function that sets up and opens an event editor window
     */
    public void setup() {
        Stage stage = new Stage();
        stage.setResizable(false);

        stage.setWidth(511);
        stage.setHeight(436);

        VBox frame = new VBox();                                                 // set up layout
        frame.setPadding(new Insets(10, 20, 20,20));
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(8);

        Button addEvent = new Button("Add Event");

        TextArea eventTitle = new TextArea(_event.getTitle());                  // event title
        eventTitle.setMinSize(300, 30);
        eventTitle.setMaxSize(300, 30);
        setMaxCharacters(eventTitle, 25);

        if (_isNew) {
            addEvent.setDisable(true);
            eventTitle.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String t1, String t2) {
                    if (t2.equals("")) {
                        addEvent.setDisable(true);
                    }
                    else {
                        addEvent.setDisable(false);
                    }
                }
            });
        }

        eventTitle.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                e.consume();
            }
        });
        TextArea eventDes = new TextArea(_event.getDetails());                  // event description
        eventDes.setMinSize(400, 200);
        eventDes.setMaxSize(400, 200);
        setMaxCharacters(eventDes, 500);

        HBox pickDateTime = new HBox();                                          // picking date
        DatePicker date = new DatePicker(_event.getDate());
        date.setMaxWidth(105);
        date.setValue(_event.getDate());

        ComboBox<String> hours = new ComboBox();                                 // pick what hour event is
        hours.setMinWidth(55);
        hours.setMaxWidth(55);
        hours.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        if (_event.getTime().getHour() > 12) {
            hours.setValue(Integer.toString(_event.getTime().getHour() - 12));
        }
        else if (_event.getTime().getHour() == 0) {
            hours.setValue("12");
        }
        else {
            hours.setValue(Integer.toString(_event.getTime().getHour()));
        }

        hours.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px; -fx-font: 11 arial");
        hours.setEditable(true);
        scrollToEdit(hours, 1, 12);
        inputValidation(hours, true, addEvent);

        Label colon = new Label(" : ");

        ComboBox<String> minutes = new ComboBox();                              // picking minute of event
        minutes.setMinWidth(55);
        minutes.setMaxWidth(55);
        minutes.getItems().addAll("00", "15", "30", "45");
        if (_event.getTime().getMinute() < 10) {
            minutes.setValue("0" + _event.getTime().getMinute());
        }
        else {
            minutes.setValue(Integer.toString(_event.getTime().getMinute()));
        }

        minutes.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px; -fx-font: 11 arial;");
        minutes.setEditable(true);
        scrollToEdit(minutes, 0, 59);
        inputValidation(minutes, false, addEvent);

        Pane spacer1 = new Pane();
        spacer1.setMinWidth(10);
        Pane spacer2 = new Pane();
        spacer2.setMinWidth(10);

        AMandPM amPM = new AMandPM(_event.getTime().getHour() < 12);

        Button bspacer = new Button("Repeating..");
        bspacer.setVisible(false);
        Pane spacer3 = new Pane();
        spacer3.setMinWidth(10);
        ToggleButton recurring = new ToggleButton("Repeating..");
        recurring.setStyle("-fx-background-color: transparent;");
        RecurrentEvents recWin = new RecurrentEvents(_event);
        recurring.setOnAction(e -> {
            if (recurring.isSelected()) {
                recurring.setStyle("-fx-background-color: lightgrey;");
                recWin.setBounds(recurring.localToScreen(recurring.getBoundsInLocal()));
                recWin.show();
            }
            else {
                recurring.setStyle("-fx-background-color: transparent;");
                recWin.hide();
            }
        });

        pickDateTime.getChildren().addAll(bspacer, date, spacer1, hours, colon, minutes, spacer2, amPM, spacer3, recurring);
        pickDateTime.setAlignment(Pos.CENTER);

        //sets 'enter' keystroke to click button
        addEvent.isDefaultButton();

        //set button to just close the Event Editor for now
        addEvent.setOnMouseClicked(event -> {
            if (amPM.isAM()) {
                setValues(eventTitle, eventDes, date, hours.getValue(), minutes.getValue(), stage);
            }
            else {
                setValues(eventTitle, eventDes, date, Integer.toString(Integer.parseInt(hours.getValue()) + 12), minutes.getValue(), stage);
            }
        });

        addEvent.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !addEvent.isDisabled()) {
                if (amPM.isAM()) {
                    setValues(eventTitle, eventDes, date, hours.getValue(), minutes.getValue(), stage);
                }
                else {
                    setValues(eventTitle, eventDes, date, Integer.toString(Integer.parseInt(hours.getValue()) + 12), minutes.getValue(), stage);
                }
            }
        });

        Button tagEditor = new Button("Tags");

        Label titleLabel = new Label("Event Title");
        Label descLabel = new Label("Description");

        titleLabel.setStyle("-fx-font: 18 arial");
        descLabel.setStyle("-fx-font: 18 arial");

        if (!_isNew) {
            Button delete = new Button("Delete Event");
            delete.setStyle("-fx-background-color: transparent;");
            delete.setTextFill(Color.RED);

            delete.setOnAction(a -> {
                Database.deleteEvent(_event.getID());
                stage.close();

                _parent.refresh();
            });

            addEvent.setText("Update Event");
            Button spacer = new Button("Delete Event");
            spacer.setVisible(false);

            BorderPane box = new BorderPane();
            box.setLeft(spacer);
            box.setCenter(addEvent);
            box.setRight(delete);

            frame.getChildren().addAll(titleLabel, eventTitle, pickDateTime, descLabel, eventDes, box, tagEditor);
        }
        else {
            frame.getChildren().addAll(titleLabel, eventTitle, pickDateTime, descLabel, eventDes, addEvent, tagEditor);
        }

        stage.setTitle("Event Editor");
        stage.setScene(new Scene(frame));

        stage.show();
        stage.setOnCloseRequest(e -> {
            recWin.close();
        });
    }

    public Stage getStage() {
        return null;
    }
    /**
     * helper function that allows a ComboBox to be modified with scrolling
     * @param box ComboBox to be modified
     */
    private void scrollToEdit(ComboBox box, int min, int max) {
       box.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean onButton) -> {       // button shows when hovering over this DateNode's box
            if (onButton) {
                box.setOnScroll(v -> {
                    double delt = v.getDeltaY();
                    int val = Integer.parseInt(box.getEditor().getText());
                    if (delt < 0) {
                        val--;
                        if (val >= min) {
                            box.getEditor().setText(Integer.toString(val));
                        }
                    }
                    else if (delt > 0) {
                        val++;
                        if (val <= max) {
                            box.getEditor().setText(Integer.toString(val));
                        }
                    }
                    else {
                        // do nothing
                    }
                });
            }
        });
    }

    /**
     * checks input for hours and minute combo box
     * @param box combo box (hours/minutes) that's to be visually changed
     * @param isHours is hours or minutes
     * @param addEventButton link to add_event button that's to be greyed out if the input is invalid
     */
    private void inputValidation(ComboBox box, boolean isHours, Button addEventButton) {
        int max, min;
        if (isHours) {
            min = 1; max = 12; }
        else {
            min = 0; max = 59; }
        TextField editor = box.getEditor();
        editor.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            char val = e.getCharacter().charAt(0);
            if (editor.getText().length() > 2 || (int) val < 48 || (int) val > 57) {                   // is a number, not > 2 chars
                editor.setText(editor.getText(0, Integer.max(editor.getText().length()-1, 0)));
                editor.requestFocus();
                editor.end();
                if (!addEventButton.isDisabled()) {
                    Timeline wait = new Timeline(
                            new KeyFrame(Duration.ZERO, evt -> box.setStyle("-fx-border-color: red; -fx-border-width: 1px 1px 1px 1px")),
                            new KeyFrame(Duration.seconds(.5), evt -> box.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px"))
                    );
                    wait.play();
                }
            }
            int p = 999;
            if (editor.getText().length() > 0) {
                p = Integer.parseInt(editor.getText());
            }
            if (p < min || p > max) {
                addEventButton.setDisable(true);
                box.setStyle("-fx-border-color: red; -fx-border-width: 1px 1px 1px 1px");
            }
            else {
                addEventButton.setDisable(false);
                box.setStyle("-fx-border-color: transparent; -fx-border-width: 1px 1px 1px 1px");
            }
        });
    }

    /**
     *
     * @param t title of event
     * @param d details of event
     * @param day day of which the event is on
     * @param hours hour of the event
     * @param minutes minute of the event
     * @param stage stage for the event window
     */
    private void setValues(TextArea t, TextArea d, DatePicker day, String hours, String minutes, Stage stage) {
        if (!_isNew) {
            Database.deleteEvent(_event.getID());
        }
        int h = 4, m = 20;      // default time, can be anything

        h = Integer.parseInt(hours);                // ensure is integer
        m = Integer.parseInt(minutes);
        LocalDateTime when = LocalDateTime.of(day.getValue().getYear(), day.getValue().getMonthValue(), day.getValue().getDayOfMonth(), h, m);        // ensure is valid time

        stage.close();

        Database.addEvent(t.getText(), when.toLocalDate() + " " + when.toLocalTime(), d.getText());
        _parent.refresh();
    }

    /**
     * helper function that sets maximum number of characters allowed in a textarea
     * @param a restricted text area
     * @param max maximum number of characters
     */
    private void setMaxCharacters(TextArea a, int max) {
        a.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (a.getText().length() > max) {
                    String s = a.getText().substring(0, max);
                    a.setText(s);
                }
            }
        });
    }
}
