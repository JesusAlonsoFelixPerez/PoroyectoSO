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

    // Variables para controlar la generación de peticiones
    private static boolean[] procesoTerminado;

    public static void inicializar(int n) {
        peticiones = new ArrayList[n];
        tipo = new ArrayList[n];
        procesoTerminado = new boolean[n];

        for (int i = 0; i < n; i++) {
            peticiones[i] = new ArrayList<>();
            tipo[i] = new ArrayList<>();
            procesoTerminado[i] = false;
        }

        cabeza = 0;
        totalAtendidas = 0;
        tiempoTotal = 0;

        System.out.println("\nDisco inicializado - Cabeza en sector 0\n");
    }

    public static void marcarProcesoTerminado(int proceso) {
        procesoTerminado[proceso] = true;
    }

    public static void agregarPeticiones(int proceso) {
        if (procesoTerminado[proceso]) {
            return;
        }
        
        int cantidad = r.nextInt(5) + 1;
        System.out.print("P" + (proceso + 1) + " genera peticiones: ");

        for (int i = 0; i < cantidad; i++) {
            int sector;
            do {
                sector = r.nextInt(20) + 1;
            } while (peticiones[proceso].contains(sector));

            char tipoOp = r.nextBoolean() ? 'L' : 'E';

            peticiones[proceso].add(sector);
            tipo[proceso].add(tipoOp);

            System.out.print(sector + "" + tipoOp);
            if (i < cantidad - 1) System.out.print(", ");
        }
        System.out.println();
    }

    private static boolean hayPeticionesPendientes() {
        for (int i = 0; i < peticiones.length; i++) {
            if (!peticiones[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static void ejecutarSCAN() {
        System.out.println("\n--- Ejecutando SCAN ---");

        if (!hayPeticionesPendientes()) {
            System.out.println("No hay peticiones pendientes.\n");
            return;
        }

        int direccion = 1;
        int atendidasEnEstaEjecucion = 0;
        int maxPeticionesPorEjecucion = 10;

        while (hayPeticionesPendientes() && atendidasEnEstaEjecucion < maxPeticionesPorEjecucion) {
            int siguienteSector = -1;
            int procesoElegido = -1;
            int idxEnLista = -1;

            for (int i = 0; i < peticiones.length; i++) {
                for (int j = 0; j < peticiones[i].size(); j++) {
                    int sector = peticiones[i].get(j);
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

            if (siguienteSector == -1) {
                direccion = -direccion;
        
                continue;
            }

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

            peticiones[procesoElegido].remove(idxEnLista);
            tipo[procesoElegido].remove(idxEnLista);
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