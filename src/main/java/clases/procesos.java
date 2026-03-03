package clases;

public class procesos {
    private char id;
    private int tiempoRestante;
    private int estado;
    private int VecesUsado;
    private int intentoDesbloquear;

    public procesos(char id) {
        this.id = id;
        this.tiempoRestante = (int) (Math.random() * 8) + 3;
        this.estado = (int)(Math.random() * 2);
        this.VecesUsado = 1;
        this.intentoDesbloquear = 0;
    }

    public char getId() {
        return id;
    }
    public void setId(char id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format("%-7s | %-10d | %-6s | %-5d | %-7d",
                id,
                tiempoRestante,
                estado,
                VecesUsado,
                intentoDesbloquear);
    }
}