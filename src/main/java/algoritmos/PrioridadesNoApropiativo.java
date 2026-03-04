package algoritmos;

import java.util.*;

public class PrioridadesNoApropiativo {
    
    public void PrioridadesNoApropiativo() {
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
            prioridad[i] = rand.nextInt(5) + 1;
            estado[i] = "Listo";
            System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] + " | Prioridad: " + prioridad[i]);
        }
        
        System.out.println("\n=== PRIORIDADES NO APROPIATIVO ===");
        
        // Ordenar índices por prioridad (burbuja simple)
        int[] indices = new int[numProcesos];
        for (int i = 0; i < numProcesos; i++) indices[i] = i;
        
        for (int i = 0; i < numProcesos - 1; i++) {
            for (int j = 0; j < numProcesos - i - 1; j++) {
                if (prioridad[indices[j]] > prioridad[indices[j + 1]]) {
                    int temp = indices[j];
                    indices[j] = indices[j + 1];
                    indices[j + 1] = temp;
                }
            }
        }
        
        // Ejecutar en orden de prioridad
        for (int i = 0; i < numProcesos; i++) {
            int index = indices[i];
            System.out.println("\n▶️ Ejecutando P" + id[index] + " hasta terminar");
            System.out.println("  ✅ P" + id[index] + " TERMINADO (tardó " + tiempo[index] + " unidades)");
        }
    }
    
    public static void ejecutar() {
        PrioridadesNoApropiativo pn = new PrioridadesNoApropiativo();
        pn.PrioridadesNoApropiativo();
    }
}