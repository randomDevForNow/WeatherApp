package com.example;

import java.io.IOException;

import com.example.Controller.DialogController;
import com.example.Controller.MainWindowController;
import com.example.Controller.InfoPanelController;
import com.example.Controller.MapController;
import com.example.Controller.SearchController; // Import InfoPanelController
import com.example.Model.ConnectingModel; // Import ConnectingModel

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


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

    public void setMain(Stage stage) throws IOException {
    System.out.println("Opening next window...");
    
    // Create an instance of the ConnectingModel
    ConnectingModel connectingModel = new ConnectingModel();

    // Load FirstWindow.fxml
    FXMLLoader firstWindowLoader = new FXMLLoader(App.class.getResource("MainWindow.fxml"));
    Parent firstWindowPane = firstWindowLoader.load();
    
    // Get the controller for FirstWindow
    MainWindowController firstWindowController = firstWindowLoader.getController();

    // Load MapView.fxml and set the model
    FXMLLoader mapLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
    Parent mapPane = mapLoader.load();
    MapController mapController = mapLoader.getController();
    mapController.setModel(connectingModel);
    
    // Load InfoPanelView.fxml and set the model
    FXMLLoader infoPanelLoader = new FXMLLoader(App.class.getResource("InfoPanelView.fxml"));
    Parent infoPanelPane = infoPanelLoader.load();
    InfoPanelController infoPanelController = infoPanelLoader.getController();
    infoPanelController.setModel(connectingModel);
    
    // Load places_search.fxml
    FXMLLoader searchLoader = new FXMLLoader(App.class.getResource("places_search.fxml"));
    Parent searchPane = searchLoader.load();

    // Assuming you have VBox or similar containers in FirstWindow.fxml
    // Add the children to their respective containers
    firstWindowController.getMapContainer().getChildren().add(mapPane);
    firstWindowController.getInfoPanelContainer().getChildren().add(infoPanelPane);
    firstWindowController.getSearchContainer().getChildren().add(searchPane);

    // Create the scene and set it in the stage
    Scene scene = new Scene(firstWindowPane);
    stage.setScene(scene);
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    // Calculate the center position
    double x = (screenBounds.getWidth() - stage.getWidth()) / 2;
    double y = (screenBounds.getHeight() - stage.getHeight()) / 2;

    // Set the position of the stage
    stage.setX(x);
    stage.setY(y);
    stage.show();

}


    public static void main(String[] args) {
        launch(args); // Launch the application
    }
}