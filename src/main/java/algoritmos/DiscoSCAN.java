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
    
    public static void inicializar(int n) {
        peticiones = new ArrayList[n];
        tipo = new ArrayList[n];
        
        for (int i = 0; i < n; i++) {
            peticiones[i] = new ArrayList<>();
            tipo[i] = new ArrayList<>();
        }
        
        cabeza = 0;
        totalAtendidas = 0;
        tiempoTotal = 0;
        
        System.out.println("\nDisco inicializado - Cabeza en sector 0\n");
    }
    
    public static void agregarPeticiones(int proceso) {
        int cantidad = r.nextInt(5) + 1;
        
        System.out.print("P" + (proceso+1) + " genera peticiones: ");
        
        for (int i = 0; i < cantidad; i++) {
            int sector;
            do {
                sector = r.nextInt(20) + 1;
            } while (peticiones[proceso].contains(sector));
            
            char tipoOp = r.nextBoolean() ? 'L' : 'E';
            
            peticiones[proceso].add(sector);
            tipo[proceso].add(tipoOp);
            
            System.out.print(sector + "" + tipoOp);
            if (i < cantidad-1) System.out.print(", ");
        }
        System.out.println();
    }
    
    public static void ejecutarSCAN() {
        System.out.println("\n--- Ejecutando SCAN ---");
        
        ArrayList<int[]> cola = new ArrayList<>();
        for (int i = 0; i < peticiones.length; i++) {
            for (int j = 0; j < peticiones[i].size(); j++) {
                int tiempo = (tipo[i].get(j) == 'L') ? 1 : 2;
                cola.add(new int[]{i, peticiones[i].get(j), tiempo});
            }
        }
        
        if (cola.isEmpty()) {
            System.out.println("No hay peticiones pendientes\n");
            return;
        }
        
        for (int i = 0; i < cola.size() - 1; i++) {
            for (int j = i + 1; j < cola.size(); j++) {
                if (cola.get(i)[1] > cola.get(j)[1]) {
                    int[] temp = cola.get(i);
                    cola.set(i, cola.get(j));
                    cola.set(j, temp);
                }
            }
        }
        
        int atendidas = 0;
        for (int[] p : cola) {
            int proceso = p[0];
            int sector = p[1];
            int transferencia = p[2];
            
            int movimiento = Math.abs(cabeza - sector);
            int tiempo = movimiento + transferencia;
            
            System.out.println("  P" + (proceso+1) + " | Sector " + sector + 
                             " | Movimiento: " + movimiento + 
                             " | Transferencia: " + transferencia +
                             " | Tiempo: " + tiempo);
            
            cabeza = sector;
            tiempoTotal += tiempo;
            atendidas++;
            
            int idx = peticiones[proceso].indexOf(sector);
            peticiones[proceso].remove(idx);
            tipo[proceso].remove(idx);
            
            if (r.nextBoolean()) {
                int nuevas = r.nextInt(3) + 1;
                for (int k = 0; k < nuevas; k++) {
                    int proc = r.nextInt(peticiones.length);
                    if (peticiones[proc].size() >= 10) continue;
                    
                    int nuevoSector;
                    do {
                        nuevoSector = r.nextInt(20) + 1;
                    } while (peticiones[proc].contains(nuevoSector));
                    
                    char nuevoTipo = r.nextBoolean() ? 'L' : 'E';
                    
                    peticiones[proc].add(nuevoSector);
                    tipo[proc].add(nuevoTipo);
                }
            }
        }
        
        totalAtendidas += atendidas;
        System.out.println("Peticiones atendidas: " + atendidas);
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
            System.out.println("Promedio por peticion: " + (tiempoTotal / totalAtendidas));
        }
        System.out.println("Posicion final de la cabeza: sector " + cabeza);
        System.out.println("==================================\n");
    }
}