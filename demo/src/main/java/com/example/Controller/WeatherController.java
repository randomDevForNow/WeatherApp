package com.example.Controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.example.Model.WeatherModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherController {

    private WeatherModel weather;

    public void updateWeather(double latitude, double longitude) {
        // Fetch weather data based on coordinates
        String apiKey = "c3af45cbaff1d83a9472068c18ab76c2"; // Add your OpenWeather API key here
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat="
                + latitude + "&lon=" + longitude + "&appid=" + apiKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                // Success
                ObjectMapper objectMapper = new ObjectMapper();
                weather = objectMapper.readValue(url, WeatherModel.class);
            } else {
                System.out.println("Error: Unable to fetch weather data.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public WeatherModel getWeather() {
        return weather;
    }
}
