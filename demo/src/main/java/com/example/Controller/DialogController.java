package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import javafx.scene.input.MouseEvent;

public class DialogController {
    @FXML
    private CheckBox locationCheckBox;  
    @FXML
    private Button getStartedButton;   
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;

    private double offsetX; 
    private double offsetY; 

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
    }
   

    private void initButtonEvent() {
        System.out.println("Initializing button event...");
        getStartedButton.setOnAction(event -> {
            openNextWindow();
            System.out.println("Working");
        });
    }

    private void openNextWindow() {
        try {
            System.out.println("Hellows");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/FirstWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            Stage currentStage = (Stage) getStartedButton.getScene().getWindow();

            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
