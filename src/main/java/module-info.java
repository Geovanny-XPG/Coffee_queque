module com.example.coffeequeque {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.coffeequeque.Controller to javafx.fxml;
    opens com.example.coffeequeque.Model to javafx.base;

    opens com.example.coffeequeque to javafx.fxml;
    exports com.example.coffeequeque;
    exports com.example.coffeequeque.Controller;
}