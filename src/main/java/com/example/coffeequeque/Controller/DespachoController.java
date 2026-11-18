package com.example.coffeequeque.Controller;

import com.example.coffeequeque.Model.Accion;
import com.example.coffeequeque.Model.Cajero;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.Usuario;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Lista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DespachoController {

    // Componentes de la interfaz gráfica inyectados desde el archivo FXML
    @FXML private Label lblTurnoActual;
    @FXML private Label lblDetalleOrden;
    @FXML private Button btnEntregar;
    @FXML private ListView<String> listViewListos;

    // Objeto que representa al cajero responsable de la sesión de despacho actual
    private Cajero cajeroEnTurno;

    // Método que se ejecuta automáticamente al inicializar la vista de despacho
    @FXML
    public void initialize() {
        // Se obtiene el usuario que inicio sesion
        Usuario actual = SesionCafeteria.getUsuarioActual();
        // Determina si es cajero segun el tipo de usuario con el que se entro
        cajeroEnTurno = (actual instanceof Cajero) ? (Cajero) actual
                : new Cajero(1, "Administrador", "admin@coffee.com", "123456", true, 1);
        if (btnEntregar != null) {
            // Activa el boton si el pedido esta disponible
            btnEntregar.setOnAction(e -> handleEntregarPedido());
        }
        // Muestra los cambios en la interfaz
        actualizarVista();
    }

    // Método encargado de refrescar la información visual en base a la cola de despacho global
    public void actualizarVista() {
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();
        ObservableList<String> itemsListos = FXCollections.observableArrayList();

        // Si la cola de despacho está vacía o es nula, limpiamos los indicadores de turnos
        if (colaDespacho == null || colaDespacho.estaVacia()) {
            if (lblTurnoActual != null) lblTurnoActual.setText("TURNO #---");
            if (lblDetalleOrden != null) lblDetalleOrden.setText("Detalle: No hay turnos pendientes");
            if (btnEntregar != null) btnEntregar.setDisable(true);
        } else {
            // Si hay pedidos listos, habilitamos el botón de entrega
            if (btnEntregar != null) btnEntregar.setDisable(false);

            // Obtenemos el pedido que está al frente de la cola (el siguiente en ser llamado)
            Pedido frente = colaDespacho.verFrente();
            if (lblTurnoActual != null) {
                lblTurnoActual.setText(String.format("TURNO #%03d", frente.getIdPedido()));
            }
            if (lblDetalleOrden != null) {
                String cliente = (frente.getCliente() != null) ? frente.getCliente().getNombre() : "General";
                lblDetalleOrden.setText(String.format("Cliente: %s | Total: $%.2f", cliente, frente.getTotal()));
            }

            // Convertimos la cola a una estructura de lista para rellenar la lista visual (ListView)
            Lista<Pedido> pedidos = colaDespacho.aLista();
            for (int i = 0; i < pedidos.getTamaño(); i++) {
                Pedido p = pedidos.obtener(i);
                // El primer elemento está "En llamado", los demás se muestran "En espera"
                String estado = (i == 0) ? "En llamado" : "En espera";
                itemsListos.add(String.format("TURNO #%03d - $%.2f (%s)", p.getIdPedido(), p.getTotal(), estado));
            }
        }

        // Asignamos la lista observable con los turnos listos al componente ListView de la UI
        if (listViewListos != null) {
            listViewListos.setItems(itemsListos);
        }
    }

    // Método que se ejecuta al presionar el botón de entregar pedido al cliente
    @FXML
    private void handleEntregarPedido() {
        ColaSimple<Pedido> colaDespacho = SesionCafeteria.getColaDespacho();
        if (colaDespacho != null && !colaDespacho.estaVacia()) {
            // Desencolamos el pedido actual que se va a entregar
            Pedido entregado = colaDespacho.desencolar();
            Pedido.estado anterior = entregado.getEstado();
            
            // Procesamos la entrega a través del objeto Cajero
            cajeroEnTurno.despacharPedido(entregado);
            
            // Registramos la acción completada en la pila del historial general
            SesionCafeteria.getHistorial().apilar(new Accion(
                    Accion.Tipo.PEDIDO_ENTREGADO, entregado, anterior,
                    "Pedido #" + entregado.getIdPedido() + " entregado por " + cajeroEnTurno.getNombre()));

            // Mostramos una alerta emergente notificando el éxito de la operación
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Entrega Exitosa");
            alert.setHeaderText(null);
            alert.setContentText("¡Pedido #" + entregado.getIdPedido() + " entregado al cliente!");
            alert.showAndWait();

            // Actualizamos la vista para reflejar el siguiente turno en la fila
            actualizarVista();
        }
    }
}
