package practica1;

import IA.Energia.Centrales;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jordi.donadeu
 */
public class ProbCentralSuccessorFunction implements SuccessorFunction{
    @Override
    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        ProbCentralBoard board=(ProbCentralBoard) aState;
        for(int i=0;i<board.getNClients(); i++){
            // FEM UN BOARD PER CADA OPERADOR QUE TINGUEM
            double consum = board.getClients().get(i).getConsumo();
            ProbCentralBoard newBoardMC = null;
            try {
                newBoardMC = new ProbCentralBoard(board.getCent(), board.getNClients(), board.getPropc(), board.getPropg()); //Board per l'operador canviar central
            } catch (Exception ex) {
                Logger.getLogger(ProbCentralSuccessorFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
            Centrales CentralsDisponibles = newBoardMC.getCentralsDisponibles(consum);
            //per un client i concret el posem a totes les centrals lliures disponibles, és a dir, que això generarà tants successors com centrals disponibles
             for (int j=0;j<CentralsDisponibles.size(); j++){
                newBoardMC.moureClient(newBoardMC.getClients().get(i), CentralsDisponibles.get(j));
                String S="Moure client "+i+"a central "+j; 
                retVal.add(new Successor(S,newBoardMC));
             }
        }
        for (int k=0;k<board.getNCentrals(); ++k){
            ProbCentralBoard newBoardBC = null;
            try {
                newBoardBC = new ProbCentralBoard(board.getCent(), board.getNClients(), board.getPropc(), board.getPropg()); //Board per l'operador canviar central
            } catch (Exception ex) {
                Logger.getLogger(ProbCentralSuccessorFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (board.buidarCentral(k)){
                String S="Es buida central "+k; 
                retVal.add(new Successor(S,newBoardBC));
            }
        }
        ProbCentralBoard newBoardBCMP = null;
        newBoardBCMP.buidarCentralAmbMesPotencial();
        String S="buidar central amb més potencial"; 
        retVal.add(new Successor(S,newBoardBCMP));
        

    return retVal;
  }
}
