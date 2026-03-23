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

        DiscoSCAN.inicializar(n);

        for (int i = 0; i < n; i++) {

            tiempo[i] = r.nextInt(8) + 3;

            int est = r.nextInt(2) + 1;
            if (est == 1) {
                estado[i] = "Listo";
            } else {
                estado[i] = "Bloqueado";
                DiscoSCAN.agregarPeticiones(i);
            }

            ejecuto[i] = false;
            intentos[i] = 0;

            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }

        int tiempoActual = 0;
        int cambios = 0;

        while (tiempoActual < tiempoMonitoreo) {

            boolean hayBloqueados = false;
            for (int i = 0; i < n; i++) {
                if (estado[i].equals("Bloqueado")) {
                    hayBloqueados = true;
                    break;
                }
            }
            
            if (hayBloqueados) {
                System.out.println("\n[SCAN] Atendiendo peticiones de disco");
                DiscoSCAN.ejecutarSCAN();
                
                for (int i = 0; i < n; i++) {
                    if (estado[i].equals("Bloqueado") && !DiscoSCAN.tienePeticiones(i)) {
                        estado[i] = "Listo";
                        intentos[i] = 0;
                        System.out.println("P" + (i+1) + " desbloqueado");
                    }
                }
            }

            int indice = -1;
            int menor = 999;

            for (int i = 0; i < n; i++) {
                if (estado[i].equals("Listo") && tiempo[i] < menor && tiempo[i] > 0) {
                    menor = tiempo[i];
                    indice = i;
                }
            }

            if (indice == -1) {
                break;
            }

            System.out.println("\nP" + (indice + 1) + " entra en ejecucion");

            tiempoActual += tiempo[indice];
            tiempo[indice] = 0;
            estado[indice] = "Terminado";
            ejecuto[indice] = true;
            cambios++;
            mostrarPCB(n, tiempo, estado);
        }

        DiscoSCAN.mostrarEstadisticas();
        reporteFinal(n, estado, ejecuto, cambios);
    }

    static void mostrarPCB(int n, int[] tiempo, String[] estado) {
        System.out.println("\nPCB:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) +
                    " | Restante: " + tiempo[i] +
                    " | Estado: " + estado[i]);
            
            System.out.print(" | Disco: ");
            if (DiscoSCAN.peticiones[i].isEmpty()) {
                System.out.print("ninguna");
            } else {
                for (int j = 0; j < DiscoSCAN.peticiones[i].size(); j++) {
                    System.out.print(DiscoSCAN.peticiones[i].get(j) +
                            "" + DiscoSCAN.tipo[i].get(j) + " ");
                }
            }
            System.out.println();
        }
    }

    static void reporteFinal(int n, String[] estado,
                             boolean[] ejecuto, int cambios) {

        int finalizados = 0;
        int nunca = 0;
        int activos = 0;
        System.out.println("\n===== REPORTE FINAL =====");

        for (int i = 0; i < n; i++) {
            if (estado[i].equals("Terminado"))
                finalizados++;
            if (!ejecuto[i])
                nunca++;
            if (estado[i].equals("Listo") || estado[i].equals("Bloqueado"))
                activos++;
        }

        System.out.println("Procesos finalizados: " + finalizados);
        System.out.println("Procesos que nunca entraron: " + nunca);
        System.out.println("Procesos aun activos: " + activos);
        System.out.println("Cambios de proceso: " + cambios);
    }
}