package algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoteriaApropiativa {
    static class Proceso {
        int id;
        String nombre;
        int prioridad;
        int boletos;
        int tiempoRafaga;    
        int tiempoRestante;   
        int tiempoEjecutado;  
        
        public Proceso(int id, String nombre, int prioridad, int tiempoRafaga) {
            this.id = id;
            this.nombre = nombre;
            this.prioridad = prioridad;
            this.boletos = prioridad;
            this.tiempoRafaga = tiempoRafaga;
            this.tiempoRestante = tiempoRafaga;
            this.tiempoEjecutado = 0;
        }
        //Seguir Mañana ya me dio hueva
        public String toString() {
            return String.format("%s (ID:%d, Prioridad:%d, Boletos:%d, Progreso:%d/%d)", 
                               nombre, id, prioridad, boletos, tiempoEjecutado, tiempoRafaga);
        }
    }

    private List<Proceso> procesos;
    private int totalBoletos;
    private Random random;
    private int quantum;  // Quantum ejecución
    
    // Constructor corregido por 837 vez
    public LoteriaApropiativa() {
        this.procesos = new ArrayList<>();  
        this.totalBoletos = 0;
        this.random = new Random();
        this.quantum = 2;  
    }
    
    // Constructor con quantum personalizado
    public LoteriaApropiativa(int quantum) {
        this.procesos = new ArrayList<>();  
        this.totalBoletos = 0;
        this.random = new Random();
        this.quantum = quantum;
    }
    
    public void agregarProceso(Proceso p) {
        procesos.add(p);
        totalBoletos += p.boletos;
        System.out.println("Proceso agregado: " + p.nombre);
    }
    
    public Proceso seleccionarGanador() {
        if (procesos.isEmpty()) return null;
        
        int boletoGanador = random.nextInt(totalBoletos) + 1;
        System.out.println("Boleto ganador: " + boletoGanador + " de " + totalBoletos);
        
        int acumulado = 0;
        for (Proceso p : procesos) {
            acumulado += p.boletos;
            if (boletoGanador <= acumulado) {
                return p;
            }
        }
        return procesos.get(0);
    }
    
    public void LoteriaNoApropiativo() {
        System.out.println("Modo Apropiativo");
        System.out.println("Quantum por proceso: " + quantum + " unidades");
        System.out.println("Los procesos pueden ser interrumpidos");
        
        int ciclo = 1;
        int tiempoGlobal = 0;
        
        while (!procesos.isEmpty()) {
            System.out.println("Ciclo" + ciclo + " (Tiempo: " + tiempoGlobal + ") ━━━");
            
            // Seleccionar proceso por lotería
            Proceso actual = seleccionarGanador();
            System.out.println("Proceso seleccionado: " + actual.nombre);
            
            // Ejecucion Parcial apropiativo
            int tiempoEjecucion = Math.min(quantum, actual.tiempoRestante);
            System.out.println("Ejecutando por " + tiempoEjecucion + " unidades...");
            
            
            actual.tiempoEjecutado += tiempoEjecucion;
            actual.tiempoRestante -= tiempoEjecucion;
            tiempoGlobal += tiempoEjecucion;
            
            System.out.println("Progreso: " + actual.tiempoEjecutado + "/" + actual.tiempoRafaga);
            
            // Verificar si el proceso terminó
            if (actual.tiempoRestante <= 0) {
                System.out.println("Proceso Completado");
                totalBoletos -= actual.boletos;
                procesos.remove(actual);
            }
            
            // Mostrar estado pedir asesoria a la profe
            System.out.println("Procesos restantes: " + procesos.size());
            for (Proceso p : procesos) {
                System.out.println("   " + p);
            }
            
            ciclo++;
            
            try { Thread.sleep(500); } catch (Exception e) {}
        }
        
        System.out.println("Todos los procesos terminados");
        System.out.println("Tiempo total de ejecución: " + tiempoGlobal);
    }
    
    public static void ejecutar() {
        //Planificador (No se que falla seguir mañana no olvidar procesos)
        LoteriaApropiativa planificador = new LoteriaApropiativa(2);
        
        
        System.out.println("Creando procesos y ejecutor");
        planificador.agregarProceso(new Proceso(1, "Google", 5, 3));
        planificador.agregarProceso(new Proceso(2, "Word", 2, 4));
        planificador.agregarProceso(new Proceso(3, "Fortnite", 8, 2));
        planificador.agregarProceso(new Proceso(4, "WhatsApp", 1, 5));

        planificador.LoteriaNoApropiativo();
    }
}