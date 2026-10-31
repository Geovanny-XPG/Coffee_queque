package com.example.coffeequeque.Model;

public class Cliente extends Usuario {
    private String matricula;
    public Cliente(int id, String nom, String mail, String pass, String matricula) {
        super(id, nom, mail, pass);
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

}
