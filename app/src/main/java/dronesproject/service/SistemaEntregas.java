package dronesproject.service;

import dronesproject.model.Drone;
import dronesproject.model.Paquete;
import dronesproject.model.Zona;

import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Collection;
import java.util.Stack; // Para la funcionalidad de deshacer
import java.util.stream.Collectors; // Para convertir a Collection si es necesario
import dronesproject.model.LightDrone;
import dronesproject.model.MediumDrone;
import dronesproject.model.HeavyDrone;

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

    // Variable para seleccionar la implementación de la flota de drones
    // true: usa TreeMap (ordenado por ID, acceso logarítmico)
    // false: usa ArrayList (orden de inserción, búsqueda por ID lineal)
    private boolean usarTreeMapParaFlota = false; // Cambiar a false para usar ArrayList

    private Map<String, Drone> flotaDronesMap;
    private List<Drone> flotaDronesList;

    private Queue<Paquete> colaDeEntregas;
    private Stack<AccionHistorial> historialAcciones; // Pila para deshacer
    private String[][] mapaEntrega; // Matriz para el mapa de entrega conceptual
    private int[][] matrizDistancias; // Matriz de distancias entre zonas

    public SistemaEntregas() {
        if (usarTreeMapParaFlota) {
            this.flotaDronesMap = new TreeMap<>();
            this.flotaDronesList = null; // No se usa
        } else {
            this.flotaDronesList = new ArrayList<>();
            this.flotaDronesMap = null; // No se usa
        }
        this.colaDeEntregas = new LinkedList<>();
        this.historialAcciones = new Stack<>();
        inicializarMapaEntrega(5, 5); // Inicializar un mapa de 5x5 por defecto
        inicializarMatrizDistancias();
    }

    private void inicializarMatrizDistancias() {
        // Ejemplo de matriz de distancias (simulada)
        // Filas y columnas corresponden al ordinal de la Zona (NORTE=0, SUR=1, etc.)
        // Distancia de una zona a sí misma es 0.
        // Estos valores deberían ser configurables o basados en datos reales.
        int numZonas = Zona.values().length;
        matrizDistancias = new int[numZonas][numZonas];
        //       NORTE SUR ESTE OESTE CENTRO
        // NORTE
        matrizDistancias[Zona.NORTE.ordinal()][Zona.NORTE.ordinal()] = 0;
        matrizDistancias[Zona.NORTE.ordinal()][Zona.SUR.ordinal()] = 10;
        matrizDistancias[Zona.NORTE.ordinal()][Zona.ESTE.ordinal()] = 5;
        matrizDistancias[Zona.NORTE.ordinal()][Zona.OESTE.ordinal()] = 7;
        matrizDistancias[Zona.NORTE.ordinal()][Zona.CENTRO.ordinal()] = 3;
        // SUR
        matrizDistancias[Zona.SUR.ordinal()][Zona.NORTE.ordinal()] = 10;
        matrizDistancias[Zona.SUR.ordinal()][Zona.SUR.ordinal()] = 0;
        matrizDistancias[Zona.SUR.ordinal()][Zona.ESTE.ordinal()] = 8;
        matrizDistancias[Zona.SUR.ordinal()][Zona.OESTE.ordinal()] = 6;
        matrizDistancias[Zona.SUR.ordinal()][Zona.CENTRO.ordinal()] = 4;
        // ESTE
        matrizDistancias[Zona.ESTE.ordinal()][Zona.NORTE.ordinal()] = 5;
        matrizDistancias[Zona.ESTE.ordinal()][Zona.SUR.ordinal()] = 8;
        matrizDistancias[Zona.ESTE.ordinal()][Zona.ESTE.ordinal()] = 0;
        matrizDistancias[Zona.ESTE.ordinal()][Zona.OESTE.ordinal()] = 12;
        matrizDistancias[Zona.ESTE.ordinal()][Zona.CENTRO.ordinal()] = 2;
        // OESTE
        matrizDistancias[Zona.OESTE.ordinal()][Zona.NORTE.ordinal()] = 7;
        matrizDistancias[Zona.OESTE.ordinal()][Zona.SUR.ordinal()] = 6;
        matrizDistancias[Zona.OESTE.ordinal()][Zona.ESTE.ordinal()] = 12;
        matrizDistancias[Zona.OESTE.ordinal()][Zona.OESTE.ordinal()] = 0;
        matrizDistancias[Zona.OESTE.ordinal()][Zona.CENTRO.ordinal()] = 5;
        // CENTRO
        matrizDistancias[Zona.CENTRO.ordinal()][Zona.NORTE.ordinal()] = 3;
        matrizDistancias[Zona.CENTRO.ordinal()][Zona.SUR.ordinal()] = 4;
        matrizDistancias[Zona.CENTRO.ordinal()][Zona.ESTE.ordinal()] = 2;
        matrizDistancias[Zona.CENTRO.ordinal()][Zona.OESTE.ordinal()] = 5;
        matrizDistancias[Zona.CENTRO.ordinal()][Zona.CENTRO.ordinal()] = 0;
    }

    public int getDistancia(Zona origen, Zona destino) {
        if (origen == null || destino == null) {
            // Considerar un valor alto o manejar el error según la lógica de negocio
            System.out.println("Advertencia: Zona de origen o destino es nula al calcular distancia.");
            return Integer.MAX_VALUE; 
        }
        return matrizDistancias[origen.ordinal()][destino.ordinal()];
    }

    // Método helper para obtener la colección de drones activa
    private Collection<Drone> getFlotaDronesActiva() {
        return usarTreeMapParaFlota ? flotaDronesMap.values() : flotaDronesList;
    }

    // Método helper para obtener un drone por ID de la colección activa
    private Drone getDronePorId(String id) {
        if (usarTreeMapParaFlota) {
            return flotaDronesMap.get(id);
        } else {
            for (Drone drone : flotaDronesList) {
                if (drone.getId().equals(id)) {
                    return drone;
                }
            }
            return null;
        }
    }

    // Método helper para eliminar un drone por ID de la colección activa
    private Drone removerDronePorId(String id) {
        if (usarTreeMapParaFlota) {
            return flotaDronesMap.remove(id);
        } else {
            Drone droneARemover = null;
            for (Drone drone : flotaDronesList) {
                if (drone.getId().equals(id)) {
                    droneARemover = drone;
                    break;
                }
            }
            if (droneARemover != null) {
                flotaDronesList.remove(droneARemover);
            }
            return droneARemover;
        }
    }

    // Método helper para verificar si la flota está vacía
    private boolean isFlotaVacia() {
        return usarTreeMapParaFlota ? flotaDronesMap.isEmpty() : flotaDronesList.isEmpty();
    }

    private void inicializarMapaEntrega(int filas, int columnas) {
        this.mapaEntrega = new String[filas][columnas];
        limpiarMapa(); // Llama a limpiarMapa para inicializar con espacios vacíos
    }

    private void limpiarMapa() {
        for (int i = 0; i < mapaEntrega.length; i++) {
            for (int j = 0; j < mapaEntrega[i].length; j++) {
                this.mapaEntrega[i][j] = "[ ]"; // Espacio vacío
            }
        }
    }

    private void actualizarRepresentacionMapa() {
        limpiarMapa(); // Limpia el mapa antes de actualizar

        // 1. Marcar la Base
        if (mapaEntrega.length > 0 && mapaEntrega[0].length > 0) {
            mapaEntrega[0][0] = "[B]"; // Base en la esquina superior izquierda
        }

        // 2. Representar drones disponibles en la base (fila 0, junto a la base)
        int dronesEnBaseCol = 1; // Columna para empezar a colocar drones en base
        for (Drone drone : getFlotaDronesActiva()) {
            if (drone.getPaqueteActual() == null) { // Drone disponible
                if (mapaEntrega.length > 0 && dronesEnBaseCol < mapaEntrega[0].length) {
                    mapaEntrega[0][dronesEnBaseCol] = "[D]"; // Drone disponible
                    dronesEnBaseCol++;
                }
            }
        }

        // 3. Representar drones en entrega (fila 1)
        int dronesEnEntregaCol = 0;
        for (Drone drone : getFlotaDronesActiva()) {
            if (drone.getPaqueteActual() != null) { // Drone con paquete
                if (mapaEntrega.length > 1 && dronesEnEntregaCol < mapaEntrega[1].length) {
                    mapaEntrega[1][dronesEnEntregaCol] = "[d]"; // Drone en entrega
                    dronesEnEntregaCol++;
                }
            }
        }

        // 4. Representar paquetes pendientes en la cola (fila 2)
        int paquetesPendientesCol = 0;
        for (Paquete paquete : colaDeEntregas) {
            if (mapaEntrega.length > 2 && paquetesPendientesCol < mapaEntrega[2].length) {
                mapaEntrega[2][paquetesPendientesCol] = "[P]"; // Paquete pendiente
                paquetesPendientesCol++;
            }
        }
    }

    public void mostrarMapaEntrega() {
        actualizarRepresentacionMapa(); // Actualiza el mapa antes de mostrarlo
        System.out.println("\n--- Mapa de Entrega Conceptual ---");
        System.out.println("Leyenda: [B]=Base, [D]=Drone Disponible, [d]=Drone en Entrega, [P]=Paquete Pendiente");
        for (int i = 0; i < mapaEntrega.length; i++) {
            for (int j = 0; j < mapaEntrega[i].length; j++) {
                System.out.print(mapaEntrega[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void agregarDrone(Drone drone) {
        if (usarTreeMapParaFlota) {
            this.flotaDronesMap.put(drone.getId(), drone);
        } else {
            // Verificar si ya existe un drone con el mismo ID para evitar duplicados si es necesario
            boolean existe = false;
            for (Drone d : flotaDronesList) {
                if (d.getId().equals(drone.getId())) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                this.flotaDronesList.add(drone);
            } else {
                System.out.println("Error: Ya existe un drone con el ID " + drone.getId() + ". No se agregó.");
                return; // No agregar al historial si no se agregó a la flota
            }
        }
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
        Queue<Paquete> paquetesNoAsignadosTemporalmente = new LinkedList<>();

        while (!colaDeEntregas.isEmpty()) {
            Paquete paqueteAEntregar = colaDeEntregas.poll();
            System.out.println("Procesando: " + paqueteAEntregar);

            Drone droneAsignado = encontrarMejorDroneParaPaquete(paqueteAEntregar);

            if (droneAsignado != null) {
                if (droneAsignado.cargarPaquete(paqueteAEntregar)) {
                    int distancia = getDistancia(droneAsignado.getZonaActual(), paqueteAEntregar.getZonaDestino());
                    System.out.println("Drone " + droneAsignado.getId() + " (" + droneAsignado.getClass().getSimpleName() + ", Zona: " + droneAsignado.getZonaActual() + ") asignado para paquete " + paqueteAEntregar.getId() + " (Zona Destino: " + paqueteAEntregar.getZonaDestino() + ", Distancia: " + distancia + " unidades).");
                    droneAsignado.entregarPaquete();
                } else {
                    // Esto no debería suceder si encontrarMejorDroneParaPaquete y puedeCargar son consistentes
                    System.out.println("Error: Drone " + droneAsignado.getId() + " no pudo cargar el paquete " + paqueteAEntregar.getId() + ". Paquete devuelto a la cola.");
                    paquetesNoAsignadosTemporalmente.offer(paqueteAEntregar);
                }
            } else {
                System.out.println("No hay drones disponibles o adecuados para " + paqueteAEntregar.getId() + " en este momento. Paquete devuelto a la cola.");
                paquetesNoAsignadosTemporalmente.offer(paqueteAEntregar);
            }
        }
        // Reintegrar paquetes no asignados a la cola principal para futuros intentos
        while(!paquetesNoAsignadosTemporalmente.isEmpty()){
            colaDeEntregas.offer(paquetesNoAsignadosTemporalmente.poll());
        }

        if (colaDeEntregas.isEmpty()) {
            System.out.println("Todas las entregas procesadas.");
        } else {
            System.out.println(colaDeEntregas.size() + " paquete(s) no pudieron ser asignados y permanecen en la cola.");
        }
    }

    private Drone encontrarMejorDroneParaPaquete(Paquete paquete) {
        List<Drone> candidatos = new ArrayList<>();
        double pesoPaquete = paquete.getPeso();

        // 1. Buscar drone ideal en Zona.CENTRO
        buscarCandidatos(paquete, Zona.CENTRO, true, candidatos);
        if (!candidatos.isEmpty()) return seleccionarMejorCandidato(candidatos, paquete.getZonaDestino());

        // 2. Buscar drone ideal en otras zonas
        for (Zona zona : Zona.values()) {
            if (zona != Zona.CENTRO) {
                buscarCandidatos(paquete, zona, true, candidatos);
            }
        }
        if (!candidatos.isEmpty()) return seleccionarMejorCandidato(candidatos, paquete.getZonaDestino());

        // 3. Buscar cualquier drone capaz en Zona.CENTRO
        buscarCandidatos(paquete, Zona.CENTRO, false, candidatos);
        if (!candidatos.isEmpty()) return seleccionarMejorCandidato(candidatos, paquete.getZonaDestino());

        // 4. Buscar cualquier drone capaz en otras zonas
        for (Zona zona : Zona.values()) {
            if (zona != Zona.CENTRO) {
                buscarCandidatos(paquete, zona, false, candidatos);
            }
        }
        if (!candidatos.isEmpty()) return seleccionarMejorCandidato(candidatos, paquete.getZonaDestino());

        return null; // No se encontró ningún drone
    }

    private void buscarCandidatos(Paquete paquete, Zona zonaBusqueda, boolean soloIdeal, List<Drone> candidatos) {
        double pesoPaquete = paquete.getPeso();
        for (Drone drone : getFlotaDronesActiva()) {
            if (drone.getPaqueteActual() == null && drone.getNivelBateria() > 20 && drone.getZonaActual() == zonaBusqueda) {
                boolean esTipoIdeal = false;
                if (pesoPaquete <= 5.0 && drone instanceof LightDrone) esTipoIdeal = true;
                else if (pesoPaquete > 5.0 && pesoPaquete <= 10.0 && drone instanceof MediumDrone) esTipoIdeal = true;
                else if (pesoPaquete > 10.0 && drone instanceof HeavyDrone) esTipoIdeal = true;

                if (drone.puedeCargar(paquete)) {
                    if (soloIdeal) {
                        if (esTipoIdeal) {
                            candidatos.add(drone);
                        }
                    } else {
                        candidatos.add(drone);
                    }
                }
            }
        }
    }

    private Drone seleccionarMejorCandidato(List<Drone> candidatos, Zona zonaDestinoPaquete) {
        Drone mejorOpcion = null;
        int menorDistancia = Integer.MAX_VALUE;
        for (Drone candidato : candidatos) {
            int distancia = getDistancia(candidato.getZonaActual(), zonaDestinoPaquete);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                mejorOpcion = candidato;
            } else if (distancia == menorDistancia) {
                // Criterio de desempate: preferir el que tiene más batería, o el de menor ID.
                if (mejorOpcion != null && candidato.getNivelBateria() > mejorOpcion.getNivelBateria()) {
                    mejorOpcion = candidato;
                } else if (mejorOpcion != null && candidato.getNivelBateria() == mejorOpcion.getNivelBateria() && candidato.getId().compareTo(mejorOpcion.getId()) < 0){
                    mejorOpcion = candidato;
                }
            }
        }
        candidatos.clear(); // Limpiar para la siguiente búsqueda
        return mejorOpcion;
    }

    public void mostrarEstadoFlota() {
        System.out.println("\n--- Estado de la Flota ---");
        if (isFlotaVacia()) {
            System.out.println("La flota de drones está vacía.");
            return;
        }
        for (Drone drone : getFlotaDronesActiva()) {
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
                Drone removido = removerDronePorId(droneQuitado.getId());
                if (removido != null) {
                    System.out.println("Deshecho: Se quitó el drone " + droneQuitado.getId());
                } else {
                    System.out.println("Error al deshacer: No se encontró el drone " + droneQuitado.getId() + " para quitar.");
                }
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
}