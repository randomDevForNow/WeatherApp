package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FirstWindowController {
    @FXML
    private Button minimizeButton;

    @FXML
    public void initialize() {
        if (minimizeButton != null) {
            minimizeButton.setOnAction(event -> {
                System.out.println("Minimizing window...");
                Stage stage = (Stage) minimizeButton.getScene().getWindow();
                stage.setIconified(true);
            });
        } else {
            System.out.println("Minimize button is null");
        }
    }
}
