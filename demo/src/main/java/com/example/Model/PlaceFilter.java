package com.example.Model;

import java.util.ArrayList;
import java.util.List;

public class PlaceFilter {

    public static String[] placeTypes;

    public static List<String> buildPlaceQueries(WeatherModel weatherModel) {
        List<String> placeQueries = new ArrayList<>();
        String apiKey = "AIzaSyBjQu-Q3qNLAtrktpgHcmtrH4WLLS7gEo8";

        // Extract weather conditions from the model
        String weatherMain = weatherModel.getWeather()[0].getMain();
        double temperature = weatherModel.getMain().getTemp() - 273.15; // Converting from Kelvin to Celsius
        double windSpeed = weatherModel.getWind().getSpeed();
        double rainVolume = weatherModel.getRain() != null ? weatherModel.getRain().get_1h() : 0;
        int humidity = weatherModel.getMain().getHumidity();
        long currentTime = System.currentTimeMillis() / 1000L; // Current time in seconds
        long sunrise = weatherModel.getSys().getSunrise();
        long sunset = weatherModel.getSys().getSunset();

        // Default: popular, open places based on weather
        placeTypes = new String[] { "restaurant" }; // Default, will be replaced based on conditions

        // Filtering logic based on weather conditions
        if (weatherMain.equals("Clear") || weatherMain.equals("Clouds")) {
            placeTypes = new String[] { "park", "cafe", "restaurant", "lodging" };
        } else if (weatherMain.equals("Rain")) {
            if (rainVolume > 1) {
                placeTypes = new String[] { "mall", "indoor_playground", "lodging" };
            } else {
                placeTypes = new String[] { "cafe", "indoor_restaurant", "lodging" };
            }
        } else if (temperature > 30) {
            placeTypes = new String[] { "swimming_pool", "ice_cream_parlor", "lodging" };
        } else if (windSpeed > 5) {
            placeTypes = new String[] { "museum", "shopping_mall", "lodging" };
        }

        if (currentTime > sunrise && currentTime < sunset) {
            placeTypes = new String[] { "park", "cafe", "restaurant", "lodging" };
        } else {
            placeTypes = new String[] { "bar", "restaurant", "lodging" };
        }

        String opennow = "true"; // Make sure places are open

        // Build queries for each type of place
        for (String type : placeTypes) {
            String placeQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + weatherModel.getCoord().getLat() + "," + weatherModel.getCoord().getLon()
                    + "&radius=500"
                    + "&type=" + type
                    + "&opennow=" + opennow
                    + "&key=" + apiKey;

            placeQueries.add(placeQuery);
        }

        return placeQueries;
    }

}
