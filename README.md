# Proyecto Sistema de Entrega con Drones

Este proyecto simula un sistema de gestión de entregas utilizando una flota de drones de diferentes capacidades.

## Estructura de Directorios

La estructura principal del proyecto es la siguiente:

```
/Users/devpaul/dev-projects/DevPaul/Java/DronesProject
├── .gitattributes
├── .gitignore
├── app/
│   ├── bin/                # Archivos compilados y binarios de la aplicación
│   │   ├── main/
│   │   └── test/
│   ├── build.gradle        # Script de construcción de Gradle para la aplicación
│   └── src/                # Código fuente de la aplicación
│       ├── main/           # Código fuente principal
│       │   └── java/
│       │       └── dronesproject/
│       │           ├── App.java            # Clase principal de la aplicación (interfaz de consola)
│       │           ├── model/              # Clases del modelo de datos (Drone, Paquete, etc.)
│       │           │   ├── Drone.java
│       │           │   ├── LightDrone.java
│       │           │   ├── MediumDrone.java
│       │           │   ├── HeavyDrone.java
│       │           │   └── Paquete.java
│       │           └── service/            # Clases de lógica de negocio
│       │               └── SistemaEntregas.java
│       └── test/           # Código fuente para pruebas (actualmente vacío)
├── gradle/
│   ├── libs.versions.toml  # Definiciones de versiones de dependencias
│   └── wrapper/            # Gradle Wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                 # Script de Gradle Wrapper para Unix/Linux/macOS
├── gradlew.bat             # Script de Gradle Wrapper para Windows
└── settings.gradle         # Script de configuración de Gradle para el proyecto
```

## Principios de Programación Orientada a Objetos (POO)

El proyecto aplica varios principios de POO:

1.  **Abstracción**:
    *   La clase `Drone` es una clase abstracta que define la interfaz común y el comportamiento base para todos los tipos de drones. Oculta los detalles complejos de la operación de un drone y expone solo las funcionalidades esenciales.

2.  **Encapsulamiento**:
    *   Los atributos de las clases `Drone` y `Paquete` son privados (`private`) o protegidos (`protected`).
    *   El acceso y la modificación de estos atributos se realizan a través de métodos públicos (getters y setters, aunque en este caso solo getters son explícitamente definidos para los atributos principales).
    *   Por ejemplo, en la clase `Paquete`, los atributos `id`, `peso` y `direccionDestino` son privados y se accede a ellos mediante `getId()`, `getPeso()`, etc.
    *   En la clase `Drone`, atributos como `id` y `capacidadCargaMaxima` son `protected` para permitir el acceso desde las subclases, pero `paqueteActual` y `nivelBateria` también siguen este principio.

3.  **Herencia**:
    *   Las clases `LightDrone`, `MediumDrone`, y `HeavyDrone` heredan de la clase base `Drone`.
    *   Esto permite que las clases hijas reutilicen el código y las propiedades de la clase padre (como `id`, `nivelBateria`, `entregarPaquete()`, `recargarBateria()`) y también puedan tener sus propias implementaciones o atributos específicos si fuera necesario.
    *   Cada subclase de `Drone` define su `capacidadCargaMaxima` específica en su constructor.

4.  **Polimorfismo**:
    *   El método `cargarPaquete(Paquete paquete)` en la clase `Drone` es abstracto y se implementa de manera específica en cada una de sus subclases (`LightDrone`, `MediumDrone`, `HeavyDrone`).
    *   Esto significa que, aunque se llame al mismo método (`cargarPaquete`) sobre un objeto `Drone`, el comportamiento real dependerá del tipo específico de drone (ligero, mediano o pesado).
    *   Por ejemplo, un `LightDrone` solo podrá cargar paquetes hasta 5kg, mientras que un `HeavyDrone` podrá cargar hasta 20kg, y la lógica de validación está en su respectiva implementación del método.

## Estructuras de Datos Utilizadas

El sistema utiliza las siguientes estructuras de datos de la Java Collections Framework:

1.  **`java.util.Map` (implementada con `java.util.TreeMap`) / `java.util.List` (implementada con `java.util.ArrayList`)**: (Configurable)
    *   **Uso**: En la clase `SistemaEntregas`, la gestión de la `flotaDrones` puede configurarse para usar `TreeMap<String, Drone>` o `ArrayList<Drone>`. Por defecto, utiliza `TreeMap`.
    *   **Configuración**: Para cambiar la implementación, modifica la variable `usarTreeMapParaFlota` en la clase `SistemaEntregas.java`:
        *   `private boolean usarTreeMapParaFlota = true;` (Usa `TreeMap`)
        *   `private boolean usarTreeMapParaFlota = false;` (Usa `ArrayList`)
    *   **Propósito**:
        *   **`TreeMap`**: Almacena y gestiona los drones utilizando el ID del drone como clave. Mantiene los drones ordenados por su ID y proporciona un acceso eficiente (tiempo logarítmico O(log n)) para operaciones como agregar, eliminar y buscar drones por ID.
        *   **`ArrayList`**: Almacena los drones en el orden en que se agregan. Las operaciones de búsqueda por ID son lineales (O(n)), pero la iteración puede ser ligeramente más rápida en algunos casos. La adición es O(1) amortizado.
    *   **Operaciones Comunes (Conceptual)**: Agregar nuevos drones, obtener un drone por su ID, eliminar un drone, iterar sobre la flota de drones. La implementación específica de estas operaciones varía internamente según la estructura de datos seleccionada.


