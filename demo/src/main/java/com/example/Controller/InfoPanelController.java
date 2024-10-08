package com.example.Controller;

import javafx.fxml.FXML;

import com.example.Model.ConnectingModel;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class InfoPanelController {

    /* FXML Elements */
    @FXML
    private BorderPane mapContainer;

    @FXML
    private Button boton;
    /* FXML Elements */

    private ConnectingModel model;

    @FXML
    private WeatherController weatherController; // Injected from FXML

    @FXML
    private SearchController searchController; // Injected from FXML

    @FXML
    private ListViewController listViewController; // Injected from FXML

    // User Variables

    @FXML
    public void initialize() {
        // methods
        weatherController = new WeatherController();
    }

    public void setModel(ConnectingModel model) {
        this.model = model;

        // Listen for changes in coordinates and pass them to the WeatherController
        model.addListener((x, y) -> {
            weatherController.updateWeather(x, y);
            // Additional logic to update search and list view if needed
        });
    }

    // Scenes
    private void setupPanel() {

    }

    // Listeners
    private void addListeners() {

    }

    private void appendScenes() {

    }
}
