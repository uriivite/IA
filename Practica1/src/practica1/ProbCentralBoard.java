
package practica1;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import java.util.*;

/**
 *
 * @author oriolvidal
 */
public class ProbCentralBoard {
    private final Centrales centrals;
    private static int ncentrals;
    private Clientes clients;
    private static int nclients;
    private int[] connexions; // index = client -> valor = index de la central
    
    private double[] nivellProduccio;
    private Random r;
    
    private double getConsumoReal(Cliente c, Central cn){
        double dist = distanciaEuclidiana(c.getCoordX(),c.getCoordY(), 
                cn.getCoordX(), cn.getCoordY());
        double boost = 1;
        if (dist > 75) boost += 0.6;
        else if (dist > 50) boost += 0.4 ;
        else if (dist > 25) boost += 0.2;
        else if (dist > 10) boost += 0.1;
        return c.getConsumo()*boost;		
    }
	
    private double distanciaEuclidiana(int x1, int y1, int x2, int y2){
            double dist;
            dist=Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
            return dist;	
    }
    
    public ProbCentralBoard (int[] cent, int ncl, double[] propc, double propg) throws Exception {
        this.r = new Random();
        int seed = r.nextInt();
        centrals = new Centrales(cent, seed);
        ncentrals = centrals.size();
        clients = new Clientes(ncl, propc, propg, seed);
        ProbCentralBoard.nclients = ncl;
        connexions = new int[nclients];
        nivellProduccio = new double[ncentrals];
        
        // Ordenem els clients de major a menor consum
        Collections.sort(clients, (Cliente c1, Cliente c2) -> {
            Double consum1 = c1.getConsumo();
            Double consum2 = c2.getConsumo();
            return consum2.compareTo(consum1);
        });
        
        // Ordenem les centrals de major a menor produccio
        Collections.sort(centrals, (Central c1, Central c2) -> {
            Double prod1 = c1.getProduccion();
            Double prod2 = c2.getProduccion();
            return prod2.compareTo(prod1);
        });
        
        // Inicialitacio del vector de produccio amb els valors maxims de produccio
        // de cada central, on i = index de la central dins de "centrals" i
        // nivellProduccio[i] = produccio de la central i
        for (int i = 0; i < ncentrals; i++) nivellProduccio[i] = centrals.get(i).getProduccion();
    }

    public void solucioInicial(int tipus) {
        switch(tipus) {
            case 0:
            //Ajunta clients garantitzats amb la central que pugui assumir l'energia demanada
                for (int i = 0; i < nclients; i++){
                        Cliente c = clients.get(i);
                        if (c.getContrato() == 0){
                                for (int j = 0; j < ncentrals; j++){
                                        Central cn = centrals.get(j);
                                        double gasto = getConsumoReal(c,cn);
                                        if (gasto <=  nivellProduccio[j] - cn.getProduccion()){
                                                connexions[i] = j;
                                                nivellProduccio[j] -= gasto;
                                        }							
                                }
                        }
                }
                break;
            case 1:
            //Ajunta clients garantitzats amb la central mes aprop que pugui assumir l'energia
                for (int i = 0; i < nclients; i++){
                    Cliente c = clients.get(i);
                    if (c.getContrato() == 0){
                        double dist_par = 0;
                        double dist_min = 141.42;//DistMax en el tauler
                        int closer;
                        for (int j = 0; j < ncentrals; j++){
                            Central cn = centrals.get(j);
                            dist_par = distanciaEuclidiana(c.getCoordX(),c.getCoordY(), 
                                    cn.getCoordX(), cn.getCoordY());
                            double gasto = getConsumoReal(c,cn);
                            if (dist_par < dist_min && gasto <=  nivellProduccio[j] - cn.getProduccion())		{
                                    connexions[i] = j;
                                    nivellProduccio[j] -= gasto;
                            }										
                        }						
                    }					
                }
                break;			
	}		
    }

  //OPERADORS
    
    //modificar el vector connexions pq a la posició del client cl hi hagi la posició de la central ce
    public void moureClient(Cliente cl, Central ce){
        int i = clients.find(cl);
        int j = centrals.find(ce);
        connexions[i] = j;
    }

    public boolean moureCentral(int i, int x, int y){
        if (x > 100 | y > 100) return false;
         centrals.get(i).setCoordX(x);
         centrals.get(i).setCoordY(y);
         return true;
    }
    
    //CONSULTORES
    
    public int getNClients(){
        return nclients;
    }
    
    public Centrales getCentrals(){
        return centrals;
    }
    
    public Clientes getClients(){
        return clients;
    }
    
    public Centrales getCentralsDisponibles(double d){ //retorna un subset de les centrals que 
        Centrales c = null;
        for (int i = 0; i < ncentrals; i++){
            if (nivellProduccio[i] - d > 0) c.add(centrals.get(i));
        }
        return c;
    }
    
    
    
    /*public void assignaACentral(Cliente cl, Central ce){
        miPair assignacio;
        assignacio = new miPair(cl, ce);
        assignacions.add(assignacio);
    }*/
}
