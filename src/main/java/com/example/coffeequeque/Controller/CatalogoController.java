package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.*;
import com.example.coffeequeque.structures.ColaSimple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class CatalogoController {

    @FXML private TextField txtBuscador;
    @FXML private Button btnBuscar;
    @FXML private FlowPane flowPaneCatalogo;
    @FXML private ListView<String> listViewCarrito;
    @FXML private Label lblTotal;
    @FXML private Button btnConfirmarPedido;

    private AdministradorProductos administrador;
    private ObservableList<String> carritoItemsFX;
    private double totalCompra = 0.0;

    private Cliente clienteActual;
    private ColaSimple<Pedido> colaPedidosGlobal;
    private int contadorIdPedidos = 1;

    @FXML
    public void initialize() {
        // Conectamos con el inventario centralizado de la cafetería
        administrador = SesionCafeteria.getAdministrador();

        carritoItemsFX = FXCollections.observableArrayList();
        if (listViewCarrito != null) {
            listViewCarrito.setItems(carritoItemsFX);
        }

        clienteActual = new Cliente(1, "Luis Cliente", "cliente@coffee.com", "123", true, "MAT-2026");
        colaPedidosGlobal = new ColaSimple<>();

        // Carga y dibuja las tarjetas del catálogo desde tu estructura enlazada
        renderizarCatalogoCompleto();

        if (btnBuscar != null) btnBuscar.setOnAction(e -> handleBuscarProducto());
        if (txtBuscador != null) txtBuscador.setOnAction(e -> handleBuscarProducto());
        if (btnConfirmarPedido != null) btnConfirmarPedido.setOnAction(e -> handleConfirmarPedido());
    }

    public void renderizarCatalogoCompleto() {
        if (flowPaneCatalogo == null) return;

        flowPaneCatalogo.getChildren().clear();

        // Obtiene la lista observable adaptada desde tu ListaProductos personalizada
        ObservableList<Producto> productosObs = administrador.getProductosObservable();

        if (productosObs.isEmpty()) {
            flowPaneCatalogo.getChildren().add(new Label("No hay productos disponibles en el catálogo."));
            return;
        }

        for (Producto p : productosObs) {
            flowPaneCatalogo.getChildren().add(crearTarjetaProducto(p));
        }
    }

    private VBox crearTarjetaProducto(Producto p) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setPrefHeight(278.0);
        card.setPrefWidth(160.0);
        card.setSpacing(5.0);
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 8; -fx-background-color: white;");

        // 1. Crear el ImageView con dimensiones fijas
        ImageView imageView = new ImageView();
        imageView.setFitHeight(110.0);
        imageView.setFitWidth(110.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        try {
            String nombreImagen = obtenerNombreImagen(p.getNombre());

            // Como tu carpeta 'imagenes' está directamente en resources, la ruta es esta:
            String ruta = "/imagenes/" + nombreImagen;

            var resourceStream = getClass().getResourceAsStream(ruta);
            if (resourceStream != null) {
                Image img = new Image(resourceStream);
                imageView.setImage(img);
            } else {
                System.out.println("⚠️ No se encontró el archivo de imagen en la ruta: " + ruta);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al cargar la imagen para: " + p.getNombre() + " -> " + e.getMessage());
        }

        Label lblNombre = new Label(p.getNombre());
        lblNombre.setStyle("-fx-font-weight: bold;");

        Label lblPrecio = new Label("Precio: $" + p.getPrecio());
        lblPrecio.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label lblStock = new Label("Stock: " + p.getStock());

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> agregarAlCarrito(p));

        // 2. ¡IMPORTANTE! Asegurarnos de meter el imageView en el VBox
        card.getChildren().addAll(imageView, lblNombre, lblPrecio, lblStock, btnAgregar);
        return card;
    }

    // Método auxiliar para asociar el nombre del producto con su archivo de imagen
    private String obtenerNombreImagen(String nombreProducto) {
        if (nombreProducto == null) return "default.png";
        switch (nombreProducto.toLowerCase()) {
            case "café americano": return "cafe_americano.PNG";
            case "capuchino": return "capuchino.PNG";
            case "pan de muerto": return "pan_de_muerto.PNG";
            case "cuernito": return "cuernito.PNG";
            case "sandwich": return "sandwich.PNG";
            case "torta de salchicha": return "torta_Salchicha.PNG";
            case "refresco 600ml": return "coca.PNG";
            case "galletas": return "galletas.PNG";
            default: return "default.png";
        }
    }

    @FXML
    private void handleBuscarProducto() {
        if (txtBuscador == null || flowPaneCatalogo == null) return;

        String query = txtBuscador.getText().trim().toLowerCase();
        ObservableList<Producto> productosObs = administrador.getProductosObservable();

        flowPaneCatalogo.getChildren().clear();
        for (Producto p : productosObs) {
            if (p.getNombre().toLowerCase().contains(query)) {
                flowPaneCatalogo.getChildren().add(crearTarjetaProducto(p));
            }
        }
    }

    private void agregarAlCarrito(Producto producto) {
        if (producto.getStock() <= 0) {
            showAlert("Producto Agotado", "Lo sentimos, este producto no cuenta con stock disponible.");
            return;
        }

        carritoItemsFX.add(producto.getNombre() + " - $" + producto.getPrecio());
        totalCompra += producto.getPrecio();
        if (lblTotal != null) {
            lblTotal.setText(String.format("Total: $%.2f", totalCompra));
        }
    }

    @FXML
    private void handleConfirmarPedido() {
        if (carritoItemsFX.isEmpty()) {
            showAlert("Carrito Vacío", "Agrega al menos un producto antes de confirmar.");
            return;
        }

        int idTurno = SesionCafeteria.generarSiguienteTurno();
        Pedido nuevoPedido = new Pedido(idTurno, clienteActual, Pedido.estado.Pendiente, totalCompra);

        SesionCafeteria.getColaCocina().encolar(nuevoPedido);

        SesionCafeteria.getHistorial().apilar(new Accion(
                Accion.Tipo.VENTA_REGISTRADA, nuevoPedido, nuevoPedido.getEstado(),
                "Nuevo pedido generado - Turno #" + idTurno + " ($" + String.format("%.2f", nuevoPedido.getTotal()) + ")"));

        showAlert("Pedido Confirmado", "¡Tu turno es el #" + idTurno + "! Ha sido enviado a cocina correctamente.");

        carritoItemsFX.clear();
        totalCompra = 0.0;
        if (lblTotal != null) {
            lblTotal.setText("Total: $0.00");
        }
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}