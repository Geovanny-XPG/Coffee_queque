package com.example.coffeequeque.Model;

public class Administrador extends Usuario {

    public Administrador(int id, String nom, String mail, String pass) {
        super(id, nom, mail, pass);
    }

    // metodos
    public void agregarProducto (Producto p) {
        System.out.println("Agregando producto");
    }
    public void eliminarProducto(int idProducto) {
        System.out.println("Eliminando producto");
    }
    public void modificarPrecio(int id, double nuevoPrecio) {
        System.out.println("Modificando precio");
    }
}
