package com.example.coffeequeue.structures;

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
}
