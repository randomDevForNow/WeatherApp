package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Declaring Scenes
        HBox root = new HBox();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dialog.fxml"));
        Parent pane = fxmlLoader.load();

        root.getChildren().addAll(pane);

        Scene scene = new Scene(root, 879, 544);

        primaryStage.setTitle("JxBrowser JavaFX");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    // FXML Loader to modularize scenes
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}