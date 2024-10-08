package com.example.Controller;

import javafx.util.Duration;

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
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MapController {

    /* FXML Elements */
    @FXML
    private BorderPane mapContainer;

    @FXML
    private Button boton;

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
    private String center;

    @FXML
    public void initialize() {
        setupBrowser();
        addListeners(); // change move to setupBrowser

        appendScenes();
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
            // ConsoleMessageLevel level = consoleMessage.level();
            String message = consoleMessage.message();
            // Set if else statements here
            if (message.equals("123")) {
                showButtonWithAnimation();
            } else {
                center = message;
            }
        });
    }

    // TO FIX!!!!!!!!!
    private void showButtonWithAnimation() {
        // Create fade-in transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), boton);

        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Set the action for when the button is clicked
        boton.setOnMouseClicked(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), boton);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                boton.setOpacity(0);
                boton.setDisable(true); // Disable to prevent further clicks
            });
            fadeOut.play();
        });

        fadeIn.setOnFinished(event -> {
            // After the button is shown, enable it (in case it's disabled)
            boton.setDisable(false);
        });

        fadeIn.play();
    }

    @FXML
    private void getMapCen() {
        // change modify js code
        window.call("hey");

        // ERROR CHECKING 1
        String[] coords = center.split(" ");

        if (coords.length == 2) {
            try {
                double lat = Double.parseDouble(coords[0].trim());
                double lng = Double.parseDouble(coords[1].trim());

                System.out.println("Latitude: " + lat);
                System.out.println("Longitude: " + lng);
            } catch (NumberFormatException e) {
                System.out.println("Error: Failed to parse latitude or longitude.");
            }
        } else {
            System.out.println("Error: Unexpected format for coordinates.");
        }

    }

    private void appendScenes() {

        mapContainer.setCenter(view);
    }
}
