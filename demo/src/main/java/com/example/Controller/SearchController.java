package com.example.Controller;

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
    private TextField searchField;  // TextField for user input
    @FXML
    private ListView<String> resultsListView; // ListView for displaying results
    @FXML
    private Button searchButton; // Button for initiating the search

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    public void initialize() {
        // Add a listener for key events on the search field
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

        // Add a listener for item clicks in the ListView
        resultsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update the search field with the selected place name
                searchField.setText(newValue);
                // Removed selectedPlaceId logic
            }
        });

        // Set up the search button action
        // Set up the search button action
searchButton.setOnAction(event -> {
    // Get the selected place name from the search field
    String selectedPlace = searchField.getText();

    // Hide the ListView when the "Choose" button is pressed
    resultsListView.setVisible(false);
    resultsListView.setManaged(false); // Adjusts layout calculations

    if (selectedPlace != null && !selectedPlace.isEmpty()) {
        System.out.println("Search button pressed for: " + selectedPlace);

        new Thread(() -> {
            try {
                String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8"; // Replace with your Google Places API key

                // Properly encode the place name for the URL
                String encodedPlace = java.net.URLEncoder.encode(selectedPlace, StandardCharsets.UTF_8.toString());
                String urlString = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + encodedPlace + "&inputtype=textquery&fields=geometry&key=" + apiKey;

                // Make the network request to fetch place details (longitude and latitude)
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

                    // Parse the response and get the latitude and longitude
                    JsonNode jsonResponse = objectMapper.readTree(response.toString());
                    JsonNode candidates = jsonResponse.get("candidates");

                    if (candidates != null && candidates.size() > 0) {
                        JsonNode location = candidates.get(0).get("geometry").get("location");
                        double latitude = location.get("lat").asDouble();
                        double longitude = location.get("lng").asDouble();

                        // Output the latitude and longitude to the console
                        System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
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
                String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8"; // Replace with your Google Places API key
    
                // Properly encode the query for the URL
                String encodedQuery = java.net.URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
                String urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + encodedQuery + "&key=" + apiKey;
    
                // Make the network request to fetch suggestions
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
    
                // Check for response code
                if (connection.getResponseCode() == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // Update the ListView with the results
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
        // Use Platform.runLater to ensure UI updates happen on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Clear the current items in the ListView
            resultsListView.getItems().clear();
            
            try {
                // Parse the JSON response using Jackson
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                JsonNode predictionsArray = jsonNode.get("predictions");
    
                // Create a Set to store unique addresses
                Set<String> uniqueAddresses = new HashSet<>();
    
                // Iterate over the predictions array
                for (JsonNode predictionNode : predictionsArray) {
                    // Extract the place name from the prediction
                    String placeName = predictionNode.get("description").asText();
    
                    // Correct the encoding if needed
                    placeName = correctEncoding(placeName);
    
                    // Normalize the place name to handle duplicates (lowercase and trim)
                    String normalizedPlaceName = placeName.toLowerCase().trim();
    
                    // Check if the normalized place name is already in the uniqueAddresses set
                    if (!uniqueAddresses.contains(normalizedPlaceName)) {
                        uniqueAddresses.add(normalizedPlaceName);
                        // Add the place name to the ListView without the place ID
                        resultsListView.getItems().add(placeName);
                    }
                }
    
                // Update the visibility of the ListView based on its content
                if (resultsListView.getItems().isEmpty()) {
                    resultsListView.setVisible(false);
                    resultsListView.setManaged(false); // Adjusts layout calculations
                } else {
                    resultsListView.setVisible(true);
                    resultsListView.setManaged(true);
                    // Resize ListView to fit the content
                    int itemCount = resultsListView.getItems().size();
                    int itemHeight = 25; // Example: height per item
                    int padding = 20; // Additional padding
                    resultsListView.setPrefHeight(Math.min(itemCount * itemHeight + padding, 400)); // Example max height of 400px
                    resultsListView.toFront();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    
    private void clearResultsListView() {
        // Clear the ListView on the JavaFX Application Thread
        Platform.runLater(() -> {
            resultsListView.getItems().clear();
            // Hide the ListView if empty
            resultsListView.setVisible(false);
            resultsListView.setManaged(false); // Adjusts layout calculations
        });
    }
    

    private String correctEncoding(String brokenString) {
        // Convert the broken string to bytes using the incorrect encoding
        byte[] bytes = brokenString.getBytes(StandardCharsets.ISO_8859_1); // Assuming it's incorrectly encoded in ISO-8859-1

        // Now create a new String using the correct encoding (UTF-8)
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
