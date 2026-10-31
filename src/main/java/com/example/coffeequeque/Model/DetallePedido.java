package com.example.coffeequeque.Model;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double subtotal;
    public DetallePedido(Producto producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }
    // getters
    public Producto getProducto() {
        return producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getSubtotal() {
        return subtotal;
    }
    // setters
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // metodos
    public void calcularSubtotal() {}
}
