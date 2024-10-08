package com.example.Controller;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.js.ConsoleMessageLevel;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MapController {

    @FXML
    private BorderPane mapContainer; // Assuming a BorderPane is defined in FXML to hold the map

    // Initialize Chromium engine with your license key.
    private EngineOptions options = EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
            .licenseKey(
                    "3GC4U6A49ZRMTY18LZW7ZC38WH3GH616N56D9C0XYMRRBQI1YSMJFHMUE6DZ93AQBLRMHJHHB8DVI9E3YN57EGGPUJJFM4XHAE48SGNMERVOAI450A5TR13PEBP9T07Y9DAVON7E9XKU7VEN")
            .build();

    public Engine engine = Engine.newInstance(options);
    public Browser browser;
    double[] coordinates = new double[2];

    @FXML
    private Button boton;

    @FXML
    private void getMapCen() {
        browser.on(ConsoleMessageReceived.class, event -> {
            ConsoleMessage consoleMessage = event.consoleMessage();
            ConsoleMessageLevel level = consoleMessage.level();
            String message = consoleMessage.message();
            System.out.println(message);
        });
    }

    @FXML
    public void initialize() {
        // Create a Browser instance.
        browser = engine.newBrowser();

        // Load the local HTML file containing the Google Maps API integration.
        String htmlFilePath = getClass().getResource("/com/example/index.html").toExternalForm();
        browser.navigation().loadUrl(htmlFilePath);

        // Create and embed JavaFX BrowserView component to display web content.
        BrowserView view = BrowserView.newInstance(browser);

        // Add the BrowserView to the mapContainer defined in FXML
        mapContainer.setCenter(view);
    }
}
