package dronesproject.service;

import dronesproject.model.Drone;
import dronesproject.model.Paquete;
import dronesproject.model.LightDrone;
import dronesproject.model.MediumDrone;
import dronesproject.model.HeavyDrone;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack; // Para la funcionalidad de deshacer

/**
 * Clase que gestiona la lógica de negocio del sistema de entregas con drones.
 */
public class SistemaEntregas {
    // Clase interna para representar una acción en el historial para deshacer
    private static class AccionHistorial {
        enum TipoAccion { DRONE_AGREGADO, PAQUETE_AGREGADO }
        TipoAccion tipo;
        Object objeto;

        AccionHistorial(TipoAccion tipo, Object objeto) {
            this.tipo = tipo;
            this.objeto = objeto;
        }
    }

    private List<Drone> flotaDrones;
    private Queue<Paquete> colaDeEntregas;
    private Stack<AccionHistorial> historialAcciones; // Pila para deshacer
    private String[][] mapaEntrega; // Matriz para el mapa de entrega conceptual

    public SistemaEntregas() {
        this.flotaDrones = new ArrayList<>();
        this.colaDeEntregas = new LinkedList<>();
        this.historialAcciones = new Stack<>();
        inicializarMapaEntrega(5, 5); // Inicializar un mapa de 5x5 por defecto
    }

    private void inicializarMapaEntrega(int filas, int columnas) {
        this.mapaEntrega = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                this.mapaEntrega[i][j] = "[ ]"; // Espacio vacío
            }
        }
        // Ejemplo: Marcar algunas ubicaciones (conceptual)
        if (filas > 0 && columnas > 0) this.mapaEntrega[0][0] = "[B]"; // Base
        if (filas > 1 && columnas > 1) this.mapaEntrega[1][1] = "[P]"; // Punto de entrega
    }

    public void mostrarMapaEntrega() {
        System.out.println("\n--- Mapa de Entrega ---");
        for (int i = 0; i < mapaEntrega.length; i++) {
            for (int j = 0; j < mapaEntrega[i].length; j++) {
                System.out.print(mapaEntrega[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void agregarDrone(Drone drone) {
        this.flotaDrones.add(drone);
        this.historialAcciones.push(new AccionHistorial(AccionHistorial.TipoAccion.DRONE_AGREGADO, drone));
        System.out.println(drone.getClass().getSimpleName() + " " + drone.getId() + " agregado a la flota.");
    }

    public void agregarPedidoEntrega(Paquete paquete) {
        this.colaDeEntregas.offer(paquete);
        this.historialAcciones.push(new AccionHistorial(AccionHistorial.TipoAccion.PAQUETE_AGREGADO, paquete));
        System.out.println("Pedido para " + paquete + " agregado a la cola de entregas.");
    }

    public void procesarEntregas() {
        System.out.println("\n--- Procesando Entregas ---");
        while (!colaDeEntregas.isEmpty()) {
            Paquete paqueteAEntregar = colaDeEntregas.poll();
            System.out.println("Procesando: " + paqueteAEntregar);

            boolean asignado = false;
            for (Drone drone : flotaDrones) {
                if (drone.getPaqueteActual() == null && drone.getNivelBateria() > 20) {
                    if (drone.cargarPaquete(paqueteAEntregar)) {
                        drone.entregarPaquete();
                        asignado = true;
                        break;
                    }
                }
            }
            if (!asignado) {
                System.out.println("No hay drones disponibles o adecuados para " + paqueteAEntregar.getId() + ". Paquete devuelto a la cola.");
                colaDeEntregas.offer(paqueteAEntregar);
                break; 
            }
        }
        if (colaDeEntregas.isEmpty()){
            System.out.println("Todas las entregas procesadas.");
        }
    }

    public void mostrarEstadoFlota() {
        System.out.println("\n--- Estado de la Flota ---");
        for (Drone drone : flotaDrones) {
            System.out.println(drone);
        }
    }

    public void deshacerUltimaAccion() {
        if (historialAcciones.isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }

        AccionHistorial ultimaAccion = historialAcciones.pop();
        switch (ultimaAccion.tipo) {
            case DRONE_AGREGADO:
                Drone droneQuitado = (Drone) ultimaAccion.objeto;
                flotaDrones.remove(droneQuitado);
                System.out.println("Deshecho: Se quitó el drone " + droneQuitado.getId());
                break;
            case PAQUETE_AGREGADO:
                Paquete paqueteQuitado = (Paquete) ultimaAccion.objeto;
                // Intentar quitar de la cola. Si ya fue procesado, no estará.
                if (colaDeEntregas.remove(paqueteQuitado)) {
                    System.out.println("Deshecho: Se quitó el paquete " + paqueteQuitado.getId() + " de la cola.");
                } else {
                    System.out.println("Deshecho: El paquete " + paqueteQuitado.getId() + " ya no estaba en la cola (posiblemente procesado).");
                }
                break;
        }
    }

    // El método main se moverá a la clase App principal del paquete dronesproject
    // public static void main(String[] args) { ... }
}