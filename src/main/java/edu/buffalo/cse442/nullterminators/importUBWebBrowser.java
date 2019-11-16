package edu.buffalo.cse442.nullterminators;

import javafx.application.Platform;
import javafx.concurrent.Worker;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class importUBWebBrowser {
    private class Course {
        String name = "";

        String dateRange = "";

        String dotw = "";
        String dotwStrRep = "";

        LocalTime timeStart;
        String timeStartStr;

        LocalTime timeEnd;
        String timeEndStr;
        String location = "";
    }

    public importUBWebBrowser() {
        scrape();
    }

    public void scrape() {
        Stage stage = new Stage();
        stage.setTitle("Import your UB Calendar..");
        String destURL = "https://www.ss.hub.buffalo.edu/psc/csprdss_2/EMPLOYEE/HRMS/c/SSR_STUDENT_FL.SSR_MD_SP_FL.GBL?Action=U&MD=Y&GMenu=SSR_STUDENT_FL&GComp=SSR_START_PAGE_FL&GPage=SSR_START_PAGE_FL&scname=CS_SSR_MANAGE_CLASSES_NAV&AJAXTransfer=y&ICAJAXTrf=true";

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(destURL);
        webEngine.getLoadWorker().stateProperty().addListener((change, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                if (webEngine.getLocation().equals(destURL + "&")) {        // "Manage Classes" page
                    LocalTime stop = LocalTime.now().plusSeconds(30);
                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                String html = (String) webEngine.executeScript("document.documentElement.outerHTML");
                                if (html.contains("<div class=\"ps_box-scrollarea-row\" id=\"win2divSSR_SBJCT_LVL1")) {
                                    t.cancel();
                                    String parsed = ListtoString(parse(html));
                                    confirm(parsed);
                                    stage.close();
                                }
                                if (stop.isBefore(LocalTime.now())) {
                                    t.cancel();
                                }
                            });
                        }
                    };
                    t.schedule(tt, 1000, 1000);
                }
            }
        });

        stage.setScene(new Scene(browser));
        stage.show();
    }

    private ArrayList<Course> parse(String html) {
        ArrayList<String> split = new ArrayList<>();
        // SEPARATE EACH OF THE CLASSES
        String target = "<div class=\"ps_box-scrollarea-row\" id=\"win2divSSR_SBJCT_LVL1_row$";
        int start = 0, end = 0;
        while (start != -1 && end != -1) {
            start = html.indexOf(target, end);
            end = html.indexOf(target, start + 1);
            if (end == -1) {
                String add = removeTags(html.substring(start));
                split.add(add);
                break;
            }

            String add = removeTags(html.substring(start, end));
            split.add(add);
        }

        ArrayList<Course> courses = new ArrayList<>();
        // FIND AND PARSE
        for (String s : split) {
            int i = 0;
            boolean hasRecitation = false;

            // Course name, like CSE368
            String name = removeSpaces(s.substring(0, s.indexOf('\n')));
            if (name.substring(6,8).equals("LR")) {
                hasRecitation = true;
            }
            name = name.substring(0, 6);

            String[] ordering = {"LEC", "REC"};
            int runs = 1;
            if (hasRecitation) {
                if (s.indexOf("LEC") > s.indexOf("REC")) {
                    ordering[0] = "REC";
                    ordering[1] = "LEC";
                }
                runs = 2;
            }
            for (int x = 0; x < runs; ++x) {
                Course c = new Course();
                i = s.indexOf("Class Nbr", i);
                // Lecture or Recitation
                c.name = name + ordering[x];

                // Date Range
                i = s.indexOf("Start/End Dates   ", i) + "Start/End Dates   ".length();
                c.dateRange = s.substring(i, s.indexOf('\n', i));

                // Days of the Week (Monday, Tuesday, Wednesday, Thursday, Friday)
                i = s.indexOf("Days: ", i) + "Days: ".length();
                String dotw = s.substring(i, s.indexOf('\n', i));
                c.dotw = dotwShort(dotw);
                c.dotwStrRep = dotw;

                // Time
                DateTimeFormatter oneDig = DateTimeFormatter.ofPattern("hh:mma", Locale.US);
                DateTimeFormatter twoDig = DateTimeFormatter.ofPattern("h:mma", Locale.US);

                i = s.indexOf("Times: ", i) + "Times: ".length();
                String timeStart = s.substring(i, s.indexOf(' ', i));
                c.timeStartStr = timeStart;
                try {
                    c.timeStart = LocalTime.parse(timeStart, oneDig);
                } catch (DateTimeParseException dtpe) {
                    c.timeStart = LocalTime.parse(timeStart, twoDig);
                }

                i = s.indexOf("to ", i) + "to ".length();
                int eol = s.indexOf(' ', i);
                if (s.indexOf('\n', i) < eol) {
                    eol = s.indexOf('\n', i);
                }
                String timeEnd = s.substring(i, eol);
                c.timeEndStr = timeEnd;
                try {
                    c.timeEnd = LocalTime.parse(timeEnd, oneDig);
                } catch (DateTimeParseException dtpe) {
                    c.timeEnd = LocalTime.parse(timeEnd, twoDig);
                }


                // Location (Room)
                i = s.indexOf("Room   ", i) + "Room   ".length();
                String location = s.substring(i, s.indexOf('\n', i));
                c.location = location;
                courses.add(c);
            }
        }
        return courses;
    }

    private void confirm(String parsed) {
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
            // TODO: add the schedule into the calendar DB
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("----- TO BE IMPLEMENTED -----");
            alert.setHeaderText(null);
            alert.setContentText("----- TO BE IMPLEMENTED -----");
            alert.showAndWait();

            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction(e -> stage.close());
        userInput.setSpacing(50);
        userInput.setAlignment(Pos.CENTER);
        userInput.getChildren().addAll(confirm, cancel);

        frame.getChildren().addAll(new Label("These classes will be added to your calendar:"), new Label(parsed), userInput);
        stage.setScene(new Scene(frame));
        stage.show();
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

    private String removeSpaces(String s) {

        while(s.indexOf(" ") != -1) {
            s = s.substring(0, s.indexOf(" ")) + s.substring(s.indexOf(" ") + 1);
        }
        return s;
    }

    private String dotwShort(String s) {
        String ret = "";
        if (s.contains("Sunday")) {
            ret = ret + "U";
        } else { ret = ret + '-'; }
        if (s.contains("Monday")) {
            ret = ret + "M";
        } else { ret = ret + '-'; }
        if (s.contains("Tuesday")) {
            ret = ret + "T";
        } else { ret = ret + '-'; }
        if (s.contains("Wednesday")) {
            ret = ret + "W";
        } else { ret = ret + '-'; }
        if (s.contains("Thursday")) {
            ret = ret + "R";
        } else { ret = ret + '-'; }
        if (s.contains("Friday")) {
            ret = ret + "F";
        } else { ret = ret + '-'; }
        if (s.contains("Saturday")) {
            ret = ret + "S";
        } else { ret = ret + '-'; }

        return ret;
    }

    private String ListtoString(ArrayList<Course> list) {
        String ret = "";
        for (Course c : list) {
            ret = ret + c.name + '\n';
            ret = ret + "        RANGE: " + c.dateRange + '\n';
            ret = ret + "        DOTW: " + c.dotwStrRep + '\n';
            ret = ret + "        TIME: " + c.timeStartStr + " - " + c.timeEndStr + '\n';
            ret = ret + "        LOC:  " + c.location + "\n";
        }
        return ret;
    }
}