package algoritmos;

import java.util.*;

public class RoundRobinNoApropiativo {
    
    public void RoundRobinNoApropiativo() {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        
        // Datos de procesos usando arreglos simples
        int numProcesos = rand.nextInt(5) + 3; // 3-7 procesos
        int[] id = new int[numProcesos];
        int[] tiempo = new int[numProcesos];
        String[] estado = new String[numProcesos];
        
        System.out.println("\n=== GENERANDO " + numProcesos + " PROCESOS ===");
        for (int i = 0; i < numProcesos; i++) {
            id[i] = i + 1;
            tiempo[i] = rand.nextInt(8) + 3; // 3-10
            estado[i] = "Listo";
            System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i]);
        }
        
        int quantum = 3;
        System.out.println("\n=== ROUND ROBIN NO APROPIATIVO (Quantum=" + quantum + ") ===");
        
        Queue<Integer> cola = new LinkedList<>();
        for (int i = 0; i < numProcesos; i++) {
            cola.add(i);
        }
        
        while (!cola.isEmpty()) {
            int index = cola.poll();
            
            System.out.println("\n▶️ Ejecutando P" + id[index]);
            tiempo[index] -= quantum;
            
            if (tiempo[index] <= 0) {
                System.out.println("  ✅ P" + id[index] + " TERMINADO");
                estado[index] = "Terminado";
            } else {
                System.out.println("  P" + id[index] + " restante: " + tiempo[index]);
                cola.add(index);
            }
        }
    }
    
    public static void ejecutar() {
        RoundRobinNoApropiativo rr = new RoundRobinNoApropiativo();
        rr.RoundRobinNoApropiativo();
    }
}