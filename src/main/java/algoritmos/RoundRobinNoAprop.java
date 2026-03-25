package algoritmos;

import java.util.*;

public class RoundRobinNoAprop {

    static Random r = new Random();

    static class Peticion {
        int sector; //número de sector del disco
        char tipo; // para saber si es L o E
    //  crear una petición.
        Peticion(int sector, char tipo) {
            this.sector = sector;
            this.tipo = tipo;
        }

        public String toString() {
            return sector + "" + tipo; // para que la peticion salga numero y la peticion
        }
    }

    static class Disco {
        int cabeza = 0;
        List<Peticion> colaActual = new ArrayList<>();//peticiones en ejecucion
        List<Peticion> colaNueva = new ArrayList<>();//nuevas peticiones 
        int totalPeticiones = 0;
        int tiempoTotal = 0;
        List<String> atendidas = new ArrayList<>();
    }

    public static void ejecutar() {

        System.out.println("===== ROUND ROBIN NO APROPIATIVO + N-SCAN =====");

        int n = r.nextInt(5) + 3;

        int[] tiempo = new int[n];
        String[] estado = new String[n];
        Disco[] discos = new Disco[n];

        for (int i = 0; i < n; i++) { //crea los procesos 

            tiempo[i] = r.nextInt(8) + 3;
            estado[i] = (r.nextInt(2) == 0) ? "Listo" : "Bloqueado";

            discos[i] = new Disco();

            if (estado[i].equals("Bloqueado")) {// se genera la peticion si esta bloqueado 
                int num = r.nextInt(5) + 1;
                for (int j = 0; j < num; j++) {
                    agregarPeticion(discos[i]);
                }
            }

            System.out.println("P" + (i + 1) +
                    " | Tiempo: " + tiempo[i] +
                    " | Estado: " + estado[i] +
                    " | Peticiones: " + discos[i].colaActual);
        }

        int indice = 0;

        while (true) {

            boolean fin = true;
            for (int i = 0; i < n; i++) {
                if (!estado[i].equals("Terminado")) {
                    fin = false;
                    break;
                }
            }
            if (fin) break;

            if (estado[indice].equals("Terminado")) {
                indice = (indice + 1) % n;
                continue;
            }

            // SI ESTÁ BLOQUEADO → SE QUEDA AQUÍ HASTA DESBLOQUEARSE
            if (estado[indice].equals("Bloqueado")) {

                System.out.println("\nP" + (indice + 1) + " en SES-HHDD...");

                while (true) {

                    boolean vacio = atenderUnaPeticion(discos[indice]);

                    if (vacio) {
                        estado[indice] = "Listo";
                        System.out.println("P" + (indice + 1) + " desbloqueado");
                        break;
                    }
                }
            }

            //  EJECUTA COMPLETO 
            if (estado[indice].equals("Listo")) {

                System.out.println("\nP" + (indice + 1) + " ejecutando COMPLETO...");

                int tiempoEjec = tiempo[indice];
                tiempo[indice] = 0;
                estado[indice] = "Terminado";

                System.out.println("P" + (indice + 1) +
                        " terminó (CPU: " + tiempoEjec + ")");

                //  REPORTE DEL DISCO
                System.out.println("Peticiones atendidas: " + discos[indice].atendidas);
                System.out.println("Tiempo total en disco: " + discos[indice].tiempoTotal);
            }

            indice = (indice + 1) % n;
        }

        System.out.println("\n===== FIN =====");
    }

    // 🔧 Atiende UNA petición
    static boolean atenderUnaPeticion(Disco d) {

        if (d.colaActual.isEmpty()) {
            d.colaActual.addAll(d.colaNueva);
            d.colaNueva.clear();
        }

        if (d.colaActual.isEmpty()) return true;

        d.colaActual.sort(Comparator.comparingInt(p -> p.sector));// ordena las peticiones de menor a mayor 

        Peticion p = d.colaActual.remove(0);

        int distancia = Math.abs(d.cabeza - p.sector);
        int rotacional = distancia;
        int transferencia = (p.tipo == 'L') ? 1 : 2;
        int total = rotacional + transferencia;

        d.tiempoTotal += total;

        String registro = p + "(t=" + total + ")";
        d.atendidas.add(registro);

        System.out.println("Atendiendo: " + p +
                " | Cabeza: " + d.cabeza +
                " -> " + p.sector +
                " | Tiempo: " + total);

        d.cabeza = p.sector;

        // nuevas peticiones
        if (r.nextBoolean()) {// seecalcula si se crea o no nuevas peticiones

            int nuevas = r.nextInt(3) + 1;

            for (int i = 0; i < nuevas; i++) {
                if (d.totalPeticiones < 10) {
                    Peticion nueva = generarUnica(d);
                    d.colaNueva.add(nueva);
                    d.totalPeticiones++;
                }
            }

            System.out.println("Nuevas: " + d.colaNueva);
        }

        return false;
    }

    static void agregarPeticion(Disco d) {// peticion inicial 
        Peticion p = new Peticion(r.nextInt(20) + 1,
                r.nextBoolean() ? 'L' : 'E');

        d.colaActual.add(p);
        d.totalPeticiones++;
    }

    static Peticion generarUnica(Disco d) {

        while (true) {
            int sector = r.nextInt(20) + 1;
            char tipo = r.nextBoolean() ? 'L' : 'E';

            boolean existe = false;

            for (Peticion p : d.colaActual)
                if (p.sector == sector) existe = true;

            for (Peticion p : d.colaNueva)
                if (p.sector == sector) existe = true;

            if (!existe)
                return new Peticion(sector, tipo);
        }
    }
}