package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Accion;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Pila;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class HistorialController {

    @FXML private ListView<Accion> listViewHistorial;
    @FXML private Button btnDeshacer;

    @FXML
    public void initialize() {
        actualizarHistorialUI();
        if (btnDeshacer != null) {
            btnDeshacer.setOnAction(e -> handleDeshacerAccion());
        }
    }

    // Muestra la pila en orden LIFO (la cima aparece primero)
    public void actualizarHistorialUI() {
        if (listViewHistorial != null) {
            listViewHistorial.setItems(SesionCafeteria.getHistorial().toObservableList());
        }
    }

    // Deshace la acción de la cima, revirtiendo también el estado real del sistema
    @FXML
    private void handleDeshacerAccion() {
        Pila<Accion> historial = SesionCafeteria.getHistorial();
        if (historial.estaVacia()) {
            showAlert("Historial Vacío", "No hay acciones registradas para deshacer.");
            return;
        }

        Accion accion = historial.desapilar();
        Pedido pedido = accion.getPedido();
        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();

        switch (accion.getTipo()) {
            case VENTA_REGISTRADA:
                // El pedido estaba al final de cocina
                colaCocina.desencolarUltimo();
                break;
            case COMANDA_LISTA:
                // Estaba al final de despacho; vuelve al frente de cocina
                colaDespacho.desencolarUltimo();
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaCocina.encolarAlFrente(pedido);
                break;
            case PEDIDO_ENTREGADO:
                // Vuelve al frente de despacho
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaDespacho.encolarAlFrente(pedido);
                break;
        }

        actualizarHistorialUI();
        showAlert("Acción Deshecha", "Se ha revertido:\n" + accion);
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}