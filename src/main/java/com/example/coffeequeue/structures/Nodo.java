package com.example.coffeequeue.structures;
//Creacion de Nodo
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;
    // El constructor crea cuando cambiemos la info
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null; //Apunta null de momento
    }
// Getters y Setters para leer y modificar datod
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}