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

/**
 * Clase que gestiona la lógica de negocio del sistema de entregas con drones.
 */
public class SistemaEntregas {
    private List<Drone> flotaDrones;
    private Queue<Paquete> colaDeEntregas;

    public SistemaEntregas() {
        this.flotaDrones = new ArrayList<>();
        this.colaDeEntregas = new LinkedList<>();
    }

    public void agregarDrone(Drone drone) {
        this.flotaDrones.add(drone);
        System.out.println(drone.getClass().getSimpleName() + " " + drone.getId() + " agregado a la flota.");
    }

    public void agregarPedidoEntrega(Paquete paquete) {
        this.colaDeEntregas.offer(paquete);
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

    // El método main se moverá a la clase App principal del paquete dronesproject
    // public static void main(String[] args) { ... }
}