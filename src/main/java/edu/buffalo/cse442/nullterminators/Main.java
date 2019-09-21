package edu.buffalo.cse442.nullterminators;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        Notification notification_demo = new Notification();
        notification_demo.setTitle("Project Grading");
        notification_demo.setDatetime("9/23/2019 3:30:00 PM");
        notification_demo.setDescription("Having our project graded by a super cool TA");
        notification_demo.showDialog();
    }
}
