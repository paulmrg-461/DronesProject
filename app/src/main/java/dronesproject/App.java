package dronesproject;

import dronesproject.model.LightDrone;
import dronesproject.model.MediumDrone;
import dronesproject.model.HeavyDrone;
import dronesproject.model.Paquete;
import dronesproject.service.SistemaEntregas;
import java.util.Scanner;
import java.util.InputMismatchException;

// Estructura de datos: java.util.Vector podría usarse para una lista sincronizada si es necesario.
// Estructura de datos: java.util.Stack podría usarse para LIFO, por ejemplo, para deshacer acciones o rastrear rutas.

// Principio POO: Encapsulamiento - Los atributos de las clases Drone y Paquete son privados.
// Principio POO: Herencia - LightDrone, MediumDrone, HeavyDrone heredan de Drone.
// Principio POO: Polimorfismo - El método cargarPaquete se comporta diferente en cada tipo de Drone.

/**
 * Clase principal de la aplicación.
 */
public class App {
    private static SistemaEntregas sistemaEntregas = new SistemaEntregas();
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // Datos iniciales de ejemplo (opcional, se pueden quitar si se prefiere empezar desde cero)
        cargarDatosIniciales();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir nueva línea

                switch (opcion) {
                    case 1:
                        agregarNuevoDrone();
                        break;
                    case 2:
                        agregarNuevoPaquete();
                        break;
                    case 3:
                        sistemaEntregas.procesarEntregas();
                        break;
                    case 4:
                        sistemaEntregas.mostrarEstadoFlota();
                        break;
                    case 5:
                        sistemaEntregas.mostrarMapaEntrega(); // Nueva opción
                        break;
                    case 6:
                        sistemaEntregas.deshacerUltimaAccion(); // Nueva opción
                        break;
                    case 7:
                        System.out.println("Saliendo del sistema...");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
            System.out.println(); // Línea en blanco para mejor legibilidad
        }
        scanner.close();
    }

    private static void cargarDatosIniciales() {
        sistemaEntregas.agregarDrone(new LightDrone("LD001"));
        sistemaEntregas.agregarDrone(new MediumDrone("MD001"));
        sistemaEntregas.agregarDrone(new HeavyDrone("HD001"));
        sistemaEntregas.agregarDrone(new LightDrone("LD002"));

        sistemaEntregas.agregarPedidoEntrega(new Paquete("P001", 3.5, "Tv 7 51N 24"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P002", 8.0, "Avenida Siempreviva 742"));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P003", 15.0, "Campanario local 17"));
    }

    private static void mostrarMenu() {
        System.out.println("--- Menú Sistema de Entregas de Drones ---");
        System.out.println("1. Agregar Drone");
        System.out.println("2. Agregar Paquete");
        System.out.println("3. Procesar Entregas");
        System.out.println("4. Mostrar Estado de la Flota");
        System.out.println("5. Mostrar Mapa de Entrega"); // Nueva opción
        System.out.println("6. Deshacer Última Acción"); // Nueva opción
        System.out.println("7. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void agregarNuevoDrone() {
        System.out.println("--- Agregar Nuevo Drone ---");
        System.out.print("Ingrese ID del drone: ");
        String id = scanner.nextLine();
        System.out.println("Seleccione tipo de drone:");
        System.out.println("1. Ligero (hasta 5kg)");
        System.out.println("2. Mediano (hasta 10kg)");
        System.out.println("3. Pesado (hasta 20kg)");
        System.out.print("Opción: ");
        try {
            int tipo = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            switch (tipo) {
                case 1:
                    sistemaEntregas.agregarDrone(new LightDrone(id));
                    break;
                case 2:
                    sistemaEntregas.agregarDrone(new MediumDrone(id));
                    break;
                case 3:
                    sistemaEntregas.agregarDrone(new HeavyDrone(id));
                    break;
                default:
                    System.out.println("Tipo de drone no válido.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida para el tipo de drone. Debe ser un número.");
            scanner.nextLine(); // Limpiar el buffer
        }
    }

    private static void agregarNuevoPaquete() {
        System.out.println("--- Agregar Nuevo Paquete ---");
        System.out.print("Ingrese ID del paquete: ");
        String id = scanner.nextLine();
        double peso = 0;
        boolean pesoValido = false;
        while(!pesoValido) {
            System.out.print("Ingrese peso del paquete (kg): ");
            try {
                peso = scanner.nextDouble();
                scanner.nextLine(); // Consumir nueva línea
                if (peso <= 0) {
                    System.out.println("El peso debe ser un valor positivo.");
                } else {
                    pesoValido = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para el peso. Debe ser un número.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
        System.out.print("Ingrese dirección de entrega: ");
        String direccion = scanner.nextLine();
        sistemaEntregas.agregarPedidoEntrega(new Paquete(id, peso, direccion));
    }
}
