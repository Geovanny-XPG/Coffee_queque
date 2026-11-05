package com.example.coffeequeque.Model;

public class Cliente extends Usuario {
    private String matricula;
    public Cliente(int id, String nom, String mail, String pass, boolean log, String matricula) {
        super(id, nom, mail, pass, log);
        this.matricula = matricula;
    }

    // getters
    public String getMatricula() {
        return matricula;
    }
    //setters
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    // metodos
    public void verMenu() {
        System.out.println("Menu");
    }
    public void crearPedido() {
        System.out.println("Creando Pedido");
    }

    public void pedirProducto(Pedido pedido, Producto producto, int cantidad) {
        if (producto.isDisponible()) {
            pedido.agregarProducto(producto, cantidad);
            System.out.println("Agregado " + producto.getNombre() + " al pedido.");
        } else {
            System.out.println("El producto no esta disponible.");}
    }
    public void cancelarPedido(Pedido pedido){
        pedido.cambiarEstado(Pedido.estado.Despachado);
        System.out.println("El pedido fue #" + pedido.getIdPedido() + "fue cancelado.");
    }

}
