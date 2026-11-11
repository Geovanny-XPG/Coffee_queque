package com.example.coffeequeque.structures;

public class Lista<T> {

    private Nodo<T> cabeza;   // primer nodo de la lista
    private int tamaño;       // cantidad de elementos

    public Lista() {
        cabeza = null;
        tamaño = 0;
    }

    // Inserta al final de la lista
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamaño++;
    }

    // Elimina la primera aparición del dato. Devuelve true si lo eliminó
    public boolean eliminar(T dato) {
        if (estaVacia()) {
            System.out.println("La lista está vacía, no hay nada que eliminar.");
            return false;
        }

        // Caso 1: el dato está en la cabeza
        if (iguales(cabeza.getDato(), dato)) {
            cabeza = cabeza.getSiguiente();
            tamaño--;
            return true;
        }

        // Caso 2: el dato está en otra posición
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != null) {
            if (iguales(actual.getDato(), dato)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamaño--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false; // no se encontró
    }

    // Elimina el elemento de una posición (0 = primer elemento)
    public T eliminarEn(int posicion) {
        validarPosicion(posicion);

        T eliminado;
        if (posicion == 0) {
            eliminado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo<T> anterior = cabeza;
            for (int i = 0; i < posicion - 1; i++) {
                anterior = anterior.getSiguiente();
            }
            Nodo<T> objetivo = anterior.getSiguiente();
            eliminado = objetivo.getDato();
            anterior.setSiguiente(objetivo.getSiguiente());
        }
        tamaño--;
        return eliminado;
    }

    // Devuelve la posición del dato, o -1 si no existe
    public int buscar(T dato) {
        Nodo<T> actual = cabeza;
        int posicion = 0;
        while (actual != null) {
            if (iguales(actual.getDato(), dato)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }

    // Recupera el dato de una posición
    public T obtener(int posicion) {
        validarPosicion(posicion);

        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // Recorre e imprime todos los elementos
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("La lista está vacía.");
            return;
        }
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.print(actual.getDato() + " -> ");
            actual = actual.getSiguiente();
        }
        System.out.println("null");
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamaño() {
        return tamaño;
    }

    // ---------- Métodos auxiliares privados ----------

    private void validarPosicion(int posicion) {
        if (estaVacia()) {
            throw new IllegalStateException("La lista está vacía.");
        }
        if (posicion < 0 || posicion >= tamaño) {
            throw new IndexOutOfBoundsException(
                    "Posición inválida: " + posicion + ". Rango válido: 0 a " + (tamaño - 1));
        }
    }

    // Compara dos datos sin fallar si alguno es null
    private boolean iguales(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
