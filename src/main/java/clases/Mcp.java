package clases;

public class Mcp {
    private char id;
    private int tiempoLlegada;
    private int prioridad;
    private int tiempoRestante;
    private int estado;
    private int VecesUsado;
    private int intentoDesbloquear;

    public Mcp(char id, int tiempoLlegada) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.prioridad = 1;
        this.tiempoRestante = (int) (Math.random() * (10 - 3) + 1)+3;
        this.estado = (int)(Math.random() * 2);
        this.VecesUsado = 0;
        this.intentoDesbloquear = 0;
    }

    public char getId() {
        return id;
    }
    public void setId(char id) {
        this.id = id;
    }
    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
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
        return "Clases.Mcp{" +
                "id=" + id +
                ", tiempoLlegada=" + tiempoLlegada +
                ", prioridad=" + prioridad +
                ", tiempoRestante=" + tiempoRestante +
                ", estado=" + estado +
                ", VecesUsado=" + VecesUsado +
                ", intentoDesbloquear=" + intentoDesbloquear +
                '}';
    }
}
