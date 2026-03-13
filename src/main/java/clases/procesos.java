package clases;

public class procesos {
    private char id;
    private int tiempoRestante;
    private int estado;
    private int VecesUsado;
    private int intentoDesbloquear;
    private boolean esRepetido;

    public procesos(char id) {
        this.id = id;
        this.tiempoRestante = (int) (Math.random() * 8) + 3;
        this.estado = (int)(Math.random() * 2);
        this.VecesUsado = 1;
        this.intentoDesbloquear = 0;
        this.esRepetido = false;
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

    public boolean getEsRepetido() {return esRepetido;}

    public void setEsRepetido(boolean esRepetido) {this.esRepetido = esRepetido;}

    @Override
    public String toString() {
        return String.format("%-7s | %-10d | %-6s | %-7d | %-13d",
                id,
                tiempoRestante,
                estado,
                VecesUsado,
                intentoDesbloquear);
    }
}