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

    // Componentes de la interfaz gráfica inyectados desde el FXML
    @FXML private ListView<Accion> listViewHistorial;
    @FXML private Button btnDeshacer;

    // Método que se ejecuta automáticamente al inicializar la vista del historial
    @FXML
    public void initialize() {
        actualizarHistorialUI();
        if (btnDeshacer != null) {
            // Vinculamos el botón de deshacer con su respectivo método manejador
            btnDeshacer.setOnAction(e -> handleDeshacerAccion());
        }
    }

    // Muestra los elementos de la pila en orden LIFO (Last In, First Out), donde la cima aparece primero
    public void actualizarHistorialUI() {
        if (listViewHistorial != null) {
            // Rellenamos el ListView con la lista observable obtenida desde la pila de la sesión
            listViewHistorial.setItems(SesionCafeteria.getHistorial().toObservableList());
        }
    }

    // Deshace la acción ubicada en la cima de la pila, revirtiendo también los cambios en el sistema
    @FXML
    private void handleDeshacerAccion() {
        Pila<Accion> historial = SesionCafeteria.getHistorial();
        
        // Verificamos si la pila está vacía antes de intentar desapilar
        if (historial.estaVacia()) {
            showAlert("Historial Vacío", "No hay acciones registradas para deshacer.");
            return;
        }

        // Extraemos la última acción realizada (la cima de la pila)
        Accion accion = historial.desapilar();
        Pedido pedido = accion.getPedido();
        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();

        // Evaluamos el tipo de acción para aplicar la lógica inversa correspondiente
        switch (accion.getTipo()) {
            case VENTA_REGISTRADA:
                // El pedido se registró y estaba al final de la cola de cocina; se elimina
                colaCocina.desencolarUltimo();
                break;
            case COMANDA_LISTA:
                // El pedido pasó a despacho; se retira de ahí y regresa al frente de cocina con su estado anterior
                colaDespacho.desencolarUltimo();
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaCocina.encolarAlFrente(pedido);
                break;
            case PEDIDO_ENTREGADO:
                // El pedido fue entregado; se recupera, se restaura su estado anterior y vuelve al frente de despacho
                pedido.cambiarEstado(accion.getEstadoAnterior());
                colaDespacho.encolarAlFrente(pedido);
                break;
        }

        // Actualizamos la interfaz gráfica y mostramos una alerta confirmando la acción revertida
        actualizarHistorialUI();
        showAlert("Acción Deshecha", "Se ha revertido:\n" + accion);
    }

    // Método auxiliar para mostrar alertas emergentes en pantalla
    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
