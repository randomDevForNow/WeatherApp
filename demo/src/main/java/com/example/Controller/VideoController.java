package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VideoController {

    @FXML
    private VBox videoContainer;

    @FXML
    private WebView videoWebView; // WebView to embed and play videos

    private final String youtubeApiKey = "AIzaSyBXuTkb5yxSQd7E1CIYdMP9VkXvE-wYTdE";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        System.out.println("VideoController initialized");
        String embeddedUrl = "https://www.youtube.com/embed/T9v23AayXU0?autoplay=1&mute=1";
        
        Platform.runLater(() -> {
            WebEngine webEngine = videoWebView.getEngine();
            webEngine.load(embeddedUrl);
        });
    }
    
    public void fetchAndPlayVideo(String location, String weatherCondition) {
        // Construct the search query based on location and weather condition
        String query = location + " " + weatherCondition + " weather today";
        String encodedQuery = query.replace(" ", "+");
        String requestUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" 
                            + encodedQuery + "&type=video&key=" + youtubeApiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::handleVideoResponse)
                .exceptionally(ex -> {
                    System.err.println("Failed to fetch videos: " + ex.getMessage());
                    return null;
                });
    }

    private void handleVideoResponse(String responseBody) {
        try {
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            JsonNode items = jsonResponse.get("items");

            if (items != null && items.size() > 0) {
                String videoId = items.get(0).get("id").get("videoId").asText();
                playVideoInWebView(videoId);
            } else {
                System.err.println("No videos found for the current query");
            }
        } catch (Exception e) {
            System.err.println("Error parsing video response: " + e.getMessage());
        }
    }

    private void playVideoInWebView(String videoId) {
        // Construct the embedded URL for YouTube
        String embeddedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
        
        // Load the video in the WebView
        Platform.runLater(() -> {
            WebEngine webEngine = videoWebView.getEngine();
            webEngine.load(embeddedUrl);
        });
    }
}
