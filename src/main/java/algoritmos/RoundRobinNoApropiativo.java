package algoritmos;

import java.util.*;

public class RoundRobinNoApropiativo {
    
    public void RoundRobinNoApropiativo() {
        Random rand = new Random();
        
        // Datos de procesos usando arreglos simples
        int numProcesos = rand.nextInt(5) + 3; // 3-7 procesos
        int[] id = new int[numProcesos];
        int[] tiempo = new int[numProcesos];
        String[] estado = new String[numProcesos];
        
        //  Acintadores de bloqueos 
        int[] bloqueos = new int[numProcesos];           // Solo para mostrar
        int[] intentosDesbloqueo = new int[numProcesos]; // Intentos fallidos de desbloqueo
        
        System.out.println("\n=== GENERANDO " + numProcesos + " PROCESOS ===");
        for (int i = 0; i < numProcesos; i++) {
            id[i] = i + 1;
            tiempo[i] = rand.nextInt(8) + 3; // 3-10
            
            
            if (rand.nextInt(3) == 0) { // 33% probabilidad
                estado[i] = "Bloqueado";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] + " | INICIA BLOQUEADO");
            } else {
                estado[i] = "Listo";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i]);
            }
            
            bloqueos[i] = 0;
            intentosDesbloqueo[i] = 0;
        }
        
        int quantum = 3;
        System.out.println("\n=== ROUND ROBIN NO APROPIATIVO (Quantum=" + quantum + ") ===");
        
        Queue<Integer> cola = new LinkedList<>();
        // SOLO AGREGAR A LA COLA LOS QUE ESTÁN LISTOS
        for (int i = 0; i < numProcesos; i++) {
            if (estado[i].equals("Listo")) {
                cola.add(i);
            }
        }
        
        int tiempoActual = 0;
        int terminados = 0;
        
        while (terminados < numProcesos && tiempoActual < 100) {
            
            
            if (cola.isEmpty()) {
                System.out.println("\n--- Tiempo " + tiempoActual + " (No hay procesos listos) ---");
                boolean desbloqueado = false;
                
                for (int i = 0; i < numProcesos; i++) {
                    if (estado[i].equals("Bloqueado")) {
                        int desbloquear = rand.nextInt(2); // 0 o 1
                        System.out.println("  Intentando desbloquear P" + id[i] + 
                                         " (intento " + (intentosDesbloqueo[i] + 1) + "/3): " + 
                                         (desbloquear == 1 ? "listo" : "Nolisto"));
                        
                        if (desbloquear == 1) {
                            System.out.println("     P" + id[i] + " se desbloquea!");
                            estado[i] = "Listo";
                            cola.add(i);
                            intentosDesbloqueo[i] = 0;
                            desbloqueado = true;
                            break;
                        } else {
                            intentosDesbloqueo[i]++;
                            if (intentosDesbloqueo[i] >= 3) {
                                System.out.println("     P" + id[i] + " MUERE POR INANICIÓN (3 intentos)");
                                estado[i] = "Muerto";
                                terminados++;
                                desbloqueado = true;
                                break;
                            }
                        }
                    }
                }
                
                if (!desbloqueado) {
                    System.out.println("  No hay procesos para desbloquear");
                }
                
                tiempoActual++;
                continue;
            }
            
            // Sacar siguiente proceso de la cola
            int index = cola.poll();
            
            System.out.println("\n--- Tiempo " + tiempoActual + " ---");
            System.out.println(" Ejecutando P" + id[index]);
            
            // BLOQUEO DURANTE EJECUCIÓN (PERO SIN MORIR)
            if (rand.nextInt(10) < 3) { // 30% probabilidad
                System.out.println("  →  P" + id[index] + " se BLOQUEA durante ejecución");
                bloqueos[index]++;
                System.out.println("  → Bloqueos totales: " + bloqueos[index]);
                estado[index] = "Bloqueado";
                intentosDesbloqueo[index] = 0;
                tiempoActual++;
                continue;
            }
            
            // Ejecución normal
            tiempo[index] -= quantum;
            tiempoActual += quantum;
            
            if (tiempo[index] <= 0) {
                System.out.println("  →  P" + id[index] + " TERMINADO");
                estado[index] = "Terminado";
                terminados++;
            } else {
                System.out.println("  → P" + id[index] + " restante: " + tiempo[index]);
                estado[index] = "Listo";
                cola.add(index);
            }
        }
        
        // REPORTE FINAL
        System.out.println("\n=== REPORTE FINAL ===");
        System.out.println("Tiempo total: " + tiempoActual);
        
        int normales = 0, muertos = 0;
        for (int i = 0; i < numProcesos; i++) {
            if (estado[i].equals("Terminado")) normales++;
            if (estado[i].equals("Muerto")) muertos++;
            
            System.out.println("P" + id[i] + ": " + estado[i] + 
                             " | Bloqueos: " + bloqueos[i] + 
                             " | Intentos fallidos: " + intentosDesbloqueo[i]);
        }
        
        System.out.println("\n  ESTADÍSTICAS:");
        System.out.println("  Terminados normales: " + normales);
        System.out.println(" Muertos por inanición: " + muertos);
    }
    
    public static void ejecutar() {
        RoundRobinNoApropiativo rr = new RoundRobinNoApropiativo();
        rr.RoundRobinNoApropiativo();
    }
}