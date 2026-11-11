package com.example.coffeequeque;

import com.example.coffeequeque.Model.AdministradorProductos;
import com.example.coffeequeque.Model.Cajero;
import com.example.coffeequeque.Model.Cliente;
import com.example.coffeequeque.Model.Pedido;
import com.example.coffeequeque.Model.Producto;
import com.example.coffeequeque.Model.SesionCafeteria;
import com.example.coffeequeque.structures.ColaSimple;
import com.example.coffeequeque.structures.Pila;

public class PruebadeIntegracion {

    public static void main(String[] args) {
        System.out.println("PRUEBA DE LA CAFETERIA Y ESTRUCTURAS");

        // 1. obtenemos la sesion central de la cafeteria
        AdministradorProductos admin = SesionCafeteria.getAdministrador();
        ColaSimple<Pedido> cola = SesionCafeteria.getColaCocina();
        Pila<String> historial = SesionCafeteria.getHistorial();

        // 2. cree un cliente y un cajero para la prueba
        Cliente cliente1 = new Cliente(1, "Israel Torres", "israel@upvm.edu.mx", "1234", true, "1325221093");
        Cajero cajero1 = new Cajero(2, "Cajero1", "cajero@coffee.com", "1234", true, 1);

        // 3. el cliente revisa el menú
        System.out.println("\n-Cliente revisando el menu-");
        cliente1.verMenu();

        // 4. El cliente crea un pedido nuevo
        System.out.println("\n-Cliente pidiendo productos-");
        cliente1.crearPedido();
        Pedido pedido1 = new Pedido(1, cliente1, Pedido.estado.Pendiente, 0.0);

        // tenemos los productos del catalogo (1: Cafe, 3: Pan)
        Producto p1 = admin.consultarProducto(1);
        Producto p2 = admin.consultarProducto(3);

        cliente1.pedirProducto(pedido1, p1, 2);
        cliente1.pedirProducto(pedido1, p2, 1);

        System.out.println("Total del pedido: $" + pedido1.getTotal());

        // 5. guardamos el pedido en la Cola de cocina (FIFO)
        System.out.println("\n-Enviando pedido a la cocina (Cola FIFO)-");
        cola.encolar(pedido1);
        historial.apilar("Nuevo pedido #" + pedido1.getIdPedido() + " ingresado a cocina");

        System.out.println("Pedidos pendientes en la cola: " + cola.getTamanio());

        // 6. la cocina despacha el pedido y el cajero cobra
        System.out.println("\n--- Atendiendo el pedido ---");
        Pedido atendido = cola.desencolar();
        cajero1.cobrarPedido(atendido);
        cajero1.despacharPedido(atendido);
        historial.apilar("Pedido #" + atendido.getIdPedido() + " cobrado y entregado");

        System.out.println("Pedidos restantes en la cola: " + cola.getTamanio());

        // 7. verificamos la Pila de historial (LIFO)
        System.out.println("\n-Revisando la Pila de historial (LIFO)-");
        System.out.println("Ultima accion (Cima de la Pila): " + historial.verCima());
        System.out.println("Total de acciones en la Pila: " + historial.getTamanio());

        System.out.println("\n=SIGUIENTE ORDEN=");
    }
}