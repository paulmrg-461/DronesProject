package dronesproject;

import dronesproject.model.LightDrone;
import dronesproject.model.MediumDrone;
import dronesproject.model.HeavyDrone;
import dronesproject.model.Paquete;
import dronesproject.model.Zona;
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
        // Todos los drones inician en la base (CENTRO por defecto o especificado)
        sistemaEntregas.agregarDrone(new LightDrone("LD001", Zona.CENTRO));
        sistemaEntregas.agregarDrone(new LightDrone("LD002", Zona.CENTRO));
        sistemaEntregas.agregarDrone(new MediumDrone("MD001", Zona.CENTRO));
        sistemaEntregas.agregarDrone(new HeavyDrone("HD001", Zona.CENTRO));

        sistemaEntregas.agregarPedidoEntrega(new Paquete("P001", 3.5, "Tv 7 51N 24", Zona.NORTE));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P002", 4.5, "Tv 7 51N 26", Zona.NORTE));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P003", 8.0, "Avenida Siempreviva 742", Zona.CENTRO));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P004", 15.0, "Campanario local 17", Zona.SUR));
        sistemaEntregas.agregarPedidoEntrega(new Paquete("P005", 20.0, "Campanario local 18", Zona.SUR));
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
        System.out.print("Opción de Tipo: ");
        try {
            int tipo = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            System.out.println("Seleccione Zona Inicial del Drone:");
            Zona[] zonas = Zona.values();
            for (int i = 0; i < zonas.length; i++) {
                System.out.println((i + 1) + ". " + zonas[i]);
            }
            System.out.print("Opción de Zona: ");
            Zona zonaSeleccionada = Zona.CENTRO; // Por defecto CENTRO si hay error
            boolean zonaValida = false;
            while(!zonaValida){
                try {
                    int opcionZona = scanner.nextInt();
                    scanner.nextLine(); // Consumir nueva línea
                    if (opcionZona > 0 && opcionZona <= zonas.length) {
                        zonaSeleccionada = zonas[opcionZona - 1];
                        zonaValida = true;
                    } else {
                        System.out.println("Opción de zona no válida. Intente de nuevo.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida para la zona. Debe ser un número. Se asignará CENTRO por defecto.");
                    scanner.nextLine(); // Limpiar el buffer
                    zonaValida = true; // Salir del bucle, se usa el defecto
                }
            }

            switch (tipo) {
                case 1:
                    sistemaEntregas.agregarDrone(new LightDrone(id, zonaSeleccionada));
                    break;
                case 2:
                    sistemaEntregas.agregarDrone(new MediumDrone(id, zonaSeleccionada));
                    break;
                case 3:
                    sistemaEntregas.agregarDrone(new HeavyDrone(id, zonaSeleccionada));
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

        System.out.println("Seleccione Zona de Destino:");
        Zona[] zonas = Zona.values();
        for (int i = 0; i < zonas.length; i++) {
            System.out.println((i + 1) + ". " + zonas[i]);
        }
        System.out.print("Opción de Zona: ");
        Zona zonaSeleccionada = null;
        boolean zonaValida = false;
        while(!zonaValida){
            try {
                int opcionZona = scanner.nextInt();
                scanner.nextLine(); // Consumir nueva línea
                if (opcionZona > 0 && opcionZona <= zonas.length) {
                    zonaSeleccionada = zonas[opcionZona - 1];
                    zonaValida = true;
                } else {
                    System.out.println("Opción de zona no válida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida para la zona. Debe ser un número.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
        sistemaEntregas.agregarPedidoEntrega(new Paquete(id, peso, direccion, zonaSeleccionada));
    }
}
