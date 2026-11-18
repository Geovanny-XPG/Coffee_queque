package com.example.coffeequeque.structures;

import com.example.coffeequeque.Model.Usuario;

public class ListaUsuarios {
    private Nodo<Usuario> cabeza;
    private int tamanio;

    public ListaUsuarios() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void agregarUsuario(Usuario usuario) {
        Nodo<Usuario> nuevoNodo = new Nodo<>(usuario);
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            Nodo<Usuario> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanio++;
    }

    // Devuelve el usuario con ese correo (sin distinguir mayúsculas) o null
    public Usuario buscarPorCorreo(String correo) {
        Nodo<Usuario> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getCorreo().equalsIgnoreCase(correo)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Usuario obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        Nodo<Usuario> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Mayor ID existente + 1
    public int siguienteId() {
        int max = 0;
        Nodo<Usuario> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().getId() > max) {
                max = actual.getDato().getId();
            }
            actual = actual.getSiguiente();
        }
        return max + 1;
    }

    public int getTamanio() {
        return tamanio;
    }
}