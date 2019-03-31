package algorithms.search;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * This class represent a solve of a Searchable problem according to the BFS algorithm
 * In our case the problem is a maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    /**
     * Constructor for DFS
     */
    public DepthFirstSearch() {
        currPath = new Stack<>();
        lStates = new ArrayList<>();
        algorithmName = "DepthFirstSearch";
    }



    /**
     * This method solves a searchable problem according to the DFS algorithm
     *
     * @param domain the maze as ISearchable
     * @return Solution
     */
    @Override
    public Solution solve(ISearchable domain) {
        Solution sol = new Solution();

        currPath.push(domain.getStartState());

        while(!currPath.isEmpty()){
            AState curr = popStack();
            curr.visit();

            if(curr.getState().equals(domain.getGoalState().getState())) {
                return createSolution(curr,sol,domain);
            }

            lStates = domain.getAllPossibleStates(curr);
            for(int i = lStates.size()-1 ; i >=0; i--){
                if(!lStates.get(i).isVisited()) {
                    lStates.get(i).setPrevS(curr);
                    currPath.push(lStates.get(i));
                }
            }

        }

        return null;
    }
}
