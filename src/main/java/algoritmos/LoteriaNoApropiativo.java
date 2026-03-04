package algoritmos;
	import java.util.*;

	public class LoteriaNoApropiativo {
	    
	    static class Proceso {
	        int id;
	        String nombre;
	        int prioridad;
	        int boletos;
	        int tiempoRafaga;    // Tiempo de ráfaga (tiempo que necesita ejecutarse)
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
	        System.out.println("✓ Proceso agregado: " + p.nombre);
	    }
	    
	    public Proceso seleccionarGanador() {
	        if (procesos.isEmpty()) return null;
	        
	        int boletoGanador = random.nextInt(totalBoletos) + 1;
	        System.out.println("\n🎲 Boleto ganador: " + boletoGanador + " de " + totalBoletos);
	        
	        int acumulado = 0;
	        for (Proceso p : procesos) {
	            acumulado += p.boletos;
	            if (boletoGanador <= acumulado) {
	                return p;
	            }
	        }
	        return procesos.get(0);
	    }
	    
	    public void LoteriaApropiativo() {
	        System.out.println("\n=== MODO NO APROPIATIVO ===");
	        System.out.println("Los procesos se ejecutan hasta terminar\n");
	        
	        int ciclo = 1;
	        while (!procesos.isEmpty()) {
	            System.out.println("━━━ CICLO " + ciclo + " ━━━");
	            
	            // 1. Seleccionar proceso
	            Proceso actual = seleccionarGanador();
	            System.out.println("▶ Proceso seleccionado: " + actual.nombre);
	            
	            // 2. EJECUCIÓN COMPLETA (NO APROPIATIVO)
	            System.out.println("   Ejecutando proceso COMPLETO...");
	            
	            // Simular ejecución completa
	            actual.tiempoRestante = 0;  // Termina en este ciclo
	            
	            System.out.println("   ✓ Proceso terminado después de " + actual.tiempoRafaga + " unidades");
	            
	            // 3. Remover proceso terminado
	            totalBoletos -= actual.boletos;
	            procesos.remove(actual);
	            
	            // Mostrar procesos restantes
	            System.out.println("\nProcesos restantes: " + procesos.size());
	            for (Proceso p : procesos) {
	                System.out.println("   " + p);
	            }
	            
	            ciclo++;
	            
	            try { Thread.sleep(1000); } catch (Exception e) {}
	        }
	        
	        System.out.println("\n✨ TODOS LOS PROCESOS TERMINARON");
	    }
	    
	    public static void ejecutar() {
	        LoteriaNoApropiativo planificador = new LoteriaNoApropiativo();
	        
	        // Crear procesos
	        planificador.agregarProceso(new Proceso(1, "Google", 5, 3));
	        planificador.agregarProceso(new Proceso(2, "Word", 2, 4));
	        planificador.agregarProceso(new Proceso(3, "Fortnite", 8, 2));
	        planificador.agregarProceso(new Proceso(4, "WhatsApp", 1, 5));
	        
	        // Ejecutar
	        planificador.LoteriaApropiativo();
	    }
	}