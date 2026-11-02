package com.example.coffeequeque.Model;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private String notas;
    //Constructor
    public DetallePedido(Producto producto, int cantidad, String notas) {
        if (cantidad<=0){
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio;
        this.subtotal = calcularSubtotal();
        this.notas = (notas != null) ? notas : "";
    }
    //Constructor alternativo sin las notas del pedido
    public DetllePedido(Producto producto, int cantidad) {
        this(producto, cantidad, "");
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
    public double getPrecioUnitario(); {
        return precioUnitario;
    }
    public double getSubtotal(); {
        return Subtotal;
    }
    public String getNotas();{
        return notas;
    }
    // setters
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.precioUnitario = producto.getprecio();
        this.subtotal = calcularSubtotal();
    }
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.cantidad = cantidad;
        this subtotal = calcularSubtotal();
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    public void setNotas(String notas) {
        this.notas = notas;
    }

    // metodos
    public void calcularSubtotal() {
        return this.cantidad * this.precioUnitaario;
    }
    
}
