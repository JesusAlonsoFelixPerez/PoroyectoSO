package algoritmos;

import java.util.Random;

public class RoundRobinAprop {

    public static void ejecutar() {

        Random r = new Random();

        System.out.println("===== ROUND ROBIN APROPIATIVO =====");

        int n = r.nextInt(10) + 1;
        int tiempoMonitoreo = r.nextInt(16) + 20;
        int quantum = r.nextInt(4) + 2;

        System.out.println("Procesos: " + n);
        System.out.println("Tiempo monitoreo: " + tiempoMonitoreo);
        System.out.println("Quantum: " + quantum);

        int[] tiempo = new int[n];
        String[] estado = new String[n];
        boolean[] ejecuto = new boolean[n];

        DiscoSCAN.inicializar(n);

        for (int i = 0; i < n; i++) {
            tiempo[i] = r.nextInt(8) + 3;
            estado[i] = (r.nextInt(2) == 0) ? "Listo" : "Bloqueado";
            ejecuto[i] = false;
            
            if (estado[i].equals("Bloqueado")) {
                DiscoSCAN.agregarPeticiones(i);
            }

            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }

        int reloj = 0;
        int cambios = 0;

        while (reloj < tiempoMonitoreo) {
            
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
                        System.out.println("P" + (i+1) + " desbloqueado");
                    }
                }
            }
            
            for (int i = 0; i < n; i++) {

                if (reloj >= tiempoMonitoreo) break;
                if (estado[i].equals("Terminado")) continue;
                if (estado[i].equals("Bloqueado")) continue;

                System.out.println("\nP" + (i + 1) + " ejecutando");

                int ejecutar = Math.min(quantum, tiempo[i]);
                tiempo[i] -= ejecutar;
                reloj += ejecutar;

                if (tiempo[i] <= 0)
                    estado[i] = "Terminado";

                ejecuto[i] = true;
                cambios++;

                mostrarPCB(n, tiempo, estado);
            }
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
            if (estado[i].equals("Terminado")) finalizados++;
            if (!ejecuto[i]) nunca++;
            if (!estado[i].equals("Terminado")) activos++;
        }

        System.out.println("Procesos finalizados: " + finalizados);
        System.out.println("Procesos que nunca entraron: " + nunca);
        System.out.println("Procesos aun activos: " + activos);
        System.out.println("Cambios de proceso: " + cambios);
    }
}