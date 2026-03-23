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
        int numProcesosRandom = (int) (Math.random() * 10) + 1;//Generar un numero de procesos randoms entre 1 a 10 para usar en el for
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
                estaBloqueado(actual);
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

            // verificamos si el termino el proceso, si no termina la baja de prioridad
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

    private static void estaBloqueado(procesos p) {
        System.out.println("\n\t--- Ejecutando SES-HHDD (C-SCAN) para Proceso " + p.getId() + " ---");

        ArrayList<Peticiones> peticiones = p.getPeticiones(); // Asumo que tienes este Getter
        if (peticiones.isEmpty()) return;

        // 1. Ordenar peticiones por sector (1-20)
        peticiones.sort((a, b) -> Integer.compare(a.getSector(), b.getSector()));

        int cabezaActual = 0; // La cabeza inicia en 0 según la imagen
        int retardoGiro = 0;
        int tiempoTransferencia = 0;

        // C-SCAN: Solo atiende en sentido ascendente
        System.out.println("\tAtendiendo peticiones en orden circular...");

        // En C-SCAN, como empezamos en 0, todas están a la "derecha"
        for (int i = 0; i < peticiones.size(); i++) {
            Peticiones pet = peticiones.get(i);

            // Cálculo de métricas
            int distancia = Math.abs(cabezaActual - pet.getSector());
            retardoGiro += distancia; // 1 por cada sector
            tiempoTransferencia += (pet.getTipo() == 'L') ? 1 : 2; // L=1, E=2

            System.out.println("\t -> Sector: " + pet.getSector() + " [" + pet.getTipo() + "] | Mov: " + distancia);

            cabezaActual = pet.getSector();

            // PUNTO 2 DE LA IMAGEN: Generar nuevas peticiones aleatorias (1 a 3)
            if (Math.random() > 0.5 && peticiones.size() < 10) {
                int nuevas = (int)(Math.random() * 3) + 1;
                for (int n = 0; n < nuevas; n++) {
                    int nuevoSec = (int)(Math.random() * 20) + 1;
                    // Aquí podrías validar que no se repita el sector
                    peticiones.add(new Peticiones());
                }
                // Re-ordenar porque entraron nuevas
                peticiones.sort((a, b) -> Integer.compare(a.getSector(), b.getSector()));
            }
        }

        System.out.println("\t[Métricas Disco] Retardo Giro Total: " + retardoGiro);
        System.out.println("\t[Métricas Disco] Tiempo Transferencia Total: " + tiempoTransferencia);

        // Una vez atendidas, el proceso se desbloquea (Punto 3 de la imagen)
        p.setEstado(1); // 1 = Listo/Ejecución
        p.setPeticiones(new ArrayList<>()); // Limpiamos las peticiones atendidas
        System.out.println("\tEl proceso " + p.getId() + " ahora está LISTO.");
    }

    private static boolean Bloqueado (procesos p){
        for (int i = 0; i < p.getCantidadPeticiones(); i++) {
            for (int j = 0; j < p.getCantidadPeticiones(); j++) {
                if(p.getPeticiones().get(i).getSector() == j){

                }
            }
        }
        return true;
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

