package practica1;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.List;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public class Practica1 {

       public static void main(String[] args) throws Exception{
        
        
        int[] cent1 = new int[]{5, 10, 25}; //vector de 3 posicions amb el número de centrals de cada tipus a generar
        int ncl = 1000; //número de clients
        double[] propc1 = new double[]{0.25, 0.3, 0.45}; //vector de 3 posicions amb les proporcions dels tipus de clients, sumen 1
        double propg1 = 0.75; //proporció de clients amb servei garantitzat
        int alg = 1; //si val 1 es farà hill climbing, si val 2 es farà simulated annealig
        int sol_inicial = 0; //si val 0 farem la primera solució inicial, si val 1 farem la segona

        ProbCentralBoard board = new ProbCentralBoard(cent1, ncl, propc1, propg1);
        board.solucioInicial(sol_inicial);
        Search search=null; 
        Problem p;
           p = new Problem(board, new ProbCentralSuccessorFunction(), new ProbCentralGoalTest(), new ProbCentralHeuristicFunction());
      
        if (alg==1) search =  new HillClimbingSearch();
        if (alg==2) search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
        // Instantiate the SearchAgent object
        SearchAgent agent = new SearchAgent(p, search);

	// We print the results of the search
        System.out.println();
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());


    }

        private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
        
    }
    
    private static void printActions(List actions) {
           for (Object action1 : actions) {
               String action = (String) action1;
               System.out.println(action);
           }
    }
    
}
