module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jxbrowser;
    requires jxbrowser.javafx;
    requires javafx.graphics;
    requires javafx.media;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports com.example;
    exports com.example.Controller;
    exports com.example.Model;

    opens com.example.Controller to javafx.fxml;
}
