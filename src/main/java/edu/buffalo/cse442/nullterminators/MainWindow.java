package edu.buffalo.cse442.nullterminators;

import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

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
    @FXML private MenuItem _clearDB;
    @FXML private MenuItem _ubImport;
    @FXML private MenuItem _importCal;
    @FXML private MenuItem _themes;

    @FXML private MenuItem _tagEditor;
    @FXML private Button _sideBar;

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

        _tagEditor.setOnAction(e -> {
           new EventTags();
        });

        SideBar sb = new SideBar();
        AtomicBoolean sbVisible = new AtomicBoolean(false);
        _sideBar.setOnAction(e-> {
            if (sbVisible.get()) {
                sb.hide();
                sbVisible.set(false);
            }
            else {
                sb.setBounds(_sideBar.localToScreen(_sideBar.getBoundsInLocal()));
                sb.show();
                sbVisible.set(true);
            }
        });
        sb.focusedProperty().addListener((obs, what, focused) -> {
            if (focused) {
                sb.show();
            }
            else {
                sb.hide();
            }
            sbVisible.set(focused);
        });
        _ubImport.setOnAction(e -> {
            new importUBWebBrowser();
        });
/*
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

