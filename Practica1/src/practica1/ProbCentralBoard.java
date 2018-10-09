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
    //private int [][] board;
    private ArrayList assignacions; //Jordi: faria un vector 
     // de pairs [client,central] per representar l'assignació d'un client a una central.
     // Oriol: Podem implmentar això que has dit amb un ArrayList de miPair, i ja
     // tenim el vector de pairs que vols.
    
    
    public ProbCentralBoard (int[] cent, int ncl, double[] propc, double propg) throws Exception {
        Random rnd = new Random();
        int seed = rnd.nextInt();
        centrals = new Centrales(cent, seed);
        ncentrals = centrals.size();
        clients = new Clientes(ncl, propc, propg, seed);
        ProbCentralBoard.nclients = ncl;
        assignacions = new ArrayList<>(nclients);
    }

    public void solucioInicial(){ //és una solució inicial bastant dolenta, es pot millorar
        int j = 0;
        for (int i = 0; i < nclients & j < ncentrals; i++){
            double diferencia = (centrals.get(j).getProduccion() - clients.get(i).getConsumo());
            if (diferencia < 0) j++;
            else {
                assignaACentral(clients.get(i), centrals.get(j));
                centrals.get(j).setProduccion((centrals.get(j).getProduccion() - diferencia));
            }
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
    public void assignaACentral(Cliente cl, Central ce){
        miPair assignacio;
        assignacio = new miPair(cl, ce);
        assignacions.add(assignacio);
    }
}