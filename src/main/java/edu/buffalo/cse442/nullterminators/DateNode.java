package edu.buffalo.cse442.nullterminators;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateNode extends AnchorPane {

    private LocalDate date;
    private Label dateView;
    private Button add_event;

    private VBox storage;

    DateNode(Node... children) {
        super(children);
        dateView = new Label();
        dateView.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        add_event = new Button("+");
        add_event.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 18));
        add_event.setTextFill(Color.DEEPSKYBLUE);
        add_event.setStyle("-fx-background-color: transparent; -fx-padding: 0 3 0 3;");
        add_event.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new EventWindow().createEEWindow();
            }
        });

        add_event.setOpacity(0.0);

        this.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                add_event.setOpacity(1.0);
            } else {
                add_event.setOpacity(0.0);
            }
        });

        storage = new VBox();

        this.setTopAnchor(storage, 0.0);
        this.setBottomAnchor(storage, 0.0);
        this.setRightAnchor(storage, 0.0);
        this.setLeftAnchor(storage, 0.0);

        HBox date = new HBox();
        Pane spacer1 = new Pane();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        date.getChildren().addAll(dateView, spacer1, add_event);

        storage.getChildren().add(date);
        this.getChildren().add(storage);
    }
    public LocalDate getDate() {
        return date;
    }

    void setDate(LocalDate setDate) {
        date = setDate;
        LocalDate lastDayOfLastMonth = setDate.minusDays(setDate.getDayOfMonth());
        if (setDate.isBefore(LocalDate.now())) {
            BackgroundFill color = new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY);
            this.setBackground(new Background(color));
        }
        else if (setDate.compareTo(LocalDate.now()) == 0) {
            BackgroundFill color = new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY);
            this.setBackground(new Background(color));

            // TODO: actually not todo, color just more noticebale. THIS IS TEMPORARY
            // this format could be transformed into {time : (event[1], event[2], ...)}
            LocalDateTime temp = LocalDateTime.now().minusHours(3);
            event(temp, "Wake up");
            event(temp.plusMinutes(13), "Poop");
            event(temp.plusMinutes(45), "Eat some breakfast");
            event(temp.plusMinutes(59), "Go back to sleep");
            // TODO: add previously stored events
        }
        else {
//            BackgroundFill color = new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY);
//            this.setBackground(new Background(color));
        }
        dateView.setText(" " + date.getDayOfMonth());
    }

    private String formatTime(LocalDateTime time) {
        int unf_hr = time.getHour();
        int unf_min = time.getMinute();
        String hours = Integer.toString(unf_hr);
        String minutes = Integer.toString(unf_min);

        if (unf_hr < 10) {
            hours = "0" + unf_hr;
        }
        if (unf_min < 10) {
            minutes = "0" + unf_min;
        }
        return hours + ":" + minutes;
    }

    private void event(LocalDateTime time, String event) {

        Button adding = new Button(formatTime(time) + " - " + event);
        HBox.setHgrow(adding, Priority.ALWAYS);
        adding.setPadding(new Insets(0, 0, 0, 0));
        adding.setFont(Font.font("Arial", FontWeight.BOLD,12));
        adding.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        adding.setOnAction(e -> {
            Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
            popup.setResizable(true);
            popup.onShownProperty().addListener(f -> {
                Platform.runLater(() -> popup.setResizable(false));
            });
            popup.setTitle("EVENT: " + event);
            popup.setHeaderText("");
            popup.setContentText("Test dialog for when clicking on an event on the calendar!");
            popup.showAndWait();
        });

        adding.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                adding.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                adding.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        storage.getChildren().add(adding);
    }

}
