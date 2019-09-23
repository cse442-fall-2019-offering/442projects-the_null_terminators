package edu.buffalo.cse442.nullterminators;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarNode extends GridPane {
    private ArrayList<ArrayList<DateNode>> dates = new ArrayList<>();
    private LocalDate offset = LocalDate.now();

    public enum VIEW {
        MONTH,
        WEEK,
        DAY
    }

    public void setOffset(LocalDate newOffset) {
        offset = newOffset;
    }

    public LocalDate getOffset() {
        return offset;
    }

    private void AddRowsAndCols(int rows, int cols) {
        //need to add empty text for elements to show up
        this.getChildren().clear();
        for(int i = 0; i < rows; i++) {
            this.addRow(i, new Text(""));
            for(int j = 0; j < cols; j++) {
                this.addColumn(j, new Text(""));
            }
        }
    }

    private void calculateDateRange(LocalDate firstDayofRange) {
        LocalDate firstDayOfMonth = firstDayofRange.minusDays(firstDayofRange.getDayOfMonth() - 1);
        int dayOffset = 0;
        int init_j = firstDayOfMonth.getDayOfWeek().getValue();

        dates.add(new ArrayList<>());
        if (init_j != 7) {
            for (int j = 0; j < init_j; ++j) {
                dates.get(0).add(new DateNode());
                dates.get(0).get(j).setDate(firstDayOfMonth.minusDays(init_j - j));
                dates.get(0).get(j).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                this.add(dates.get(0).get(j), j, 0);
            }
        }
        else {
            init_j = 0;
        }
        for (int j = init_j; j < this.getColumnCount(); j++) {
            dates.get(0).add(new DateNode());
            dates.get(0).get(j).setDate(firstDayOfMonth.plusDays(dayOffset));
            dates.get(0).get(j).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
            this.add(dates.get(0).get(j), j, 0);
            dayOffset++;
        }
        for (int i = 1; i < this.getRowCount() - 1; i++) {
            dates.add(new ArrayList<>());
            for(int j = 0; j < this.getColumnCount(); j++) {
                dates.get(i).add(new DateNode());
                dates.get(i).get(j).setDate(firstDayOfMonth.plusDays(dayOffset));
                dates.get(i).get(j).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                this.add(dates.get(i).get(j), j, i);
                dayOffset++;
            }
        }
    }

    private void setDateRange(VIEW view, LocalDate date) {
        switch(view) {
            case MONTH:
                LocalDate firstDayOfMonth = date.minusDays(date.getDayOfMonth() - 1);
                calculateDateRange(firstDayOfMonth);
                break;
            case DAY:
                dates.add(new ArrayList<>());
                dates.get(0).add(new DateNode());
                dates.get(0).get(0).setDate(date);
                dates.get(0).get(0).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                this.add(dates.get(0).get(0), 0, 0);
                break;
            case WEEK:
                LocalDate firstDayOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
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
        setDateRange(viewOfCalendar, LocalDate.now());
    }

    public void changeMonths(LocalDate toDate) {
        AddRowsAndCols(5, 7);
        setDateRange(VIEW.MONTH, toDate);
    }

    public CalendarNode() {
        super();
        changeView(VIEW.MONTH);
        this.setStyle("-fx-background-fill: black, white; -fx-background-insets: 0, 1 ;");
    }
}
