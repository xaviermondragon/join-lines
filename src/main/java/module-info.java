module com.example.joinlines {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.joinlines to javafx.fxml;
    exports com.example.joinlines;
}