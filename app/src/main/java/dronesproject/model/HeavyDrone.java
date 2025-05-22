package dronesproject.model;

/**
 * Drone pesado.
 * Principio POO: Herencia - HeavyDrone es un tipo de Drone.
 */
public class HeavyDrone extends Drone {
    public HeavyDrone(String id, Zona zonaInicial) {
        super(id, 20.0, zonaInicial);
    }

    public HeavyDrone(String id) {
        super(id, 20.0); // Llama al constructor de Drone que asigna Zona.CENTRO por defecto
    }

    // Principio POO: Polimorfismo - Implementación específica para HeavyDrone.
    @Override
    public boolean cargarPaquete(Paquete paquete) {
        if (paquete.getPeso() <= this.capacidadCargaMaxima && this.paqueteActual == null) {
            this.paqueteActual = paquete;
            System.out.println("HeavyDrone " + id + " cargó " + paquete.getId() + " (Peso: " + paquete.getPeso() + "kg)");
            return true;
        } else if (this.paqueteActual != null) {
            System.out.println("HeavyDrone " + id + " ya tiene un paquete.");
            return false;
        } else {
            System.out.println("HeavyDrone " + id + " no puede cargar paquete " + paquete.getId() + ". Peso excede capacidad (" + paquete.getPeso() + "kg > " + this.capacidadCargaMaxima + "kg)");
            return false;
        }
    }
}
