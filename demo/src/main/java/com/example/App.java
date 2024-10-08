package com.example;

import java.io.IOException;

import com.example.Controller.MapController;
import com.example.Controller.VideoController;
import com.example.Controller.InfoPanelController;
import com.example.Controller.SearchController;
import com.example.Model.ConnectingModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the root container for the scenes
            VBox root = new VBox(); // Use VBox to vertically align the Map and Info sections
            HBox mapAndVideoContainer = new HBox(); // Use HBox to align map and video next to each other
            HBox searchAndInfoContainer = new HBox(); // Use HBox for search and info panel

            // Create an instance of the ConnectingModel
            ConnectingModel connectingModel = new ConnectingModel();

            // Load Map scene
            FXMLLoader mapLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
            Parent mapPane = mapLoader.load();
            MapController mapController = mapLoader.getController();
            mapController.setModel(connectingModel); // Set the model for MapController

            // Load Video scene
            FXMLLoader videoLoader = new FXMLLoader(App.class.getResource("VideoView.fxml"));
            Parent videoPane = videoLoader.load();
            VideoController videoController = videoLoader.getController();

            // Fetch the initial video using a default location and weather condition
            String defaultLocation = "Manila"; // Example: default location is Manila
            String defaultWeather = "sunny"; // Example: default weather condition
            videoController.fetchAndPlayVideo(defaultLocation, defaultWeather);

            // Load Search scene
            FXMLLoader searchLoader = new FXMLLoader(App.class.getResource("places_search.fxml"));
            Parent searchPane = searchLoader.load();
            SearchController searchController = searchLoader.getController();
            // (Add any necessary initializations for SearchController)

            // Load Info Panel scene
            FXMLLoader infoPanelLoader = new FXMLLoader(App.class.getResource("InfoPanelView.fxml"));
            Parent infoPanelPane = infoPanelLoader.load();
            InfoPanelController infoPanelController = infoPanelLoader.getController();
            infoPanelController.setModel(connectingModel); // Set the model for InfoPanelController

            // Add Map and Video Panes to the mapAndVideoContainer
            mapAndVideoContainer.getChildren().addAll(mapPane, videoPane);

            // Add Search and Info Panel Panes to searchAndInfoContainer
            searchAndInfoContainer.getChildren().addAll(searchPane, infoPanelPane);

            // Add the two HBox containers to the root VBox
            root.getChildren().addAll(mapAndVideoContainer, searchAndInfoContainer);

            // Set up the main scene
            Scene scene = new Scene(root, 1280, 800);
            primaryStage.setTitle("Weather and Location-Based Video App");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Listen for changes in coordinates and update video
            connectingModel.addListener((x, y) -> {
                String updatedLocation = "Some City"; // You should have a method to translate coordinates to a city name
                String currentWeather = "sunny"; // Replace with current weather condition
                videoController.fetchAndPlayVideo(updatedLocation, currentWeather);
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
