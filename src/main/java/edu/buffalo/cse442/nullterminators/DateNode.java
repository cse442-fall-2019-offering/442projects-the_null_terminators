package edu.buffalo.cse442.nullterminators;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import java.time.LocalDate;

public class DateNode extends AnchorPane {
    private LocalDate date;
    private Text dateView;
    private Button add_event;

    DateNode(Node... children) {
        super(children);
        dateView = new Text();
        add_event = new Button("+");
        add_event.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO: Add link to Event Adder UI.
            }
        });

        Pane spacer = new Pane();
        spacer.setMinSize(10, 1);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(add_event, spacer, dateView);
    }

    public LocalDate getDate() {
        return date;
    }

    void setDate(LocalDate newDate) {
        date = newDate;
        if (newDate == LocalDate.now()) {
            
        }
        dateView.setText(Integer.toString(date.getDayOfMonth()));
    }
}
