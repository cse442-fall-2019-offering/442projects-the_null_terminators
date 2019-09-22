package edu.buffalo.cse442.nullterminators;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarNode extends GridPane {
    private ArrayList<ArrayList<DateNode>> dates = new ArrayList<>();

    public enum VIEW {
        MONTH,
        WEEK,
        DAY
    }

    private void AddRowsAndCols(int rows, int cols) {
        this.getChildren().clear();
        dates.clear();
        //need to add empty text for elements to show up
        for(int i = 0; i < rows; i++) {
            this.addRow(i, new Text(""));
            for(int j = 0; j < cols; j++) {
                this.addColumn(j, new Text(""));
            }
        }
    }

    private void calculateDateRange(LocalDate firstDayOfRange) {
        int dayOffset = 0;
        for (int i = 0; i < this.getRowCount() - 1; i++) {
            dates.add(new ArrayList<>());
            for (int j = 0; j < this.getColumnCount(); j++) {
                dates.get(i).add(new DateNode());
                dates.get(i).get(j).setDate(firstDayOfRange.plusDays(dayOffset));
                dates.get(i).get(j).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                this.add(dates.get(i).get(j), j, i);
                dayOffset++;
            }
        }
    }

    private void setDateRange(VIEW view) {
        LocalDate currentDay = LocalDate.now();
        switch(view) {
            case MONTH:
                LocalDate firstDayOfMonth = currentDay.minusDays(currentDay.getDayOfMonth() - 1);
                calculateDateRange(firstDayOfMonth);
                break;
            case DAY:
                dates.add(new ArrayList<>());
                dates.get(0).add(new DateNode());
                dates.get(0).get(0).setDate(currentDay);
                dates.get(0).get(0).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                this.add(dates.get(0).get(0), 0, 0);
                break;
            case WEEK:
                LocalDate firstDayOfWeek = currentDay.minusDays(currentDay.getDayOfWeek().getValue() - 1);
                calculateDateRange(firstDayOfWeek);
                break;
        }

    }

    public void changeView(VIEW viewOfCalendar) {
        switch(viewOfCalendar) {
            case MONTH:
                AddRowsAndCols(5, 7);
                break;
            case WEEK:
                AddRowsAndCols(1, 7);
                break;
            case DAY:
                AddRowsAndCols(1, 1);
                break;
        }
        setDateRange(viewOfCalendar);
    }

    public CalendarNode() {
        super();
        changeView(VIEW.MONTH);
        this.setStyle("-fx-background-fill: black, white; -fx-background-insets: 0, 1 ;");
    }
}
