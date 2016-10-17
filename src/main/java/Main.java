/**
 * Implementatio - Main.java 16/10/16
 * <p>
 * Copyright 20XX Eleazar Díaz Delgado. All rights reserved.
 */
package main.java;

/**
 * TODO: Commenta algo
 *
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("Main.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("PushDown Automaton");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

