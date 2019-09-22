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
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1600);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(800);
        Scene scene = new Scene(root);
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