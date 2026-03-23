package DiscoDuro;

public class Peticiones {
    private int sector;
    private char tipo;

    public Peticiones() {
        this.sector = (int)(Math.random()*20)+1;
        this.tipo = ((int)(Math.random()*2)+1 == 1)? 'L':'S';
    }

    public char getTipo() {
        return tipo;
    }

    public int getSector() {
        return sector;
    }
}
