package com.example.Controller;

import com.example.Model.ConnectingModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SearchController {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> resultsListView;
    @FXML
    private Button searchButton;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ConnectingModel model;

    public void setModel(ConnectingModel model) {
        this.model = model;
    }

    public void initialize() {
        searchField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String query = searchField.getText();
                if (!query.isEmpty()) {
                    // Call a method to fetch suggestions based on the query
                    fetchPlaceSuggestions(query);
                } else {
                    // Clear the ListView if the search field is empty
                    clearResultsListView();
                }
            }
        });

        // Add listener for item clicks in the ListView
        resultsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update search field with the selected place name
                searchField.setText(newValue);
                // Removed selectedPlaceId logic
            }
        });

        searchButton.setOnAction(event -> {
            String selectedPlace = searchField.getText();

            resultsListView.setVisible(false);
            resultsListView.setManaged(false);

            if (selectedPlace != null && !selectedPlace.isEmpty()) {
                System.out.println("Search button pressed for: " + selectedPlace);

                new Thread(() -> {
                    try {
                        String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";

                        // Properly encode place name for the URL
                        String encodedPlace = java.net.URLEncoder.encode(selectedPlace,
                                StandardCharsets.UTF_8.toString());
                        String urlString = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                                + encodedPlace + "&inputtype=textquery&fields=geometry&key=" + apiKey;

                        // Make network request to fetch place details (longitude and latitude)
                        URL url = new URL(urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");

                        if (connection.getResponseCode() == 200) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            JsonNode jsonResponse = objectMapper.readTree(response.toString());
                            JsonNode candidates = jsonResponse.get("candidates");

                            if (candidates != null && candidates.size() > 0) {
                                JsonNode location = candidates.get(0).get("geometry").get("location");
                                double latitude = location.get("lat").asDouble();
                                double longitude = location.get("lng").asDouble();

                                System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
                                model.setCenterCoordinates(latitude, longitude);
                            } else {
                                System.out.println("No location data found for the selected place.");
                            }
                        } else {
                            System.out.println("Error fetching location data: " + connection.getResponseCode());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

    }

    private void fetchPlaceSuggestions(String query) {
        new Thread(() -> {
            try {
                String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";
                String encodedQuery = java.net.URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
                String urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + encodedQuery
                        + "&key=" + apiKey;

                // Make network request to fetch suggestions
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Check response code
                if (connection.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // Update ListView with the results
                    updateResultsListView(response.toString());
                } else {
                    System.out.println("Error: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateResultsListView(String jsonResponse) {
        Platform.runLater(() -> {
            resultsListView.getItems().clear();

            try {
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode predictionsArray = jsonNode.get("predictions");

                // Create Set to store unique addresses
                Set<String> uniqueAddresses = new HashSet<>();

                // Iterate over the predictions array
                for (JsonNode predictionNode : predictionsArray) {
                    String placeName = predictionNode.get("description").asText();

                    placeName = correctEncoding(placeName);

                    String normalizedPlaceName = placeName.toLowerCase().trim();

                    if (!uniqueAddresses.contains(normalizedPlaceName)) {
                        uniqueAddresses.add(normalizedPlaceName);
                        resultsListView.getItems().add(placeName);
                    }
                }

                if (resultsListView.getItems().isEmpty()) {
                    resultsListView.setVisible(false);
                    resultsListView.setManaged(false);
                } else {
                    resultsListView.setVisible(true);
                    resultsListView.setManaged(true);
                    int itemCount = resultsListView.getItems().size();
                    int itemHeight = 25;
                    int padding = 20;
                    resultsListView.setPrefHeight(Math.min(itemCount * itemHeight + padding, 400));
                    resultsListView.toFront();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clearResultsListView() {
        // Clear ListView on the JavaFX Application Thread
        Platform.runLater(() -> {
            resultsListView.getItems().clear();
            resultsListView.setVisible(false);
            resultsListView.setManaged(false);
        });
    }

    private String correctEncoding(String brokenString) {
        byte[] bytes = brokenString.getBytes(StandardCharsets.ISO_8859_1);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
