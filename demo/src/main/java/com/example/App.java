package com.example;

import java.io.IOException;

import com.example.Controller.MapController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*
         * only thing to do here is to call JavaFX Components and add Children
         * ====Hierarchy====
         * HBox (root)
         *   VBox (OpeningScreenController)
         *   ImageView
         *   Label
         *   HBox
         *     Checkbox
         *     Label
         *   Button
         *  BorderPane <1st Controller> (To be added are the video api which has <3rd Controller>) /
         *  VBox (InfoPanel) <2nd Controller>
         *      HBox (WeatherField)
         *      [Add to here later]
         *  HBox (SearchField)
         *      TextField
         *      Button
         *  VBox (SuggestionCtr)
         *      Label (Places near you)
         *      HBox
         *      Label (Tags Label)
         *      Label (Filters (For Loop))
         *  ListView
         *      HBox
         *      ImageView
         *      VBox
         *          Label (Title)
         *          HBox (Rating)
         *          Label (RatingsText)
         *          Label (Stars)
         *          Label (Address) (Opening Hours??)
         *      Label (Filters (For Loop))
         */

        // Declaring Scenes
        HBox root = new HBox();
        FXMLLoader mapLoader = new FXMLLoader(App.class.getResource("MapView.fxml"));
        Parent pane = mapLoader.load();

        MapController mapController = mapLoader.getController();

        primaryStage.setOnCloseRequest(event -> {
            if (mapController.engine != null) {
                mapController.engine.close(); // Close the engine when the window is closing
            }
        });

        



        /*
         * VBox infoPanel = (VBox) loadFXML("InfoPanelView");
         * HBox weatherField = (HBox) loadFXML("WeatherView");
         * HBox searchField = (HBox) loadFXML("SearchView");
         * VBox suggestionCtr = (VBox) loadFXML("SuggestionView");
         * ListView<String> placesList = (ListView<String>) loadFXML("PlacesListView");
         * 
         * // Adding Children
         * infoPanel.getChildren().addAll(weatherField, searchField, suggestionCtr,
         * placesList);
         */
        root.getChildren().addAll(pane);

        Scene scene = new Scene(root, 1280, 800);

        primaryStage.setTitle("JxBrowser JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // FXML Loader to modularize scenes
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
