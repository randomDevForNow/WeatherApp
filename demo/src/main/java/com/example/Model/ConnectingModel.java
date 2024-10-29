package com.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConnectingModel {

    private double centerX;
    private double centerY;

    private double Xs;
    private double Ys;

    private List<CoordinateListener> coordinateListeners = new ArrayList<>();

    private List<SelectedListener> selectedListeners = new ArrayList<>();

    private List<Consumer<List<PlaceModel>>> placeListeners = new ArrayList<>();

    private List<PlaceModel> placesData = new ArrayList<>();

    public interface CoordinateListener {
        void onCoordinatesChanged(double x, double y);
    }

    // Add a coordinate listener
    public void addCoordinateListener(CoordinateListener listener) {
        coordinateListeners.add(listener);
    }

    // Set center coordinates and notify listeners
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

    public interface SelectedListener {
        void onSelectedChanged(double x, double y);
    }

    // Add selected listener
    public void addSelectedListener(SelectedListener listener) {
        selectedListeners.add(listener);
    }

    // Set center coordinates and notify listeners
    public void setSelectedCoordinates(double x, double y) {
        this.Xs = x;
        this.Ys = y;
        notifySelectedListeners();
    }

    public double getSelectedX() {
        return centerX;
    }

    public double getSelectedY() {
        return centerY;
    }

    // Notify coordinate listeners
    private void notifySelectedListeners() {
        for (SelectedListener listener : selectedListeners) {
            listener.onSelectedChanged(Xs, Ys);
        }
    }

    // Add place listener
    public void addPlaceListener(Consumer<List<PlaceModel>> listener) {
        placeListeners.add(listener);
    }

    // Set places data and notify listeners
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
            listener.accept(placesData); // Notify with current list of PlaceModel
        }
    }
}
