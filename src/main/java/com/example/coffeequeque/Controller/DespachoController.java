package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Cajero;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DespachoController {

    @FXML private Label lblTurnoActual;
    @FXML private Label lblDetalleOrden;
    @FXML private Button btnEntregar;
    @FXML private ListView<String> listViewListos;

    private Cajero cajeroEnTurno;

    @FXML
    public void initialize() {
        cajeroEnTurno = new Cajero(1, "Cajero Mostrador", "mostrador@coffee.com", "123", true, 1);
        if (btnEntregar != null) {
            btnEntregar.setOnAction(e -> handleEntregarPedido());
        }
        actualizarVista();
    }

    public void actualizarVista() {
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();
        ObservableList<String> itemsListos = FXCollections.observableArrayList();

        if (colaDespacho == null || colaDespacho.estaVacia()) {
            if (lblTurnoActual != null) lblTurnoActual.setText("TURNO #---");
            if (lblDetalleOrden != null) lblDetalleOrden.setText("Detalle: No hay turnos pendientes");
        } else {
            Pedido frente = colaDespacho.verFrente();
            if (lblTurnoActual != null) {
                lblTurnoActual.setText(String.format("TURNO #%03d", frente.getIdPedido()));
            }
            if (lblDetalleOrden != null) {
                String cliente = (frente.getCliente() != null) ? frente.getCliente().getNombre() : "General";
                lblDetalleOrden.setText(String.format("Cliente: %s | Total: $%.2f", cliente, frente.getTotal()));
            }
            itemsListos.add(String.format("TURNO #%03d - $%.2f (Listo)", frente.getIdPedido(), frente.getTotal()));
        }

        if (listViewListos != null) {
            listViewListos.setItems(itemsListos);
        }
    }

    @FXML
    private void handleEntregarPedido() {
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();
        if (colaDespacho != null && !colaDespacho.estaVacia()) {
            Pedido entregado = colaDespacho.desencolar();
            cajeroEnTurno.despacharPedido(entregado);
            SesionCafeteria.getHistorial().apilar("Pedido #" + entregado.getIdPedido() + " entregado por " + cajeroEnTurno.getNombre());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Entrega Exitosa");
            alert.setHeaderText(null);
            alert.setContentText("¡Pedido #" + entregado.getIdPedido() + " entregado al cliente!");
            alert.showAndWait();

            actualizarVista();
        }
    }
}