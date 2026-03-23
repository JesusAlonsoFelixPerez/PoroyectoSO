import algoritmos.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int eleccion;

        Scanner sc = new Scanner(System.in);
        System.out.println("-----Elija el Algoritmo-----");
        System.out.println("1- Round Robin ");
        System.out.println("2- Prioridades ");
        System.out.println("3- Multiples Colas de Prioridad ");
        System.out.println("4- Proceso mas corto primero (SJF)");
        System.out.println("5- Planificacion Garantizada");
        System.out.println("6- Boleto de loteria");
        System.out.println("7- Planificacion Equitativa");
        System.out.println("8- Cerrar");
        System.out.println("----------------------------");
        eleccion = sc.nextInt();


        switch (eleccion) {
            case 1:
                eleccion = 0;
                System.out.println("===Round Robin===");
                System.out.println("1- Apropiativo");
                System.out.println("2- No Apropiativo");
                eleccion = sc.nextInt();

                if(eleccion == 1){
                    RoundRobinAprop.ejecutar();
                } else{
                    RoundRobinNoAprop.ejecutar();
                }
                break;

            case 2:
                eleccion = 0;
                System.out.println("===Prioridades===");
                System.out.println("1- Apropiativo");
                System.out.println("2- No Apropiativo");
                eleccion = sc.nextInt();

                if(eleccion == 1){
                    PrioridadesApropiativo.ejecutar();
                } else{
                    PrioridadesNoApropiativo.ejecutar();
                }
                break;

            case 3:
                MultiplesColasPrioridad.MCP();
                break;

            case 4:
                MasCorto.ejecutar();
                break;

            case 5:
                PlanificacionGarantizada.ejecutar();
                break;

            case 6:
                eleccion = 0;
                System.out.println("===Boletos de Loteria===");
                System.out.println("1- Apropiativo");
                System.out.println("2- No Apropiativo");
                eleccion = sc.nextInt();

                if(eleccion == 1){
                    LoteriaApropiativa.ejecutar();
                } else{
                    LoteriaNoApropiativo.ejecutar();
                }
                break;

            case 7:
                PlanificacionEquitativa.ejecutar();
                break;

            case 8:
                System.out.println("Adios");
                break;

            default:
                System.out.println("Eleccion no valido");
                System.out.println("Adios");
                break;

        }
    }
}