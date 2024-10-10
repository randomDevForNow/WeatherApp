package com.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.Controller.VideoController;

public class MainWindowController {
    @FXML
    private BorderPane mapContainer;
    @FXML
    private VBox infoPanelContainer;
    @FXML
    private HBox searchContainer;
    
    @FXML
    private HBox videoContainer; // Reference to the container for the video

    @FXML
    private VideoController videoController; // Add reference to VideoController

    @ FXML
    public void initialize() {
        // Manually load the VideoView.fxml to ensure the VideoController is instantiated
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/VideoView.fxml"));
            VBox videoView = loader.load();
            videoController = loader.getController();

            if (videoController != null) {
                System.out.println("VideoController successfully integrated.");
            } else {
                System.err.println("Failed to initialize VideoController.");
            }

            // Add the loaded VideoView to the videoContainer
            videoContainer.getChildren().add(videoView);

        } catch (IOException e) {
            System.err.println("Error loading VideoView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to trigger video update
    public void updateVideoBasedOnLocationAndWeather(String location, String weatherCondition) {
        if (videoController != null) {
            videoController.fetchAndPlayVideo(location, weatherCondition);
        }
    }

    public BorderPane getMapContainer() {
        return mapContainer;
    }

    public VBox getInfoPanelContainer() {
        return infoPanelContainer;
    }

    public HBox getSearchContainer() {
        return searchContainer;
    }

}
