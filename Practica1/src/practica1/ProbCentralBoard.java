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
        falta recalcular consum del client a la nova central        
        falta recalcular capacitat central nova i a la central que tenia abans
        
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
                for (int j = 0;j < ncentrals;j++){
                    int iReal = (int)nivellProduccio[j].getFirst();
                    if (c != iReal){
                        double gasto = getConsumoReal(clients.get(i),centrals.get(iReal));
                        double prodActual = (double) nivellProduccio[j].getSecond();
                        if (centralActiva(iReal) &&  prodActual - gasto >= 0){
                            connexions[i] = iReal;
                            nivellProduccio[j].setSecond(prodActual-gasto);
                            for (int k = 0;k < ncentrals;k++){//Es feo pero es necessita aquest bucle per trobar la central c
                                                              //i sumar-li el gasto que recupera
                                if(c == (int)nivellProduccio[k].getFirst()){
                                    double aux = (double) nivellProduccio[k].getSecond();
                                    nivellProduccio[k].setSecond(aux+gasto);
                                }
                            }
                        }
                        else return false;
                        
                    }
                }
            }
            
        }
        return true;
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
        for (int i=0; i < nclients; ++i){
            
        }
        return consum;
    }
    
    public boolean centralActiva(int i){//Retorna si la central i està activa o no
        Double aux = new Double(centrals.get(i).getProduccion());
        return aux.equals(nivellProduccio[i].getSecond());
    }
    
    /*public int getCentralActivMesProd(int c, double d){//Retorna la central activa amb mes prod que pugui assumir el consum d
        double prodMin = centrals.get(0).getProduccion(); //Inicialitzacio arbitraria
        int indexMin = -1;
        for (int i = 0; i < ncentrals; i++){
            if (i != c && centralActiva(i)){                
               if (nivellProduccio[i] - d > 0 && nivellProduccio[i] - d < prodMin){
                   indexMin = i;
                   prodMin = nivellProduccio[i] - d;
               }
            }        
        }
        return indexMin;
    }*/
    
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