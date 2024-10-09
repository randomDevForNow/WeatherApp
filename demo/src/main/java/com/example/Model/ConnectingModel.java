package com.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConnectingModel {

    private double centerX;
    private double centerY;

    // Listener for changes in coordinates
    private List<CoordinateListener> coordinateListeners = new ArrayList<>();

    // Listener for changes in places data
    private List<Consumer<List<PlaceModel>>> placeListeners = new ArrayList<>();

    private List<PlaceModel> placesData = new ArrayList<>(); // List to hold PlaceModel objects

    public interface CoordinateListener {
        void onCoordinatesChanged(double x, double y);
    }

    // Add a coordinate listener
    public void addCoordinateListener(CoordinateListener listener) {
        coordinateListeners.add(listener);
    }

    // Set the center coordinates and notify listeners
    public void setCenterCoordinates(double x, double y) {
        this.centerX = x;
        this.centerY = y;
        notifyCoordinateListeners();
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    // Notify coordinate listeners
    private void notifyCoordinateListeners() {
        for (CoordinateListener listener : coordinateListeners) {
            listener.onCoordinatesChanged(centerX, centerY);
        }
    }

    // Add a place listener
    public void addPlaceListener(Consumer<List<PlaceModel>> listener) {
        placeListeners.add(listener);
    }

    // Set the places data and notify listeners
    public void setPlacesData(List<PlaceModel> placesData) {
        this.placesData = placesData;
        notifyPlaceListeners();
    }

    public List<PlaceModel> getPlacesData() {
        return placesData;
    }

    // Notify place listeners
    private void notifyPlaceListeners() {
        for (Consumer<List<PlaceModel>> listener : placeListeners) {
            listener.accept(placesData); // Notify with the current list of PlaceModel
        }
    }
}
