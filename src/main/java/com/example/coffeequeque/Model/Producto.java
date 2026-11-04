package com.example.coffeequeque.Model;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private int stock;
    private boolean disponible;

    public Producto(int idProducto, String nombre, double precio, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.disponible = stock > 0;
    }

    // getters
    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public boolean isDisponible() { return disponible; }

    // setters
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) {
        this.stock = stock;
        this.disponible = stock > 0;
    }

    // metodos
    public void cambiarDisponibilidad(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return idProducto + " - " + nombre + " | $" + precio
                + " | stock: " + stock
                + " | " + (disponible ? "disponible" : "agotado");
    }
}
