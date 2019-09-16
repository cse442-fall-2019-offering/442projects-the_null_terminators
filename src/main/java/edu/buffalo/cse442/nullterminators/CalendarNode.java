package edu.buffalo.cse442.nullterminators;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarNode extends GridPane {
    private ArrayList<ArrayList<DateNode>> dates = new ArrayList<>();

    private void AddRowsAndCols(int rows, int cols) {
        //need to add empty text for elements to show up
        for(int i = 0; i < rows; i++) {
            this.addRow(i, new Text(""));
            for(int j = 0; j < cols; j++) {
                this.addColumn(j, new Text(""));
            }
        }
    }

    private void setDateRange() {
        LocalDate currentDay = LocalDate.now();
        int dayOffset = 0;
        for(int i = 0; i < this.getRowCount(); i++) {
            dates.add(new ArrayList<>());
            for(int j = 0; j < this.getColumnCount(); j++) {
                dates.get(i).add(new DateNode());
                dates.get(i).get(j).setDate(currentDay.plusDays(dayOffset));
                this.add(dates.get(i).get(j), j, i);
                dayOffset++;
            }
        }
    }

    public CalendarNode() {
        super();
        AddRowsAndCols(6, 7);
        setDateRange();
    }
}
