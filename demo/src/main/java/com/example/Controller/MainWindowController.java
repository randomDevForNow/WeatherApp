package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindowController {
    @FXML
    private BorderPane mapContainer;
    @FXML
    private VBox infoPanelContainer;
    @FXML
    private HBox searchContainer;

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
