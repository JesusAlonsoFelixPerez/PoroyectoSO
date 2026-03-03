package algoritmos;

import java.util.ArrayList;
import clases.*;

public class MultiplesColasPrioridad {
    public static void MCP() {
        System.out.println("\t=====Algoritmo Multiples Colas de Prioridad=====");

        ArrayList<procesos> Q1 = new ArrayList<>();
        ArrayList<procesos> Q2 = new ArrayList<>();
        ArrayList<procesos> Q3 = new ArrayList<>();
        ArrayList<procesos> Q4 = new ArrayList<>();

        // Listas para el reporte final
        ArrayList<procesos> terminados = new ArrayList<>();
        ArrayList<procesos> nuncaEjecutados = new ArrayList<>();
        ArrayList<procesos> enEjecucion = new ArrayList<>();

        int tiempo = 0;
        int tiempoMonitoreo = (int) (Math.random() * 10) + 26;// genera un tiempo de monitoreo entre 25 a 35
        int numProcesosRandom = (int) (Math.random() * 10) + 1;//Generar un numero de procesos randoms entre 1 a 10 para usar en el for
        int quantumBase = (int) (Math.random() * 4) + 2;
        int CambioProcesos = 0;

        System.out.println("Tiempo Monitoreo: " + tiempoMonitoreo + " | Procesos Creados: " + numProcesosRandom + " | Quantum Base: " + quantumBase);
        System.out.println("--------------------------------------------------");
        System.out.println("Proceso | T.Restante | Estado | V.Usado | Int.Desbloeo");

        // Creando los procesos
        for (int i = 0; i < numProcesosRandom; i++) {
            procesos p = new procesos((char) (i + 65));
            Q1.add(p);
            nuncaEjecutados.add(p);
            enEjecucion.add(p);
            System.out.println(p.toString());
        }
        System.out.println("--------------------------------------------------\n");

        while (tiempo < tiempoMonitoreo && (!Q1.isEmpty() || !Q2.isEmpty() || !Q3.isEmpty() || !Q4.isEmpty())) {
            procesos actual = null;
            int colaActual = 0;
            boolean todosRevisadosQ1 = false;

            // Extraer proceso de la cola de mayor prioridad disponible
            if (!Q1.isEmpty()) {
                actual = Q1.remove(0);
                colaActual = 1;
            } else if (!Q2.isEmpty()) {
                actual = Q2.remove(0);
                colaActual = 2;
            } else if (!Q3.isEmpty()) {
                actual = Q3.remove(0);
                colaActual = 3;
            } else if (!Q4.isEmpty()) {
                actual = Q4.remove(0);
                colaActual = 4;
            }

            if (actual == null) break;

            // Revisar si está bloqueado
            if (estaBloqueado(actual)) {
                devolverACola(actual, colaActual, Q1, Q2, Q3, Q4); // Regresa a su cola
                continue;
            }

            // Si llegamos aquí, se ejecutará el proceso
            nuncaEjecutados.remove(actual); // Ya no pertenece a la lista de "nunca ejecutados"

            // Multiplicador: Prioridad por cantidad de veces usado
            int tiempoAsignado = quantumBase * actual.getVecesUsado();
            int tiempoAEjecutar = Math.min(tiempoAsignado, actual.getTiempoRestante());

            // Evitamos pasarnos del tiempo límite de monitoreo
            if (tiempo + tiempoAEjecutar > tiempoMonitoreo) {
                tiempoAEjecutar = tiempoMonitoreo - tiempo;
            }

            System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " (Cola Q" + colaActual + ", A usado " + tiempoAEjecutar + " Unidades de Tiempo y le quedan: " + (actual.getTiempoRestante() - tiempoAEjecutar) + ")");

            // Simulamos la ejecución
            tiempo += tiempoAEjecutar;
            actual.setTiempoRestante(actual.getTiempoRestante() - tiempoAEjecutar);
            actual.setVecesUsado(actual.getVecesUsado() + 1);
            CambioProcesos++;

            // Verificamos qué pasa con el proceso tras ejecutar
            if (actual.getTiempoRestante() <= 0) {
                System.out.println("\t El proceso " + actual.getId() + " ha Terminado");
                enEjecucion.remove(actual);
                terminados.add(actual);
            } else if (tiempo < tiempoMonitoreo) {
                // Si no terminó, baja su prioridad pasándolo a una cola inferior (máximo Q4)
                devolverACola(actual, Math.min(colaActual + 1, 4), Q1, Q2, Q3, Q4);
            }
        }
        //hace el reporte final
        reporteFinal(terminados,nuncaEjecutados,enEjecucion,CambioProcesos);

    }

    // devolver un proceso a su respectuva cola
    private static void devolverACola(procesos p, int cola, ArrayList<procesos> q1, ArrayList<procesos> q2, ArrayList<procesos> q3, ArrayList<procesos> q4) {
        switch (cola) {
            case 1:
                q1.add(p);
                break;
            case 2:
                q2.add(p);
                break;
            case 3:
                q3.add(p);
                break;
            case 4:
                q4.add(p);
                break;
        }
    }

    private static boolean estaBloqueado(procesos p) {
        if (p.getEstado() == 1) {
            return false;
        } else {
            //genera un numero random de 1 o 0
            int intentoDesbloquear = (int) (Math.random() * 2);

            if (intentoDesbloquear == 0) {
                //fallo al debloquear
                p.setIntentoDesbloquear(p.getIntentoDesbloquear() + 1);
                System.out.println("El proceso " + p.getId() + " Sigue bloqueado.");
                System.out.println("Intentos fallidos: " + p.getIntentoDesbloquear());

                if (p.getIntentoDesbloquear() >= 3) {
                    System.out.println("Muerte del proceso " + p.getId() + " Por inanicion");
                    System.exit(0);
                }
                return true;
            } else {
                //logro desbloquearse
                System.out.println("El proceso " + p.getId() + " logro desbloquearse");
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

