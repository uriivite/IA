/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    /*private ArrayList assignacions; //Jordi: faria un vector 
     // de pairs [client,central] per representar l'assignació d'un client a una central.
     // Oriol: Podem implmentar això que has dit amb un ArrayList de miPair, i ja
     // tenim el vector de pairs que vols.*/
    
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
        Random r = new Random();
        int seed = r.nextInt();
        centrals = new Centrales(cent, seed);
        ncentrals = centrals.size();
        clients = new Clientes(ncl, propc, propg, seed);
        ProbCentralBoard.nclients = ncl;
        connexions = new int[nclients];
        nivellProduccio = new double[ncentrals];
        // Cal inicialitar el vector de produccio amb els valors maxims de produccio
        // de cada central.
        for (int i = 0; i < ncentrals; i++) nivellProduccio[i] = centrals.get(i).getProduccion();
    }

    // Opció: Ordenar de forma decreixent les centrals segons producció i els clients 
    //  segons la demanda que necessiten.
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
    //La solució inicial es basa en seguint l'ordre en què ens donen les centrals i els clients, anar posant clients a la primera central fins que s'ompli la seva capacitat. Un cop s'omple la primera central
    //seguir amb la seguent, i així fins que s'hagin assignat tots els clients

    // Oriol: Si volguessim millorar la solucio inicial, a algorismia ens han ensenyat una
    // cosa bastant pepina que es logarítmica i ja estaria una mica més optimitzat.
    // no sé si és O(n log(n)) o O(log n) però potser despres ja estem una mica més
    // a prop de la solucio final.

   /* public int [][] getBoard(){
         return(board);
    }*/

    public boolean moureClient(int i, int x, int y){
        if (x > 100 | y > 100) return false;
         clients.get(i).setCoordX(x);
         clients.get(i).setCoordY(y);
         return true;
    }

    public boolean moureCentral(int i, int x, int y){
        if (x > 100 | y > 100) return false;
         centrals.get(i).setCoordX(x);
         centrals.get(i).setCoordY(y);
         return true;
    }
    
    /*public void assignaACentral(Cliente cl, Central ce){
        miPair assignacio;
        assignacio = new miPair(cl, ce);
        assignacions.add(assignacio);
    }*/
}