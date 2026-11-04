package com.example.coffeequeque.Model;

import com.example.coffeequeue.structures.ListaProductos;

public class AdministradorProductos {
    private ListaProductos catalogo;

    public AdministradorProductos() {
        this.catalogo = new ListaProductos();
    }

    public void registrarNuevoProducto(int id, String nombre, double precio, int stock) {
        if (catalogo.buscarPorId(id) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el ID especificado.");
        }
        Producto nuevo = new Producto(id, nombre, precio, stock);
        catalogo.agregarProducto(nuevo);
    }

    public boolean darDeBajaProducto(int id) {
        return catalogo.eliminarPorId(id);
    }

    public Producto consultarProducto(int id) {
        Producto p = catalogo.buscarPorId(id);
        if (p == null) {
            throw new IllegalStateException("El producto no se encuentra en el catálogo.");
        }
        return p;
    }

    public void actualizarStock(int id, int nuevoStock) {
        Producto p = consultarProducto(id);
        p.setStock(nuevoStock);
    }

    public void listarInventario() {
        if (catalogo.estaVacia()) {
            System.out.println("El catálogo está vacío.");
        } else {
            catalogo.mostrarProductos();
        }
    }
}
