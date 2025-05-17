# Proyecto Sistema de Entrega con Drones

Este proyecto simula un sistema de gestión de entregas de paquetes utilizando una flota de drones. Permite agregar drones de diferentes tipos (ligero, mediano, pesado), agregar paquetes con diferentes pesos y destinos, procesar las entregas asignando drones a paquetes, mostrar el estado de la flota y un mapa conceptual de las entregas.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes principales:

-   `dronesproject`: Contiene la clase principal `App.java` que maneja la interacción con el usuario y el menú de la aplicación.
-   `dronesproject.model`: Contiene las clases que representan las entidades del dominio:
    -   `Drone.java`: Clase abstracta base para todos los tipos de drones.
    -   `LightDrone.java`, `MediumDrone.java`, `HeavyDrone.java`: Clases concretas que heredan de `Drone` y representan los diferentes tipos de drones con capacidades de carga específicas.
    -   `Paquete.java`: Representa un paquete a ser entregado, con su ID, peso, dirección y zona de destino.
    -   `Zona.java`: Enumeración que define las diferentes zonas geográficas de operación.
-   `dronesproject.service`: Contiene la clase `SistemaEntregas.java` que encapsula la lógica de negocio principal del sistema, como la gestión de la flota de drones, la cola de paquetes, el procesamiento de entregas y la visualización de información.

## Principios de Programación Orientada a Objetos (POO) Aplicados

El proyecto aplica los siguientes principios de POO:

1.  **Abstracción**:
    *   **Descripción**: La abstracción consiste en ocultar los detalles complejos y mostrar solo la información esencial al usuario o a otras partes del sistema. Se enfoca en el "¿qué hace?" en lugar del "¿cómo lo hace?".
    *   **Aplicación**: La clase `Drone` es una clase abstracta que define la interfaz común y el comportamiento esencial para todos los tipos de drones (e.g., `getId()`, `getNivelBateria()`, `cargarPaquete()`, `entregarPaquete()`). Las clases concretas como `LightDrone` implementan los detalles específicos.

2.  **Encapsulamiento**:
    *   **Descripción**: El encapsulamiento consiste en agrupar los datos (atributos) y los métodos que operan sobre esos datos dentro de una unidad (clase). Protege los datos de accesos externos no autorizados, exponiendo solo interfaces controladas (getters y setters).
    *   **Aplicación**:
        *   En la clase `Drone`, los atributos como `id`, `capacidadCargaMaxima`, `nivelBateria`, `paqueteActual` y `zonaActual` son `protected`, lo que permite el acceso desde las subclases pero no directamente desde fuera del paquete (a menos que se usen getters/setters públicos).
        *   En la clase `Paquete`, los atributos son privados y se accede a ellos mediante métodos públicos (getters).
        *   La clase `SistemaEntregas` encapsula la lógica de gestión de la flota y las entregas, ocultando los detalles de implementación de las colecciones de drones y paquetes.

3.  **Herencia**:
    *   **Descripción**: La herencia permite que una clase (subclase o clase derivada) adquiera las propiedades y métodos de otra clase (superclase o clase base). Promueve la reutilización de código y establece una relación "es un" entre clases.
    *   **Aplicación**: Las clases `LightDrone`, `MediumDrone`, y `HeavyDrone` heredan de la clase abstracta `Drone`. Esto significa que comparten los atributos y métodos comunes definidos en `Drone` (como `id`, `nivelBateria`, `entregarPaquete()`) y pueden tener sus propias implementaciones o atributos específicos. Por ejemplo, cada tipo de drone tiene una `capacidadCargaMaxima` diferente, establecida en sus constructores.

4.  **Polimorfismo**:
    *   **Descripción**: El polimorfismo (que significa "muchas formas") permite que objetos de diferentes clases respondan al mismo mensaje (llamada a método) de maneras diferentes. Se logra comúnmente a través de la sobrescritura de métodos.
    *   **Aplicación**:
        *   El método `cargarPaquete(Paquete paquete)` es abstracto en la clase `Drone` y es implementado de forma específica por cada subclase (`LightDrone`, `MediumDrone`, `HeavyDrone`). Aunque se llame al mismo método `cargarPaquete()` sobre un objeto `Drone`, el comportamiento exacto dependerá del tipo real del drone (ligero, mediano o pesado), verificando si el peso del paquete es adecuado para su capacidad específica.
        *   El método `toString()` es sobrescrito en la clase `Drone` (y potencialmente en otras) para proporcionar una representación en cadena específica de cada objeto.

## Estructuras de Datos Utilizadas

