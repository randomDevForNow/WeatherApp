package com.example;

import java.io.IOException;

import com.example.Controller.DialogController;
import com.example.Controller.InfoPanelController;
import com.example.Controller.MapController;
import com.example.Controller.SearchController; // Import InfoPanelController
import com.example.Model.ConnectingModel; // Import ConnectingModel

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {


    @Override
    public void start(Stage stage) {
        try {

            
            // Create the root container for the scenes
            HBox root = new HBox();

            // Add both panes to the root container
            FXMLLoader fxmlLoader2 = new FXMLLoader(App.class.getResource("Dialog.fxml"));
            Parent pane = fxmlLoader2.load();
            DialogController controller = fxmlLoader2.getController();
            controller.someValueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    setMain(stage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
            root.getChildren().addAll(pane);

            //root.getChildren().addAll(mapPane, infoPanelPane, searchPane); // Add infoPanelPane to the root

            // Set up the main scene
            Scene scene = new Scene(root, 879, 544);
            scene.getStylesheets().add(getClass().getResource("windowStyles.css").toExternalForm());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("JxBrowser JavaFX");
            stage.setScene(scene);
            stage.show();
            controller.setDraggable2();
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for debugging
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage()); // Handle unexpected exceptions
            e.printStackTrace();
        }


    }

    public void setMain(Stage stage) throws IOException{
        System.out.println("Opening next window...");
            ConnectingModel connectingModel = new ConnectingModel();

            HBox root = new HBox();

            // Create an instance of the ConnectingModel
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
            Parent mapPane = fxmlLoader.load();
            MapController mapController = fxmlLoader.getController();
            mapController.setModel(connectingModel); // Set the model for MapController

            // Load Info Panel scene (assuming it has its own FXML file)
            FXMLLoader infoPanelLoader = new FXMLLoader(App.class.getResource("InfoPanelView.fxml")); 
 
            Parent infoPanelPane = infoPanelLoader.load();
            InfoPanelController infoPanelController = infoPanelLoader.getController();
            infoPanelController.setModel(connectingModel); // Set the model for InfoPanelController

            // Load Search scene
            FXMLLoader searchLoader = new FXMLLoader(App.class.getResource("places_search.fxml"));
            Parent searchPane = searchLoader.load();

            System.out.println(mapPane.getId());

            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/FirstWindow.fxml"));
            root.getChildren().addAll(mapPane, infoPanelPane, searchPane); // Add infoPanelPane to the root
            Scene scene = new Scene(root, 1500, 700);
            
            stage.setScene(scene);
            stage.show();
    
    }

    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}