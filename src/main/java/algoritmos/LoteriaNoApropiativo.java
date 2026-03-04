package algoritmos;
	import java.util.*;

	public class LoteriaNoApropiativo {
	    
	    static class Proceso {
	        int id;
	        String nombre;
	        int prioridad;
	        int boletos;
	        int tiempoRafaga;    
	        int tiempoRestante;
	        
	        public Proceso(int id, String nombre, int prioridad, int tiempoRafaga) {
	            this.id = id;
	            this.nombre = nombre;
	            this.prioridad = prioridad;
	            this.boletos = prioridad;
	            this.tiempoRafaga = tiempoRafaga;
	            this.tiempoRestante = tiempoRafaga;
	        }
	        
	        public String toString() {
	            return String.format("%s (ID:%d, Prioridad:%d, Boletos:%d, T.restante:%d)", 
	                               nombre, id, prioridad, boletos, tiempoRestante);
	        }
	    }
	    
	    private List<Proceso> procesos;
	    private int totalBoletos;
	    private Random random;
	    public LoteriaNoApropiativo() {
	        procesos = new ArrayList<>();
	        totalBoletos = 0;
	        random = new Random();
	        try (Scanner scanner = new Scanner(System.in)) {
				
			};
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
	    //Checar los apuntes de la clase
	    public void LoteriaApropiativo() {
	        System.out.println("Modo No apropiativo");
	        System.out.println("Los procesos se ejecutan hasta terminar");
	        
	        int ciclo = 1;
	        while (!procesos.isEmpty()) {
	            System.out.println("Ciclo " + ciclo);
	            
	            // Seleccionar proceso
	            Proceso actual = seleccionarGanador();
	            System.out.println("Proceso seleccionado: " + actual.nombre);
	            
	            // Ejecucion completa no apropiativo
	            System.out.println("   Ejecutando proceso completo...");
	            
	            // Simular ejecución completa
	            actual.tiempoRestante = 0;  // Termina en este ciclo
	            
	            System.out.println("Proceso terminado después de " + actual.tiempoRafaga + " unidades");
	            
	            // Remover proceso terminado
	            totalBoletos -= actual.boletos;
	            procesos.remove(actual);
	            
	            // Mostrar procesos restantes
	            System.out.println("Procesos restantes: " + procesos.size());
	            for (Proceso p : procesos) {
	                System.out.println("   " + p);
	            }
	            
	            ciclo++;
	            
	            try { Thread.sleep(1000); } catch (Exception e) {}
	        }
	        
	        System.out.println("Todos los procesos terminaron");
	    }
	    
	    public static void ejecutar() {
	        LoteriaNoApropiativo planificador = new LoteriaNoApropiativo();
	        
	        // No se me vuelvan a olvidar los procesos
	        planificador.agregarProceso(new Proceso(1, "Google", 5, 3));
	        planificador.agregarProceso(new Proceso(2, "Word", 2, 4));
	        planificador.agregarProceso(new Proceso(3, "Fortnite", 8, 2));
	        planificador.agregarProceso(new Proceso(4, "WhatsApp", 1, 5));
	        
	     
	        planificador.LoteriaApropiativo();
	    }
	}