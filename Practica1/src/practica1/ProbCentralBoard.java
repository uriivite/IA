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
    private double[] propc;
    private double propg;
    private int[] cent;
    
    private miPair[] nivellProduccio; // Controla la diferència entre la
										// capacitat que te una 
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
    
    // Ordena les centrals segons la diferència del total de producció
    // que poden donar i l'energia que estan donant. L'identificador de
    // la central és l'identificació que sel's hi ha assignat al començament
    // del programa. Ordenacio de menor a major diferència.
    private void ordenaCentrals(miPair[] produccions) {
			
	}
	
    private double distanciaEuclidiana(int x1, int y1, int x2, int y2){
            double dist;
            dist=Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
            return dist;	
    }
    
    public ProbCentralBoard (int[] cent1, int ncl, double[] propc1, double propg1) throws Exception {
        this.r = new Random();
        int seed = r.nextInt();
        centrals = new Centrales(cent, seed);
        ncentrals = centrals.size();
        clients = new Clientes(ncl, propc, propg, seed);
        ProbCentralBoard.nclients = ncl;
        connexions = new int[nclients];
        nivellProduccio = new MiPair[ncentrals];
        propc = propc1;
        propg = propg1;
	cent = cent1;
        
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
        for (int i = 0; i < ncentrals; i++) {
				nivellProduccio[i].setFirst(i);
				nivellProduccio[i].setSecond(centrals.get(i).getProduccion());
			}
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
    
    /*public boolean moureCentral(int i, int x, int y){
        if (x > 100 | y > 100) return false;
         centrals.get(i).setCoordX(x);
         centrals.get(i).setCoordY(y);
         return true;
    }*/
    
    //Intenta repartir tots els clients de la central amb index j en altres centrals, si ho aconsegueix retorna cert, sinó fals
    public boolean buidarCentral(int c){
        //Criteri de assignacio
            //Primera opcio: nivellProduccio[] - getConsumoReal == 0)
            //Segona opcio: Central activa més buida de totes
        boolean error = false;
        for(int i = 0; i < nclients;i++){
            if (connexions[i] == c){
                for (int j = 0;j < ncentrals;j++){
                    if (j == c) j++;//Saltar-se la central que volem buidar(arreglar perque suposo que pot petar)
                    double gasto = getConsumoReal(clients.get(i),centrals.get(j));
                    if(nivellProduccio[j] - gasto == 0){
                        connexions[i] = j;
                        nivellProduccio[j] -= gasto;
                        nivellProduccio[c] += gasto;
                    }
                    else{
                        int cOptima;
                        cOptima = getCentralActivMenysProd(c,gasto);
                        if (cOptima != -1){
                            connexions[i] = cOptima;
                            nivellProduccio[cOptima]-= gasto;
                            nivellProduccio[c] += gasto;
                        }
                        else error = true;
                    }
                }
            }
            
        }
        return !error;
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
    
    public double[] getPropc(){
        return propc;
    }
    
    public double getPropg(){
        return propg;
    }
	
    public int[] getCent(){
        return cent;
    }	
    
    public boolean centralActiva(int i){//Retorna si la central i està activa o no
        return nivellProduccio[i]!= centrals.get(i).getProduccion();
    }
    
    public int getCentralActivMenysProd(int c, double d){//Retorna la central activa mes lliure que pugui assumir el consum d
        double prodMin = 0; //Inicialitzacio arbitraria
        int indexMin = -1;
        for (int i = 0; i < ncentrals; i++){
            if (c == i) i++;//Si c és l'ultim element pot petar??
            if (centralActiva(i)){                
               if (nivellProduccio[i] - d > 0 && nivellProduccio[i] > prodMin){
                   indexMin = i;
                   prodMin = nivellProduccio[i];
               }
            }        
        }
        return indexMin;
    }
    
    public Centrales getCentralsDisponibles(double d){ //retorna un subset de les centrals que 
        Centrales c = null;
        for (int i = 0; i < ncentrals; i++){
            if (nivellProduccio[i] - d > 0) c.add(centrals.get(i));
        }
        return c;
    }
    
    public boolean isGoalState() {
		return(false);
		}
    
    
    
    /*public void assignaACentral(Cliente cl, Central ce){
        miPair assignacio;
        assignacio = new miPair(cl, ce);
        assignacions.add(assignacio);
    }*/
}
