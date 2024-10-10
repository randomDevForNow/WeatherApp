module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires jxbrowser; // Use only this line
    requires jxbrowser.javafx;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires  java.net.http;

    exports com.example;
    exports com.example.Controller;
    exports com.example.Model;

    opens com.example.Controller to javafx.fxml;
}
