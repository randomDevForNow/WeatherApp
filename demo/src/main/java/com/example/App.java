package com.example;

import java.io.IOException;

import com.example.Controller.MapController;
import com.example.Controller.SearchController;
import com.example.Controller.InfoPanelController; // Import InfoPanelController
import com.example.Model.ConnectingModel; // Import ConnectingModel

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the root container for the scenes
            HBox root = new HBox();

            // Create an instance of the ConnectingModel
            ConnectingModel connectingModel = new ConnectingModel();

            // Load Map scene
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
            Parent mapPane = fxmlLoader.load();
            MapController mapController = fxmlLoader.getController();
            mapController.setModel(connectingModel); // Set the model for MapController

            // Load Info Panel scene (assuming it has its own FXML file)
            FXMLLoader infoPanelLoader = new FXMLLoader(App.class.getResource("InfoPanelView.fxml")); 
 
            Parent infoPanelPane = infoPanelLoader.load();
            InfoPanelController infoPanelController = infoPanelLoader.getController();
            infoPanelController.setModel(connectingModel); // Set the model for InfoPanelController

            // Load Search scene
            FXMLLoader searchLoader = new FXMLLoader(App.class.getResource("places_search.fxml"));
            Parent searchPane = searchLoader.load();
            SearchController searchController = searchLoader.getController();

            // Close the engine when the window is closing
            primaryStage.setOnCloseRequest(event -> {
                if (mapController.engine != null) {
                    mapController.engine.close(); // Ensure the engine is closed to free resources
                }
            });

            // Add both panes to the root container
            root.getChildren().addAll(mapPane, infoPanelPane, searchPane); // Add infoPanelPane to the root

            // Set up the main scene
            Scene scene = new Scene(root, 1280, 800);
            primaryStage.setTitle("JxBrowser JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for debugging
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage()); // Handle unexpected exceptions
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}