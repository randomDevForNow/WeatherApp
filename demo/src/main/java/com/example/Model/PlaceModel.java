package com.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceModel {
    private String name;
    private String vicinity;
    private double rating;
    private List<Photo> photos;
    private String photoUrl; // Add this line for the photo URL
    private List<String> types; // Add this line for the types

    // Getters and setters for name, vicinity, rating, etc.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPhotoUrl() { // Getter for photoUrl
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) { // Setter for photoUrl
        this.photoUrl = photoUrl;
    }

    public List<String> getTypes() { // Getter for types
        return types;
    }

    public void setTypes(List<String> types) { // Setter for types
        this.types = types;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {
        private String photo_reference;
        private int height;
        private int width;

        // Getters and setters
        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
