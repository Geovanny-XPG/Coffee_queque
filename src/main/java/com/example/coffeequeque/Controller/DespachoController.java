package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Cajero;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeue.structures.ColaSimple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DespachoController {

    @FXML private Label lblTurnoActual;
    @FXML private Label lblDetalleOrden;
    @FXML private Button btnEntregar;
    @FXML private ListView<String> listViewListos;

    // Estructura de cola para gestionar los turnos listos para entrega
    private ColaSimple<Pedido> colaPedidosListos;
    private Pedido pedidoActualEnLlamado;
    private Cajero cajeroEnTurno;

    @FXML
    public void initialize() {
        colaPedidosListos = new ColaSimple<>();
        cajeroEnTurno = new Cajero(1, "Cajero Mostrador", "mostrador@coffee.com", "123", true, 1);

        // Configurar acción del botón de entrega
        btnEntregar.setOnAction(e -> handleEntregarPedido());

        // Estado inicial de la interfaz
        lblTurnoActual.setText("TURNO #---");
        lblDetalleOrden.setText("Detalle: Ninguno en curso");
    }

    /**
     * Permite recibir un nuevo pedido listo desde cocina y agregarlo a la cola de despacho.
     */
    public void recibirPedidoListo(Pedido pedido) {
        colaPedidosListos.encolar(pedido);

        // Si no hay ningún turno en pantalla, cargamos el primero automáticamente
        if (pedidoActualEnLlamado == null && !colaPedidosListos.estaVacia()) {
            cargarSiguienteTurno();
        } else {
            actualizarListaListosUI();
        }
    }

    /**
     * Acción del botón "Entregar Pedido": despacha el pedido actual y pasa al siguiente en la cola.
     */
    @FXML
    private void handleEntregarPedido() {
        if (pedidoActualEnLlamado != null) {
            // Usando métodos de tu clase Cajero
            cajeroEnTurno.despacharPedido(pedidoActualEnLlamado);

            // Verificamos si hay más en la cola
            if (!colaPedidosListos.estaVacia()) {
                cargarSiguienteTurno();
            } else {
                pedidoActualEnLlamado = null;
                lblTurnoActual.setText("TURNO #---");
                lblDetalleOrden.setText("Detalle: No hay turnos pendientes");
            }
            actualizarListaListosUI();
        } else {
            lblDetalleOrden.setText("No hay ningún turno seleccionado para entregar.");
        }
    }

    /**
     * Saca el siguiente pedido del frente de la cola (FIFO) y lo muestra en la zona principal.
     */
    private void cargarSiguienteTurno() {
        if (!colaPedidosListos.estaVacia()) {
            pedidoActualEnLlamado = colaPedidosListos.desencolar();
            lblTurnoActual.setText("TURNO #" + String.format("%03d", pedidoActualEnLlamado.getIdPedido()));
            lblDetalleOrden.setText("Cliente: " + pedidoActualEnLlamado.getCliente().getNombre());
            actualizarListaListosUI();
        }
    }

    /**
     * Refresca el ListView de la derecha con los turnos que siguen en espera.
     */
    private void actualizarListaListosUI() {
        ObservableList<String> itemsListos = FXCollections.observableArrayList();

        listViewListos.setItems(itemsListos);
    }
}