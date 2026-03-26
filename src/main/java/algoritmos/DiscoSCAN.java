package algoritmos;

import java.util.ArrayList;
import java.util.Random;

public class DiscoSCAN {

    public static ArrayList<Integer>[] peticiones;
    public static ArrayList<Character>[] tipo;

    private static int cabeza = 0;
    private static int totalAtendidas = 0;
    private static int tiempoTotal = 0;
    private static Random r = new Random();

    // Nuevas variables para controlar la terminación de procesos
    private static int[] peticionesGeneradas;      // Cuántas ha generado cada proceso
    private static boolean[] procesoTerminado;     // Si el proceso ya no debe generar más
    private static final int MAX_PETICIONES_POR_PROCESO = 10; // Límite de peticiones por proceso

    public static void inicializar(int n) {
        peticiones = new ArrayList[n];
        tipo = new ArrayList[n];
        peticionesGeneradas = new int[n];
        procesoTerminado = new boolean[n];

        for (int i = 0; i < n; i++) {
            peticiones[i] = new ArrayList<>();
            tipo[i] = new ArrayList<>();
            peticionesGeneradas[i] = 0;
            procesoTerminado[i] = false;
        }

        cabeza = 0;
        totalAtendidas = 0;
        tiempoTotal = 0;

        System.out.println("\nDisco inicializado - Cabeza en sector 0\n");
    }

    /**
     * Genera peticiones para un proceso específico, solo si el proceso no ha terminado
     * y no ha alcanzado el máximo de peticiones permitidas.
     */
    public static void agregarPeticiones(int proceso) {
        if (procesoTerminado[proceso]) {
            System.out.println("P" + (proceso + 1) + " ya terminó, no se pueden agregar más peticiones.");
            return;
        }

        int restantes = MAX_PETICIONES_POR_PROCESO - peticionesGeneradas[proceso];
        if (restantes <= 0) {
            // Ya alcanzó el máximo, marcar como terminado si no tiene peticiones pendientes
            if (peticiones[proceso].isEmpty()) {
                procesoTerminado[proceso] = true;
                System.out.println("P" + (proceso + 1) + " alcanzó su límite de peticiones y ha terminado.");
            }
            return;
        }

        int cantidad = r.nextInt(Math.min(restantes, 5)) + 1; // Máximo 5, pero sin pasarse del límite
        System.out.print("P" + (proceso + 1) + " genera peticiones: ");

        for (int i = 0; i < cantidad; i++) {
            int sector;
            do {
                sector = r.nextInt(20) + 1;
            } while (peticiones[proceso].contains(sector));

            char tipoOp = r.nextBoolean() ? 'L' : 'E';

            peticiones[proceso].add(sector);
            tipo[proceso].add(tipoOp);
            peticionesGeneradas[proceso]++;

            System.out.print(sector + "" + tipoOp);
            if (i < cantidad - 1) System.out.print(", ");
        }
        System.out.println();

        // Verificar si con esta generación el proceso alcanzó su límite y no tiene pendientes
        if (peticionesGeneradas[proceso] == MAX_PETICIONES_POR_PROCESO && peticiones[proceso].isEmpty()) {
            procesoTerminado[proceso] = true;
            System.out.println("P" + (proceso + 1) + " ha completado todas sus peticiones y ha terminado.");
        }
    }

