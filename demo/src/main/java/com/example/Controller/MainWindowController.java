package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainWindowController {
    @FXML
    private BorderPane mapContainer;
    @FXML
    private VBox infoPanelContainer;
    @FXML
    private HBox searchContainer;
    
    @FXML
    private HBox videoContainer; // Container for the video

    @FXML
    private VideoController videoController; // Reference to VideoController

    @FXML
    public void initialize() {
        // Initialization logic for MainWindowController
        System.out.println("MainWindowController initialized");
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
