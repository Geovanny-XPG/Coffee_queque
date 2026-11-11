package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.Pila;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class HistorialController {

    @FXML private ListView<String> listViewHistorial;
    @FXML private Button btnDeshacer;

    private Pila<String> pilaHistorial;

    @FXML
    public void initialize() {
        actualizarHistorialUI();
        if (btnDeshacer != null) {
            btnDeshacer.setOnAction(e -> handleDeshacerAccion());
        }
    }

    /**
     * Recorre la Pila<String> y la convierte en una ObservableList respetando el orden LIFO
     * (el elemento en la cima se muestra primero en el ListView).
     */

    private void actualizarHistorialUI() {
        if (listViewHistorial != null) {
            listViewHistorial.setItems(SesionCafeteria.getHistorial().toObservableList());
        }
    }

    /**
     * Acción del botón "Deshacer Última Acción (Pop)": remueve el elemento de la cima de la pila.
     */

    @FXML
    private void handleDeshacerAccion() {
        Pila<String> historial = SesionCafeteria.getHistorial();
        if (!historial.estaVacia()) {
            String accionDeshecha = historial.desapilar();
            actualizarHistorialUI();
            showAlert("Acción Deshecha", "Se ha revertido la última acción:\n" + accionDeshecha);
        } else {
            showAlert("Historial Vacío", "No hay acciones registradas para deshacer.");
        }
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}