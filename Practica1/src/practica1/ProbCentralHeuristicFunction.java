/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import aima.search.framework.HeuristicFunction;
/**
 *
 * @author jordi.donadeu
 */
public class ProbCentralHeuristicFunction implements HeuristicFunction{
      public boolean equals(Object obj) {
      boolean retValue;
      
      retValue = super.equals(obj);
      return retValue;
  }
  
  public double getHeuristicValue(Object state) {
   ProbCentralBoard board=(ProbCentralBoard)state;
   int heur = 0;
   /*int cob=board.calculaCobertura();
   int pot=board.potenciaEfectiva();
   if (pot>board.getMaxPotencia()) return(java.lang.Integer.MAX_VALUE);
   heur=((board.getDimPlano()*board.getDimPlano())-cob); */
   return(heur); 
  }
}
