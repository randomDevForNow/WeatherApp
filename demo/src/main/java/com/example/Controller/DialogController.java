package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class DialogController {

    @FXML
    private CheckBox locationCheckBox;  
    @FXML
    private Button getStartedButton;   

    @FXML
    public void initialize() {
        getStartedButton.setOnAction(event -> {
            if (locationCheckBox.isSelected()) {
                openNextWindow();
            } else {
                System.out.println("Checkbox not selected, cannot proceed.");
            }
        });
    }
    private void openNextWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FirstWindow.fxml"));
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
