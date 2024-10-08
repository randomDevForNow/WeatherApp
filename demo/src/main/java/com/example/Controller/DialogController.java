package com.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class DialogController {
    @FXML
    private CheckBox locationCheckBox;  
    @FXML
    private Button getStartedButton;   

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
            stage.show();

            Stage currentStage = (Stage) getStartedButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}