package algoritmos;

import java.util.*;

public class PrioridadesNoApropiativo {
    
    public void PrioridadesNoApropiativo() {
        Random rand = new Random();
        
        // Generar procesos
        int numProcesos = rand.nextInt(5) + 3; // 3-7
        int[] id = new int[numProcesos];
        int[] tiempo = new int[numProcesos];
        int[] prioridad = new int[numProcesos];
        String[] estado = new String[numProcesos];
        int[] bloqueos = new int[numProcesos];
        int[] intentos = new int[numProcesos];
        
        System.out.println("\n=== GENERANDO " + numProcesos + " PROCESOS ===");
        for (int i = 0; i < numProcesos; i++) {
            id[i] = i + 1;
            tiempo[i] = rand.nextInt(8) + 3;
            prioridad[i] = rand.nextInt(5) + 1;
            
            // Algunos inician bloqueados
            if (rand.nextInt(3) == 0) {
                estado[i] = "Bloqueado";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] + " | Prioridad: " + prioridad[i] + " | INICIA BLOQUEADO");
            } else {
                estado[i] = "Listo";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] + " | Prioridad: " + prioridad[i]);
            }
            
            bloqueos[i] = 0;
            intentos[i] = 0;
        }
        
        System.out.println("\n=== PRIORIDADES NO APROPIATIVO ===");
        
        int tiempoActual = 0;
        int terminados = 0;
        
        while (terminados < numProcesos && tiempoActual < 100) {
            
           
            System.out.println("\n--- Tiempo " + tiempoActual + " ---");
            
           
            int actual = -1;
            for (int i = 0; i < numProcesos; i++) {
                if (estado[i].equals("Listo")) {
                    if (actual == -1 || prioridad[i] < prioridad[actual]) {
                        actual = i;
                    }
                }
            }
            
            
            if (actual == -1) {
                System.out.println("  No hay procesos listos");
                
                for (int i = 0; i < numProcesos; i++) {
                    if (estado[i].equals("Bloqueado")) {
                        int desbloquear = rand.nextInt(2);
                        System.out.println("  Intentando desbloquear P" + id[i] + " (intento " + (intentos[i] + 1) + "/3): " + (desbloquear == 1 ? "exitoso" : "fallido"));
                        
                        if (desbloquear == 1) {
                            System.out.println("    P" + id[i] + " se desbloquea");
                            estado[i] = "Listo";
                            intentos[i] = 0;
                        } else {
                            intentos[i]++;
                            if (intentos[i] >= 3) {
                                System.out.println("    P" + id[i] + " MUERE POR INANICION");
                                estado[i] = "Muerto";
                                terminados++;
                            }
                        }
                    }
                }
                
                tiempoActual++;
                continue;
            }
            
          
            System.out.println("  Ejecutando P" + id[actual] + " (Prioridad " + prioridad[actual] + ") hasta terminar");
            
            if (rand.nextInt(10) < 3) {
                System.out.println("    P" + id[actual] + " se BLOQUEA");
                bloqueos[actual]++;
                estado[actual] = "Bloqueado";
                intentos[actual] = 0;
                tiempoActual++;
                continue;
            }
            
       
            int tiempoEjecutado = tiempo[actual];
            tiempoActual += tiempoEjecutado;
            tiempo[actual] = 0;
            
            System.out.println("    Ejecuto " + tiempoEjecutado + " unidades");
            System.out.println("    P" + id[actual] + " TERMINA");
            
            estado[actual] = "Terminado";
            terminados++;
        }
        
        // Reporte final
        System.out.println("\n=== REPORTE FINAL ===");
        for (int i = 0; i < numProcesos; i++) {
            System.out.println("P" + id[i] + ": " + estado[i] + " | Bloqueos: " + bloqueos[i]);
        }
    }
    
    public static void ejecutar() {
        PrioridadesNoApropiativo pn = new PrioridadesNoApropiativo();
        pn.PrioridadesNoApropiativo();
    }
}