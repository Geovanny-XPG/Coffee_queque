package com.example.coffeequeque.Model;

import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Pila;

public class SesionCafeteria {

    // Inventario de productos (se crea la primera vez que se pide)
    private static AdministradorProductos administradorGlobal;

    // Pedidos que esperan ser preparados en cocina (FIFO)
    private static ColaSimple<Pedido> colaCocinaGlobal;

    // Pedidos ya preparados que esperan ser entregados al cliente (FIFO)
    private static ColaSimple<Pedido> colaDespachoGlobal;

    // Pila de acciones realizadas en el sistema (LIFO), usada para deshacer
    private static Pila<Accion> historialGlobal;

    // Contador que asigna un número de turno único a cada pedido
    private static int contadorTurnos = 1;

    // Registro de usuarios (clientes y cajeros) con su persistencia en archivo
    private static AdministradorUsuarios administradorUsuariosGlobal;

    // Usuario que tiene la sesión iniciada; es null si nadie ha entrado
    private static Usuario usuarioActual;

    // ---------- Colas, historial e inventario ----------

    /** Devuelve la cola de despacho; la crea si aún no existe. */
    public static ColaSimple<Pedido> getColaDespacho() {
        if (colaDespachoGlobal == null) {
            colaDespachoGlobal = new ColaSimple<>();
        }
        return colaDespachoGlobal;
    }

    /** Devuelve el administrador de productos; en su primer uso carga los productos iniciales. */
    public static AdministradorProductos getAdministrador() {
        if (administradorGlobal == null) {
            administradorGlobal = new AdministradorProductos();
            cargarDatosIniciales(administradorGlobal);
        }
        return administradorGlobal;
    }

    /** Devuelve la cola de cocina; la crea si aún no existe. */
    public static ColaSimple<Pedido> getColaCocina() {
        if (colaCocinaGlobal == null) {
            colaCocinaGlobal = new ColaSimple<>();
        }
        return colaCocinaGlobal;
    }

    /** Devuelve la pila del historial; la crea si aún no existe. */
    public static Pila<Accion> getHistorial() {
        if (historialGlobal == null) {
            historialGlobal = new Pila<>();
        }
        return historialGlobal;
    }

    /** Entrega el siguiente número de turno. Es synchronized para que dos hilos no obtengan el mismo. */
    public static synchronized int generarSiguienteTurno() {
        return contadorTurnos++;
    }

    // ---------- Usuarios y sesión ----------

    /**
     * Devuelve el administrador de usuarios. En su primer uso lee los usuarios
     * guardados en el archivo y precarga al cajero si todavía no existe.
     */
    public static AdministradorUsuarios getAdministradorUsuarios() {
        if (administradorUsuariosGlobal == null) {
            administradorUsuariosGlobal = new AdministradorUsuarios();
            administradorUsuariosGlobal.cargarDesdeArchivo();

            // Cajero precargado: el registro público no puede crear cajeros
            // (se verifica el correo para no duplicarlo en cada ejecución)
            if (!administradorUsuariosGlobal.existeCorreo("mostrador@coffee.com")) {
                administradorUsuariosGlobal.agregarUsuarioInterno(
                        new Cajero(administradorUsuariosGlobal.siguienteId(),
                                "Administrador", "admin@coffee.com", "123456", false, 1));
            }
        }
        return administradorUsuariosGlobal;
    }

    /** Devuelve el usuario con sesión iniciada, o null si no hay ninguno. */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /** Guarda al usuario que acaba de autenticarse como el usuario actual. */
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    /** Marca al usuario como no logueado y limpia la sesión. */
    public static void cerrarSesion() {
        if (usuarioActual != null) {
            usuarioActual.setLog(false);
            usuarioActual = null;
        }
    }

    /** Indica si el usuario actual es un cajero (se usa para restringir el historial). */
    public static boolean esCajero() {
        return usuarioActual instanceof Cajero;
    }

    // ---------- Datos iniciales ----------

    /** Registra los productos con los que arranca el catálogo. */
    private static void cargarDatosIniciales(AdministradorProductos admin) {
        try {
            admin.registrarNuevoProducto(1, "Café Americano", 35.0, 20);
            admin.registrarNuevoProducto(2, "Capuchino", 45.0, 15);
            admin.registrarNuevoProducto(3, "Pan de Muerto", 25.0, 10);
        } catch (Exception e) {
            // Si falla (por ejemplo, un ID repetido) la app sigue, pero se avisa por consola
            System.err.println("Advertencia: No se pudieron registrar los productos iniciales - " + e.getMessage());
            e.printStackTrace();
        }
    }
}