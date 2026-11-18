package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Accion;
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

    // Componentes gráficos inyectados desde el archivo FXML de la vista de cocina
    @FXML
    private FlowPane flowPaneComandas;

    @FXML
    private Label lblPendientes;

    // Método que se ejecuta automáticamente al cargar la vista
    @FXML
    public void initialize() {
        renderizarComandas();
    }

    // Método principal para actualizar y dibujar las tarjetas de comandas pendientes en pantalla
    public void renderizarComandas() {
        if (flowPaneComandas == null) return;
        // Limpiamos el contenedor visual para evitar duplicados al refrescar
        flowPaneComandas.getChildren().clear();

        // Obtenemos la cola global de pedidos pendientes desde la sesión compartida
        ColaSimple<Pedido> colaCocina = SesionCafeteria.getColaCocina();

        // Si la cola está vacía, mostramos un mensaje indicando que no hay órdenes
        if (colaCocina == null || colaCocina.estaVacia()) {
            if (lblPendientes != null) {
                lblPendientes.setText("Pendientes: 0");
            }
            Label lblVacio = new Label("No hay comandas pendientes en cocina.");
            lblVacio.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            flowPaneComandas.getChildren().add(lblVacio);
            return;
        }

        // Actualizamos el contador visual con el número total de pendientes
        if (lblPendientes != null) {
            lblPendientes.setText("Pendientes: " + colaCocina.getTamanio());
        }

        // Convertimos la cola a una estructura de lista para poder recorrerla y renderizarla
        Lista<Pedido> pedidos = colaCocina.aLista();

        // Recorremos cada pedido para crear su respectiva tarjeta visual (comanda)
        for (int i = 0; i < pedidos.getTamaño(); i++) {
            boolean esElPrimero = (i == 0); // Identificamos si es el primer pedido de la cola
            flowPaneComandas.getChildren().add(crearTarjetaComanda(pedidos.obtener(i), esElPrimero));
        }
    }

    // Método auxiliar para construir gráficamente la tarjeta (VBox) de cada comanda
    private VBox crearTarjetaComanda(Pedido pedido, boolean esElPrimero) {
        VBox card = new VBox();
        card.setPrefHeight(220.0);
        card.setPrefWidth(190.0);
        card.setSpacing(8.0);
        card.setStyle("-fx-border-color: #d7d7d7; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #fffdf9; -fx-padding: 10;");

        // Etiqueta para el ID de la orden formateado a 3 dígitos (ej. #001)
        Label lblTitulo = new Label("Orden #" + String.format("%03d", pedido.getIdPedido()));
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        // Etiqueta para mostrar el nombre del cliente asociado al pedido
        String nombreCliente = (pedido.getCliente() != null) ? pedido.getCliente().getNombre() : "General";
        Label lblCliente = new Label("Cliente: " + nombreCliente);
        lblCliente.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");

        // Contenedor interno para detalles de productos y el monto total
        VBox boxProductos = new VBox();
        boxProductos.setPrefHeight(80.0);
        boxProductos.setStyle("-fx-background-color: #fafafa; -fx-padding: 8; -fx-background-radius: 4;");
        Label lblTotal = new Label(String.format("Total: $%.2f", pedido.getTotal()));
        lblTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        boxProductos.getChildren().add(lblTotal);

        // Botón de acción: habilitado y destacado solo si es el primer pedido en la fila (FIFO)
        Button btnListo = new Button(esElPrimero ? "Marcar como Listo" : "En espera");
        btnListo.setMaxWidth(Double.MAX_VALUE);

        if (esElPrimero) {
            // Estilo y lógica para el pedido que está al frente de la cola
            btnListo.setStyle("-fx-background-color: #1e5a11; -fx-text-fill: white; -fx-font-weight: bold;");
            btnListo.setOnAction(e -> {
                // Desencolamos el pedido actual de cocina
                Pedido listo = SesionCafeteria.getColaCocina().desencolar();
                Pedido.estado anterior = listo.getEstado();
                // Cambiamos su estado y lo pasamos a la cola de despacho
                listo.cambiarEstado(Pedido.estado.Cobrado);
                SesionCafeteria.getColaDespacho().encolar(listo);
                
                // Registramos la acción completada en el historial general (pila)
                SesionCafeteria.getHistorial().apilar(new Accion(
                        Accion.Tipo.COMANDA_LISTA, listo, anterior,
                        "Orden #" + listo.getIdPedido() + " lista en cocina"));

                // Mostramos una alerta de confirmación al usuario
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Comanda Lista");
                alert.setHeaderText(null);
                alert.setContentText("La orden #" + listo.getIdPedido() + " pasó a Ventanilla de Despacho.");
                alert.showAndWait();

                // Volvemos a renderizar las comandas para reflejar los cambios
                renderizarComandas();
            });
        } else {
            // Si no es el primero, el botón se deshabilita para mantener el orden de preparación
            btnListo.setDisable(true);
            card.setStyle(card.getStyle() + "-fx-opacity: 0.85;");
        }

        // Agregamos todos los elementos construidos al VBox principal de la tarjeta
        card.getChildren().addAll(lblTitulo, lblCliente, boxProductos, btnListo);
        return card;
    }
}
