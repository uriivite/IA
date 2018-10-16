package practica1;

import aima.search.framework.GoalTest;


public class ProbCentralGoalTest implements GoalTest{
    public boolean isGoalState(Object aState) {
    ProbCentralBoard board=(ProbCentralBoard)aState;
    return(board.isGoalState());
  }
}
