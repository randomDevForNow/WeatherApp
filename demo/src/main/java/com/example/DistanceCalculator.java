package com.example;

import com.example.Model.PlaceModel;

public class DistanceCalculator {

    // Method to calculate distance between two geographical points
    public static double calcDist(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radius of the Earth in km
        double dLat = toRad(lat2 - lat1);
        double dLon = toRad(lon2 - lon1);
        lat1 = toRad(lat1);
        lat2 = toRad(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    // Method to convert degrees to radians
    private static double toRad(double value) {
        return value * Math.PI / 180;
    }

    // Method to get the distance from the current location to a PlaceModel instance
    public static double getDistance(PlaceModel place, double currentLat, double currentLon) {
        double placeLat = place.getGeometry().getLocation().getLat(); // Get latitude from PlaceModel
        double placeLon = place.getGeometry().getLocation().getLng(); // Get longitude from PlaceModel

        // Calculate the distance in kilometers
        double distanceInKm = calcDist(currentLat, currentLon, placeLat, placeLon);

        // Convert to meters and round to the nearest whole number
        return Math.round(distanceInKm * 1000); // Distance in meters
    }
}