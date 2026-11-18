package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Accion;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Pila;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HistorialController {

    // Lista visual del historial y botón de deshacer, inyectados desde el FXML
    @FXML private ListView<Accion> listViewHistorial;
    @FXML private Button btnDeshacer;

    @FXML
    public void initialize() {
        // Segunda barrera de seguridad: solo el personal de caja puede ver y usar el historial.
        // (El login ya dirige a los clientes al catálogo, pero esto cubre otros caminos de acceso)
        if (!SesionCafeteria.esCajero()) {
            if (btnDeshacer != null) btnDeshacer.setDisable(true);
            if (listViewHistorial != null) {
                listViewHistorial.setPlaceholder(new Label("Acceso restringido al personal de caja."));
            }
            return;
        }

        actualizarHistorialUI();
        if (btnDeshacer != null) {
            btnDeshacer.setOnAction(e -> handleDeshacerAccion());
        }
    }

    /**
     * Recorre la Pila<Accion> y la convierte en una ObservableList respetando el orden LIFO
     * (el elemento en la cima se muestra primero en el ListView).
     * El texto de cada fila sale del toString() de Accion.
     */
    public void actualizarHistorialUI() {
        if (listViewHistorial != null) {
            listViewHistorial.setItems(SesionCafeteria.getHistorial().toObservableList());
        }
    }

    /**
     * Acción del botón "Deshacer Última Acción (Pop)": remueve el elemento de la cima de la pila
     * y revierte su efecto real en el sistema (colas y estado del pedido), no solo en la lista visible.
     */
    @FXML
    private void handleDeshacerAccion() {
        // Aunque el botón esté deshabilitado para otros roles, se vuelve a comprobar aquí
        if (!SesionCafeteria.esCajero()) return;

        Pila<Accion> historial = SesionCafeteria.getHistorial();
        if (historial.estaVacia()) {
            showAlert("Historial Vacío", "No hay acciones registradas para deshacer.");
            return;
        }

        // Pop: se extrae la acción más reciente (LIFO)
        Accion accion = historial.desapilar();
        Pedido pedido = accion.getPedido();
        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();

        // Como la pila es LIFO, el pedido siempre está donde cada caso espera encontrarlo
        switch (accion.getTipo()) {
            case VENTA_REGISTRADA:
                // El pedido se encoló al final de cocina: se quita de ahí
                colaCocina.desencolarUltimo();
                break;
            case COMANDA_LISTA:
                // Estaba al final de despacho: sale de ahí y vuelve al frente de cocina
                // con el estado que tenía antes de marcarse como listo
                colaDespacho.desencolarUltimo();
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaCocina.encolarAlFrente(pedido);
                break;
            case PEDIDO_ENTREGADO:
                // Ya había salido de la cola: vuelve al frente de despacho
                // con su estado anterior a la entrega
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaDespacho.encolarAlFrente(pedido);
                break;
        }

        // Se refresca la lista visible y se informa qué acción se revirtió
        actualizarHistorialUI();
        showAlert("Acción Deshecha", "Se ha revertido:\n" + accion);
    }

    // Muestra una ventana informativa con el título y mensaje recibidos
    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}