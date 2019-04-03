

package algorithms.search;
import java.util.ArrayList;

/**
 * This class represent a interface problems that can be solve by a searching algorithm
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public interface ISearchable {

    /**
     * This method return all the possible state to move to, from the current state
     * @param s current state
     * @return list of successors states
     */
    ArrayList<AState> getAllPossibleStates(AState s) ;

    /**
     * This method return the start state of the problem
     * @return AState of start
     */
    AState getStartState();

    /**
     * This method return the goal state of the problem
     * @return AState of goal
     */
    AState getGoalState();

}

