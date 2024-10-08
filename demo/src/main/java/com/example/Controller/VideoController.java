package com.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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
import java.util.HashMap;
import java.util.Map;

public class VideoController {

    @FXML
    private VBox videoContainer;

    @FXML
    private ListView<String> videoListView; // List to display video titles

    @FXML
    private WebView videoWebView; // WebView to embed and play videos

    private final String youtubeApiKey = "AIzaSyBXuTkb5yxSQd7E1CIYdMP9VkXvE-wYTdE";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // A simple map to store video IDs and titles
    private final Map<String, String> videoMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Event listener for list view item click
        videoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String videoId = videoMap.get(newValue);
                if (videoId != null) {
                    playVideoInWebView(videoId);
                }
            }
        });
    }

    public void fetchVideos(String query) {
        // Replace spaces with '+' for the query
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

            // Ensure we run UI updates on the JavaFX Application Thread
            Platform.runLater(() -> {
                videoListView.getItems().clear(); // Clear any previous entries
                videoMap.clear();

                if (items != null) {
                    for (JsonNode item : items) {
                        String videoTitle = item.get("snippet").get("title").asText();
                        String videoId = item.get("id").get("videoId").asText();
                        videoListView.getItems().add(videoTitle);
                        videoMap.put(videoTitle, videoId);
                    }
                } else {
                    videoListView.getItems().add("No videos found");
                }
            });
        } catch (Exception e) {
            System.err.println("Error parsing video response: " + e.getMessage());
        }
    }

    private void playVideoInWebView(String videoId) {
        // Construct the embedded URL for YouTube
        String embeddedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";

        // Get the WebEngine from the WebView and load the video
        WebEngine webEngine = videoWebView.getEngine();
        webEngine.load(embeddedUrl);
    }
}
