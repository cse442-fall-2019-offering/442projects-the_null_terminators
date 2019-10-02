package edu.buffalo.cse442.nullterminators;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.time.LocalDate;

public class DateNode extends AnchorPane {
    private LocalDate date;
    private Text dateView;
    private Text today;
    private Button add_event;

    DateNode(Node... children) {
        super(children);
        dateView = new Text();
        today = new Text();
        add_event = new Button("+");
        add_event.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new EventWindow().createEEWindow();
            }
        });

        BorderPane boxy = new BorderPane();
        boxy.setLeft(dateView);
        boxy.setCenter(today);
        boxy.setRight(add_event);

        this.setTopAnchor(boxy, 0.0);
        this.setRightAnchor(boxy, 0.0);
        this.setBottomAnchor(boxy, 0.0);
        this.setLeftAnchor(boxy, 5.0);

        this.getChildren().add(boxy);
    }

    public LocalDate getDate() {
        return date;
    }

    void setDate(LocalDate newDate) {
        date = newDate;
        if (newDate.compareTo(LocalDate.now()) == 0) {
            today.setText("TODAAYY");
        }
        dateView.setText(Integer.toString(date.getDayOfMonth()));
    }
}
