package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeue.structures.ColaSimple;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CocinaController {

    @FXML
    private FlowPane flowPaneComandas;

    // Estructura de cola para gestionar los pedidos que entran a cocina
    private ColaSimple<Pedido> colaCocina;

    @FXML
    public void initialize() {
        colaCocina = new ColaSimple<>();

        // Limpiamos la comanda estática de ejemplo del FXML al iniciar para cargar datos reales o de prueba
        flowPaneComandas.getChildren().clear();
    }

    /**
     * Método para agregar un nuevo pedido recibido a la cola de cocina y renderizar su tarjeta visual.
     */
    public void agregarPedidoACocina(Pedido pedido) {
        colaCocina.encolar(pedido);
        renderizarComandas();
    }

    /**
     * Recorre la cola de cocina y redibuja las tarjetas (tarjetas de comanda) en el FlowPane.
     */
    private void renderizarComandas() {
        flowPaneComandas.getChildren().clear();

        VBox card = new VBox();
        card.setPrefHeight(220.0);
        card.setPrefWidth(180.0);
        card.setSpacing(8.0);
        card.setStyle("-fx-border-color: #d7d7d7; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #fffdf9; -fx-padding: 10;");

        Label lblTitulo = new Label("Orden #001");
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblHora = new Label("Hora: 10:15 AM");
        lblHora.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        VBox boxProductos = new VBox();
        boxProductos.setPrefHeight(90.0);
        boxProductos.setPrefWidth(160.0);
        boxProductos.setStyle("-fx-background-color: #fafafa; -fx-padding: 5; -fx-background-radius: 4;");
        boxProductos.getChildren().add(new Label("- Detalle del pedido"));

        Button btnListo = new Button("Marcar como Listo");
        btnListo.setMaxWidth(Double.MAX_VALUE);
        btnListo.setStyle("-fx-background-color: #1e5a11; -fx-text-fill: white;");
        btnListo.setOnAction(e -> {
            // Acción al marcar como listo (desencolar o cambiar estado)
            flowPaneComandas.getChildren().remove(card);
        });

        card.getChildren().addAll(lblTitulo, lblHora, boxProductos, btnListo);
        flowPaneComandas.getChildren().add(card);
    }
}