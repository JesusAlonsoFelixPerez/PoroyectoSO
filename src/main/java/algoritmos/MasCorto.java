package algoritmos;

import java.util.Random;

public class MasCorto {

    public static void ejecutar() {

        Random r = new Random();

        System.out.println("===== PROCESO MAS CORTO PRIMERO (SJF) =====");

        int tiempoMonitoreo = r.nextInt(16) + 20;
        int n = r.nextInt(10) + 1;

        System.out.println("Tiempo de monitoreo: " + tiempoMonitoreo);
        System.out.println("Numero de procesos: " + n);

        int[] tiempo = new int[n];
        String[] estado = new String[n];
        boolean[] ejecuto = new boolean[n];
        int[] intentos = new int[n];

        for (int i = 0; i < n; i++) {
            tiempo[i] = r.nextInt(8) + 3;
            int est = r.nextInt(2) + 1;

            if (est == 1)
                estado[i] = "Listo";
            else
                estado[i] = "Bloqueado";

            ejecuto[i] = false;
            intentos[i] = 0;

            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }

        int tiempoActual = 0;
        int cambios = 0;

        while (tiempoActual < tiempoMonitoreo) {

            int indice = -1;
            int menor = 999;

            for (int i = 0; i < n; i++) {
                if (estado[i].equals("Listo") && tiempo[i] < menor && tiempo[i] > 0) {
                    menor = tiempo[i];
                    indice = i;
                }
            }

            if (indice == -1) {
                System.out.println("No hay procesos listos.");
                break;
            }

            if (estado[indice].equals("Bloqueado")) {

                int desbloqueo = r.nextInt(2);

                if (desbloqueo == 1) {
                    estado[indice] = "Listo";
                    intentos[indice] = 0;
                } else {
                    intentos[indice]++;
                }

                if (intentos[indice] == 3) {
                    System.out.println("Muerte del proceso P" + (indice + 1) + " por inanicion");
                    break;
                }
            }

            System.out.println("\nP" + (indice + 1) + " entra en ejecucion");

            tiempoActual += tiempo[indice];
            tiempo[indice] = 0;
            estado[indice] = "Terminado";
            ejecuto[indice] = true;
            cambios++;

            mostrarPCB(n, tiempo, estado);
        }

        reporteFinal(n, estado, ejecuto, cambios);
    }

    static void mostrarPCB(int n, int[] tiempo, String[] estado) {

        System.out.println("\nPCB ACTUAL:");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) +
                    " | Restante: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }
    }

    static void reporteFinal(int n, String[] estado,
            boolean[] ejecuto, int cambios) {

        int finalizados = 0;
        int nunca = 0;
        int enEjecucion = 0;

        System.out.println("\n===== REPORTE FINAL =====");

        for (int i = 0; i < n; i++) {

            if (estado[i].equals("Terminado"))
                finalizados++;
            if (!ejecuto[i])
                nunca++;
            if (estado[i].equals("Listo") || estado[i].equals("Bloqueado"))
                enEjecucion++;
        }

        System.out.println("Procesos finalizados: " + finalizados);
        System.out.println("Procesos que nunca entraron en ejecucion: " + nunca);
        System.out.println("Procesos aun activos: " + enEjecucion);
        System.out.println("Cambios de proceso: " + cambios);
    }
}