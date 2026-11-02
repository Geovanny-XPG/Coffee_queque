public class ColaSimple<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    // Constructor de la Cola
    public ColaSimple() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    // Método para verificar si la cola está vacía - O(1)
    public boolean estaVacia() {
        return frente == null;
    }

    // Método para agregar un elemento al final de la cola - O(1)
    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
        }
        fin = nuevoNodo;
        tamanio++;
    }

    // Método para remover y retornar el elemento del frente (FIFO) - O(1)
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía, no se pueden procesar más pedidos.");
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null; // Si se vació por completo, actualizamos el fin a null
        }
        tamanio--;
        return dato;
    }

    // Método para ver el elemento al frente sin extraerlo - O(1)
    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return frente.getDato();
    }

    // Método para obtener el tamaño actual - O(1)
    public int getTamanio() {
        return tamanio;
    }
}