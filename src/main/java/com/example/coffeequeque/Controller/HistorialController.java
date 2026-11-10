package com.example.coffeequeque.Controller;

import com.example.coffeequeue.structures.Pila;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        pilaHistorial = new Pila<>();

        // Configurar la acción del botón Deshacer (Pop)
        btnDeshacer.setOnAction(e -> handleDeshacerAccion());

        // Actualizar la vista inicial
        actualizarHistorialUI();
    }

    /**
     * Permite registrar una nueva acción en el sistema (equivalente a un apilar / push).
     */
    public void registrarAccion(String descripcionAccion) {
        pilaHistorial.apilar(descripcionAccion);
        actualizarHistorialUI();
    }

    /**
     * Acción del botón "Deshacer Última Acción (Pop)": remueve el elemento de la cima de la pila.
     */
    @FXML
    private void handleDeshacerAccion() {
        if (!pilaHistorial.estaVacia()) {
            String accionDeshacer = pilaHistorial.desapilar();

            actualizarHistorialUI();

            showAlert("Acción Deshecha", "Se ha revertido la acción:\n" + accionDeshacer);
        } else {
            showAlert("Historial Vacío", "No hay más acciones registradas para deshacer.");
        }
    }

    /**
     * Recorre la Pila<String> y la convierte en una ObservableList respetando el orden LIFO
     * (el elemento en la cima se muestra primero en el ListView).
     */
    private void actualizarHistorialUI() {
        ObservableList<String> itemsHistorialFX = FXCollections.observableArrayList();

        if (!pilaHistorial.estaVacia()) {
            // Nota: Si tu Pila permite recorrer los elementos desde la cima sin perderlos
            // (o si creas un método auxiliar en Pila<T> que devuelva una copia), puedes listar sus elementos.
            // Una opción directa si creas un método de apoyo en tu Pila:
            // itemsHistorialFX.addAll(pilaHistorial.obtenerElementosComoLista());
        }

        listViewHistorial.setItems(itemsHistorialFX);
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}