package algoritmos;

import java.util.ArrayList;
import java.util.HashMap;
import clases.*;

public class PlanificacionGarantizada {

    public static void ejecutar() {

        System.out.println("\t===== Planificación Garantizada =====");

        ArrayList<procesos> procesos = new ArrayList<>();

        //para el reporte al final de la ejecucion
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
        // llevar el control de uso de la cpu
        HashMap<procesos, Integer> tiempoUsado = new HashMap<>();
        for (procesos p : procesos) {//un bucle que recorre todos los procesos y a todos le agrega un valor inicial de 0
            tiempoUsado.put(p, 0);
        }

        while (tiempo < tiempoMonitoreo && !procesos.isEmpty()) {
            //entra en el bucle mientras mientras que el tiempo sea menor al tiempo de monitoreo y 
            // cuando los procesos no se encuentren vacios

            procesos actual = null;
            double menorProporcion = Double.MAX_VALUE;

            // busca el más atrasado proporcionalmente
            for (procesos p : procesos) { //recorre todos lo proceso
                if (p.getTiempoRestante() > 0) { //verifica si aun tiene tiempo restante
                    double proporcion;

                    if (tiempo == 0) {
                        proporcion = 0;
                    } else {
                        proporcion = (double) tiempoUsado.get(p) / tiempo;
                    }

                    if (proporcion < menorProporcion) {//compara el proceso con el de mayor proporcion y si es menor entra en el for
                        menorProporcion = proporcion; //el proceso pasa a hacer el de meor porporcion
                        actual = p;//selecciona el proceso de menor proporcion para ejecutarlo
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

            System.out.println("Tiempo [" + tiempo + "]\tSe ejecuta Proceso " + actual.getId() + " (Uso " + tiempoAEjecutar + " Unidades de Tiempo y le quedan: " + (actual.getTiempoRestante() - tiempoAEjecutar) + ", Unidades de tiempo que le tocaban "+ tiempoAEjecutar * 2 + " )");
            
            // simula la ejecución
            actual.setTiempoRestante(actual.getTiempoRestante() - tiempoAEjecutar);//al tiempo restante del proceso le resta el tiempo que se va a ejecutar
            tiempoUsado.put(actual, tiempoUsado.get(actual) + tiempoAEjecutar);//acumula el tiempo que un proceso a tenido
            tiempo += tiempoAEjecutar;//suma el tiempo ejecutado al tiempo total
            cambiosProcesos++; 

            // verifica si el proceso actual ya termino
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

        if (p.getEstado() == 1) {//verifica si el proceso esta bloqueado
            return false;//si no esta bloqueado devuelve false
        } else {
            int intentoDesbloquear = (int) (Math.random() * 2);// genera de manera random un 1 o 0
           
            if (intentoDesbloquear == 0) { //si sale 0 entonces se queda bloqueado
                p.setIntentoDesbloquear(p.getIntentoDesbloquear() + 1); // suma 1 a los intentos de desbloqueo
                System.out.println("El proceso " + p.getId() + " sigue bloqueado.");
                System.out.println("Intentos fallidos: " + p.getIntentoDesbloquear());

                if (p.getIntentoDesbloquear() >= 3) { //si llega a 3 intentos de desbloqueo el proceso muere y se termina la ejecucion
                    System.out.println("Muerte del proceso " + p.getId() + " por inanición");
                    System.exit(0);
                }
                return true;//regresa true por que sigue bloqueado
            } else {
                //en caso de que salga 1 entonces el proceso cambia de estado a desbloqueado
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