    /**
     * Verifica si aún hay peticiones pendientes en algún proceso no terminado.
     */
    private static boolean hayPeticionesPendientes() {
        for (int i = 0; i < peticiones.length; i++) {
            if (!peticiones[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ejecuta el algoritmo SCAN real.
     */
    public static void ejecutarSCAN() {
        System.out.println("\n--- Ejecutando SCAN ---");

        if (!hayPeticionesPendientes()) {
            System.out.println("No hay peticiones pendientes.\n");
            return;
        }

        // Dirección inicial: 1 = hacia sectores mayores, -1 = hacia menores
        int direccion = 1;
        int atendidasEnEstaEjecucion = 0;

        // Mientras haya peticiones pendientes
        while (hayPeticionesPendientes()) {
            // Buscar la siguiente petición en la dirección actual
            int siguienteSector = -1;
            int procesoElegido = -1;
            int idxEnLista = -1;

            for (int i = 0; i < peticiones.length; i++) {
                for (int j = 0; j < peticiones[i].size(); j++) {
                    int sector = peticiones[i].get(j);
                    // Si está en la dirección actual y es la más cercana en esa dirección
                    if ((direccion == 1 && sector >= cabeza) || (direccion == -1 && sector <= cabeza)) {
                        if (siguienteSector == -1 ||
                                (direccion == 1 && sector < siguienteSector) ||
                                (direccion == -1 && sector > siguienteSector)) {
                            siguienteSector = sector;
                            procesoElegido = i;
                            idxEnLista = j;
                        }
                    }
                }
            }

            // Si no se encontró ninguna en la dirección actual, invertir dirección
            if (siguienteSector == -1) {
                direccion = -direccion;
                System.out.println("  No hay más peticiones en esta dirección, invirtiendo a " +
                        (direccion == 1 ? "mayores" : "menores"));
                continue;
            }

            // Atender la petición encontrada
            char tipoOp = tipo[procesoElegido].get(idxEnLista);
            int transferencia = (tipoOp == 'L') ? 1 : 2;
            int movimiento = Math.abs(cabeza - siguienteSector);
            int tiempo = movimiento + transferencia;

            System.out.println("  P" + (procesoElegido + 1) + " | Sector " + siguienteSector +
                    " | Movimiento: " + movimiento +
                    " | Transferencia: " + transferencia +
                    " | Tiempo: " + tiempo);

            cabeza = siguienteSector;
            tiempoTotal += tiempo;
            atendidasEnEstaEjecucion++;

            // Eliminar la petición atendida
            peticiones[procesoElegido].remove(idxEnLista);
            tipo[procesoElegido].remove(idxEnLista);

            // Opcional: generar nuevas peticiones para procesos que aún no han terminado
            // (se puede comentar si no se desea generación dinámica)
            if (r.nextBoolean()) {
                int nuevas = r.nextInt(3) + 1; // 1 a 3 nuevas peticiones en total
                for (int k = 0; k < nuevas; k++) {
                    // Elegir un proceso al azar que no haya terminado
                    int proc;
                    int intentos = 0;
                    do {
                        proc = r.nextInt(peticiones.length);
                        intentos++;
                        if (intentos > 50) break; // Evitar bucle infinito si todos terminaron
                    } while (procesoTerminado[proc] ||
                            peticionesGeneradas[proc] >= MAX_PETICIONES_POR_PROCESO);
                    if (intentos > 50) continue; // No se encontró proceso activo

                    // Generar una nueva petición
                    int nuevoSector;
                    do {
                        nuevoSector = r.nextInt(20) + 1;
                    } while (peticiones[proc].contains(nuevoSector));
                    char nuevoTipo = r.nextBoolean() ? 'L' : 'E';
                    peticiones[proc].add(nuevoSector);
                    tipo[proc].add(nuevoTipo);
                    peticionesGeneradas[proc]++;

                    // Si con esta nueva petición el proceso alcanza su límite y luego se atiende, se marcará después
                    if (peticionesGeneradas[proc] == MAX_PETICIONES_POR_PROCESO && peticiones[proc].size() == 1) {
                        // No marcamos ahora porque aún tiene pendiente
                    }
                }
            }

            // Verificar si el proceso del que atendimos ya no tiene peticiones y ya generó todas
            if (peticiones[procesoElegido].isEmpty() &&
                    peticionesGeneradas[procesoElegido] >= MAX_PETICIONES_POR_PROCESO) {
                procesoTerminado[procesoElegido] = true;
                System.out.println("  P" + (procesoElegido + 1) + " ha terminado (no más peticiones).");
            }
        }

        totalAtendidas += atendidasEnEstaEjecucion;
        System.out.println("Peticiones atendidas en esta ejecución: " + atendidasEnEstaEjecucion);
        System.out.println("Tiempo total acumulado: " + tiempoTotal);
        System.out.println("--- Fin SCAN ---\n");
    }

    public static boolean tienePeticiones(int proceso) {
        return peticiones[proceso] != null && !peticiones[proceso].isEmpty();
    }

    public static void mostrarEstadisticas() {
        System.out.println("\n===== ESTADISTICAS DEL DISCO =====");
        System.out.println("Peticiones atendidas: " + totalAtendidas);
        System.out.println("Tiempo total de servicio: " + tiempoTotal);
        if (totalAtendidas > 0) {
            System.out.println("Promedio por petición: " + (tiempoTotal / totalAtendidas));
        }
        System.out.println("Posición final de la cabeza: sector " + cabeza);
        System.out.println("==================================\n");
    }
}