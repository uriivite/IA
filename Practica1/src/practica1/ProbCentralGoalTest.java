/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import aima.search.framework.GoalTest;
/**
 *
 * @author jordi.donadeu
 */
public class ProbCentralGoalTest implements GoalTest{
    public boolean isGoalState(Object aState) {
    ProbCentralBoard board=(ProbCentralBoard)aState;
    return(board.isGoalState());
  }
}
