package com.example.coffeequeque.Model;

public class Usuario {
    private int id;
    private String nombre, correo, password;
    private boolean log;

    public Usuario(int id, String nom, String mail, String pass, boolean log) {
        this.id = id;
        this.nombre = nom;
        this.correo = mail;
        this.password = pass;
        this.log = log;
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
    public boolean isLog() {
        return log;
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
    public void setLog(boolean log) {
        this.log = log;
    }
    // metodos
    public void login(String user, String pass) {
        if(this.correo.equals(user) && this.password.equals(pass)) {
            this.log = true;
            System.out.println("Usuario Logueado");
        }
        else {
            System.out.println("Datos de Acceso Incorrectos");
        }
    }
    public void logout() {
        if(this.log) {
            this.log = false;
            System.out.println("Sesión Terminada");
        }
        else {
            System.out.println("No hay sesiones activas.");
        }
    }

}
