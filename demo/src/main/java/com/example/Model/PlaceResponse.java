package com.example.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceResponse {
    private PlaceModel[] results; // Array of PlaceModel

    public PlaceModel[] getResults() {
        return results;
    }

    public void setResults(PlaceModel[] results) {
        this.results = results;
    }
}
