package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindowController {
    @FXML
    private BorderPane mapContainer;
    @FXML
    private VBox infoPanelContainer;
    @FXML
    private HBox searchContainer;
    @FXML
    private Button closeButton;
    @FXML
    private Pane weatherContainer;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private HBox videoContainer;

    
    public BorderPane getMapContainer() {
        return mapContainer;
    }

    public VBox getInfoPanelContainer() {
        return infoPanelContainer;
    }

    public HBox getSearchContainer() {
        return searchContainer;
    }

    public Pane getWeatherContainer(){
        return  weatherContainer;
    }



    @FXML
    public void initialize(){
        if (closeButton != null) {
            closeButton.setOnAction(event -> {
                System.out.println("Closing window...");
                Stage stage = (Stage) closeButton.getScene().getWindow(); 
                stage.close();
                System.exit(0);
            });
        }

        if (minimizeButton != null) {
            minimizeButton.setOnAction(event -> {
                System.out.println("Minimizing window...");
                Stage stage = (Stage) minimizeButton.getScene().getWindow(); 
                stage.setIconified(true);
            });
        }
        if (maximizeButton != null) {
            maximizeButton.setOnAction(event -> {
                System.out.println("Maximizing window...");
                Stage stage = (Stage) maximizeButton.getScene().getWindow();
                if (stage.isMaximized()) {
                    stage.setMaximized(false);
                } else {
                    stage.setMaximized(true);
                }
            });
        }
   
    }
}
