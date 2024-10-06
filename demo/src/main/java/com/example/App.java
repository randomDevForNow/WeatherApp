package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // create web engine and view

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.load(App.class.getResource("maps.html").toExternalForm());

        // create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(webView, 1000, 700, Color.web("#666970"));
        stage.setScene(scene);
        // show stage
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}