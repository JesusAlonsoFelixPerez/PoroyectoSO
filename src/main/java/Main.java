import algoritmos.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        System.out.println("-----Elija el Algoritmo-----");
        System.out.println("1- Multiples Colas de Prioridad");
        System.out.println("2- Planificacion Garantizada");
        System.out.println("3- Cerrar");
        System.out.println("----------------------------");
        //int eleccion = sc.nextInt();
        int eleccion = 1;

        switch (eleccion) {
            case 1:
                MultiplesColasPrioridad.MCP();
                break;

            case 2:
                PlanificacionGarantizada.ejecutar();
                break;

            case 3:
                System.out.println("Adios");
                break;
        }
    }
}