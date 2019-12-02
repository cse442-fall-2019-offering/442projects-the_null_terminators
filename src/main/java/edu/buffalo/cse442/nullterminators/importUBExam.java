package edu.buffalo.cse442.nullterminators;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class importUBExam {
    private CalendarNode _cn;

    private class Exam {
        String course;
        String type;

        LocalDate date;
        LocalTime start;
        LocalTime end;

        String location;
    }
    private ArrayList<Exam> _exams;

    public importUBExam(CalendarNode cn) {
        _cn = cn;

    }

    public void openBrowser() {
        ArrayList<String[]> check = Database.getEventsByTag(69);
        if (!check.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Schedule Already Exists!");
            alert.setHeaderText(null);
            alert.setContentText("You already have an existing exam schedule - if you want to add another, remove your current schedule and try again");
            alert.showAndWait();
            return;
        }

        String destURL = "https://www.ss.hub.buffalo.edu/psc/csprdss_1/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_EXAM_L.GBL?&pslnkid=UB_S201908091033137534114377&ICAJAXTrf=true&ICMDTarget=start";
        Stage stage = new Stage();
        stage.setTitle("Import UB Exam Schedule");

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(destURL);

        AtomicBoolean found = new AtomicBoolean(false);
        webEngine.getLoadWorker().stateProperty().addListener((obs, n, o) -> {
            //if (n == Worker.State.SUCCEEDED) {
                LocalTime stop = LocalTime.now().plusSeconds(30);
                Timer t = new Timer();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            String html = (String) webEngine.executeScript("document.documentElement.outerHTML");
                            if (html.contains("trSS_EXAMSCH1_VW$0")) {
                                if (!found.get()) {
                                    found.set(true);
                                    t.cancel();
                                    parse(html);
                                    stage.close();
                                }
                            }
                            if (stop.isBefore(LocalTime.now())) {
                                t.cancel();
                            }
                        });
                    }
                };
                t.schedule(tt, 1000, 1000);
           // }
        });


        stage.setScene(new Scene(browser));
        stage.show();
    }

    public void parse(String html) {
        // SPLIT
        ArrayList<String> split = new ArrayList<>();
        String target = "trSS_EXAMSCH1_VW$0_row";
        int start = 0, end = 0;
        while (start != -1 && end != -1) {
            start = html.indexOf(target, end);
            end = html.indexOf(target, start + 1);
            if (end == -1) {
                end = html.indexOf("Group Box", start + 1);
                String add = removeTags(html.substring(start, end));
                split.add(add);
                break;
            }
            String add = removeTags(html.substring(start, end));
            split.add(add);
        }

        _exams = new ArrayList<>();
        for (String s : split) {
            s = s.replaceAll("(?m)^[ \t]*\r?\n", "");
            String[] arr = s.split("\n", 7);

            Exam e = new Exam();
            e.course = removeSpaces(arr[0]).substring(0, 6);
            e.type = removeSpaces(arr[3]);
            e.date = StringToDate(removeSpaces(arr[4]));

            arr[5] = removeSpaces(arr[5]);
            e.start = StringToTime(arr[5].substring(0, arr[5].indexOf('-')));
            e.end = StringToTime(arr[5].substring(arr[5].indexOf('-') + 1));

            e.location = removeSpaces(arr[6]);

            _exams.add(e);
        }

        confirm();
    }

    private void confirm() {
        Stage stage = new Stage();
        stage.setMaxHeight(500);
        stage.setMaxWidth(750);
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(25);
        frame.setPadding(new Insets(25,25,25,25));


        HBox userInput = new HBox();
        Button confirm = new Button("Confirm");
        confirm.setOnAction(e -> {
            addExams();
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction(e -> stage.close());
        userInput.setSpacing(50);
        userInput.setAlignment(Pos.CENTER);
        userInput.getChildren().addAll(confirm, cancel);

        frame.getChildren().addAll(
                new Label("These exams will be added to your calendar:"),
                new Label(ListToString(_exams)),
                userInput);
        stage.setScene(new Scene(frame));
        stage.show();
    }

    private void addExams() {
        for (Exam e : _exams) {
            String title = e.course + " " + e.type;
            String datetime = e.date + " " + e.start;   // YYYY-MM-DD HH:MM
            String description =
                    "Date: " + e.date + '\n' +
                    "Location: " + e.location + '\n' +
                    "Duration: " + e.start + " - " + e.end;

            Database.addEvent(title, datetime, description, "exam", "", 69);
        }
        _cn.refreshChildren();
    }

    public void removeExams() {
        ArrayList<String[]> res = Database.getEventsByTag(69);
        if (res.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("There's no schedule to remove!");
            alert.setHeaderText(null);
            alert.setContentText("Add your schedule and try again (if you want it removed)!");
            alert.showAndWait();
            return;
        }
        for (String[] s : res) {
            Database.deleteEvent(Integer.parseInt(s[0]));
        }
        _cn.refreshChildren();
    }

    private String ListToString(ArrayList<Exam> list) {
        String s = "";
        for (Exam e : list) {
            s = s + e.course + " " + e.type + '\n';
            s = s + "    DATE: " + e.date + '\n';
            s = s + "    TIME: " + TimeToAMPM(e.start) + " - " + TimeToAMPM(e.end) + '\n';
            s = s + "    LOCATION: " + e.location + '\n';
        }

        return s;
    }



    /*
    METHODS BELOW TAKEN FROM IMPORTUBBROWSER
     */
    private String removeSpaces(String s) {

        while(s.indexOf(" ") != -1) {
            s = s.substring(0, s.indexOf(" ")) + s.substring(s.indexOf(" ") + 1);
        }
        return s;
    }
    private String removeTags(String html) {
        String ret = "";
        for (int i = 0; i < html.length(); ++i) {
            int start = html.indexOf('>', i) + 1;
            int end = html.indexOf('<', start);

            if (end < i || end == -1) { break; }
            ret = ret + html.substring(start, end) + ' ';
            i = end;
        }
        return ret;
    }
    private LocalDate StringToDate(String s) {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM/d/yyyy");
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("M/dd/yyyy");
        DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("M/d/yyyy");

        LocalDate ret = LocalDate.now();
        try {
            ret = LocalDate.parse(s, dtf1);
        } catch (DateTimeParseException dtpe1) {
            try {
                ret = LocalDate.parse(s, dtf2);
            } catch (DateTimeParseException dtpe2) {
                try {
                    ret = LocalDate.parse(s, dtf3);
                } catch (DateTimeParseException dtpe3) {
                    ret = LocalDate.parse(s, dtf4);
                }
            }
        }
        return ret;
    }
    private LocalTime StringToTime(String s) {
        DateTimeFormatter oneDig = DateTimeFormatter.ofPattern("hh:mma", Locale.US);
        DateTimeFormatter twoDig = DateTimeFormatter.ofPattern("h:mma", Locale.US);
        try {
            return LocalTime.parse(s, oneDig);
        } catch (DateTimeParseException dtpe) {
            return LocalTime.parse(s, twoDig);
        }
    }
    private String TimeToAMPM(LocalTime t) {
        String pattern = "hh:mma";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return t.format(dateFormat);
    }
}
