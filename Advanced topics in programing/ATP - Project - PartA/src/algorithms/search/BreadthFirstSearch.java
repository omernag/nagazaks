package algorithms.search;

import sun.misc.Queue;

import java.util.ArrayList;
import java.util.Deque;
import java.util.PriorityQueue;


/**
 * This class represent a solve of a Searchable problem according to the BFS algorithm
 * In our case the problem is a maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {

    Queue<AState> openList;


    /**
     * Constructor for BFS
     */
    public BreadthFirstSearch() {
        openList = new Queue<>();
        lStates = new ArrayList<>();
        algorithmName = "BreadthFirstSearch";

    }

    /**
     * This method solves a searchable problem according to the BFS algorithm
     *
     * @param domain the maze as ISearchable
     * @return Solution
     */
    @Override
    public Solution solve(ISearchable domain) throws InterruptedException {
        Solution sol = new Solution();
        //sol.solPath.add(domain.getStartState());
        openList.enqueue(domain.getStartState());
        domain.getStartState().visit();

        while(!openList.isEmpty()){
            visitedNodes++;
            AState curr = openList.dequeue();

            curr.done();
            lStates = domain.getAllPossibleStates(curr);

            if(curr.getState().equals(domain.getGoalState().getState()))
                return createSolution(curr,sol,domain);

            for(int i = 0 ; i < lStates.size(); i++){
                if(!lStates.get(i).isDone()) {
                    if (!lStates.get(i).isVisited()) {
                        lStates.get(i).visit();
                        lStates.get(i).setPrevS(curr);
                        openList.enqueue(lStates.get(i));
                    }
                }
            }
        }

        return sol;
    }
}
