module com.example.coffeequeque {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.coffeequeque to javafx.fxml;
    exports com.example.coffeequeque;
}