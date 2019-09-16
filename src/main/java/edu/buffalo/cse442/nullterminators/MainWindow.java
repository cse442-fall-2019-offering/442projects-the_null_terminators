package edu.buffalo.cse442.nullterminators;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MainWindow {

    @FXML private GridPane calendarGrid;

    public MainWindow() {
        System.out.println("Constructor");
    }

    @FXML
    void initialize() {

    }
}
