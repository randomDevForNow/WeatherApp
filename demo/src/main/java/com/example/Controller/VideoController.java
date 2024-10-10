package com.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class VideoController {
    @FXML
    private WebView videoWebView;

    @FXML
    public void initialize() {
        System.out.println("VideoController initialized");
        String embeddedUrl = "https://www.youtube.com/embed/T9v23AayXU0?autoplay=1&mute=1";
        
        Platform.runLater(() -> {
            WebEngine webEngine = videoWebView.getEngine();
            webEngine.load(embeddedUrl);
        });
    }
}
