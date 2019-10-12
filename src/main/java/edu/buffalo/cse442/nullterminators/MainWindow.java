package edu.buffalo.cse442.nullterminators;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;

public class MainWindow {
    @FXML private DotwNode _dotw;
    @FXML private CalendarNode _calendarGrid;
    @FXML private DateDisplay _dateDisplayController;
    @FXML private Button _before;
    @FXML private Button _today;
    @FXML private Button _after;

    @FXML private MostRecentEvent _mre;

    @FXML private MenuItem _weekViewButton;
    @FXML private MenuItem _monthViewButton;
    @FXML private MenuItem _dayViewButton;
    @FXML private MenuItem _ubImport;
    @FXML private MenuItem _importCal;
    @FXML private MenuItem _themes;

    private LocalDate _date = LocalDate.now();
    private CalendarNode.VIEW _view = CalendarNode.VIEW.MONTH;


    public MainWindow() {

    }

    @FXML
    void initialize() {
        _dotw.show();
        _dateDisplayController.updateDateText(LocalDate.now());

        _before.setOnAction(e -> {
            switchHelper(-1);
            _dateDisplayController.updateDateText(_date);
            _calendarGrid.change(_view, _date);
        });

        _today.setOnAction(e -> {
            _date = LocalDate.now();
            _dateDisplayController.updateDateText(_date);
            _calendarGrid.change(_view, _date);
        });

        _after.setOnAction(e -> {
            switchHelper(1);
            _dateDisplayController.updateDateText(_date);
            _calendarGrid.change(_view, _date);
        });

        _weekViewButton.setOnAction(e -> {
            _dotw.show();
            _view = CalendarNode.VIEW.WEEK;
            _calendarGrid.change(_view, _date);
        });
        _monthViewButton.setOnAction(e -> {
            _dotw.show();
            _view = CalendarNode.VIEW.MONTH;
            _calendarGrid.change(_view, _date);
        });
        _dayViewButton.setOnAction(e -> {
            _dotw.hide();
            _view = CalendarNode.VIEW.DAY;
            _calendarGrid.change(_view, _date);
        });
/*
        ubImport.setOnAction(e -> {
            //new importUBCal().window();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Featuring coming soon!!");
            alert.setHeaderText(null);
            alert.setContentText("Feature coming soon!!");
            alert.onShownProperty().addListener(x -> {
                Platform.runLater(() -> alert.setResizable(false));
            });
            alert.showAndWait();
        });

        RESET.setOnAction(e -> {
            System.out.println(LocalDate.now().getDayOfWeek().getValue() + " - " + LocalDate.now().getDayOfWeek());
        });
        importCal.setOnAction(e -> {
            //new importCalendars().window();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Featuring coming soon!!");
            alert.setHeaderText(null);
            alert.setContentText("Feature coming soon!!");
            alert.onShownProperty().addListener(x -> {
                Platform.runLater(() -> alert.setResizable(false));
            });
            alert.showAndWait();
        });

        themes.setOnAction(e -> {
            new Theme().openThemeEditor();
        });

 */
    }

    /** if dir = -1:
     *      go backward
     *  if dir =  1:
     *      go forward
     */
    private void switchHelper(int dir) {
        switch(_view) {
            case MONTH: {
                if (dir == 1) {
                   _date = _date.plusMonths(1);
                } else {
                    _date = _date.minusMonths(1);
                }}
                break;
            case WEEK: {
                if (dir == 1) {
                    _date = _date.plusWeeks(1);
                } else {
                    _date = _date.minusWeeks(1);
                }}
                break;
            case DAY: {
                if (dir == 1) {
                    _date = _date.plusDays(1);
                } else {
                    _date = _date.minusDays(1);
                }}
                break;
        }
    }
}

