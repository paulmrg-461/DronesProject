package dronesproject.model;

/**
 * Drone ligero.
 * Principio POO: Herencia - LightDrone es un tipo de Drone.
 */
public class LightDrone extends Drone {
    public LightDrone(String id, Zona zonaInicial) {
        super(id, 5.0, zonaInicial);
    }

    public LightDrone(String id) {
        super(id, 5.0); // Llama al constructor de Drone que asigna Zona.CENTRO por defecto
    }

    // Principio POO: Polimorfismo - Implementación específica para LightDrone.
    @Override
    public boolean cargarPaquete(Paquete paquete) {
        if (paquete.getPeso() <= this.capacidadCargaMaxima && this.paqueteActual == null) {
            this.paqueteActual = paquete;
            System.out.println("LightDrone " + id + " cargó " + paquete.getId() + " (Peso: " + paquete.getPeso() + "kg)");
            return true;
        } else if (this.paqueteActual != null) {
            System.out.println("LightDrone " + id + " ya tiene un paquete.");
            return false;
        } else {
            System.out.println("LightDrone " + id + " no puede cargar paquete " + paquete.getId() + ". Peso excede capacidad (" + paquete.getPeso() + "kg > " + this.capacidadCargaMaxima + "kg)");
            return false;
        }
    }
}