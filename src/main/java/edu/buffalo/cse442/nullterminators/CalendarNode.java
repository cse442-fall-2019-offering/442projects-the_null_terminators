package edu.buffalo.cse442.nullterminators;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * class that sets up the main grid - day/week/month view
 */
public class CalendarNode extends GridPane {
    private ArrayList<ArrayList<DateNode>> dates = new ArrayList<>();
    private LocalDate offset = LocalDate.now();

    public enum VIEW {
        MONTH,
        WEEK,
        DAY
    }

    public CalendarNode() {
        super();
        change(VIEW.MONTH, LocalDate.now());
    }

    /**
     * function that changes views based on the date or view required
     * @param viewOfCalendar DAY, WEEK, MONTH
     * @param toDate date to be switched to
     */
    public void change(VIEW viewOfCalendar, LocalDate toDate) {
        this.getChildren().clear();
        dates.clear();
        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();
        switch(viewOfCalendar) {
            case MONTH:
                AddRowsAndCols(5, 7);
                break;
            case WEEK:
                AddRowsAndCols(1, 7);
                break;
            case DAY:
                AddRowsAndCols(1,1);
                break;
        }
        setDateRange(viewOfCalendar, toDate);
    }


    /**
     * helper function that adds constraints for the grid dependent on the view
     * @param rows number of rows (1 or 5)
     * @param cols number of columns (1 or 7)
     */
    private void AddRowsAndCols(int rows, int cols) {
        for (int i = 0; i < rows; ++i) {
            RowConstraints r_add = new RowConstraints();
            r_add.setPercentHeight(Double.valueOf(1.0/rows) * 100);
            this.getRowConstraints().add(r_add);
        }
        for (int j = 0; j < cols; ++j) {
            ColumnConstraints c_add = new ColumnConstraints();
            c_add.setPercentWidth(Double.valueOf(1.0/cols) * 100);
            this.getColumnConstraints().add(c_add);
        }
    }

    /**
     * calculates range of days that needs to be drawn on, adds DateNodes to each grid accordingly
     * @param firstDay first day that needs to be drawn on
     */
    private void calculateDateRange(LocalDate firstDay) {
        int dayOffset = 0;
        int init_j = firstDay.getDayOfWeek().getValue();
        dates.add(new ArrayList<>());
        if (init_j == 7) {
            init_j = 0;
        }
        for (int i = 0; i < this.getRowCount(); i++) {
            dates.add(new ArrayList<>());
            for(int j = 0; j < this.getColumnCount(); j++) {
                dates.get(i).add(new DateNode());
                dates.get(i).get(j).setDate(firstDay.plusDays(dayOffset));
                dates.get(i).get(j).setStyle("-fx-border-color: black; -fx-border-width: 1 1 1 1;");
                if (i == 0) {
                    if (j < init_j) {
                        dates.get(i).get(j).setDate(firstDay.minusDays(init_j - j));                                // overwrite previous values for days, special case (previous month days)
                        BackgroundFill color = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
                        dates.get(i).get(j).setBackground(new Background(color));
                        dayOffset--;
                    }
                }
                this.add(dates.get(i).get(j), j, i);
                dayOffset++;
            }
        }
    }

    /**
     * sets that day that needs to be shown
     * @param view view required to calculate how many days
     * @param date date that it "starts" on
     */
    private void setDateRange(VIEW view, LocalDate date) {
        switch(view) {
            case MONTH:
                String month = Integer.toString(date.getMonthValue());
                if (date.getMonthValue() < 10) {
                    month = "0" + date.getMonthValue();
                }
                LocalDate firstDayOfMonth = LocalDate.parse(date.getYear() + "-" + month + "-" + "01");
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
                LocalDate firstDay = date;
                if (firstDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    firstDay = firstDay.plusDays(1);
                }
                else if (firstDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    while (firstDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        firstDay = firstDay.minusDays(1);
                    }
                }
                else {}     // do nothing
                calculateDateRange(firstDay);
                break;
        }
    }

}
