package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FirstWindowController {
    @FXML private Button minimizeButton;
    @FXML private Text proxLabel;
    @FXML private Button changeProxLabel;

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


///////////////////////////////////////////////////
        int ctr = 1;
        if (changeProxLabel != null) {
            ctr ++;
        } 
        else {
            System.out.println("changeProxLabel is not clicked");
        }

        if (ctr%2 != 0) {
            proxLabel.setText( "near me");
        }
        else {
            proxLabel.setText( "anywhere");
        }


    }

}
