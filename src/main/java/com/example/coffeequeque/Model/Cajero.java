package com.example.coffeequeque.Model;

import com.example.coffeequeque.structures.Lista;

public class Cajero extends Usuario {
    private int numeroCaja;

    public Cajero(int id, String nom, String mail, String pass, boolean log, int numCaja) {
        super(id, nom, mail, pass, log);
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
    public void cobrarPedido(Pedido pedido) {
        pedido.cambiarEstado(Pedido.estado.Cobrado);
        System.out.println("Cajero " + getNombre() + " cobró el pedido #" + pedido.getIdPedido());
    }

    public void despacharPedido(Pedido pedido) {
        pedido.cambiarEstado(Pedido.estado.Despachado);
        System.out.println("Cajero " + getNombre() + " despachó el pedido #" + pedido.getIdPedido());
    }

    public void deshacerPedido(Pedido pedido) {
        pedido.cambiarEstado(Pedido.estado.Pendiente);
        System.out.println("Cajero " + getNombre() + " deshizo el pedido #" + pedido.getIdPedido());
    }

    public void verInventario(Lista<Producto> listaProductos) {
        System.out.println("--- INVENTARIO (Cajero " + getNombre() + ") ---");
        listaProductos.mostrar();
    }
}