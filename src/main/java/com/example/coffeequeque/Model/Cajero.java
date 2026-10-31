package com.example.coffeequeque.Model;

public class Cajero extends Usuario {
    private int numeroCaja;
    public Cajero(int id, String nom, String mail, String pass, int numCaja) {
        super(id, nom, mail, pass);
        this.numeroCaja = numCaja;
    }

    // getters
    public int getNumeroCaja() {
        return numeroCaja;
    }
    // setters
    public void setNumeroCaja(int numeroCaja) {
        this.numeroCaja = numeroCaja;
    }
    // metodos
    public void cobrarPedido() {
        System.out.println("Cobrando Pedido");
    }
    public void despacharPedido() {
        System.out.println("Despachando Pedido");
    }
    public void deshacerCobro() {
        System.out.println("Deshaciendo cobro");
    }
}
