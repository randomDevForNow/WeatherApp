package com.example.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.Model.ConnectingModel;
import com.example.Model.PlaceFetcher;
import com.example.Model.PlaceFilter;
import com.example.Model.PlaceModel;
import com.example.Model.WeatherModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class InfoPanelController {

    /* FXML Elements */
    @FXML
    private HBox filterListContainer = new HBox();

    @FXML
    private ListView<PlaceModel> placeList = new ListView<>(); // ListView for displaying PlaceModel objects
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
        model.addCoordinateListener((x, y) -> {
            weatherController.updateWeather(x, y);
            weather = weatherController.getWeather();

            // Fetch places based on the updated weather
            fetchPlacesBasedOnWeather(); // change move this to init or somewhere
        });
    }

    private void fetchPlacesBasedOnWeather() {
        // Build place queries using the current weather data
        if (weather != null) {
            List<String> placeQueries = PlaceFilter.buildPlaceQueries(weather);
            List<PlaceModel> placesData = PlaceFetcher.fetchPlaces(placeQueries);

            if (model != null) {
                model.setPlacesData(placesData);
            }

            setFilterList(placesData);
        }
    }

    // FIRST THREAD EXAMPLE
    private void setFilterList(List<PlaceModel> placesData) {
        // Ensure this runs on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Clear previous items in the HBox
            filterListContainer.getChildren().clear();

            if (PlaceFilter.placeTypes != null) {
                for (int i = 0; i < PlaceFilter.placeTypes.length; i++) {
                    String placeType = PlaceFilter.placeTypes[i];
                    ToggleButton toggleButton = new ToggleButton(placeType);

                    // Set ToggleButton Styling
                    toggleButton.setBackground(
                            new Background(new BackgroundFill(Color.rgb(80, 184, 231), CornerRadii.EMPTY, null)));
                    toggleButton.setTextFill(Color.WHITE);
                    toggleButton.setPadding(new Insets(1, 2, 1, 2));

                    toggleButton.setOnAction(event -> {
                        // Handle toggle button selection/deselection
                        if (toggleButton.isSelected()) {
                            System.out.println("Selected: " + placeType);

                            // Filter placesData based on the selected place type
                            List<PlaceModel> filteredPlaces = filterPlacesByType(placesData, placeType);
                            setPlaceList(filteredPlaces, new String[] { placeType }); // Call setPlaceList with filtered
                                                                                      // data
                        } else {
                            System.out.println("Deselected: " + placeType);

                            // Reapply filters or show all places
                            List<PlaceModel> allPlaces = new ArrayList<>(placesData); // or however you fetch all places
                            setPlaceList(allPlaces, PlaceFilter.placeTypes); // Reset or apply remaining filters if
                                                                             // needed
                        }
                    });

                    // Add ToggleButton to the HBox inside the ScrollPane
                    filterListContainer.getChildren().add(toggleButton);

                    // Automatically select the first ToggleButton and trigger its action
                    if (i == 0) {
                        toggleButton.setSelected(true); // Set the first ToggleButton to selected
                        toggleButton.fire(); // Trigger its action
                    }
                }
            }
        });
    }

    private List<PlaceModel> filterPlacesByType(List<PlaceModel> placesData, String placeType) {
        // Filter the placesData based on the placeType
        return placesData.stream()
                .filter(place -> place.getTypes() != null && place.getTypes().contains(placeType)) // Check if types
                                                                                                   // list contains
                                                                                                   // placeType
                .collect(Collectors.toList());
    }

    private void setPlaceList(List<PlaceModel> placesData, String[] filters) {
        Platform.runLater(() -> {
            placeList.getItems().clear();
            placeList.getItems().addAll(placesData);
            System.out.println(placeList.getItems().size());
        });
    }

    // Scenes
    private void setupPanel() {
        // Setup logic for the panel if needed
    }

    // Listeners
    private void addListeners() {
        placeList.setCellFactory(param -> new ListCell<PlaceModel>() {
            @Override
            protected void updateItem(PlaceModel place, boolean empty) {
                super.updateItem(place, empty);
                if (empty || place == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox cellLayout = new HBox(10);
                    cellLayout.setPrefHeight(140); // Set preferred height if needed
                    cellLayout.setPrefWidth(180);
                    // Create an empty ImageView fo r the place photo
                    ImageView coverImage = new ImageView();
                    coverImage.setFitWidth(60);
                    coverImage.setFitHeight(130);
                    coverImage.setPreserveRatio(true); // Maintain the aspect ratio
                    
                    
                    Image loadingImage = new Image(getClass().getResourceAsStream("/com/example/icons/loadingImage.gif"));
                    coverImage.setImage(loadingImage);

                    VBox infoBox = new VBox(5);
                    Label nameLabel = new Label(place.getName());
                    nameLabel.setWrapText(true);
                    Label typeLabel = new Label(String.join(", ", place.getTypes()));
                    typeLabel.setWrapText(true);
                    Label ratingLabel = new Label("Rating: " + place.getRating());
                    ratingLabel.setWrapText(true);
                    Label vicinityLabel = new Label(place.getVicinity());
                    vicinityLabel.setWrapText(true);
                    infoBox.getChildren().addAll(nameLabel, typeLabel, ratingLabel, vicinityLabel);
                    VBox.setVgrow(nameLabel, Priority.ALWAYS); // Makes labels grow as necessary
                    
                    // Add image and info box to cell layout
                    cellLayout.getChildren().addAll(coverImage, infoBox);
                    setGraphic(cellLayout);

                    // If the place has photos, fetch the image in a background thread
                    if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                        String photoReference = place.getPhotos().get(0).getPhoto_reference();
                        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                + photoReference + "&key=AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8"; // Replace with your
                                                                                                   // API key

                        // Fetch the image in a separate thread
                        new Thread(() -> {
                            try {
                                Image image = new Image(photoUrl);
                                // Update the UI on the JavaFX Application Thread
                                Platform.runLater(() -> coverImage.setImage(image));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();

                        infoBox.setOnMouseEntered(event -> {
                            if (!isSelected()) {
                                infoBox.setBackground(
                                        new Background(new BackgroundFill(Color.rgb(220,240,250), CornerRadii.EMPTY, null)));
                            }
                        });
                        infoBox.setOnMouseExited(event -> {
                            if (!isSelected()) {
                                infoBox.setBackground(
                                        new Background(new BackgroundFill(Color.rgb(255,255,255), CornerRadii.EMPTY, null)));
                            }
                           });
                        if (isSelected()) {
                            // apply the yellow background and black text when selected
                            cellLayout.setStyle("-fx-border-color: #ffffff");
                            infoBox.setBackground(new Background(new BackgroundFill(
                                    Color.rgb(220,240,250), CornerRadii.EMPTY, null)));
                            nameLabel.setTextFill(Color.rgb(0, 0, 0));
                            typeLabel.setTextFill(Color.rgb(0, 0, 0));
                            ratingLabel.setTextFill(Color.rgb(0, 0, 0));    
                            vicinityLabel.setTextFill(Color.rgb(0, 0, 0));
                        } else {
                            // When not selected, reset background and text colors
                            infoBox.setBackground(new Background(new BackgroundFill(
                                    Color.rgb(255,255,255), CornerRadii.EMPTY, null)));
                        
                        }
    
                        // Set the click event to trigger selection
                        infoBox.setOnMouseClicked(event -> {
                            getListView().requestFocus();
                            getListView().getSelectionModel().select(getIndex());
                        });
                    }
                }
            }
        });
        placeList.setStyle(
            "-fx-control-inner-background: #ffffff; -fx-background-insets: 0; -fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;-fx-padding: 0; -fx-background-padding: 0; -fx-background-color: transparent; -fx-border-insets: 0; -fx-overflow-x: hidden; -fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;");
    
    
    }
    
    private void appendScenes() {
        // Logic to append scenes if needed
    }
}
