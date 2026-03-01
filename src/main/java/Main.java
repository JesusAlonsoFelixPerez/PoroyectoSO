import clases.Mcp;

import algoritmos.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----Elija el Algoritmo-----");
        System.out.println("1- Multiples Colas de Prioridad");
        System.out.println("2- Cerrar");
        System.out.println("----------------------------");
        int eleccion = sc.nextInt();

        switch (eleccion) {
            case 1:
                MultiplesColasPrioridad.MCP();
                break;

            case 2:
                System.out.println("Adios");
        }
    }
}