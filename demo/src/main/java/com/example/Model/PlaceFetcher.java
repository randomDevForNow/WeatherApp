package com.example.Model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class PlaceFetcher {
    public static String fetchPlaces(List<String> placeQueries) {
        StringBuilder results = new StringBuilder();

        for (String placeQuery : placeQueries) {
            try {
                URL url = new URL(placeQuery);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    // Success
                    Scanner scanner = new Scanner(url.openStream());

                    // Read the response
                    while (scanner.hasNext()) {
                        results.append(scanner.nextLine());
                    }
                    scanner.close();
                } else {
                    System.out.println("Error: Unable to fetch places for query: " + placeQuery);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println(results.toString());
        return results.toString();
    }
}
