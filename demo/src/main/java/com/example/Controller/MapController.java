package com.example.Controller;

import javafx.util.Duration;

import com.example.DistanceCalculator;
import com.example.Model.ConnectingModel;
import com.example.Model.PlaceModel;
import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.event.ConsoleMessageReceived;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.frame.Frame;
import com.teamdev.jxbrowser.js.ConsoleMessage;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.view.javafx.BrowserView;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MapController {

    /* FXML Elements */
    @FXML
    private BorderPane mapContainer;

    /* FXML Elements */

    // Using Chromium Engine as WebView has no hardware accel
    private EngineOptions options = EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
            .licenseKey(
                    "3GC4U6A49ZRMTY18LZW7ZC38WH3GH616N56D9C0XYMRRBQI1YSMJFHMUE6DZ93AQBLRMHJHHB8DVI9E3YN57EGGPUJJFM4XHAE48SGNMERVOAI450A5TR13PEBP9T07Y9DAVON7E9XKU7VEN")
            .build();

    // Main needs Access to close properly
    public Engine engine = Engine.newInstance(options);

    // JxBrowser Variables
    private Browser browser;
    private Frame frame;
    private JsObject window;
    private BrowserView view;

    // User Variables
    private ConnectingModel model;
    private double lat;
    private double lng;

    @FXML
    public void initialize() {
        setupBrowser();
        addListeners(); // change move to setupBrowser
        appendScenes();

    }

    public void setModel(ConnectingModel model) {
        this.model = model; // Set the model

        model.addPlaceListener(placesData -> {
            removeMarkers();
            // Loop through the placesData and create markers
            for (PlaceModel place : placesData) {
                createMarkerForPlace(place);
            }
        });
    }

    private void removeMarkers() {

    }

    private void createMarkerForPlace(PlaceModel place) {
        String jsFunction = String.format(
                "createMarker('%s', '%s', %f, %f, %f,'%s', '%s', %.1f, '%s')",
                place.getName(),
                place.getPhotoUrl(),
                place.getGeometry().getLocation().getLat(),
                place.getGeometry().getLocation().getLng(),
                DistanceCalculator.getDistance(place, lat, lng),
                place.getVicinity(),
                place.getTypes().toString(), // change to FIX
                place.getRating(),
                "Open");
        // Execute the JavaScript function in the frame
        frame.executeJavaScript(jsFunction);

    }

    // compute for distance

    private void goToMarker() {

    }

    private void setupBrowser() {

        browser = engine.newBrowser();
        browser.navigation().loadUrl(getClass().getResource("/com/example/index.html").toExternalForm());
        frame = browser.mainFrame().get();
        view = BrowserView.newInstance(browser);
        window = frame.executeJavaScript("window");
    }

    private void addListeners() {
        browser.on(ConsoleMessageReceived.class, event -> {
            ConsoleMessage consoleMessage = event.consoleMessage();
            String message = consoleMessage.message();

            // Split the message by space
            String[] parts = message.split("\\s+");

            // Check if the message starts with the code "123"
            if (parts.length >= 3 && parts[0].equals("coor")) {
                // Get latitude (2nd string) and longitude (3rd string)
                String latitude = parts[1];
                String longitude = parts[2];

                // Call the method to show the button with animation
                getMapCen(latitude, longitude);

            }
            // other code
            /*
             * else if {
             * 
             * }
             */
        });
    }

    private void getMapCen(String latitude, String longitude) {

        lat = Double.parseDouble(latitude);
        lng = Double.parseDouble(longitude);

        model.setCenterCoordinates(lat, lng);
    }

    private void appendScenes() {
        mapContainer.setCenter(view);
    }
}
