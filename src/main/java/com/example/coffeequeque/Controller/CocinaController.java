package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Lista;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CocinaController {

    @FXML
    private FlowPane flowPaneComandas;

    @FXML
    private Label lblPendientes;

    @FXML
    public void initialize() {
        renderizarComandas();
    }

    public void renderizarComandas() {
        if (flowPaneComandas == null) return;
        flowPaneComandas.getChildren().clear();

        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();

        if (colaCocina == null || colaCocina.estaVacia()) {
            if (lblPendientes != null) {
                lblPendientes.setText("Pendientes: 0");
            }
            Label lblVacio = new Label("No hay comandas pendientes en cocina.");
            lblVacio.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            flowPaneComandas.getChildren().add(lblVacio);
            return;
        }

        if (lblPendientes != null) {
            lblPendientes.setText("Pendientes: " + colaCocina.getTamanio());
        }

        // Copia de la cola en orden FIFO (no modifica la cola real)
        Lista<Pedido> pedidos = colaCocina.aLista();

        for (int i = 0; i < pedidos.getTamaño(); i++) {
            boolean esElPrimero = (i == 0);
            flowPaneComandas.getChildren().add(crearTarjetaComanda(pedidos.obtener(i), esElPrimero));
        }
    }

    private VBox crearTarjetaComanda(Pedido pedido, boolean esElPrimero) {
        VBox card = new VBox();
        card.setPrefHeight(220.0);
        card.setPrefWidth(190.0);
        card.setSpacing(8.0);
        card.setStyle("-fx-border-color: #d7d7d7; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #fffdf9; -fx-padding: 10;");

        Label lblTitulo = new Label("Orden #" + String.format("%03d", pedido.getIdPedido()));
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        String nombreCliente = (pedido.getCliente() != null) ? pedido.getCliente().getNombre() : "General";
        Label lblCliente = new Label("Cliente: " + nombreCliente);
        lblCliente.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        VBox boxProductos = new VBox();
        boxProductos.setPrefHeight(80.0);
        boxProductos.setStyle("-fx-background-color: #fafafa; -fx-padding: 8; -fx-background-radius: 4;");
        Label lblTotal = new Label(String.format("Total: $%.2f", pedido.getTotal()));
        lblTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        boxProductos.getChildren().add(lblTotal);

        Button btnListo = new Button(esElPrimero ? "Marcar como Listo" : "En espera");
        btnListo.setMaxWidth(Double.MAX_VALUE);

        if (esElPrimero) {
            btnListo.setStyle("-fx-background-color: #1e5a11; -fx-text-fill: white; -fx-font-weight: bold;");
            btnListo.setOnAction(e -> {
                Pedido listo = SesionCafeteria.getColaCocina().desencolar();
                listo.cambiarEstado(Pedido.estado.Cobrado);
                SesionCafeteria.getColaDespacho().encolar(listo);
                SesionCafeteria.getHistorial().apilar("Orden #" + listo.getIdPedido() + " lista en cocina");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Comanda Lista");
                alert.setHeaderText(null);
                alert.setContentText("La orden #" + listo.getIdPedido() + " pasó a Ventanilla de Despacho.");
                alert.showAndWait();

                renderizarComandas();
            });
        } else {
            // Solo el frente de la cola se puede despachar (respeta el FIFO)
            btnListo.setDisable(true);
            card.setStyle(card.getStyle() + "-fx-opacity: 0.85;");
        }

        card.getChildren().addAll(lblTitulo, lblCliente, boxProductos, btnListo);
        return card;
    }
}