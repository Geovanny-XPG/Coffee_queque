package com.example.coffeequeque.Model;

public class SesionCafeteria {
    private static AdministradorProductos administradorGlobal;

    public static AdministradorProductos getAdministrador() {
        if (administradorGlobal == null) {
            administradorGlobal = new AdministradorProductos();
            cargarDatosIniciales(administradorGlobal);
        }
        return administradorGlobal;
    }

    private static void cargarDatosIniciales(AdministradorProductos admin) {
        try {
            admin.registrarNuevoProducto(1, "Café Americano", 35.0, 20);
            admin.registrarNuevoProducto(2, "Capuchino", 45.0, 15);
            admin.registrarNuevoProducto(3, "Pan de Muerto", 25.0, 10);
        } catch (Exception e) {

        }
    }
}