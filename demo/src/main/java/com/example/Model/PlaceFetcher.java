package com.example.Model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaceFetcher {
    private static final String BASE_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    public static List<PlaceModel> fetchPlaces(List<String> placeQueries) {
        List<PlaceModel> placeList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String placeQuery : placeQueries) {
            try {
                URL url = new URL(placeQuery);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    // Success
                    Scanner scanner = new Scanner(url.openStream());

                    // Read the response
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    scanner.close();

                    // Parse JSON response into PlaceResponse
                    PlaceResponse placeResponse = objectMapper.readValue(jsonResponse.toString(), PlaceResponse.class);

                    // Iterate through results and create PlaceModel objects
                    for (PlaceModel place : placeResponse.getResults()) {
                        // Check if there are photos and construct photo URL
                        if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
                            String photoReference = place.getPhotos().get(0).getPhoto_reference();
                            String photoUrl = BASE_PHOTO_URL + photoReference
                                    + "&key=AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";
                            place.setPhotoUrl(photoUrl);
                        } else {
                            place.setPhotoUrl("No photo available");
                        }

                        // Check if place with the same title already exists
                        boolean exists = placeList.stream().anyMatch(p -> p.getName().equals(place.getName()));
                        if (!exists) {
                            placeList.add(place); // Only add if no place with same title exists
                        }
                    }
                } else {
                    System.out.println("Error: Unable to fetch places for query: " + placeQuery);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return placeList; // Return list of PlaceModel objects
    }

}
