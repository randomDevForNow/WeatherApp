module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jxbrowser;
    requires jxbrowser.javafx;
    requires javafx.graphics;

    exports com.example;
    exports com.example.Controller;
    exports com.example.Model;

    opens com.example.Controller to javafx.fxml;

}
