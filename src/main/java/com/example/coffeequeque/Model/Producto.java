package com.example.coffeequeque.Model;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private boolean disponible;

    public Producto(int idProducto, String nombre, double precio, boolean disponible) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    // getters
    public int getIdProducto() {
        return idProducto;
    }
    public String getNombre() {
        return nombre;
    }
    public double getPrecio() {
        return precio;
    }
    public boolean isDisponible() {
        return disponible;
    }

    // setters
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // metodos
    public void cambiarDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }
}
