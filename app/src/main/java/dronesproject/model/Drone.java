package dronesproject.model;

// Principio POO: Abstracción - Define la interfaz común para todos los drones.
public abstract class Drone {
    // Principio POO: Encapsulamiento - Atributos protegidos para ser accesibles por subclases.
    protected String id;
    protected double capacidadCargaMaxima; // en kg
    protected double nivelBateria; // en porcentaje
    protected Paquete paqueteActual;
    protected Zona zonaActual; // Nueva propiedad para la zona del drone

    public Drone(String id, double capacidadCargaMaxima, Zona zonaInicial) {
        this.id = id;
        this.capacidadCargaMaxima = capacidadCargaMaxima;
        this.nivelBateria = 100.0; // Batería llena al inicio
        this.paqueteActual = null;
        this.zonaActual = zonaInicial;
    }

    // Constructor sobrecargado para mantener compatibilidad o para drones sin zona inicial específica
    public Drone(String id, double capacidadCargaMaxima) {
        this(id, capacidadCargaMaxima, Zona.CENTRO); // Por defecto en la zona CENTRO
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

    public Zona getZonaActual() {
        return zonaActual;
    }

    public void setZonaActual(Zona zonaActual) {
        this.zonaActual = zonaActual;
    }

    /**
     * Intenta cargar un paquete en el drone.
     * Principio POO: Polimorfismo - Este método será sobrescrito por las subclases.
     * @param paquete El paquete a cargar.
     * @return true si el paquete fue cargado, false en caso contrario.
     */
    /**
     * Verifica si el drone puede cargar el paquete especificado, sin cargarlo realmente.
     * @param paquete El paquete a verificar.
     * @return true si el drone puede cargar el paquete, false en caso contrario.
     */
    public boolean puedeCargar(Paquete paquete) {
        return paquete.getPeso() <= this.capacidadCargaMaxima;
    }

    public abstract boolean cargarPaquete(Paquete paquete);

    public void entregarPaquete() {
        if (paqueteActual != null) {
            System.out.println("Drone " + id + " (Zona actual: " + zonaActual + ") entregando " + paqueteActual + " en " + paqueteActual.getDireccionDestino() + " (Zona destino: " + paqueteActual.getZonaDestino() + ")");
            Zona zonaDestinoPaquete = paqueteActual.getZonaDestino();
            this.paqueteActual = null;
            this.nivelBateria -= 10; // Simulación de consumo de batería base
            // Aquí se podría añadir un consumo de batería adicional basado en la distancia recorrida
            // this.nivelBateria -= calcularConsumoPorDistancia(getDistancia(this.zonaActual, zonaDestinoPaquete));
            this.setZonaActual(zonaDestinoPaquete); // Actualizar la zona del drone a la del paquete entregado
            System.out.println("Drone " + id + " ahora en zona " + this.zonaActual + ". Batería restante: " + this.nivelBateria + "%");
        } else {
            System.out.println("Drone " + id + " no tiene paquete para entregar.");
        }
    }

    // Método para calcular la distancia (requeriría acceso a SistemaEntregas o pasar la matriz de distancias)
    // Por simplicidad, este método no está completamente implementado aquí, ya que la matriz de distancias está en SistemaEntregas.
    // Una mejor solución sería que SistemaEntregas calcule la distancia y la pase al drone si es necesario para el consumo de batería.
    /* private int getDistancia(Zona origen, Zona destino) {
        // Lógica para obtener la distancia, posiblemente delegando a una clase de servicio o utilidad
        return 0; // Placeholder
    } */

    /* private double calcularConsumoPorDistancia(int distancia) {
        // Lógica para calcular el consumo de batería basado en la distancia
        return distancia * 0.5; // Ejemplo: 0.5% por unidad de distancia
    } */

    public void recargarBateria() {
        this.nivelBateria = 100.0;
        System.out.println("Drone " + id + " batería recargada.");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{id='" + id + "', cargaMax=" + capacidadCargaMaxima +
               ", bateria=" + nivelBateria + ", paquete=" + (paqueteActual != null ? paqueteActual.getId() : "ninguno") +
               ", zona=" + zonaActual + "}";
    }
}