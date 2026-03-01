package algoritmos;

import clases.Mcp;

import java.util.ArrayList;
import clases.*;

public class MultiplesColasPrioridad {

    public static void MCP (){
        System.out.println("\t=====Algoritmo Multiples Colas de Prioridad=====");


        ArrayList<Mcp> Procesos = new ArrayList<>();
        //Generar un numero de procesos randoms entre 1 a 10 para usar en el for
        int NumProcesosRandom = (int)(Math.random() * 10) + 1;

        for (int i = 0; i < NumProcesosRandom; i++) {
            Procesos.add(new Mcp((char)(i + 65),i));
            System.out.println(Procesos.get(i).toString());
        }
    }






}
