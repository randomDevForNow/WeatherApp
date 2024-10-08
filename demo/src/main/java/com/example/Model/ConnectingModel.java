package com.example.Model;

import java.util.ArrayList;
import java.util.List;

public class ConnectingModel {
    
    private double centerX;
    private double centerY;

    // Listener for changes in coordinates
    private List<CoordinateListener> listeners = new ArrayList<>();

    public interface CoordinateListener {
        void onCoordinatesChanged(double x, double y);
    }

    // Add a listener
    public void addListener(CoordinateListener listener) {
        listeners.add(listener);
    }

    // Set the center coordinates and notify listeners
    public void setCenterCoordinates(double x, double y) {
        this.centerX = x;
        this.centerY = y;
        notifyListeners();
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    private void notifyListeners() {
        for (CoordinateListener listener : listeners) {
            listener.onCoordinatesChanged(centerX, centerY);
        }
    }
}
