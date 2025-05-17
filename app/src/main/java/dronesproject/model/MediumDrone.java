package dronesproject.model;

/**
 * Drone mediano.
 * Principio POO: Herencia - MediumDrone es un tipo de Drone.
 */
public class MediumDrone extends Drone {
    public MediumDrone(String id, Zona zonaInicial) {
        super(id, 10.0, zonaInicial);
    }

    public MediumDrone(String id) {
        super(id, 10.0); // Llama al constructor de Drone que asigna Zona.CENTRO por defecto
    }

    // Principio POO: Polimorfismo - Implementación específica para MediumDrone.
    @Override
    public boolean cargarPaquete(Paquete paquete) {
        if (paquete.getPeso() <= this.capacidadCargaMaxima && this.paqueteActual == null) {
            this.paqueteActual = paquete;
            System.out.println("MediumDrone " + id + " cargó " + paquete.getId() + " (Peso: " + paquete.getPeso() + "kg)");
            return true;
        } else if (this.paqueteActual != null) {
            System.out.println("MediumDrone " + id + " ya tiene un paquete.");
            return false;
        } else {
            System.out.println("MediumDrone " + id + " no puede cargar paquete " + paquete.getId() + ". Peso excede capacidad (" + paquete.getPeso() + "kg > " + this.capacidadCargaMaxima + "kg)");
            return false;
        }
    }
}