package com.example.coffeequeque.Model;

public class Usuario {
    private int id;
    private String nombre, correo, password;

    public Usuario(int id, String nom, String mail, String pass) {
        this.id = id;
        this.nombre = nom;
        this.correo = mail;
    }

    //getters
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getCorreo() {
        return correo;
    }
    public String getPassword() {
        return password;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        System.out.println("Has iniciado sesion");
    }
    public void logout() {
        System.out.println("Has finalizado sesion");
    }

}
