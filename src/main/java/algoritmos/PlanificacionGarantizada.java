package algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import clases.*;

public class PlanificacionGarantizada {

    public static void ejecutar() {

        System.out.println("\t===== Planificación Garantizada =====");

        ArrayList<procesos> procesos = new ArrayList<>();

        ArrayList<procesos> terminados = new ArrayList<>();
        ArrayList<procesos> nuncaEjecutados = new ArrayList<>();
        ArrayList<procesos> enEjecucion = new ArrayList<>();

        int tiempo = 0;
        int tiempoMonitoreo = (int) (Math.random() * 10) + 26; // 26–35
        //int numProcesos = (int) (Math.random() * 10) + 1;
        int numProcesos = 3;
        //int quantum = (int) (Math.random() * 4) + 2; // 2–5
        int quantum = 2;
        int cambiosProcesos = 0;

        System.out.println("Tiempo Monitoreo: " + tiempoMonitoreo + " | Procesos: " + numProcesos + " | Quantum: " + quantum);

        System.out.println("--------------------------------------------------");
        System.out.println("Proceso | T.Restante | Estado | V.Usado | Int.Desbloeo");
        // Crear procesos
        for (int i = 0; i < numProcesos; i++) {
            procesos p = new procesos((char) (i + 65));
            procesos.add(p);
            nuncaEjecutados.add(p);
            enEjecucion.add(p);
            System.out.println(p.toString());
        }
        //verifica si el tiempo del proceso esta repetido
        System.out.println("================================================================");
        for (int i = 0; i < numProcesos; i++) {
            for (int j = 0; j <numProcesos ; j++) {
                if(procesos.get(i).getTiempoRestante() == procesos.get(j).getTiempoRestante() && procesos.get(i).getId() != procesos.get(j).getId()) {
                    System.out.println(procesos.get(i).toString());
                    System.out.println(procesos.get(j).toString());
                    System.out.println(procesos.get(i).getTiempoRestante() == procesos.get(j).getTiempoRestante());
                    procesos.get(i).setEsRepetido(true);
                }
            }
        }
        System.out.println("================================================================");

        System.out.println("--------------------------------------------------\n");
        // llevar el control de uso de la cpu
        HashMap<procesos, Integer> tiempoUsado = new HashMap<>();
        for (procesos p : procesos) {
            tiempoUsado.put(p, 0);
        }

        //bucle principal
        while (tiempo < tiempoMonitoreo && !procesos.isEmpty()) {

            procesos actual = null;
            double menorProporcion = Double.MAX_VALUE;

            // busca el más atrasado proporcionalmente
            for (procesos p : procesos) {
                if (p.getTiempoRestante() > 0) {
                    double proporcion;

                    if (tiempo == 0) {
                        proporcion = 0;
                    } else {
                        proporcion = (double) tiempoUsado.get(p) / tiempo;
                    }

                    if (proporcion < menorProporcion) {
                        menorProporcion = proporcion;
                        actual = p;
                    }
                }
            }

            if (actual == null) break;

            //verifica si esta bloqueado
            if (estaBloqueado(actual)) {
                continue;
            }

            // quita el proceso actualde nunca ejecutados
            nuncaEjecutados.remove(actual);

            // el tiempo que ejecutara el proceso
            int tiempoAEjecutar = Math.min(quantum, actual.getTiempoRestante());

            // evitar pasar límite de monitoreo
            if (tiempo + tiempoAEjecutar > tiempoMonitoreo) {
                tiempoAEjecutar = tiempoMonitoreo - tiempo;
            }

            if(actual.getTiempoRestante() < 0){
                actual.setTiempoRestante(0);
            }


            // simula la ejecución
            if (actual.getEsRepetido() == true){
                int tiempoUsadoProceso = (tiempoAEjecutar * 2);
                int leQuedan = (actual.getTiempoRestante() - (tiempoAEjecutar * 2));
                if (leQuedan < 0){
                    tiempoAEjecutar -= leQuedan;
                    leQuedan = 0;
                }
                System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " es repetido? " + actual.getEsRepetido()+ " (Uso " + tiempoUsadoProceso + " Unidades de Tiempo y le quedan: " + leQuedan + ", Unidades de tiempo que le tocaban "+ tiempoAEjecutar * 2 + " )");
                actual.setTiempoRestante(actual.getTiempoRestante() - (tiempoAEjecutar * 2));
                tiempoUsado.put(actual, tiempoUsado.get(actual) + tiempoAEjecutar);
                tiempo += tiempoAEjecutar * 2;
                cambiosProcesos++;

            } else{
                int tiempoUsadoProceso = (tiempoAEjecutar);
                int leQuedan = (actual.getTiempoRestante() - tiempoAEjecutar);
                if (leQuedan < 0){
                    tiempoAEjecutar -= leQuedan;
                    leQuedan = 0;
                }
                System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " es repetido? " + actual.getEsRepetido()+ " (Uso " + tiempoUsadoProceso + " Unidades de Tiempo y le quedan: " + leQuedan + ", Unidades de tiempo que le tocaban "+ tiempoAEjecutar + ")");
                actual.setTiempoRestante(actual.getTiempoRestante() - tiempoAEjecutar);
                tiempoUsado.put(actual, tiempoUsado.get(actual) + tiempoAEjecutar);
                tiempo += tiempoAEjecutar;
                cambiosProcesos++;
            }



            // verificar si un proceso ya termino
            if (actual.getTiempoRestante() <= 0) {
                System.out.println("\tProceso " + actual.getId() + " ha terminado");
                terminados.add(actual);
                enEjecucion.remove(actual);
            }
        }
        //hace el reporte final
       reporteFinal(terminados,nuncaEjecutados,enEjecucion,cambiosProcesos);
    }

    private static boolean estaBloqueado(procesos p) {

        if (p.getEstado() == 1) {
            return false;
        } else {

            int intentoDesbloquear = (int) (Math.random() * 2);

            if (intentoDesbloquear == 0) {

                p.setIntentoDesbloquear(p.getIntentoDesbloquear() + 1);

                System.out.println("El proceso " + p.getId() + " sigue bloqueado.");
                System.out.println("Intentos fallidos: " + p.getIntentoDesbloquear());

                if (p.getIntentoDesbloquear() >= 3) {
                    System.out.println("Muerte del proceso " + p.getId() + " por inanición");
                    System.exit(0);
                }

                return true;

            } else {

                System.out.println("\tEl proceso " + p.getId() + " logró desbloquearse");

                p.setEstado(1);
                p.setIntentoDesbloquear(0);
                return false;
            }
        }
    }

    private static void reporteFinal(
            ArrayList<procesos> terminados,
            ArrayList<procesos> nuncaEjecutados,
            ArrayList<procesos> enEjecucion,
            int cambiosProcesos) {

        System.out.println("\n========= REPORTE FINAL =========");

        System.out.print("Procesos terminados: ");
        for (procesos p : terminados) {
            System.out.print(p.getId() + " ");
        }

        System.out.print("\nProcesos nunca ejecutados: ");
        for (procesos p : nuncaEjecutados) {
            System.out.print(p.getId() + " ");
        }

        System.out.print("\nProcesos en ejecución: ");
        for (procesos p : enEjecucion) {
            if (p.getTiempoRestante() > 0) {
                System.out.print(p.getId() + " ");
            }
        }

        System.out.println("\nCambios de proceso: " + cambiosProcesos);
        System.out.println("==================================");
    }

}