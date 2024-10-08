package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

import com.example.Model.ConnectingModel;
import com.example.Model.PlaceFetcher;
import com.example.Model.PlaceFilter;
import com.example.Model.PlaceModel;
import com.example.Model.WeatherModel;

public class InfoPanelController {

    /* FXML Elements */
    @FXML
    private BorderPane mapContainer;

    @FXML
    private Button boton;

    @FXML
    private ListView<PlaceModel> listView; // ListView for displaying PlaceModel objects
    /* FXML Elements */

    private ConnectingModel model;

    @FXML
    private WeatherController weatherController; // Injected from FXML

    @FXML
    private SearchController searchController; // Injected from FXML

    @FXML
    private ListViewController listViewController; // Injected from FXML

    // User Variables
    private WeatherModel weather;
    private PlaceFilter placeFilter;
    private PlaceFetcher placeFetcher;

    @FXML
    public void initialize() {
        // Initialize the WeatherController and other components
        weatherController = new WeatherController();
        listViewController = new ListViewController();
        placeFilter = new PlaceFilter();
        placeFetcher = new PlaceFetcher();
        addListeners();
    }

    public void setModel(ConnectingModel model) {
        this.model = model;

        // Listen for changes in coordinates and pass them to the WeatherController
        model.addListener((x, y) -> {
            weatherController.updateWeather(x, y);
            weather = weatherController.getWeather();

            // Fetch places based on the updated weather
            fetchPlacesBasedOnWeather();
        });
    }

    private void fetchPlacesBasedOnWeather() {
        // Build place queries using the current weather data
        if (weather != null) {
            List<String> placeQueries = PlaceFilter.buildPlaceQueries(weather);
            List<PlaceModel> placesData = PlaceFetcher.fetchPlaces(placeQueries);

            // Clear previous items from the ListView
            listView.getItems().clear();

            // Add the fetched places to the ListView
            listView.getItems().addAll(placesData);

        }
    }

    // Scenes
    private void setupPanel() {
        // Setup logic for the panel if needed
    }

    // Listeners
    private void addListeners() {
        listView.setCellFactory(param -> new ListCell<PlaceModel>() {
            @Override
            protected void updateItem(PlaceModel place, boolean empty) {
                super.updateItem(place, empty);
                if (empty || place == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox cellLayout = new HBox(10);
                    cellLayout.setPrefHeight(100); // Set preferred height if needed

                    // Create ImageView for the place photo
                    /*
                     * ImageView coverImage = new ImageView();
                     * if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                     * String photoReference = place.getPhotos().get(0).getPhoto_reference();
                     * String photoUrl =
                     * "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                     * + photoReference + "&key=AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8"; // Replace
                     * with your
                     * // actual API key
                     * coverImage.setImage(new Image(photoUrl));
                     * coverImage.setFitHeight(100); // Set a height for the image
                     * coverImage.setPreserveRatio(true); // Maintain the aspect ratio
                     * }
                     */

                    VBox infoBox = new VBox(5);
                    Label nameLabel = new Label(place.getName());
                    Label typeLabel = new Label(String.join(", ", place.getTypes()));
                    Label ratingLabel = new Label("Rating: " + place.getRating());
                    Label vicinityLabel = new Label(place.getVicinity());

                    infoBox.getChildren().addAll(nameLabel, typeLabel, ratingLabel, vicinityLabel);
                    VBox.setVgrow(nameLabel, Priority.ALWAYS); // Makes labels grow as necessary

                    // Add image and info box to cell layout
                    // cellLayout.getChildren().addAll(coverImage, infoBox);
                    cellLayout.getChildren().addAll(infoBox);

                    // Set the graphic for the cell
                    setGraphic(cellLayout);
                }
            }
        });
    }

    private void appendScenes() {
        // Logic to append scenes if needed
    }
}
