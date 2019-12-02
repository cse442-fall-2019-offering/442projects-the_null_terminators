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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class importUBWebBrowser {
    private CalendarNode _cn;
    private class Course {
        String name = "";

        LocalDate dateStart;
        LocalDate dateEnd;

        String dotw = "";
        String dotwStrRep = "";

        LocalTime timeStart;
        LocalTime timeEnd;

        String location = "";
    }

    private ArrayList<Course> _courses;
    private String _notAdded;

    public importUBWebBrowser(CalendarNode cn) {
        _cn = cn;
        choose();
    }

    /**
     * set up for GUI to show options - import a new class schedule, or edit the exising one.
     */
    private void choose() {
        Stage stage = new Stage();
        stage.setTitle("Class Schedule Settings");
        VBox frame = new VBox();
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(5);
        frame.setPadding(new Insets(10,10,10,10));

        Button openBrowser = new Button("Import Class Schedule!");
        openBrowser.setOnAction(e -> {
            grabHTML();
            stage.close();
        });

        Button remClass = new Button("Remove Class Schedule");
        remClass.setOnAction(e -> {
            this.removeSchedule();
            stage.close();
        });

        Pane spacer = new Pane();
        spacer.setMinHeight(10);

        Button examSched = new Button("Import Exam Schedule!");
        examSched.setOnAction(e -> {
            (new importUBExam(_cn)).openBrowser();
            stage.close();
        });

        Button remExams = new Button("Remove Exam Schedule");
        remExams.setOnAction(e -> {
            (new importUBExam(_cn)).removeExams();
            stage.close();
        });



        frame.getChildren().addAll(new Label("What would you like to do?"), openBrowser, remClass, spacer, examSched, remExams);
        stage.setScene(new Scene(frame));
        stage.show();
    }

    /**
     * opens up browser, grabs HTML when Class Schedule page comes up
     * the parse call is WITHIN this function
     */
    private void grabHTML() {
        ArrayList<String[]> check = Database.getEventsByTag(70);
        if (!check.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Schedule Already Exists!");
            alert.setHeaderText(null);
            alert.setContentText("You already have an existing class schedule - if you want to add another, remove your current schedule and try again");
            alert.showAndWait();
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Import your UB Calendar..");
        String destURL = "https://www.ss.hub.buffalo.edu/psc/csprdss_2/EMPLOYEE/HRMS/c/SSR_STUDENT_FL.SSR_MD_SP_FL.GBL?Action=U&MD=Y&GMenu=SSR_STUDENT_FL&GComp=SSR_START_PAGE_FL&GPage=SSR_START_PAGE_FL&scname=CS_SSR_MANAGE_CLASSES_NAV&AJAXTransfer=y&ICAJAXTrf=true";
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(destURL);

        _courses = new ArrayList<>();

        AtomicBoolean found = new AtomicBoolean(false);
        webEngine.getLoadWorker().stateProperty().addListener((change, oldState, newState) -> {
         //   if (newState == Worker.State.SUCCEEDED) {
                LocalTime stop = LocalTime.now().plusSeconds(30);
                Timer t = new Timer();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            String html = (String) webEngine.executeScript("document.documentElement.outerHTML");
                            if (html.contains("win2divSSR_SBJCT_LVL1")) {
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
         //   }
        });

        stage.setScene(new Scene(browser));
        stage.show();
    }

    private void parse(String html) {
        // Divide each of the classes up into substrings (simplicity?)
        ArrayList<String> split = new ArrayList<>();
        String target = "win2divSSR_SBJCT_LVL1_row$";
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

        _courses = new ArrayList<>();
        _notAdded = "";
        // Parse course name, date, time, and location for each split.
        for (String s : split) {
            int i = 0;  // current position
            boolean hasRecitation = false;

            // Course name, like CSE368
            String name = removeSpaces(s.substring(0, s.indexOf('\n')));
            if (name.substring(6,8).equals("LR")) {
                hasRecitation = true;
            }
            name = name.substring(0, 6);

            // Decide if the course has a recitation or not
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


                // Date Range (MM/dd/yyyy to MM/dd/yyyy)
                i = s.indexOf("Start/End Dates   ", i) + "Start/End Dates   ".length();

                c.dateStart = StringToDate(s.substring(i, s.indexOf(' ', i)));
                i = s.indexOf("- ", i) + "- ".length();
                int eol = s.indexOf(' ', i);
                if (s.indexOf('\n', i) < eol) {
                    eol = s.indexOf('\n', i);
                }
                c.dateEnd = StringToDate(s.substring(i, s.indexOf(' ', eol)));


                // Days of the Week (Monday, Tuesday, Wednesday, Thursday, Friday)
                i = s.indexOf("Days: ", i) + "Days: ".length();
                String dotw = s.substring(i, s.indexOf('\n', i));
                c.dotw = dotwShort(dotw);
                c.dotwStrRep = dotw;

                // Time of Class (hh:mma - hh:mma)
                DateTimeFormatter oneDig = DateTimeFormatter.ofPattern("hh:mma", Locale.US);
                DateTimeFormatter twoDig = DateTimeFormatter.ofPattern("h:mma", Locale.US);

                i = s.indexOf("Times: ", i) + "Times: ".length();
                String timeLine = s.substring(i, s.indexOf('\n', i));       // Grab the current line
                if (timeLine.contains("To be Announced")) {
                    _notAdded = _notAdded + c.name + "\n    ";
                    continue;
                }

                String timeStart = s.substring(i, s.indexOf(' ', i));
                try {
                    c.timeStart = LocalTime.parse(timeStart, oneDig);
                } catch (DateTimeParseException dtpe) {
                    c.timeStart = LocalTime.parse(timeStart, twoDig);
                }

                i = s.indexOf("to ", i) + "to ".length();
                eol = s.indexOf(' ', i);
                if (s.indexOf('\n', i) < eol) {
                    eol = s.indexOf('\n', i);
                }
                String timeEnd = s.substring(i, eol);
                try {
                    c.timeEnd = LocalTime.parse(timeEnd, oneDig);
                } catch (DateTimeParseException dtpe) {
                    c.timeEnd = LocalTime.parse(timeEnd, twoDig);
                }


                // Location (Room)
                i = s.indexOf("Room   ", i) + "Room   ".length();
                String location = s.substring(i, s.indexOf('\n', i));
                c.location = location;
                _courses.add(c);
            }
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
            // TODO: add the schedule into the calendar DB
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("----- TO BE IMPLEMENTED -----");
            alert.setHeaderText(null);
            alert.setContentText("Temporary, separate values added to the database, waiting on recurrent event functionality for.. linkage.. odd word.. linkage..");
            alert.showAndWait();
            addSchedule(_courses);
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction(e -> stage.close());
        userInput.setSpacing(50);
        userInput.setAlignment(Pos.CENTER);
        userInput.getChildren().addAll(confirm, cancel);


        Label notToBeAdded = new Label("Classes NOT to be added:\n" + _notAdded);
        if (_notAdded.equals("")) {
            notToBeAdded = new Label("");
        }

        frame.getChildren().addAll(
                new Label("These classes will be added to your calendar:"),
                new Label(ListtoString(_courses)),
                notToBeAdded,
                userInput);
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
            ret = ret + "        RANGE: " + c.dateStart + " - " + c.dateEnd + '\n';
            ret = ret + "        DOTW: " + c.dotwStrRep + '\n';
            ret = ret + "        TIME: " + TimeToAMPM(c.timeStart) + " - " + TimeToAMPM(c.timeEnd) + '\n';
            ret = ret + "        LOC:  " + c.location + "\n";
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

    private String TimeToAMPM(LocalTime t) {
        String pattern = "hh:mma";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        return t.format(dateFormat);
    }

    /**
     * TODO: this method is not at all efficient, but it's temporary and will add events to calendar
     * Yeah this method is completely disgusting, don't.. just don't. this CANNOT BE IN THE FINAL PROJECT.
     * PLEASE. DO NOT. PLEASE.
     *
     * IT IS TEMPORARY. I REPEAT. IT IS TEMPORARY. TEMPORARY.
     * @param list
     */
    private void addSchedule(ArrayList<Course> list) {
        for (Course c : list) {
            String title = c.name;
            String description =
                    "Location: " + c.location + '\n'
                    + c.dateStart + " - " + c.dateEnd + '\n';
            // TODO: possibly modify this so there isn't only two mf things in the description

            for (LocalDate i = c.dateStart; i.isBefore(c.dateEnd); i = i.plusDays(1)) {     // for every day from start to end
                int monthInt = i.getMonthValue();
                int dayInt = i.getDayOfMonth();
                String month = Integer.toString(monthInt);
                String day = Integer.toString(dayInt);

                if (monthInt < 10) {
                    month = "0" + month;
                }
                if (dayInt < 10) {
                    day = "0" + day;
                }

                String datetime = i.getYear() + "-" + month + "-" + day + " " + c.timeStart;
                String dotw = i.getDayOfWeek().toString();
                char dotwShort = ' ';
                if (dotw.equals("SUNDAY")) {                // again.. TEMPORARY, changes String to characters of Course days
                    dotwShort = 'U';
                } else if (dotw.equals("MONDAY")) {
                    dotwShort = 'M';
                } else if (dotw.equals("TUESDAY")) {
                    dotwShort = 'T';
                } else if (dotw.equals("WEDNESDAY")) {
                    dotwShort = 'W';
                } else if (dotw.equals("THURSDAY")) {
                    dotwShort = 'R';
                } else if (dotw.equals("FRIDAY")) {
                    dotwShort = 'F';
                } else if (dotw.equals("SATURDAY")) {
                    dotwShort = 'S';
                }

                if (c.dotw.contains(dotwShort + "")) {      // again.. VERY TEMPORARY
                    Database.addEvent(title, datetime, description, "", "", 70);
                }
            }
        }
        _cn.refreshChildren();
    }

    private void removeSchedule() {
        ArrayList<String[]> res = Database.getEventsByTag(70);

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
}