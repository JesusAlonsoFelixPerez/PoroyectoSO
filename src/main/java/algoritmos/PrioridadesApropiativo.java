package algoritmos;

import java.util.*;

public class PrioridadesApropiativo {

    public void PrioridadesApropiativo() {

        Random rand = new Random();

        // Generar procesos
        int numProcesos = rand.nextInt(5) + 3; // 3-7 procesos
        int[] id = new int[numProcesos];
        int[] tiempo = new int[numProcesos];
        int[] prioridad = new int[numProcesos];
        String[] estado = new String[numProcesos];
        int[] bloqueos = new int[numProcesos];
        int[] intentos = new int[numProcesos];

        System.out.println("\n=== GENERANDO " + numProcesos + " PROCESOS ===");

        for (int i = 0; i < numProcesos; i++) {

            id[i] = i + 1;
            tiempo[i] = rand.nextInt(8) + 3; // tiempo 3-10
            prioridad[i] = rand.nextInt(5) + 1; // prioridad 1-5

            // Algunos inician bloqueados
            if (rand.nextInt(3) == 0) {
                estado[i] = "Bloqueado";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] +
                        " | Prioridad: " + prioridad[i] + " | INICIA BLOQUEADO");
            } else {
                estado[i] = "Listo";
                System.out.println("P" + id[i] + " | Tiempo: " + tiempo[i] +
                        " | Prioridad: " + prioridad[i]);
            }

            bloqueos[i] = 0;
            intentos[i] = 0;
        }

        int quantum = 2;

        System.out.println("\n=== PRIORIDADES APROPIATIVO ===");

        int tiempoActual = 0;
        int terminados = 0;

        while (terminados < numProcesos) {

            System.out.println("\n--- Tiempo " + tiempoActual + " ---");

            // Buscar proceso de mayor prioridad (aunque esté bloqueado)
            int actual = -1;// si -1 quiere decir que no se a encontrado proceso

            for (int i = 0; i < numProcesos; i++) {

                if (estado[i].equals("Terminado") || estado[i].equals("Muerto"))
                    continue;

                if (actual == -1 || prioridad[i] < prioridad[actual]) { //aqui se ddetermina cual el e proceso menor 
                    actual = i;
                }
            }

            // Si todos terminaron
            if (actual == -1) {
                break;
            }

            // SI ESTA BLOQUEADO
            if (estado[actual].equals("Bloqueado")) {

                System.out.println("P" + id[actual] + " está BLOQUEADO");
        // 
                while (estado[actual].equals("Bloqueado")) {// esto hasta que el proceso en curso se desbloquee o muera 

                    int desbloquear = rand.nextInt(2);

                    System.out.println("Intento desbloqueo P" + id[actual] +
                            " (" + (intentos[actual] + 1) + "/3): " +
                            (desbloquear == 1 ? "exitoso" : "fallido"));

                    if (desbloquear == 1) {

                        estado[actual] = "Listo";
                        intentos[actual] = 0;

                        System.out.println("P" + id[actual] + " se DESBLOQUEA");

                    } else {

                        intentos[actual]++;

                        if (intentos[actual] >= 3) {

                            System.out.println("P" + id[actual] + " MUERE POR INANICION");

                            estado[actual] = "Muerto";
                            terminados++;
                            break;
                        }
                    }

                    tiempoActual++;
                }

                if (estado[actual].equals("Muerto"))
                    continue;
            }

            // EJECUTAR PROCESO
            System.out.println("Ejecutando P" + id[actual] +
                    " (Prioridad " + prioridad[actual] + ")");

            // Posibilidad de bloqueo
            if (rand.nextInt(10) < 3) {

                System.out.println("P" + id[actual] + " se BLOQUEA");

                estado[actual] = "Bloqueado";
                bloqueos[actual]++;
                intentos[actual] = 0;

                tiempoActual++;

                continue;
            }

            // Ejecución normal
            tiempo[actual] -= quantum;
            tiempoActual += quantum;

            if (tiempo[actual] <= 0) {

                System.out.println("P" + id[actual] + " TERMINA");

                estado[actual] = "Terminado";
                terminados++;

            } else {

                System.out.println("Tiempo restante P" + id[actual] +
                        ": " + tiempo[actual]);

                estado[actual] = "Listo";
            }
        }

        // REPORTE FINAL
        System.out.println("\n=== REPORTE FINAL ===");

        for (int i = 0; i < numProcesos; i++) {

            System.out.println("P" + id[i] +
                    " | Estado: " + estado[i] +
                    " | Bloqueos: " + bloqueos[i]);
        }
    }

    public static void ejecutar() {

        PrioridadesApropiativo pa = new PrioridadesApropiativo();
        pa.PrioridadesApropiativo();
    }
}