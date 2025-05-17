package dronesproject.model;

// Principio POO: Encapsulamiento - Los atributos de las clases Drone y Paquete son privados.
// Principio POO: Herencia - LightDrone, MediumDrone, HeavyDrone heredan de Drone.
// Principio POO: Polimorfismo - El método cargarPaquete se comporta diferente en cada tipo de Drone.

/**
 * Representa un paquete a ser entregado.
 */
public class Paquete {
    private String id;
    private double peso; // en kg
    private String direccionDestino;
    private Zona zonaDestino;

    // Principio POO: Encapsulamiento - Constructor para inicializar el objeto Paquete.
    public Paquete(String id, double peso, String direccionDestino, Zona zonaDestino) {
        this.id = id;
        this.peso = peso;
        this.direccionDestino = direccionDestino;
        this.zonaDestino = zonaDestino;
    }

    // Principio POO: Encapsulamiento - Getters para acceder a los atributos.
    public String getId() {
        return id;
    }

    public double getPeso() {
        return peso;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public Zona getZonaDestino() {
        return zonaDestino;
    }

    @Override
    public String toString() {
        return "Paquete{id='" + id + "', peso=" + peso + ", destino='" + direccionDestino + "', zona=" + zonaDestino + "'}";
    }
}