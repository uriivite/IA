/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jordi.donadeu
 */
public class ProbCentralSucccessorFunction implements SuccessorFunction{
    public List getSuccessors(Object aState) {
    ArrayList retVal= new ArrayList();
    ProbCentralBoard board=(ProbCentralBoard) aState;
    for(int i=0;i<board.getNumAntenas();i++){
      // FEM UN CAS PER CADA OPERADOR QUE TINGUEM 
       ProbCentralBoard newBoardU= new ProbCentralBoard(board.getNumAntenas(),board.getMaxPotencia(),board.getAntenas(),board.getDimPlano(),board.getPlano());
       if (newBoardU.MoverAntena(i, -1,0)) {
          String S=new String(ProbCentralBoard.MOVER+" "+i+" Up");
          retVal.add(new Successor(S,newBoardU));
       }
       ProbCentralBoard newBoardPU= new ProbCentralBoard(board.getNumAntenas(),board.getMaxPotencia(),board.getAntenas(),board.getDimPlano(),board.getPlano());
       if (newBoardPU.aumentaPotencia(i)) {
          String S=new String(ProbCentralBoard.AUMENTAR+" "+i);
          retVal.add(new Successor(S,newBoardPU));
       }
       
       ProbCentralBoard newBoardPD= new ProbCentralBoard(board.getNumAntenas(),board.getMaxPotencia(),board.getAntenas(),board.getDimPlano(),board.getPlano());
       if (newBoardPD.disminuyePotencia(i)) {
          String S=new String(ProbCentralBoard.DISMINUIR+" "+i);
          retVal.add(new Successor(S,newBoardPD));
       }
       
    }
     
     return retVal;
  }
}
