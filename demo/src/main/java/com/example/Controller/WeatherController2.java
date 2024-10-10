package com.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import com.example.App;

public class WeatherController2 {

    private static final String WEATHER_API_KEY = "3d29ec11647646df94e93355240410";  
    private static final String WEATHER_BASE_URL = "https://api.weatherapi.com/v1/current.json";
    
    @FXML
    private TextField cityInput;
    @FXML
    private Label locationLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label windSpeedLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label word1;
    @FXML
    private Label word2;
    @FXML
    private Label timer;  // Label for displaying date and time
    @FXML
    private Label des1;   // Label for weather recommendation
    @FXML
    private ImageView weatherIcon;
    @FXML
    private ImageView weather2;

    @FXML
    void initialize() {
        // Start the clock asynchronously
        CompletableFuture.runAsync(this::startClock);

        word1.setText("a bit hot");
        word2.setText("windy");

        String absolutePath = "C:/path/to/your/image/sunny-icon.png"; // Update with your actual path
        try {
            Image testIcon = new Image("file:///" + absolutePath);
            weatherIcon.setImage(testIcon);
            System.out.println("Test image loaded successfully from absolute path");
        } catch (Exception e) {
            System.out.println("Could not load test image with absolute path");
            e.printStackTrace();
        }

        cityInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Fetch weather data asynchronously when the user presses Enter
                fetchWeatherAsync();
            }
        });
    }

    // Method to start a clock that updates the timer Label with the current date and time
    private void startClock() {
        Runnable clockRunnable = () -> {
            Timer timerClock = new Timer(true);
            timerClock.scheduleAtFixedRate(new java.util.TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy h:mm a");
                        timer.setText(now.format(formatter));
                    });
                }
            }, 0, 1000);
        };

        // Run clock on a separate thread to prevent UI freezing
        Thread clockThread = new Thread(clockRunnable);
        clockThread.start();
    }

    // Method to fetch weather asynchronously using CompletableFuture
    public void fetchWeatherAsync() {
        String city = cityInput.getText().trim();

        if (city.isEmpty()) {
            Platform.runLater(() -> updateWeatherUI("Enter a city", "", "", "", ""));
            return;
        }

        // Run the weather fetching task asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("LAAAAAAAAAAH");
                String weatherData = getWeatherData(city);
                Platform.runLater(() -> updateWeatherUIFromResponse(city, weatherData));
            } catch (IOException | InterruptedException ex) {
                Platform.runLater(() -> updateWeatherUI("Error", "", "", "", ex.getMessage()));
            }
        });
    }

    // Fetch weather data from API
    private String getWeatherData(String city) throws IOException, InterruptedException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        String urlString = String.format("%s?key=%s&q=%s&aqi=no", WEATHER_BASE_URL, WEATHER_API_KEY, encodedCity);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("HTTP Status Code: " + response.statusCode());
        }
    }

    // Parse weather data and update the UI
    private void updateWeatherUIFromResponse(String city, String responseBody) {
        String tempKeyword = "\"temp_c\":";
        String humidityKeyword = "\"humidity\":";
        String windKeyword = "\"wind_kph\":";
        String conditionKeyword = "\"text\":\"";

        int tempIndex = responseBody.indexOf(tempKeyword);
        int humidityIndex = responseBody.indexOf(humidityKeyword);
        int windIndex = responseBody.indexOf(windKeyword);
        int conditionIndex = responseBody.indexOf(conditionKeyword);

        if (tempIndex != -1 && humidityIndex != -1 && windIndex != -1 && conditionIndex != -1) {
            int tempStart = tempIndex + tempKeyword.length();
            int tempEnd = responseBody.indexOf(",", tempStart);
            String temperature = responseBody.substring(tempStart, tempEnd) + "°C";

            int humidityStart = humidityIndex + humidityKeyword.length();
            int humidityEnd = responseBody.indexOf(",", humidityStart);
            String humidity = responseBody.substring(humidityStart, humidityEnd) + "%";

            int windStart = windIndex + windKeyword.length();
            int windEnd = responseBody.indexOf(",", windStart);
            String windSpeed = responseBody.substring(windStart, windEnd) + " km/h";

            int conditionStart = conditionIndex + conditionKeyword.length();
            int conditionEnd = responseBody.indexOf("\"", conditionStart);
            String condition = responseBody.substring(conditionStart, conditionEnd);

            updateWeatherUI(city, temperature, humidity, windSpeed, condition);
        } else {
            System.out.println("Running else");
            updateWeatherUI("Weather data incomplete", "", "", "", "");
        }
    }

    // Update the weather UI elements
    private void updateWeatherUI(String location, String temperature, String humidity, String windSpeed, String description) {
        locationLabel.setText(location);
        temperatureLabel.setText(temperature);
        humidityLabel.setText("Humidity: " + humidity);
        windSpeedLabel.setText("Wind Speed: " + windSpeed);
        descriptionLabel.setText(description);

        setWeatherDescriptions(description.toLowerCase());
        updateWeatherIcon(description.toLowerCase());
    }

    // Set weather descriptions
    private void setWeatherDescriptions(String condition) {
        condition = condition.toLowerCase(); // Normalize the condition to lowercase

        if (condition.contains("sunny")) {
            word1.setText("Bright and sunny.");
            word2.setText("Good for outdoors.");
            des1.setText("Great day to be outside.");
        } else if (condition.contains("rain") || condition.contains("showers")) {
            word1.setText("Rainy day.");
            word2.setText("Grab an umbrella.");
            des1.setText("Better to stay indoors.");
        } else if (condition.contains("cloudy")) {
            word1.setText("Cloudy skies.");
            word2.setText("Chance of rain.");
            des1.setText("Might stay inside.");
        } else if (condition.contains("windy")) {
            word1.setText("Windy day.");
            word2.setText("Hold onto hats!");
            des1.setText("Stay inside, it's windy.");
        } else if (condition.contains("snow")) {
            word1.setText("Snow is falling.");
            word2.setText("Dress warmly.");
            des1.setText("Best to stay inside.");
        } else {
            word1.setText("Normal day.");
            word2.setText("Have a nice day.");
            des1.setText("Feel free to go out.");
        }
    }

    // Update the weather icon based on the condition
    private void updateWeatherIcon(String condition) {
        condition = condition.toLowerCase(); // Normalize the condition to lowercase
        String iconPath;

        if (condition.contains("sunny")) {
            iconPath = App.class.getResource("sunny-icon.png").toString();
        } else if (condition.contains("rain") || condition.contains("showers")) {
            iconPath = App.class.getResource("rainy-icon.png").toString();
        } else if (condition.contains("cloudy")) {
            iconPath = App.class.getResource("cloudy-icon.png").toString();
        } else if (condition.contains("windy")) {
            iconPath = App.class.getResource("windy-icon.png").toString();
        } else if (condition.contains("snow")) {
            iconPath = App.class.getResource("snowy-icon.png").toString();
        } else {
            iconPath = App.class.getResource("default-weather-icon.png").toString();
        }

        try {
            Image icon = new Image(iconPath);
            weatherIcon.setImage(icon);
            System.out.println("Icon loaded successfully: " + iconPath);
        } catch (Exception e) {
            System.out.println("Could not load icon: " + iconPath);
            e.printStackTrace();
        }
    }
}