El sistema utiliza diversas estructuras de datos de Java para gestionar la información de manera eficiente:

1.  **`java.util.List` (implementada con `java.util.ArrayList`)**:
    *   **Descripción**: Una lista es una colección ordenada de elementos que permite duplicados. `ArrayList` es una implementación redimensionable basada en un array.
    *   **Uso**:
        *   En `SistemaEntregas`, `flotaDronesList` (si `usarTreeMapParaFlota` es `false`) se usa para almacenar la colección de drones. Permite el acceso por índice y mantiene el orden de inserción.
        *   Se utiliza internamente en métodos como `encontrarMejorDroneParaPaquete` para crear listas temporales de drones candidatos.

2.  **`java.util.Map` (implementada con `java.util.TreeMap`)**:
    *   **Descripción**: Un mapa es una colección de pares clave-valor. `TreeMap` almacena las claves en orden natural (o según un comparador) y proporciona acceso eficiente (logarítmico) a los valores basado en sus claves.
    *   **Uso**:
        *   En `SistemaEntregas`, `flotaDronesMap` (si `usarTreeMapParaFlota` es `true`) se usa para almacenar la flota de drones, utilizando el ID del drone como clave. Esto permite una búsqueda rápida de drones por su ID.

3.  **`java.util.Queue` (implementada con `java.util.LinkedList`)**:
    *   **Descripción**: Una cola es una colección diseñada para mantener elementos antes de su procesamiento, siguiendo el principio FIFO (First-In, First-Out). `LinkedList` puede usarse como una cola.
    *   **Uso**:
        *   En `SistemaEntregas`, `colaDeEntregas` se utiliza para gestionar los paquetes pendientes de entrega. Los nuevos paquetes se añaden al final de la cola (`offer()`) y se procesan desde el principio de la cola (`poll()`).

4.  **`java.util.Stack`**:
    *   **Descripción**: Una pila es una colección que sigue el principio LIFO (Last-In, First-Out). Es útil para operaciones como deshacer/rehacer o el seguimiento de llamadas a funciones.
    *   **Uso**:
        *   En `SistemaEntregas`, `historialAcciones` se utiliza para implementar la funcionalidad de "Deshacer Última Acción". Cada vez que se agrega un drone o un paquete, se guarda una `AccionHistorial` en la pila. La operación de deshacer recupera (`pop()`) la última acción de la pila y la revierte.

5.  **Matrices (Arrays Bidimensionales `String[][]` e `int[][]`)**:
    *   **Descripción**: Una matriz es una estructura de datos que almacena elementos en una cuadrícula bidimensional (o multidimensional).
    *   **Uso**:
        *   En `SistemaEntregas`, `mapaEntrega` (un `String[][]`) se utiliza para crear una representación visual simple del estado de las entregas, mostrando la base, drones disponibles, drones en entrega y paquetes pendientes.
        *   `matrizDistancias` (un `int[][]`) se utiliza para almacenar las distancias simuladas entre las diferentes zonas de operación. Esto permite calcular rápidamente la "distancia" para la asignación de drones y la simulación del consumo de batería.

6.  **`java.util.Vector` (Mencionado en comentarios, no usado activamente en la lógica principal)**:
    *   **Descripción**: `Vector` es similar a `ArrayList`, pero es sincronizado (thread-safe). Esto significa que sus operaciones pueden ser más lentas debido a la sobrecarga de la sincronización.
    *   **Posible Uso (Teórico)**: Si el sistema necesitara ser accedido por múltiples hilos concurrentemente y se requiriera una lista sincronizada para la flota de drones o paquetes, `Vector` podría ser una opción, aunque las colecciones más modernas del paquete `java.util.concurrent` (como `CopyOnWriteArrayList`) suelen ser preferidas para la concurrencia. En el estado actual del proyecto (un solo hilo de ejecución principal), `ArrayList` o `TreeMap` son más apropiados.

## Cómo Ejecutar

1.  Compilar el proyecto (por ejemplo, usando un IDE como IntelliJ IDEA, Eclipse, o mediante la línea de comandos con Gradle si está configurado).
2.  Ejecutar la clase `dronesproject.App`.
3.  Seguir las instrucciones del menú en la consola.

## Mejoras Futuras (Sugerencias)

*   Implementar persistencia de datos (guardar y cargar el estado del sistema en archivos o una base de datos).
*   Mejorar la lógica de cálculo de consumo de batería y tiempos de entrega.
*   Añadir una interfaz gráfica de usuario (GUI).
*   Implementar pruebas unitarias.
*   Permitir la configuración de la matriz de distancias desde un archivo externo.