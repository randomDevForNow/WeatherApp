package com.example;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import com.teamdev.jxbrowser.engine.RenderingMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize Chromium engine with your license key.
        EngineOptions options = EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                .licenseKey(
                        "3GC4U6A49ZRMTY18LZW7ZC38WH3GH616N56D9C0XYMRRBQI1YSMJFHMUE6DZ93AQBLRMHJHHB8DVI9E3YN57EGGPUJJFM4XHAE48SGNMERVOAI450A5TR13PEBP9T07Y9DAVON7E9XKU7VEN")
                .build();
        Engine engine = Engine.newInstance(options);

        // Create a Browser instance.
        Browser browser = engine.newBrowser();

        // Load the local HTML file containing the Google Maps API integration.
        String htmlFilePath = App.class.getResource("index.html").toExternalForm();
        browser.navigation().loadUrl(htmlFilePath);

        // Create and embed JavaFX BrowserView component to display web content.
        BrowserView view = BrowserView.newInstance(browser);

        Scene scene = new Scene(new BorderPane(view), 1280, 800);
        primaryStage.setTitle("JxBrowser JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Shutdown Chromium and release allocated resources when the application is
        // closed.
        primaryStage.setOnCloseRequest(event -> engine.close());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
