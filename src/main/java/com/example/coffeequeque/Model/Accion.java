package com.example.coffeequeque.Model;

public class Accion {
    public enum Tipo { VENTA_REGISTRADA, COMANDA_LISTA, PEDIDO_ENTREGADO }

    // Atributos de la clase
    private Tipo tipo;
    private Pedido pedido;
    private Pedido.estado estadoAnterior;
    private String descripcion;

    // Constructor
    public Accion(Tipo tipo, Pedido pedido, Pedido.estado estadoAnterior, String descripcion) {
        this.tipo = tipo;
        this.pedido = pedido;
        this.estadoAnterior = estadoAnterior;
        this.descripcion = descripcion;
    }

    // getters y setters
    public Tipo getTipo() {
        return tipo;
    }
    public Pedido getPedido() {
        return pedido;
    }
    public Pedido.estado getEstadoAnterior() {
        return estadoAnterior;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public void setEstadoAnterior(Pedido.estado estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {  return descripcion; }
}
