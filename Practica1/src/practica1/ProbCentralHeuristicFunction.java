package practica1;

import aima.search.framework.HeuristicFunction;

public class ProbCentralHeuristicFunction implements HeuristicFunction{
      public boolean equals(Object obj) {
      boolean retValue;
      
      retValue = super.equals(obj);
      return retValue;
  }
  
  public double getHeuristicValue(Object state) {
   ProbCentralBoard board=(ProbCentralBoard)state;
   double heur = 0;
   double consum_clients = board.getConsumClients(); 
   double consum_centrals= board.getConsumCentrals(); //serà el sumatori del cost de totes les centrals q tinguin algun client
   double compensacio_clients = board.getCompensacio(); 
   heur=(consum_clients - consum_centrals - compensacio_clients);
   return -1*(heur); 
  }
}
