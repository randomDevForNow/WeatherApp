package com.example.Controller;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
//import com.example.Model.ConnectingModel;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MapController {

    // private ConnectingModel model;

    @FXML
    private BorderPane mapContainer; // Assuming a BorderPane is defined in FXML to hold the map

    // Initialize Chromium engine with your license key.
    private EngineOptions options = EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
            .licenseKey(
                    "3GC4U6A49ZRMTY18LZW7ZC38WH3GH616N56D9C0XYMRRBQI1YSMJFHMUE6DZ93AQBLRMHJHHB8DVI9E3YN57EGGPUJJFM4XHAE48SGNMERVOAI450A5TR13PEBP9T07Y9DAVON7E9XKU7VEN")
            .build();

    public Engine engine = Engine.newInstance(options);

    /*
     * public void setModel(ConnectingModel model) {
     * this.model = model;
     * }
     */

    @FXML
    public void initialize() {

        // Create a Browser instance.
        Browser browser = engine.newBrowser();

        // Load the local HTML file containing the Google Maps API integration.
        String htmlFilePath = getClass().getResource("/com/example/index.html").toExternalForm();
        browser.navigation().loadUrl(htmlFilePath);

        // Create and embed JavaFX BrowserView component to display web content.
        BrowserView view = BrowserView.newInstance(browser);

        // Add the BrowserView to the mapContainer defined in FXML
        mapContainer.setCenter(view);

    }
}
