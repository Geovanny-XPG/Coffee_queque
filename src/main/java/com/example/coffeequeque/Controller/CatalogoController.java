package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Cliente;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.Producto;
import com.example.coffeequeue.structures.ColaSimple;
import com.example.coffeequeue.structures.Lista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CatalogoController {

    @FXML private TextField txtBuscador;
    @FXML private Button btnBuscar;
    @FXML private FlowPane flowPaneCatalogo;
    @FXML private ListView<String> listViewCarrito;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmarPedido;

    // Estructuras de datos personalizadas y auxiliares
    private Lista<Producto> listaCatalogo;
    private ObservableList<String> carritoItemsFX;
    private double totalCompra = 0.0;

    // Simulación del cliente actual y cola global de pedidos para enviar a cocina
    private Cliente clienteActual;
    private ColaSimple<Pedido> colaPedidosGlobal;
    private int contadorIdPedidos = 1;

    @FXML
    public void initialize() {
        listaCatalogo = new Lista<>();
        carritoItemsFX = FXCollections.observableArrayList();
        listViewCarrito.setItems(carritoItemsFX);

        clienteActual = new Cliente(1, "Luis Cliente", "cliente@coffee.com", "123", true, "MAT-2026");
        colaPedidosGlobal = new ColaSimple<>();

        // Cargar productos de prueba iniciales en la estructura personalizada
        cargarCatalogoInicial();

        // Renderizar dinámicamente las tarjetas del catálogo desde la estructura Lista
        renderizarCatalogo(listaCatalogo);

        // Configurar acciones de botones
        btnBuscar.setOnAction(e -> handleBuscarProducto());
        txtBuscador.setOnAction(e -> handleBuscarProducto());
        btnConfirmarPedido.setOnAction(e -> handleConfirmarPedido());
    }

    private void cargarCatalogoInicial() {
        listaCatalogo.agregar(new Producto(1, "Café Americano", 35.0, 20));
        listaCatalogo.agregar(new Producto(2, "Capuchino", 45.0, 15));
        listaCatalogo.agregar(new Producto(3, "Pan de Muerto", 25.0, 10));
        listaCatalogo.agregar(new Producto(4, "Cuernito", 45.0, 8));
        listaCatalogo.agregar(new Producto(5, "Sandwich", 30.0, 12));
        listaCatalogo.agregar(new Producto(6, "Torta de Salchicha", 40.0, 10));
        listaCatalogo.agregar(new Producto(7, "Refresco 600ml", 16.0, 25));
        listaCatalogo.agregar(new Producto(8, "Galletas", 18.0, 30));
    }

    /**
     * Recorre la estructura Lista<Producto> y construye las tarjetas visuales (VBox) en el FlowPane.
     */
    private void renderizarCatalogo(Lista<Producto> listaAMostrar) {
        flowPaneCatalogo.getChildren().clear();

        // Como Lista<T> cuenta con getTamaño() y obtener(i), podemos iterar de forma limpia:
        for (int i = 0; i < listaAMostrar.getTamaño(); i++) {
            Producto p = listaAMostrar.obtener(i);

            VBox card = new VBox();
            card.setAlignment(Pos.CENTER);
            card.setPrefHeight(278.0);
            card.setPrefWidth(160.0);
            card.setSpacing(5.0);
            card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8; -fx-background-color: white;");

            Label lblNombre = new Label(p.getNombre());
            lblNombre.setStyle("-fx-font-weight: bold;");

            Label lblPrecio = new Label("Precio: $" + p.getPrecio());
            lblPrecio.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");

            Button btnAgregar = new Button("Agregar");
            btnAgregar.setOnAction(e -> agregarAlCarrito(p));

            card.getChildren().addAll(lblNombre, lblPrecio, btnAgregar);
            flowPaneCatalogo.getChildren().add(card);
        }
    }

    @FXML
    private void handleBuscarProducto() {
        String query = txtBuscador.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            renderizarCatalogo(listaCatalogo);
            return;
        }

        // Filtrar elementos usando tu estructura Lista personalizada
        Lista<Producto> resultados = new Lista<>();
        for (int i = 0; i < listaCatalogo.getTamaño(); i++) {
            Producto p = listaCatalogo.obtener(i);
            if (p.getNombre().toLowerCase().contains(query)) {
                resultados.agregar(p);
            }
        }
        renderizarCatalogo(resultados);
    }

    private void agregarAlCarrito(Producto producto) {
        carritoItemsFX.add(producto.getNombre() + " - $" + producto.getPrecio());
        totalCompra += producto.getPrecio();
        lblTotal.setText(String.format("Total: $%.2f", totalCompra));
    }

    @FXML
    private void handleConfirmarPedido() {
        if (carritoItemsFX.isEmpty()) {
            showAlert("Carrito Vacío", "Agrega al menos un producto antes de confirmar.");
            return;
        }

        // Crear el objeto Pedido e ingresarlo a la ColaSimple para cocina
        Pedido nuevoPedido = new Pedido(contadorIdPedidos++, clienteActual, Pedido.estado.Pendiente, totalCompra);
        colaPedidosGlobal.encolar(nuevoPedido);

        showAlert("Pedido Confirmado", "Tu pedido #" + nuevoPedido.getIdPedido() + " ha sido enviado a cocina.");

        // Limpiar carrito y totales
        carritoItemsFX.clear();
        totalCompra = 0.0;
        lblTotal.setText("Total: $0.00");
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}