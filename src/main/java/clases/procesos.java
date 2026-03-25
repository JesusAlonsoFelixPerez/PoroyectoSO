package clases;

import java.util.ArrayList;
import DiscoDuro.Peticiones;

public class procesos {

    private char id;
    private int tiempoRestante;
    private int estado;
    private int VecesUsado;
    private int intentoDesbloquear;
    private boolean esRepetido;
    private ArrayList<Peticiones> peticiones;
    private int cantidadPeticiones = (int) (Math.random() * 5) + 1;
    private int prioridad;


    public procesos(char id) {
        this.id = id;
        this.tiempoRestante = (int) (Math.random() * 8) + 3;
        this.estado = (int)(Math.random() * 2);
        this.VecesUsado = 1;
        this.intentoDesbloquear = 0;
        this.esRepetido = false;
        this.prioridad = (int) (Math.random() * 4)+1;
        this.peticiones = new ArrayList<>();

        while (this.peticiones.size() < cantidadPeticiones) {
            Peticiones nueva = new Peticiones();

            // verifica si ya esxite una peticion en el mismo sector
            boolean yaExiste = false;
            for (Peticiones p : this.peticiones) {
                if (p.getSector() == nueva.getSector()) {
                    yaExiste = true;
                    break;
                }
            }

            if (!yaExiste) {
                // si la peticion no se encuentra repetida se agrega a peticiones
                this.peticiones.add(nueva);
            }
        }
        // ordena las peticiones
        this.peticiones.sort((a, b) -> Integer.compare(a.getSector(), b.getSector()));
    }

    public char getId() {
        return id;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public int getIntentoDesbloquear() {
        return intentoDesbloquear;
    }

    public void setIntentoDesbloquear(int intentoDesbloquear) {
        this.intentoDesbloquear = intentoDesbloquear;
    }

    public int getVecesUsado() {
        return VecesUsado;
    }

    public void setVecesUsado(int vecesUsado) {
        VecesUsado = vecesUsado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getCantidadPeticiones() {
        return peticiones.size();
    }

    public void setPeticiones(ArrayList<Peticiones> peticiones) {
        this.peticiones = peticiones;
    }

    public String toStringPlanificacionGarantizada() {
        String encabezado = String.format("%-7s | %-10d | %-6d | ",
                id, tiempoRestante, estado);

        StringBuilder sb = new StringBuilder(encabezado);

        if (estado == 0) {
            for (int i = 0; i < cantidadPeticiones; i++) {
                sb.append(String.format("(%02d%c)", peticiones.get(i).getSector(), peticiones.get(i).getTipo()));
                if (i < cantidadPeticiones - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {

        String encabezado = String.format("%-7c | %-10d | %-6d | %-7d | %-12d | %-9d | ",
                id, tiempoRestante, estado, VecesUsado, intentoDesbloquear, prioridad);

        StringBuilder sb = new StringBuilder(encabezado);

        if (estado == 0) {
            for (int i = 0; i < cantidadPeticiones; i++) {
                sb.append(String.format("(%02d%c)", peticiones.get(i).getSector(), peticiones.get(i).getTipo()));
                if (i < cantidadPeticiones - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    public ArrayList<Peticiones> getPeticiones() {
        return peticiones;
    }
}