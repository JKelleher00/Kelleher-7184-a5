package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 John Kelleher
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class starter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TableViewer.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("First Page");
        primaryStage.show();
    }
}
