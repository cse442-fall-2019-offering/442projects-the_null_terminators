package edu.buffalo.cse442.nullterminators;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    static void runMe(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Calendar Application");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("main_style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        /*
        Notification notification_demo = new Notification();
        notification_demo.setTitle("Project Grading");
        notification_demo.setDatetime("9/23/2019 3:30:00 PM");
        notification_demo.setDescription("Having our project graded by a super cool TA");
        notification_demo.showDialog();

        */
    }
}