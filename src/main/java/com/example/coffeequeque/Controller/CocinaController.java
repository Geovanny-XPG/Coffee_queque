package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
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
    public void initialize() {
        renderizarComandas();
    }

    public void renderizarComandas() {
        if (flowPaneComandas == null) return;
        flowPaneComandas.getChildren().clear();

        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();

        if (colaCocina == null || colaCocina.estaVacia()) {
            Label lblVacio = new Label("No hay comandas pendientes en cocina.");
            lblVacio.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            flowPaneComandas.getChildren().add(lblVacio);
            return;
        }

        Pedido pedidoFrente = colaCocina.verFrente();
        flowPaneComandas.getChildren().add(crearTarjetaComanda(pedidoFrente));
    }

    private VBox crearTarjetaComanda(Pedido pedido) {
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
        Label lblPendientes = new Label("En cola: " + SesionCafeteria.getColaCocina().getTamanio());
        boxProductos.getChildren().addAll(lblTotal, lblPendientes);

        Button btnListo = new Button("Marcar como Listo");
        btnListo.setMaxWidth(Double.MAX_VALUE);
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

        card.getChildren().addAll(lblTitulo, lblCliente, boxProductos, btnListo);
        return card;
    }
}