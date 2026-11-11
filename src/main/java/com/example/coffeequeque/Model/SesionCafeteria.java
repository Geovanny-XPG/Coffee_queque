package com.example.coffeequeque.Model;

import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Pila;

public class SesionCafeteria {
    private static AdministradorProductos administradorGlobal;
    private static ColaSimple<Pedido> colaCocinaGlobal;
    private static Pila<String> historialGlobal;
    private static int contadorTurnos = 1;

    public static AdministradorProductos getAdministrador() {
        if (administradorGlobal == null) {
            administradorGlobal = new AdministradorProductos();
            cargarDatosIniciales(administradorGlobal);
        }
        return administradorGlobal;
    }

    public static ColaSimple<Pedido> getColaCocina() {
        if (colaCocinaGlobal == null) {
            colaCocinaGlobal = new ColaSimple<>();
        }
        return colaCocinaGlobal;
    }

    public static Pila<String> getHistorial() {
        if (historialGlobal == null) {
            historialGlobal = new Pila<>();
        }
        return historialGlobal;
    }

    public static synchronized int generarSiguienteTurno() {
        return contadorTurnos++;
    }

    private static void cargarDatosIniciales(AdministradorProductos admin) {
        try {
            admin.registrarNuevoProducto(1, "Café Americano", 35.0, 20);
            admin.registrarNuevoProducto(2, "Capuchino", 45.0, 15);
            admin.registrarNuevoProducto(3, "Pan de Muerto", 25.0, 10);
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudieron registrar los productos iniciales - " + e.getMessage());
            e.printStackTrace();
        }
    }
}