package algoritmos;

import java.util.Random;

public class RoundRobinNoAprop {

    public static void ejecutar() {

        // Random para generar valores aleatorios
        Random r = new Random();

        System.out.println("===== ROUND ROBIN NO APROPIATIVO =====");

        // Número de procesos aleatorios entre 1 y 10
        int n = r.nextInt(10) + 1;
//arreglos
        System.out.println("Procesos: " + n);
        int[] tiempo = new int[n];
        String[] estado = new String[n];
        boolean[] ejecuto = new boolean[n];
        int[] intentos = new int[n];

        // creacion de procesos 
        for (int i = 0; i < n; i++) {

            // Tiempo aleatorio 
            tiempo[i] = r.nextInt(8) + 3;

            // Estado inicial aleatorio: Listo o Bloqueado
            estado[i] = (r.nextInt(2) == 0) ? "Listo" : "Bloqueado";

            ejecuto[i] = false;
            intentos[i] = 0;

            // Mostrar información del proceso
            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }

        int cambios = 0;
        int indice = 0;

        // -------- --------
        // El ciclo continúa hasta que todos los procesos terminen
        while (true) {

            boolean todosTerminados = true;

            // Verificar si todavía hay procesos activos
            for (int i = 0; i < n; i++) {
                if (!estado[i].equals("Terminado")) {
                    todosTerminados = false;
                    break;
                }
            }

            // Si todos terminaron, salir del ciclo
            if (todosTerminados) break;

            // Si el proceso actual ya terminó, pasar al siguiente
            if (estado[indice].equals("Terminado")) {
                indice = (indice + 1) % n;
                continue;
            }

            // corrreccion
            if (estado[indice].equals("Bloqueado")) {

                System.out.println("\nP" + (indice + 1) + " está BLOQUEADO");

                // Se genera aleatoriamente si se desbloquea o no
                int desbloqueo = r.nextInt(2);

                if (desbloqueo == 1) {

                    // El proceso logra desbloquearse
                    estado[indice] = "Listo";

                    // Reiniciar contador de intentos
                    intentos[indice] = 0;

                    System.out.println("Proceso P" + (indice + 1) + " se desbloqueó");
                } else {
                    // Falló el intento de desbloqueo
                    intentos[indice]++;
                      System.out.println("Intento de desbloqueo #" +
                            intentos[indice] + " fallido para P" + (indice + 1));

                    // aqui hice el cambio para cuando termine o muera por inanicion se brinque al siguiente
                    if (intentos[indice] == 3) {

                        System.out.println("P" + (indice + 1) +
                                " murió por inanición");

                        estado[indice] = "Terminado";
                        // Pasar al siguiente proceso
                        indice = (indice + 1) % n;
                  
                    }
                }  // Volver al inicio del ciclo sin cambiar proceso
                continue;
            }

            // -------- EJECUCIÓN DEL PROCESO --------
            if (estado[indice].equals("Listo")) {

                System.out.println("\nP" + (indice + 1) + " ejecutando...");

                // Marcar que el proceso sí ejecutó al menos una vez
                ejecuto[indice] = true;

                // Aumentar contador de cambios de proceso
                cambios++;

                int tiempoEjecucion = tiempo[indice];

                // Como es no apropiativo, ejecuta todo su tiempo
                tiempo[indice] = 0;

                // El proceso termina
                estado[indice] = "Terminado";

                System.out.println("P" + (indice + 1) +
                        " finalizó, ejecutó " + tiempoEjecucion + " unidades");

                // Mostrar estado actual de los procesos
                mostrar(n, tiempo, estado);

                // Pasar al siguiente proceso
                indice = (indice + 1) % n;
            }
        }
        reporteFinal(n, estado, ejecuto, cambios);
    }

    //  PCB 
    static void mostrar(int n, int[] tiempo, String[] estado) {

        System.out.println("\nPCB:");

        for (int i = 0; i < n; i++) {

            System.out.println("P" + (i + 1) +
                    " | Restante: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }
    }

    // -------- REPORTE FINAL --------
    static void reporteFinal(int n, String[] estado, boolean[] ejecuto, int cambios) {

        int finalizados = 0;
        int nunca = 0;
        int activos = 0;

        System.out.println("\n===== REPORTE FINAL =====");

        for (int i = 0; i < n; i++) {

            // Contar procesos terminados
            if (estado[i].equals("Terminado"))
                finalizados++;

            // Procesos que nunca entraron al CPU
            if (!ejecuto[i])
                nunca++;

            // Procesos que quedaron activos
            if (!estado[i].equals("Terminado"))
                activos++;
        }

        System.out.println("Procesos finalizados: " + finalizados);
        System.out.println("Procesos que nunca entraron: " + nunca);
        System.out.println("Procesos aun activos: " + activos);
        System.out.println("Cambios de proceso: " + cambios);
    }
}