package utilidades;

import java.util.ArrayList;
import clases.*;
public class ProcesoBloqueado {
    public static boolean estaBloqueado(Mcp p) {
        if (p.getEstado() == 1) {
            return false;
        } else{
            //genera un numero random de 1 o 0
            int intentoDesbloquear = (int) (Math.random() * 2);

            if (intentoDesbloquear == 0) {
                //fallo al debloquear
                p.setIntentoDesbloquear(p.getIntentoDesbloquear() + 1);
                System.out.println("El proceso " + p.getId() + " Sigue bloqueado.");
                System.out.println("Intentos fallidos: " + p.getIntentoDesbloquear());

                if (p.getIntentoDesbloquear() >= 3) {
                    System.out.println("Muerte del proceso " + p.getId() + " Por inanicion");
                    System.exit(0);
                }
                return true;
            } else {
                //logro desbloquearse
                System.out.println("El proceso " + p.getId() + " logro desbloquearse");
                p.setEstado(1);
                p.setIntentoDesbloquear(0);
                return false;
            }
        }
    }
}
