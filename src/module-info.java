module SoftwareII {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;


    exports Main to javafx.graphics;
    exports controller;
    opens controller to javafx.fxml;
    opens model;
}