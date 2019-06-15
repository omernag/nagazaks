package algorithms.search;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * This class represent a solve of a Searchable problem according to the BFS algorithm
 * In our case the problem is a maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {

    protected PriorityQueue<AState> openList;
    protected AState curr;

    /**
     * Constructor for BFS
     * and generates a comparator by the cost param
     * in this class all cost are default
     */
    public BreadthFirstSearch() {
        openList = new PriorityQueue<>();
        lStates = new ArrayList<>();
        algorithmName = "BreadthFirstSearch";

        Comparator<AState> byCost = (o1, o2) -> o1.compareTo(o2);
        openList = new PriorityQueue<>(byCost);
        openList.comparator();

    }

    /**
     * This method solves a searchable problem according to the BFS algorithm
     *
     * @param domain the maze as ISearchable
     * @return Solution
     */
    @Override
    public Solution solve(ISearchable domain) {
        Solution sol = new Solution();
        lVisitedStates = new HashMap<>();

        openList.add(domain.getStartState());
        lVisitedStates.put(domain.getStartState().toString(),1);

        while(!openList.isEmpty()){
            curr = popOpenList();
            lStates = domain.getAllPossibleStates(curr);

            if(curr.getState().equals(domain.getGoalState().getState()))
                return createSolution(curr, sol, domain);

            for(int i = 0 ; i < lStates.size(); i++) {
                if (!lVisitedStates.containsKey((lStates.get(i).toString()))) {
                    lVisitedStates.put(lStates.get(i).toString(),1);
                    lStates.get(i).setPrevS(curr);
                    lStates.get(i).setCost(0);
                    openList.add(lStates.get(i));
                }
            }
        }//end of while
        return sol;
    }

    /**
     * This method return the next state to exit the queue
     * and add 1 to the visited nodes param
     * @return the next state to exit the queue
     */
    protected AState popOpenList(){
        visitedNodes++;
        return openList.poll();
    }
}
