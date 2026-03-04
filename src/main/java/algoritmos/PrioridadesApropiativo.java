package algoritmos;

import java.util.*;

public class PrioridadesApropiativo {
    
    public void PrioridadesApropiativo() {
        Random rand = new Random();
        
        // Datos con arreglos
        int numProcesos = rand.nextInt(5) + 3; // 3-7
        int[] id = new int[numProcesos];
        int[] tiempo = new int[numProcesos];
        int[] prioridad = new int[numProcesos];
        String[] estado = new String[numProcesos];
        
        System.out.println("\n=== GENERANDO " + numProcesos + " PROCESOS ===");
        for (int i = 0; i < numProcesos; i++) {
            id[i] = i + 1;
            tiempo[i] = rand.nextInt(8) + 3;
            prioridad[i] = rand.nextInt(5) + 1; // 1-5 (1 más alta)
            estado[i] = "Listo";
            System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] + " | Prioridad: " + prioridad[i]);
        }
        
        int quantum = 2;
        System.out.println("\n=== PRIORIDADES APROPIATIVO ===");
        
        int tiempoActual = 0;
        int terminados = 0;
        
        while (terminados < numProcesos) {
            // Buscar proceso de mayor prioridad (menor número)
            int indexEjecutar = -1;
            for (int i = 0; i < numProcesos; i++) {
                if (!estado[i].equals("Terminado")) {
                    if (indexEjecutar == -1 || prioridad[i] < prioridad[indexEjecutar]) {
                        indexEjecutar = i;
                    }
                }
            }
            
            if (indexEjecutar == -1) break;
            
            System.out.println("\n▶️ Ejecutando P" + id[indexEjecutar] + " (Prioridad " + prioridad[indexEjecutar] + ")");
            tiempo[indexEjecutar] -= quantum;
            tiempoActual += quantum;
            
            if (tiempo[indexEjecutar] <= 0) {
                System.out.println("  ✅ P" + id[indexEjecutar] + " TERMINADO");
                estado[indexEjecutar] = "Terminado";
                terminados++;
            } else {
                System.out.println("  P" + id[indexEjecutar] + " restante: " + tiempo[indexEjecutar]);
            }
        }
    }
    
    public static void ejecutar() {
        PrioridadesApropiativo pa = new PrioridadesApropiativo();
        pa.PrioridadesApropiativo();
    }
}