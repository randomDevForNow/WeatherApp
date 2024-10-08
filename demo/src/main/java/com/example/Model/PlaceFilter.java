package com.example.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaceFilter {

    // change set to private later
    public Set<String> placeTypesSet;

    public static List<String> buildPlaceQueries(WeatherModel weatherModel) {
        List<String> placeQueries = new ArrayList<>();
        String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";

        // Extract weather conditions from the model
        String weatherMain = weatherModel.getWeather()[0].getMain();
        double temperature = weatherModel.getMain().getTemp() - 273.15; // Convert from Kelvin to Celsius
        double windSpeed = weatherModel.getWind().getSpeed();
        double rainVolume = weatherModel.getRain() != null ? weatherModel.getRain().get_1h() : 0;
        int humidity = weatherModel.getMain().getHumidity();
        long currentTime = System.currentTimeMillis() / 1000L; // Current time in seconds
        long sunrise = weatherModel.getSys().getSunrise();
        long sunset = weatherModel.getSys().getSunset();

        // Default: popular, open places based on weather
        Set<String> placeTypesSet = new HashSet<>();
        String[] placeTypes = { "restaurant" }; // Default, will be replaced based on conditions

        // Apply filtering logic based on weather conditions
        if (weatherMain.equals("Clear") || weatherMain.equals("Clouds")) {
            placeTypes = new String[] { "park", "cafe", "restaurant" }; // Outdoor, leisure places on clear days
        } else if (weatherMain.equals("Rain")) {
            if (rainVolume > 1) {
                placeTypes = new String[] { "mall", "indoor_playground" }; // Indoor malls in heavy rain
            } else {
                placeTypes = new String[] { "cafe", "indoor_restaurant" }; // Cafes in light rain
            }
        } else if (temperature > 30) {
            placeTypes = new String[] { "swimming_pool", "ice_cream_parlor" }; // Cool places in hot weather
        } else if (windSpeed > 5) {
            placeTypes = new String[] { "museum", "shopping_mall" }; // Indoor places during windy conditions
        }

        // Daytime or nighttime check
        if (currentTime > sunrise && currentTime < sunset) {
            placeTypes = new String[] { "park", "cafe", "restaurant" }; // Daytime outdoor places
        } else {
            placeTypes = new String[] { "bar", "restaurant" }; // Nighttime places
        }

        for (String type : placeTypes) {
            placeTypesSet.add(type);
        }

        // Add opening hours filter
        String opennow = "true"; // Prioritize places that are currently open

        // Build queries for each type of place
        for (String type : placeTypes) {
            String placeQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + weatherModel.getCoord().getLat() + "," + weatherModel.getCoord().getLon()
                    + "&radius=1000"
                    + "&type=" + type
                    + "&opennow=" + opennow
                    + "&key=" + apiKey;

            placeQueries.add(placeQuery);
        }

        return placeQueries;
    }
}
