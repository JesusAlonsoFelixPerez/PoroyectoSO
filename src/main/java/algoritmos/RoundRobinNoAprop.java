package algoritmos;

import java.util.*;

public class RoundRobinNoAprop {

    static Random r = new Random();

    static class Peticion {
        int sector;
        char tipo;

        Peticion(int sector, char tipo) {
            this.sector = sector;
            this.tipo = tipo;
        }

        public String toString() {
            return sector + "" + tipo;
        }
    }

    static class Disco {
        int cabeza = 0;
        boolean subiendo = true;

        ArrayList<Peticion> colaActual = new ArrayList<>();
        ArrayList<Peticion> colaNueva = new ArrayList<>();

        int totalGeneradas = 0;
        int tiempoTotal = 0;

        ArrayList<String> atendidas = new ArrayList<>();
    }

    public static void ejecutar() {

        System.out.println("===== ROUND ROBIN NO APROPIATIVO + N-STEP SCAN =====");

        int n = r.nextInt(5) + 3;

        int[] tiempoCPU = new int[n];
        String[] estado = new String[n];
        Disco[] discos = new Disco[n];

        System.out.println("\n===== PROCESOS =====");

        for (int i = 0; i < n; i++) {
            tiempoCPU[i] = r.nextInt(8) + 3;
            estado[i] = (r.nextInt(2) == 0) ? "Listo" : "Bloqueado";

            discos[i] = new Disco();

            if (estado[i].equals("Bloqueado")) {
                int num = r.nextInt(5) + 1;
                for (int j = 0; j < num; j++) {
                    agregarPeticionInicial(discos[i]);
                }
            }

            System.out.println("P" + (i + 1)
                    + " | CPU: " + tiempoCPU[i]
                    + " | Estado: " + estado[i]
                    + " | Peticiones: " + discos[i].colaActual);
        }

        int indice = 0;

        while (!todosTerminados(estado)) {

            if (estado[indice].equals("Terminado")) {
                indice = (indice + 1) % n;
                continue;
            }

            if (estado[indice].equals("Bloqueado")) {
                System.out.println("\n--- P" + (indice + 1) + " entra al SES-HHDD ---");

                while (!discoVacio(discos[indice])) {
                    atenderPeticion(discos[indice]);
                }

                estado[indice] = "Listo";
                System.out.println("P" + (indice + 1) + " desbloqueado");
            }

            if (estado[indice].equals("Listo")) {
                System.out.println("\n>>> P" + (indice + 1) + " ejecutando en CPU...");

                int t = tiempoCPU[indice];
                tiempoCPU[indice] = 0;
                estado[indice] = "Terminado";

                System.out.println("P" + (indice + 1) + " terminó (CPU: " + t + ")");
                System.out.println("Atendidas: " + discos[indice].atendidas);
                System.out.println("Tiempo total disco: " + discos[indice].tiempoTotal);
            }

            indice = (indice + 1) % n;
        }

        System.out.println("\n===== REPORTE FINAL =====");

        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1)
                    + " | Estado: " + estado[i]
                    + " | Tiempo disco: " + discos[i].tiempoTotal
                    + " | Atendidas: " + discos[i].atendidas.size());
        }

        System.out.println("\n===== FIN =====");
    }

    static boolean todosTerminados(String[] estado) {
        for (String e : estado) {
            if (!e.equals("Terminado")) return false;
        }
        return true;
    }

    static boolean discoVacio(Disco d) {
        return d.colaActual.isEmpty() && d.colaNueva.isEmpty();
    }

    static void atenderPeticion(Disco d) {

        if (d.colaActual.isEmpty()) {

            if (!hayEnMismoSentido(d, d.colaNueva)) {
                d.subiendo = !d.subiendo;
                System.out.println("Cambio de sentido -> " + (d.subiendo ? "SUBIENDO" : "BAJANDO"));
            }

            d.colaActual.addAll(d.colaNueva);
            d.colaNueva.clear();
        }

        Peticion p = buscarSiguiente(d);

        if (p == null) {
            d.subiendo = !d.subiendo;
            System.out.println("Cambio de sentido -> " + (d.subiendo ? "SUBIENDO" : "BAJANDO"));
            p = buscarSiguiente(d);
        }

        if (p == null) return;

        d.colaActual.remove(p);

        int rotacional = Math.abs(d.cabeza - p.sector);
        int transferencia = (p.tipo == 'L') ? 1 : 2;
        int total = rotacional + transferencia;

        d.tiempoTotal += total;
        d.atendidas.add(p + "(t=" + total + ")");

        System.out.println("Atendiendo: " + p
                + " | Cabeza: " + d.cabeza + " -> " + p.sector
                + " | Sentido: " + (d.subiendo ? "SUBIENDO" : "BAJANDO")
                + " | Rotacional: " + rotacional
                + " | Transferencia: " + transferencia
                + " | Total: " + total);

        d.cabeza = p.sector;

        generarNuevasPeticiones(d);
    }

    static Peticion buscarSiguiente(Disco d) {

        Peticion mejor = null;

        if (d.subiendo) {
            int menor = 999;

            for (Peticion p : d.colaActual) {
                if (p.sector >= d.cabeza && p.sector < menor) {
                    menor = p.sector;
                    mejor = p;
                }
            }

        } else {
            int mayor = -1;

            for (Peticion p : d.colaActual) {
                if (p.sector <= d.cabeza && p.sector > mayor) {
                    mayor = p.sector;
                    mejor = p;
                }
            }
        }

        return mejor;
    }

    static boolean hayEnMismoSentido(Disco d, ArrayList<Peticion> cola) {
        for (Peticion p : cola) {
            if (d.subiendo && p.sector >= d.cabeza) return true;
            if (!d.subiendo && p.sector <= d.cabeza) return true;
        }
        return false;
    }

    static void generarNuevasPeticiones(Disco d) {

        if (d.totalGeneradas >= 10) return;

        if (r.nextBoolean()) {
            int nuevas = r.nextInt(3) + 1;

            int disponibles = 10 - d.totalGeneradas;
            if (nuevas > disponibles) nuevas = disponibles;

            for (int i = 0; i < nuevas; i++) {
                Peticion nueva = generarUnica(d);
                d.colaNueva.add(nueva);
                d.totalGeneradas++;
            }

            if (nuevas > 0) {
                System.out.println("Nuevas peticiones: " + d.colaNueva);
            }
        }
    }

    static void agregarPeticionInicial(Disco d) {
        if (d.totalGeneradas >= 10) return;

        Peticion p = generarUnica(d);
        d.colaActual.add(p);
        d.totalGeneradas++;
    }

    static Peticion generarUnica(Disco d) {
        while (true) {
            int sector = r.nextInt(20) + 1;
            char tipo = r.nextBoolean() ? 'L' : 'E';

            boolean existe = false;

            for (Peticion p : d.colaActual) {
                if (p.sector == sector) existe = true;
            }

            for (Peticion p : d.colaNueva) {
                if (p.sector == sector) existe = true;
            }

            if (!existe) return new Peticion(sector, tipo);
        }
    }
}