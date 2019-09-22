package edu.buffalo.cse442.nullterminators;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static void runMe(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Calendar Application");
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("main_style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Notification notification_demo = new Notification();
        notification_demo.setTitle("Project Grading");
        notification_demo.setDatetime("9/23/2019 3:30:00 PM");
        notification_demo.setDescription("Having our project graded by a super cool TA");
        notification_demo.showDialog();
    }
}