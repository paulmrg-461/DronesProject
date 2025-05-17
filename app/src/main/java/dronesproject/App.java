package dronesproject;

import dronesproject.model.LightDrone;
import dronesproject.model.MediumDrone;
import dronesproject.model.HeavyDrone;
import dronesproject.model.Paquete;
import dronesproject.service.SistemaEntregas;

// Estructura de datos: java.util.Vector podría usarse para una lista sincronizada si es necesario.
// Estructura de datos: java.util.Stack podría usarse para LIFO, por ejemplo, para deshacer acciones o rastrear rutas.

// Principio POO: Encapsulamiento - Los atributos de las clases Drone y Paquete son privados.
// Principio POO: Herencia - LightDrone, MediumDrone, HeavyDrone heredan de Drone.
// Principio POO: Polimorfismo - El método cargarPaquete se comporta diferente en cada tipo de Drone.

/**
 * Clase principal de la aplicación.
 */
public class App {
    public static void main(String[] args) {
        SistemaEntregas sistemaEntregas = new SistemaEntregas();

        // Crear y agregar drones a la flota
        sistemaEntregas.agregarDrone(new LightDrone("LD001"));
        sistemaEntregas.agregarDrone(new MediumDrone("MD001"));
        sistemaEntregas.agregarDrone(new HeavyDrone("HD001"));
        sistemaEntregas.agregarDrone(new LightDrone("LD002"));

        sistemaEntregas.mostrarEstadoFlota();

        // Crear paquetes y agregarlos a la cola de entregas
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P001", 3.5, "Calle Falsa 123"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P002", 8.0, "Avenida Siempreviva 742"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P003", 15.0, "Plaza Mayor 1"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P004", 1.0, "Calle Luna 24"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P005", 25.0, "Gran Vía 100")); // Este excederá la capacidad de todos

        // Procesar las entregas
        sistemaEntregas.procesarEntregas();

        sistemaEntregas.mostrarEstadoFlota();

        // Ejemplo de uso de Stack (Pila) - no directamente en la lógica de entrega principal aquí,
        // pero podría usarse para un historial de comandos o rutas.
        // java.util.Stack<String> historialOperaciones = new java.util.Stack<>();
        // historialOperaciones.push("Drone LD001 despegó");
        // historialOperaciones.push("Drone LD001 entregó P004");
        // System.out.println("Última operación: " + historialOperaciones.pop());

        // Ejemplo de uso de Matriz - podría ser para un mapa de celdas
        // int[][] mapa = new int[10][10]; // Matriz de 10x10
        // mapa[2][3] = 1; // Marcar celda (2,3) como visitada u obstáculo
    }
}
