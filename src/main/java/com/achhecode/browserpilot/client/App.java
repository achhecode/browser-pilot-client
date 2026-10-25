
package com.achhecode.browserpilot.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private PrimaryController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("primary.fxml"));

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root, 950, 900);

        stage.setTitle("Linkedin Puzzle Solver");
        stage.setMinWidth(720);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
