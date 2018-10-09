package IA.Energia;

import java.util.Random;
import java.util.Vector;
import java.lang.Math;
import IA.Energia.Centrales;
import IA.Energia.Clientes;


public class ProbEnergiaBoard {
	private static Centrales cent; //Llista de centrals
	private static Clientes cli; //Llista de clients
	private static int nclients;
	private static int ncentrals;
	private int[] connexions;
	private double[] nivellProduccio;
	private Random r = new Random();
	
	
	//Constructor
	public ProbEnergiaBoard(int[] iniTipoCent, int nclients, double[] iniTipoClient){
		cent = new Centrales(iniTipoCent,r.nextInt());
		cli = new Clientes(r.nextInt(nclients),iniTipoClient,0.75,r.nextInt());
		this.nclients = nclients;
		ncentrals = cent.size();
		connexions = new int[nclients];
		nivellProduccio = new double[ncentrals];
		for (int i = 0; i < ncentrals; i++) nivellProduccio[i] = 0;	
	}
	
	public void generarEstatInicial(int tipus){
		switch(tipus){
			case 0:
			//Ajunta clients garantitzats amb la central que pugui assumir l'energia demanada
				for (int i = 0; i < nclients; i++){
					Client c = cli.get(i);
					if (c.getContrato() == 0){
						for (int j = 0; j < ncentrals; j++){
							Central cn = cent.get(j);
							double gasto = getConsumoReal(c,cn);
							if (gasto <=  cn.getProduccion() - nivellProduccio[j]){
								connexions[i] = j;
								nivellProduccio[j] += gasto;
							}							
						}
					}
				}
				break;
			
			case 1:
			//Ajunta clients garantitzats amb la central mes aprop que pugui assumir l'energia
				for (int i = 0; i < nclients; i++){
					Client c = cli.get(i);
					if (c.getContrato() == 0){
						double dist_par = 0;
						double dist_min = 141.42;//DistMax en el tauler
						int closer;
						for (int j = 0; j < ncentrals; j++){
							Central cn = cent.get(j);
							dist_par = distanciaEuclidiana(c.getCoordX(),c.getCoordY(), 
														  cn.getCoordX(), cn.getCoordY());
							double gasto = getConsumoReal(c,cn);
							if (dist_par < dist_min && gasto <=  cn.getProduccion() - nivellProduccio[j])		{
								connexions[i] = j;
								nivellProduccio[j] += gasto;
							}										
						}						
					}					
				}
				break;			
		}		
	}
	
	private double getConsumoReal(Client c, Central cn){
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
		dist=Math.sqrt(Math.pow(nc-x,2)+Math.pow(nc-y,2));
		return dist;	
	}
	
	//OPERADORS
	
	
	
	
	
	//ESTAT FINAL
	
	public boolean isGoal(){
		
	}		
}
