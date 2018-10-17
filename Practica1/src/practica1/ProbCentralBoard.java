
package practica1;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import java.util.*;
import java.lang.*;

/**
 *
 * @author oriolvidal
 */
public class ProbCentralBoard {
    private final Centrales centrals;
    private static int ncentrals;
    private Clientes clients;
    private static int nclients;
    private int[] connexions; // index = client -> valor = index de la central o -1 si no te central
    private double[] propc;
    private double propg;
    private int[] cent;
    
    private miPair[] nivellProduccio;
    private Random r;
	
// Ordenem les centrals segons l'Energia "lliure" que encara poden suministrar.
    private void ordenaCentrals() {
        Arrays.sort(nivellProduccio, (miPair o1, miPair o2) -> {
            Double dif1;
            dif1 = (double) o1.getSecond();
            Double dif2 = (double) o2.getSecond();
            return dif1.compareTo(dif2);
        });
    }
    
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
    
        
    private boolean centralActiva(int i){//Retorna si la central i està activa o no
        Double aux = centrals.get(i).getProduccion();
        return aux.equals(nivellProduccio[i].getSecond());
    }
    
    private boolean centralPlena(int i){
		return (double)nivellProduccio[i].getSecond() == 0;
	}
	
    public ProbCentralBoard (int[] cent1, int ncl, double[] propc1, double propg1) throws Exception {
        this.r = new Random();
        int seed = r.nextInt();
        centrals = new Centrales(cent, seed);
        ncentrals = centrals.size();
        clients = new Clientes(ncl, propc, propg, seed);
        ProbCentralBoard.nclients = ncl;
        connexions = new int[nclients];
        nivellProduccio = new miPair[ncentrals];
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
        for (int i = 0; i < ncentrals; i++){
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
					if (gasto <=  ((double)nivellProduccio[j].getSecond() - cn.getProduccion())){
                                                connexions[i] = j;
                                                nivellProduccio[j].setSecond((double)nivellProduccio[j].getSecond() - gasto);
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
			    if ((dist_par < dist_min) && (gasto <=  (double)nivellProduccio[j].getSecond() - cn.getProduccion())) {
                                    connexions[i] = j;
                                    nivellProduccio[j].setSecond((double)nivellProduccio[j].getSecond() - gasto);
                            }										
                        }						
                    }					
                }
                break;			
	}
	    this.ordenaCentrals();
    }

  //OPERADORS
    
    //modificar el vector connexions pq a la posició del client cl hi hagi la posició de la central ce
    public void moureClient(Cliente cl, Central ce){
        boolean found = false;
        int i = 0;
        int j = 0;
        for (; i < nclients && !found; ++i){
            if (cl == clients.get(i)) found = true;
        }
        found = false;
        for (; j < ncentrals && !found; ++j){
            if (ce == centrals.get(j)) found = true;
        }    
        connexions[i] = j;
        //falta recalcular consum del client a la nova central        
        //falta recalcular capacitat central nova i a la central que tenia abans
        
    }
    
    /*public boolean moureCentral(int i, int x, int y){
        if (x > 100 | y > 100) return false;
         centrals.get(i).setCoordX(x);
         centrals.get(i).setCoordY(y);
         return true;
    }*/
    
    //Intenta repartir tots els clients de la central amb index c en altres centrals, si ho aconsegueix retorna cert, sinó fals
    public boolean buidarCentral(int c){
        for(int i = 0; i < nclients;i++){
            if (connexions[i] == c){
				boolean centralTrobada = false;
                for (int j = 0; j < ncentrals && !centralTrobada; j++){
                    int iReal = (int)nivellProduccio[j].getFirst();
                    if (c != iReal){
                        double gasto = getConsumoReal(clients.get(i),centrals.get(iReal));
                        double prodActual = (double) nivellProduccio[j].getSecond();
                        if (centralActiva(iReal) &&  prodActual - gasto >= 0){
							centralTrobada = true;
                            connexions[i] = iReal;
                            nivellProduccio[j].setSecond(prodActual-gasto);
                            boolean assignat = false;
                            for (int k = 0;k < ncentrals && !assignat;k++){/*Es feo pero es necessita aquest bucle per trobar la central c
																			*i sumar-li el gasto que recupera*/
                                if(c == (int)nivellProduccio[k].getFirst()){
									assignat = true;
                                    double aux = (double) nivellProduccio[k].getSecond();
                                    nivellProduccio[k].setSecond(aux+gasto);
                                }
                            }
                        }
                    }
                }
                if (!centralTrobada) return false;
            }
            
        }
        return true;
    }
    
    public void buidarCentrals(){//Intenta repartir els clients de la segona meitat de centrals, a la primera part
								 //Per experimentacio mirar quin percentatge és el mes eficient
        for (int j = ncentrals-1; j >= ncentrals/2; j--){
            int iReal = (int)nivellProduccio[j].getFirst();
            if (centralActiva(iReal) && !centralPlena(j) && migRendiment(j)){
                for (int i = 0; i < nclients;i++){
                    if (connexions[i] == iReal){
						boolean assignat = fals;
                        for (int k = 0; k < ncentrals/2 && !assignat; k++){
							if(centralActiva(k) && !centralPlena(k)){
								double gasto = getConsumoReal(clients.get(i),centrals.get(k));
								double prodActual = (double) nivellProduccio[k].getSecond();
								if (prodActual - gasto >= 0){
									assignat = true;
									connexions[i] = (int)nivellProduccio[k].getFirst();
									nivellProduccio[k].setSecond(prodActual-gasto);
									double auxProd = nivellProduccio[j].getSecond(); 
									nivellProduccio[j].setSecond(auxProd+gasto);
								}
							}
						}
                    }
                }                
            }
        }
    }  
    
    //CONSULTORES
    
    public int getNClients(){
        return nclients;
    }
    
    public int getNCentrals(){
        return ncentrals;
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
    
    public int getPreu(int tipus, int c){
        int preu;
        if (tipus == 0){
            if (c == 0) preu=40;
            else preu=30;
        }
        else if (tipus == 1){
            if (c == 0) preu=50;
            else preu=40;
        }
        else {
            if (c==0) preu=60;
            else preu=50;
        }
        return preu;
    }
    
    public double getConsum(int tipus, double produccio){
        double consum;
        if (tipus == 0) consum = 2000 + produccio*5;
        else if (tipus == 1) consum = 1000 + produccio*8;
        else consum = 500 + produccio*15;
        return consum;
    }
    
    public double getConsumClients(){ //serà el sumatori del consum*preuMW(segons prioritat) de tots els clients q estan a una central
        double consum = 0;
        for (int i=0; i < nclients; ++i){
            if (connexions[i] != -1){
                consum += clients.get(i).getConsumo() * getPreu(clients.get(i).getTipo(), clients.get(i).getContrato());
            }
        }
        return consum;
    }
    
    public double getConsumCentrals(){ //serà el sumatori del cost de totes les centrals q tinguin algun client
        double consum = 0;
        for (int i=0; i < ncentrals; ++i){
            if (centralActiva(i)) consum+= getConsum(centrals.get(i).getTipo(), centrals.get(i).getProduccion());
        }
        return consum;
    }
    
	public boolean isGoalState() {
		return(false);
    }

    /*public Centrales getCentralsDisponibles(double d){ //retorna un subset de les centrals que 
        Centrales c = null;
        for (int i = 0; i < ncentrals; i++){
            if (nivellProduccio[i] - d > 0) c.add(centrals.get(i));
        }
        return c;
    }*/ 
    
    /*public void assignaACentral(Cliente cl, Central ce){
        miPair assignacio;
        assignacio = new miPair(cl, ce);
        assignacions.add(assignacio);
    }*/
}
