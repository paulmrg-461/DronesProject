package dronesproject.model;

// Principio POO: Abstracción - Define la interfaz común para todos los drones.
public abstract class Drone {
    // Principio POO: Encapsulamiento - Atributos protegidos para ser accesibles por subclases.
    protected String id;
    protected double capacidadCargaMaxima; // en kg
    protected double nivelBateria; // en porcentaje
    protected Paquete paqueteActual;

    public Drone(String id, double capacidadCargaMaxima) {
        this.id = id;
        this.capacidadCargaMaxima = capacidadCargaMaxima;
        this.nivelBateria = 100.0; // Batería llena al inicio
        this.paqueteActual = null;
    }

    // Principio POO: Encapsulamiento - Getters
    public String getId() {
        return id;
    }

    public double getNivelBateria() {
        return nivelBateria;
    }

    public Paquete getPaqueteActual() {
        return paqueteActual;
    }

    /**
     * Intenta cargar un paquete en el drone.
     * Principio POO: Polimorfismo - Este método será sobrescrito por las subclases.
     * @param paquete El paquete a cargar.
     * @return true si el paquete fue cargado, false en caso contrario.
     */
    public abstract boolean cargarPaquete(Paquete paquete);

    public void entregarPaquete() {
        if (paqueteActual != null) {
            System.out.println("Drone " + id + " entregando " + paqueteActual + " en " + paqueteActual.getDireccionDestino());
            this.paqueteActual = null;
            this.nivelBateria -= 10; // Simulación de consumo de batería
        } else {
            System.out.println("Drone " + id + " no tiene paquete para entregar.");
        }
    }

    public void recargarBateria() {
        this.nivelBateria = 100.0;
        System.out.println("Drone " + id + " batería recargada.");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{id='" + id + "', cargaMax=" + capacidadCargaMaxima +
               ", bateria=" + nivelBateria + ", paquete=" + (paqueteActual != null ? paqueteActual.getId() : "ninguno") + "}";
    }
}