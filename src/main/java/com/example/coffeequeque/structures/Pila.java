package com.example.coffeequeque.structures;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Estructura de Datos Lineal: Pila (Stack)
 * Principio: LIFO (Last In, First Out)
 */
public class Pila<T> {
    private Nodo<T> cima;
    private int tamanio;

    // Constructor: Inicializa la pila vacía
    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    /**
     * Verifica si la pila no contiene elementos.
     */
    public boolean estaVacia() {
        return cima == null;
    }

    /**
     * Retorna el número actual de elementos en la pila.
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * 1. Apilar (Push): Inserta un nuevo elemento en la cima de la pila.
     */
    public void apilar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(cima);
        cima = nuevoNodo;
        tamanio++;
    }

    /**
     * 2. Desapilar (Pop): Extrae y elimina el elemento ubicado en la cima.
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new RuntimeException("Error: La pila se encuentra vacía, no se puede desapilar.");
        }
        T datoExtraido = cima.getDato();
        cima = cima.getSiguiente();
        tamanio--;
        return datoExtraido;
    }

    /**
     * 3. Ver Cima (Peek): Consulta el elemento superior sin eliminarlo.
     */
    public T verCima() {
        if (estaVacia()) {
            throw new RuntimeException("Error: La pila se encuentra vacía.");
        }
        return cima.getDato();
    }

    /**
     * Limpia o vacía completamente la pila.
     */
    public void vaciar() {
        cima = null;
        tamanio = 0;
    }
    // Metodo para mostrar visualmente la pila
    public ObservableList<T> toObservableList() {
        ObservableList<T> listaFX = FXCollections.observableArrayList();
        Nodo<T> actual = cima;
        while (actual != null) {
            listaFX.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return listaFX;
    }
}
