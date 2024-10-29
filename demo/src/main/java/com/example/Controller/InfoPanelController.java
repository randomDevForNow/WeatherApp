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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InfoPanelController {

    /* FXML Elements */
    @FXML
    private HBox filterListContainer = new HBox();

    @FXML
    private ListView<PlaceModel> placeList = new ListView<>();

    /* FXML Elements */

    private ConnectingModel model;

    @FXML
    private WeatherController weatherController;

    @FXML
    private SearchController searchController;

    // User Variables
    private WeatherModel weather;
    private PlaceFilter placeFilter;
    private PlaceFetcher placeFetcher;

    @FXML
    public void initialize() {
        // Initialize the WeatherController and other components
        weatherController = new WeatherController();
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
            fetchPlacesBasedOnWeather();
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

    private void setFilterList(List<PlaceModel> placesData) {
        Platform.runLater(() -> {
            filterListContainer.getChildren().clear();

            final ToggleGroup toggleGroup = new ToggleGroup();

            if (PlaceFilter.placeTypes != null) {
                for (int i = 0; i < PlaceFilter.placeTypes.length; i++) {
                    String placeType = PlaceFilter.placeTypes[i];
                    ToggleButton toggleButton = new ToggleButton(placeType);

                    // Set ToggleButton Styling
                    toggleButton.setBackground(
                            new Background(new BackgroundFill(
                                    Color.rgb(80, 184, 231),
                                    new CornerRadii(7),
                                    null)));
                    toggleButton.setTextFill(Color.WHITE);
                    toggleButton.setPadding(new Insets(5, 5, 5, 5));

                    toggleButton.setOnAction(event -> {
                        if (toggleButton.isSelected()) {
                            System.out.println("Selected: " + placeType);

                            // Clear current place list before adding new places
                            resetPlaceList(); // Reset the place list

                            // Filter placesData based on the selected place type
                            List<PlaceModel> filteredPlaces = filterPlacesByType(placesData, placeType);
                            setPlaceList(filteredPlaces, new String[] { placeType }); // Call setPlaceList with filtered
                                                                                      // data
                        } else {
                            System.out.println("Deselected: " + placeType);
                            // Optionally handle deselection logic here if needed
                        }
                    });

                    // Add ToggleButton to the HBox inside the ScrollPane
                    filterListContainer.getChildren().add(toggleButton);
                }

                // Automatically select the first ToggleButton and trigger its action
                if (!filterListContainer.getChildren().isEmpty()) {
                    ToggleButton firstButton = (ToggleButton) filterListContainer.getChildren().get(0);
                    firstButton.setSelected(true); // Set the first ToggleButton to selected
                    firstButton.fire(); // Trigger its action
                }
            }
        });
    }

    private void resetPlaceList() {
        placeList.getItems().clear();
    }

    private List<PlaceModel> filterPlacesByType(List<PlaceModel> placesData, String placeType) {
        // Filter the placesData based on the placeType
        return placesData.stream()
                .filter(place -> place.getTypes() != null && place.getTypes().contains(placeType))
                .collect(Collectors.toList());
    }

    private void setPlaceList(List<PlaceModel> placesData, String[] filters) {
        Platform.runLater(() -> {
            placeList.getItems().clear();
            placeList.getItems().addAll(placesData);
            System.out.println(placeList.getItems().size());
        });
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
                    cellLayout.setStyle("-fx-padding: 10; ");

                    cellLayout.setPrefHeight(140);
                    cellLayout.setPrefWidth(180);
                    cellLayout.setStyle("--fx-border-color: #e0e0e0;");

                    // Create an empty ImageView fo r the place photo
                    HBox imageViewContainer = new HBox();
                    imageViewContainer.setPrefSize(60, 140);
                    imageViewContainer.setStyle("-fx-background-color: transparent; -fx-clip: auto;");

                    ImageView coverImage = new ImageView();

                    coverImage.setFitHeight(140);
                    coverImage.setFitWidth(110);
                    coverImage.setPreserveRatio(false);

                    imageViewContainer.getChildren().addAll(coverImage);

                    VBox infoBox = new VBox(5);
                    infoBox.setAlignment(Pos.CENTER_LEFT);
                    Label nameLabel = new Label(place.getName());
                    nameLabel.setWrapText(true);
                    nameLabel.setStyle(
                            "-fx-font-family: \"Arial\"; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight:bold; ");

                    Label typeLabel = new Label(String.join(", ", place.getTypes()));
                    typeLabel.setWrapText(true);
                    typeLabel.setStyle("-fx-font-family: \"Arial\"; -fx-text-fill: #50b8e7; -fx-font-weight:bold;");

                    Label ratingLabel = new Label("Rating: " + place.getRating() + " ★");
                    ratingLabel.setWrapText(true);
                    ratingLabel.setStyle("-fx-font-family: \"Arial\"; -fx-text-fill: #FFAA1D; ");

                    Label vicinityLabel = new Label(place.getVicinity());
                    vicinityLabel.setWrapText(true);
                    vicinityLabel.setStyle("-fx-font-family: \"Arial\"; -fx-text-fill: black; ");

                    infoBox.getChildren().addAll(nameLabel, typeLabel, ratingLabel, vicinityLabel);
                    VBox.setVgrow(nameLabel, Priority.ALWAYS);

                    cellLayout.getChildren().addAll(imageViewContainer, infoBox);
                    setGraphic(cellLayout);

                    if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                        String photoReference = place.getPhotos().get(0).getPhoto_reference();
                        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                + photoReference + "&key=AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";
                        new Thread(() -> {
                            try {
                                Image image = new Image(photoUrl);
                                Platform.runLater(() -> coverImage.setImage(image));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();

                        Rectangle clip = new Rectangle();
                        clip.setWidth(60);
                        clip.setHeight(140);

                        cellLayout.setOnMouseEntered(event -> {
                            if (!isSelected()) {
                                cellLayout.setBackground(
                                        new Background(
                                                new BackgroundFill(Color.rgb(220, 240, 250), CornerRadii.EMPTY, null)));
                            }
                        });
                        cellLayout.setOnMouseExited(event -> {
                            if (!isSelected()) {
                                cellLayout.setBackground(
                                        new Background(
                                                new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, null)));
                            }
                        });
                        if (isSelected()) {
                            cellLayout.setStyle("-fx-border-color: #ffffff");
                            cellLayout.setBackground(new Background(new BackgroundFill(
                                    Color.rgb(220, 240, 250), CornerRadii.EMPTY, null)));
                            nameLabel.setTextFill(Color.rgb(0, 0, 0));

                            typeLabel.setTextFill(Color.rgb(0, 0, 0));
                            ratingLabel.setTextFill(Color.rgb(0, 0, 0));
                            vicinityLabel.setTextFill(Color.rgb(0, 0, 0));
                            model.setSelectedCoordinates(place.getGeometry().getLocation().getLat(),
                                    place.getGeometry().getLocation().getLng());
                        } else {
                            cellLayout.setBackground(new Background(new BackgroundFill(
                                    Color.rgb(255, 255, 255), CornerRadii.EMPTY, null)));

                        }

                        cellLayout.setOnMouseClicked(event -> {
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

}