2.  **`java.util.Queue` (implementada con `java.util.LinkedList`)**:
    *   **Uso**: En la clase `SistemaEntregas`, el atributo `colaDeEntregas` es una `LinkedList<Paquete>` utilizada como una cola (FIFO - First-In, First-Out).
    *   **Propósito**: Se utiliza para gestionar los paquetes pendientes de entrega. Los nuevos pedidos de entrega se añaden al final de la cola (`colaDeEntregas.offer(paquete)`) y se procesan desde el principio de la cola (`colaDeEntregas.poll()`).
    *   **Operaciones Comunes**: Encolar nuevos paquetes, desencolar paquetes para procesarlos, verificar si la cola está vacía.

3.  **`java.util.Stack` (Pila - LIFO)**:
    *   **Uso**: En la clase `SistemaEntregas`, el atributo `historialAcciones` es un `Stack<AccionHistorial>`.
    *   **Propósito**: Se utiliza para implementar la funcionalidad de "deshacer" la última acción realizada (agregar drone o agregar paquete). Cada acción relevante se guarda en la pila, permitiendo revertirla.
    *   **Operaciones Comunes**: Apilar una nueva acción (`historialAcciones.push(accion)`), desapilar la última acción para deshacerla (`historialAcciones.pop()`).

### Posibles Usos Futuros de Otras Estructuras de Datos:

*   **`java.util.Vector`:
    *   Si se necesitara una lista sincronizada (thread-safe) para la flota de drones o la cola de paquetes en un entorno multihilo, `Vector` podría ser una opción, aunque las colecciones concurrentes del paquete `java.util.concurrent` (como `CopyOnWriteArrayList` o `ConcurrentLinkedQueue`) suelen ser preferibles por su mejor rendimiento en escenarios de alta concurrencia.

4.  **Matrices (Arrays Multidimensionales `String[][]`)**:
    *   **Uso**: En la clase `SistemaEntregas`, el atributo `mapaEntrega` es un `String[][]`.
    *   **Propósito**: Se utiliza para representar un mapa conceptual del área de entrega en la consola. Cada celda de la matriz representa una posición en el mapa y contiene un símbolo que indica si hay una base, un drone disponible, un drone en entrega o un paquete pendiente.
    *   **Operaciones Comunes**: Inicializar el mapa, limpiar el mapa, actualizar las celdas del mapa según el estado de los drones y paquetes.

*   **Otras estructuras de árbol (ej. Árboles Binarios de Búsqueda personalizados, Árboles N-arios)**:
    *   Podrían usarse para representar jerarquías (por ejemplo, una estructura organizativa de drones si tuvieran supervisores o estuvieran agrupados por zonas geográficas complejas).
    *   Para optimizar búsquedas espaciales si el sistema manejara coordenadas geográficas de forma más detallada (ej. Quadtrees o K-D Trees para encontrar el drone más cercano a un punto).
    *   `java.util.TreeSet`: Si se necesitara una colección ordenada de elementos únicos sin un valor asociado (a diferencia de `TreeMap`), por ejemplo, para mantener un registro ordenado de IDs de paquetes procesados.

## Cómo Ejecutar la Aplicación

1.  Asegúrate de tener Java Development Kit (JDK) instalado (versión 11 o superior recomendada).
2.  El proyecto utiliza Gradle como sistema de construcción.
3.  Desde la raíz del proyecto (`/Users/devpaul/dev-projects/DevPaul/Java/DronesProject`), puedes compilar y ejecutar la aplicación usando los siguientes comandos en la terminal:

    *   Para compilar el proyecto:
        ```bash
        ./gradlew build
        ```
    *   Para ejecutar la aplicación (después de compilar):
        ```bash
        ./gradlew run
        ```
        O directamente:
        ```bash
        java -cp app/build/classes/java/main dronesproject.App
        ```

Al ejecutar, se presentará un menú en la consola para interactuar con el sistema.

## Mapa de Entrega Conceptual (Reactivo)

El sistema incluye una representación visual conceptual del estado de las entregas a través de un mapa en la consola. Este mapa se actualiza dinámicamente para reflejar la ubicación y el estado de los drones y paquetes.

### Representación en el Mapa:

El mapa utiliza los siguientes símbolos para representar los diferentes elementos:

*   `[B]`: Representa la **Base** de operaciones de los drones. Usualmente ubicada en la esquina superior izquierda del mapa (coordenada 0,0).
*   `[D]`: Representa un **Drone Disponible** en la base, listo para una nueva asignación de entrega. Estos se muestran en la primera fila, a continuación de la base.
*   `[d]`: Representa un **Drone en Entrega**, es decir, un drone que actualmente está transportando un paquete. Estos se muestran típicamente en la segunda fila del mapa.
*   `[P]`: Representa un **Paquete Pendiente** en la cola de entregas, esperando ser asignado a un drone. Estos se muestran en la tercera fila del mapa.
*   `[ ]`: Representa un espacio vacío en el mapa.

### Funcionamiento:

Cuando se selecciona la opción "Mostrar Mapa de Entrega" en el menú principal, el sistema:
1.  Limpia la representación anterior del mapa.
2.  Actualiza el mapa basándose en el estado actual de:
    *   La ubicación de la base.
    *   Los drones que están en la base y disponibles.
    *   Los drones que están actualmente realizando una entrega.
    *   Los paquetes que están en la cola esperando ser procesados.
3.  Imprime el mapa actualizado en la consola junto con la leyenda de los símbolos.

Esta funcionalidad permite tener una visión general y en tiempo real (conceptual) de la operación del sistema de entregas.