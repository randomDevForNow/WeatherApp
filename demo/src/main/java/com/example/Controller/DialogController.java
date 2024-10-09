package com.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogController {
    @FXML
    private CheckBox locationCheckBox;  
    @FXML
    private Button getStartedButton;   
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    private double xOffset = 0; 
    private double yOffset = 0; 

    @FXML
    public void initialize() {
        System.out.println("Initializing dialog controller...");
        if (getStartedButton != null) {
            System.out.println("Button is not null");
              getStartedButton.setDisable(true);
            initButtonEvent();
            locationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                getStartedButton.setDisable(!newValue);
            });
        } else {
            System.out.println("Button is null");
        }

        if (closeButton != null) {
            closeButton.setOnAction(event -> {
                System.out.println("Closing window...");
                Stage stage = (Stage) closeButton.getScene().getWindow(); 
                stage.close(); 
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
    public void setDraggable2() {
        Node root = closeButton.getScene().getRoot();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void initButtonEvent() {
        System.out.println("Initializing button event...");
        getStartedButton.setOnAction(event -> {
            openNextWindow();
            System.out.println("Working");
        });
    }

    public void setDraggable(Node root, Stage stage) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    private void openNextWindow() {
        try {
            System.out.println("Opening next window...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/FirstWindow.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.UNDECORATED);
    
            setDraggable(root, newStage);
    

            newStage.show();
    
            
            Stage currentStage = (Stage) getStartedButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace(); 
    }
}
}