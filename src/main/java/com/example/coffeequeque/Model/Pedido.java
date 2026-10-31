package com.example.coffeequeque.Model;

public class Pedido {
    public enum estado {Pendiente, Cobrado, Despachado};

    private int idPedido;
    private Cliente cliente;
    private estado estado;
    private double total;

    public Pedido(int idPedido, Cliente cliente, estado estado, double total) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.estado = estado;
        this.total = total;
    }

    // getters
    public int getIdPedido() {
        return idPedido;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public estado getEstado() {
        return estado;
    }
    public double getTotal() {
        return total;
    }
    // setters
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    // metodos
    public void calcularTotal(){}
    public void cambiarEstado(estado estado) {
        this.estado = estado;
    }

}
