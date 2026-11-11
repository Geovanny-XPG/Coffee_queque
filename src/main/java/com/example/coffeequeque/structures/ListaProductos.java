package com.example.coffeequeque.structures;

import com.example.coffeequeque.Model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListaProductos {
    private Nodo<Producto> cabeza;
    private int tamanio;

    public ListaProductos() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregarProducto(Producto producto) {
        Nodo<Producto> nuevoNodo = new Nodo<>(producto);
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            Nodo<Producto> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    public Producto buscarPorId(int idProducto) {
        Nodo<Producto> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getIdProducto() == idProducto) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean eliminarPorId(int idProducto) {
        if (estaVacia()) {
            return false;
        }

        if (cabeza.getDato().getIdProducto() == idProducto) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }

        Nodo<Producto> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().getIdProducto() == idProducto) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void mostrarProductos() {
        Nodo<Producto> actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public ObservableList<Producto> toObservableList() {
        ObservableList<Producto> listaFX = FXCollections.observableArrayList();
        Nodo<Producto> actual = cabeza;
        while (actual != null) {
            listaFX.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return listaFX;
    }
}
