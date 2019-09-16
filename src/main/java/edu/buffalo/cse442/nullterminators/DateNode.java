package edu.buffalo.cse442.nullterminators;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.time.LocalDate;

public class DateNode extends AnchorPane {
    private LocalDate date;
    private Text dateView;

    DateNode(Node... children) {
        super(children);
        dateView = new Text();
        this.getChildren().add(dateView);
    }

    public LocalDate getDate() {
        return date;
    }

    void setDate(LocalDate newDate) {
        date = newDate;
        dateView.setText(Integer.toString(date.getDayOfMonth()));
    }
}
