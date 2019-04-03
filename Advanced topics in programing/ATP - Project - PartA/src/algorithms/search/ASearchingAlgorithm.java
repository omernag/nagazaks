package algorithms.search;

import java.util.*;

/**
 * This class represent an abstract class for searching algorithms
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected String algorithmName;
    protected ArrayList<AState> lStates;
    protected int visitedNodes;
    protected HashMap<String,Integer> lVisitedStates;
    protected HashMap<String,Integer> lAddedToNeighbors;

    /**
     * This method run backwards from the goal state to the start state,
     * and adds the states on the path to the list
     * @param curr the goal state
     * @param domain the problem
     * @param sol an empty solution list
     * @return Solution as a state list
     */
    public Solution createSolution(AState curr, Solution sol, ISearchable domain){
        while(!curr.getState().equals(domain.getStartState().getState())) {
            sol.solPath.add(curr);
            curr = curr.getPrevS();
        }
        sol.solPath.add(curr);
        return sol;
    }

    /**
     * This abstract method meant to solve a given problem
     * @param domain a problem that implements the ISearchable interface
     * @return a list as solution
     */
    @Override
    public abstract Solution solve(ISearchable domain) ;

    /**
     * This abstract method meant to return the name of the searching algorithm
     * @return name as string
     */
    @Override
    public String getName() {
        return algorithmName;
    }

    /**
     * This method return the number of visited states during the search
     * @return number of states as int
     */
    @Override
    public int getNumberOfNodesEvaluated() {
        return visitedNodes;
    }
}
