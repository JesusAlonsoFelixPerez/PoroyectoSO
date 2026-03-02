package algoritmos;

import java.util.ArrayList;
import clases.*;
import utilidades.*;

public class MultiplesColasPrioridad {
    public static void MCP() {
        System.out.println("\t=====Algoritmo Multiples Colas de Prioridad=====");

        ArrayList<Mcp> Q1 = new ArrayList<>();
        ArrayList<Mcp> Q2 = new ArrayList<>();
        ArrayList<Mcp> Q3 = new ArrayList<>();
        ArrayList<Mcp> Q4 = new ArrayList<>();

        // Listas para el reporte final
        ArrayList<Mcp> terminados = new ArrayList<>();
        ArrayList<Mcp> nuncaEjecutados = new ArrayList<>();
        ArrayList<Mcp> enEjecucion = new ArrayList<>();

        int tiempo = 0;
        int tiempoMonitoreo = (int) (Math.random() * 10) + 26;// genera un tiempo de monitoreo entre 25 a 35
        int numProcesosRandom = (int) (Math.random() * 10) + 1;//Generar un numero de procesos randoms entre 1 a 10 para usar en el for
        //int quantumBase = (int) (Math.random() * 4) + 2;
        int quantumBase = 2;
        int CambioProcesos = 0;

        System.out.println("Tiempo Monitoreo: " + tiempoMonitoreo + " | Procesos Creados: " + numProcesosRandom + " | Quantum Base: " + quantumBase);
        System.out.println("--------------------------------------------------");

        // Creando los procesos
        for (int i = 0; i < numProcesosRandom; i++) {
            Mcp p = new Mcp((char) (i + 65));
            Q1.add(p);
            nuncaEjecutados.add(p);
            enEjecucion.add(p);
            System.out.println(p.toString());
        }
        System.out.println("--------------------------------------------------\n");

        while (tiempo < tiempoMonitoreo && (!Q1.isEmpty() || !Q2.isEmpty() || !Q3.isEmpty() || !Q4.isEmpty())) {
            Mcp actual = null;
            int colaActual = 0;
            boolean todosRevisadosQ1 = false;

            // Extraer proceso de la cola de mayor prioridad disponible
            if      (!Q1.isEmpty()) { actual = Q1.remove(0); colaActual = 1; }
            else if (!Q2.isEmpty()) { actual = Q2.remove(0); colaActual = 2; }
            else if (!Q3.isEmpty()) { actual = Q3.remove(0); colaActual = 3; }
            else if (!Q4.isEmpty()) { actual = Q4.remove(0); colaActual = 4; }

            if (actual == null) break;

            // Revisar si está bloqueado
            if (ProcesoBloqueado.estaBloqueado(actual)) {
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

            // Simulamos la ejecución
            tiempo += tiempoAEjecutar;
            actual.setTiempoRestante(actual.getTiempoRestante() - tiempoAEjecutar);
            actual.setVecesUsado(actual.getVecesUsado() + 1);
            CambioProcesos++;

            //formatearLista(Q1);
            // Verificamos qué pasa con el proceso tras ejecutar
            if (actual.getTiempoRestante() <= 0) {
                System.out.println("\t El proceso " + actual.getId() + " ha Terminado");
                enEjecucion.remove(actual);
                terminados.add(actual);
            } else if (tiempo < tiempoMonitoreo) {
                // Si no terminó, baja su prioridad pasándolo a una cola inferior (máximo Q4)
                devolverACola(actual, Math.min(colaActual + 1, 4), Q1, Q2, Q3, Q4);
            }
            System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " (Cola Q" + colaActual + ", A usado " + tiempoAEjecutar + " Unidades de Tiempo y le quedan: " + (actual.getTiempoRestante()) + ")");

        }



    }

    // Método de utilidad para encolar un proceso en su respectiva cola
    private static void devolverACola(Mcp p, int cola, ArrayList<Mcp> q1, ArrayList<Mcp> q2, ArrayList<Mcp> q3, ArrayList<Mcp> q4) {
        switch (cola) {
            case 1: q1.add(p); break;
            case 2: q2.add(p); break;
            case 3: q3.add(p); break;
            case 4: q4.add(p); break;
        }
    }

    // Método de utilidad para imprimir las listas en el reporte
    private static String formatearLista(ArrayList<Mcp> lista) {
        if (lista.isEmpty()) return "Ninguno";
        StringBuilder sb = new StringBuilder();
        for (Mcp p : lista) sb.append(p.getId()).append(" ");
        return sb.toString().trim();
    }

}

