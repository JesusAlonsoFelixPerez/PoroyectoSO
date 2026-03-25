package algoritmos;

import java.util.ArrayList;

import DiscoDuro.Peticiones;
import clases.*;

public class MultiplesColasPrioridad {
    public static void MCP() {
        System.out.println("\t=====Algoritmo Multiples Colas de Prioridad=====");
        //colas de prioridad
        ArrayList<procesos> Q1 = new ArrayList<>();
        ArrayList<procesos> Q2 = new ArrayList<>();
        ArrayList<procesos> Q3 = new ArrayList<>();
        ArrayList<procesos> Q4 = new ArrayList<>();

        // para el reporte al final de la ejecucion
        ArrayList<procesos> terminados = new ArrayList<>();
        ArrayList<procesos> nuncaEjecutados = new ArrayList<>();
        ArrayList<procesos> enEjecucion = new ArrayList<>();


        int tiempo = 0;
        int tiempoMonitoreo = (int) (Math.random() * 10) + 26;// genera un tiempo de monitoreo entre 25 a 35
        //int numProcesosRandom = (int) (Math.random() * 10) + 1;//Generar un numero de procesos randoms entre 1 a 10 para usar en el for
        int numProcesosRandom = 5;//Generar un numero de procesos randoms entre 1 a 10 para usar en el for
        //int quantumBase = (int) (Math.random() * 4) + 2;
        int quantumBase = 2;
        int CambioProcesos = 0;

        System.out.println("Tiempo Monitoreo: " + tiempoMonitoreo + " | Procesos Creados: " + numProcesosRandom + " | Quantum Base: " + quantumBase);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Proceso | T.Restante | Estado | V.Usado | Int.Desbloeo | Prioridad | Peticiones");

        // Creando los procesos
        for (int i = 0; i < numProcesosRandom; i++) {
            procesos p = new procesos((char) (i + 65));
            p.getPrioridad();

            if (p.getPrioridad() == 1){
                Q1.add(p);
            } else  if (p.getPrioridad() == 2){
                Q2.add(p);
            }  else  if (p.getPrioridad() == 3){
                Q3.add(p);
            }  else  if (p.getPrioridad() == 4){
                Q4.add(p);
            }
            nuncaEjecutados.add(p);
            enEjecucion.add(p);
            System.out.println(p.toString());
        }
        System.out.println("-------------------------------------------------------------------------\n");

        while (tiempo < tiempoMonitoreo && (!Q1.isEmpty() || !Q2.isEmpty() || !Q3.isEmpty() || !Q4.isEmpty())) {
            procesos actual = null;
            int colaActual = 0;


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

            // revisa si el proceso actual esta bloqueado
            if (actual.getEstado() == 0) {
                tiempo += estaBloqueado(actual, tiempo);
                devolverACola(actual, colaActual, Q1, Q2, Q3, Q4); // Regresa a su cola
                continue;
            }

            // se ejecuta el proceso
            nuncaEjecutados.remove(actual); //quita el proceso actual de nunca ejecutados

            // multiplica la prioridad por la cantidad de veces que se uso el proceso actual
            int tiempoAsignado = quantumBase * actual.getVecesUsado();
            int tiempoAEjecutar = Math.min(tiempoAsignado, actual.getTiempoRestante());

            // para no pasarnos del tiempo limite del monitoreo
            if (tiempo + tiempoAEjecutar > tiempoMonitoreo) {
                tiempoAEjecutar = tiempoMonitoreo - tiempo;
            }

            System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " es de prioridad "  + colaActual+" (Cola Q" + colaActual + ", A usado " + tiempoAEjecutar + " Unidades de Tiempo y le quedan: " + (actual.getTiempoRestante() - tiempoAEjecutar) + ")");

            // Simula la ejecucion del proceso
            tiempo += tiempoAEjecutar;
            actual.setTiempoRestante(actual.getTiempoRestante() - tiempoAEjecutar);
            actual.setVecesUsado(actual.getVecesUsado() + 1);
            CambioProcesos++;

            if (actual.getTiempoRestante() <= 0) {
                System.out.println("\t El proceso " + actual.getId() + " ha Terminado");
                enEjecucion.remove(actual);
                terminados.add(actual);
            } else if (tiempo < tiempoMonitoreo) {
                // si no termina baja el proceso de prioridad
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

    private static int estaBloqueado(procesos p , int tiempo) {
        System.out.println("\n\t--- Ejecutando C-SCAN para proceso " + p.getId() + " ---");

        ArrayList<Peticiones> lista = p.getPeticiones();
        if (lista.isEmpty()) return tiempo;

        int cabezaActual = 0;
        int retardoGiro = 0;
        int tiempoTransferencia = 0;

        // se ejecuta hasta que las peticiones del poceso se encuentren vacias
        while (!lista.isEmpty()) {
            // ordena las peticiones
            lista.sort((a, b) -> {
                int res = Integer.compare(a.getSector(), b.getSector());
                return (res == 0) ? Character.compare(a.getTipo(), b.getTipo()) : res;
            });

            // busca la sigueinte peticion mas cercana por delate
            int indiceSiguiente = -1;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getSector() >= cabezaActual) {
                    indiceSiguiente = i;
                    break;
                }
            }

            // cuando llega al final se devuelve a 0
            if (indiceSiguiente == -1) {
                System.out.println("\t No hay peticiones adelante. Brazo salta de sector " + cabezaActual + " al sector 0.");
                // el salto a 0 tamien consume tiempo
                retardoGiro += cabezaActual;
                cabezaActual = 0;
                continue;
            }

            // atiende las peticiones que encuentra
            Peticiones pet = lista.remove(indiceSiguiente);
            int distancia = Math.abs(cabezaActual - pet.getSector());
            retardoGiro += distancia;
            tiempoTransferencia += (pet.getTipo() == 'L') ? 1 : 2;

            System.out.println("\tAtendiendo Sector: " + pet.getSector() + pet.getTipo() + " | Mov: " + distancia);
            cabezaActual = pet.getSector();

            // probabilidad de generar nuevas peticiones
            if (Math.random() > 0.8 && lista.size() < 10) {
                Peticiones nueva = new Peticiones();
                // revisa que las peticiones no estan repetidas
                boolean existe = false;
                for(Peticiones ex : lista) { if(ex.getSector() == nueva.getSector()) existe = true; }

                if(!existe) {
                    lista.add(nueva);
                    System.out.println("\t\tNueva petición: " + nueva.getSector() + nueva.getTipo());
                }
            }
        }

        System.out.println("\t[Métricas Disco] Retardo: " + retardoGiro + " | Transferencia: " + tiempoTransferencia);
        p.setEstado(1);
        System.out.println("\tEl proceso " + p.getId() + " terminó sus peticiones");
        return tiempo + retardoGiro;
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

