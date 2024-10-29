package com.example;

import java.io.IOException;

import com.example.Controller.DialogController;
import com.example.Controller.MainWindowController;
import com.example.Controller.InfoPanelController;
import com.example.Controller.MapController;
import com.example.Model.ConnectingModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
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
                    e.printStackTrace();
                }
            });
            root.getChildren().addAll(pane);

            Scene scene = new Scene(root, 879, 544);
            scene.getStylesheets().add(getClass().getResource("windowStyles.css").toExternalForm());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("JxBrowser JavaFX");
            stage.setScene(scene);
            stage.show();
            controller.setDraggable2();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setMain(Stage stage) throws IOException {
        System.out.println("Opening next window...");

        ConnectingModel connectingModel = new ConnectingModel();

        FXMLLoader firstWindowLoader = new FXMLLoader(App.class.getResource("MainWindow.fxml"));
        Parent firstWindowPane = firstWindowLoader.load();

        MainWindowController firstWindowController = firstWindowLoader.getController();

        FXMLLoader mapLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
        Parent mapPane = mapLoader.load();
        MapController mapController = mapLoader.getController();
        mapController.setModel(connectingModel);

        FXMLLoader infoPanelLoader = new FXMLLoader(App.class.getResource("InfoPanelView.fxml"));
        Parent infoPanelPane = infoPanelLoader.load();
        InfoPanelController infoPanelController = infoPanelLoader.getController();
        infoPanelController.setModel(connectingModel);

        FXMLLoader weatherLoader = new FXMLLoader(App.class.getResource("weather.fxml"));
        Parent weatherPane = weatherLoader.load();

        FXMLLoader searchLoader = new FXMLLoader(App.class.getResource("places_search.fxml"));
        Parent searchPane = searchLoader.load();

        firstWindowController.getWeatherContainer().getChildren().addAll(weatherPane);
        firstWindowController.getMapContainer().getChildren().add(mapPane);
        firstWindowController.getInfoPanelContainer().getChildren().add(infoPanelPane);
        firstWindowController.getSearchContainer().getChildren().add(searchPane);

        Scene scene = new Scene(firstWindowPane);
        scene.getStylesheets().add(getClass().getResource("windowStyles.css").toExternalForm());
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double x = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double y = (screenBounds.getHeight() - stage.getHeight()) / 2;

        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}