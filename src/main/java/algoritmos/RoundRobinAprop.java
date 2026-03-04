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
        int[] intentos = new int[n];

        for (int i = 0; i < n; i++) {

            tiempo[i] = r.nextInt(8) + 3;
            estado[i] = (r.nextInt(2) == 0) ? "Listo" : "Bloqueado";
            ejecuto[i] = false;
            intentos[i] = 0;

            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i]);
        }
        int reloj = 0;
        int cambios = 0;

        while (reloj < tiempoMonitoreo) {

            boolean todosTerminados = true;

            for (int i = 0; i < n; i++) {
                if (reloj >= tiempoMonitoreo)
                    break;
                if (estado[i].equals("Terminado"))
                    continue;
                todosTerminados = false;
                if (estado[i].equals("Bloqueado")) {
                    int desbloqueo = r.nextInt(2);
                    if (desbloqueo == 1) {
                        estado[i] = "Listo";
                        intentos[i] = 0;
                        System.out.println("P" + (i + 1) + " se desbloqueo");
                    } else {
                        intentos[i]++;
                        System.out.println("Intento fallido desbloqueo P" + (i + 1));
                    }
                    if (intentos[i] == 3) {
                        System.out.println("Muerte del proceso P" + (i + 1) + " por inanicion");
                        reporteFinal(n, estado, ejecuto, cambios);
                        return;
                    }
                    continue;

                }
                System.out.println("\nP" + (i + 1) + " ejecutando");
                int ejecutar = Math.min(quantum, tiempo[i]);
                tiempo[i] -= ejecutar;
                reloj += ejecutar;
                if (tiempo[i] <= 0)
                    estado[i] = "Terminado";

                ejecuto[i] = true;
                cambios++;
                mostrar(n, tiempo, estado);
            }
            if (todosTerminados)
                break;
        }

        reporteFinal(n, estado, ejecuto, cambios);
    }

    static void mostrar(int n, int[] tiempo, String[] estado) {
        System.out.println("\nPCB:");
        for (int i = 0; i < n; i++)
            System.out.println("P" + (i + 1) +
                    " | Restante: " + tiempo[i] +
                    " | Estado: " + estado[i]);
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
            if (!estado[i].equals("Terminado"))
                activos++;
        }
        System.out.println("Procesos finalizados: " + finalizados);
        System.out.println("Procesos que nunca entraron: " + nunca);
        System.out.println("Procesos aun activos: " + activos);
        System.out.println("Cambios de proceso: " + cambios);
    }
}