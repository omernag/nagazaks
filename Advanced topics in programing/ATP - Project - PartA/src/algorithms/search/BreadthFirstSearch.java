package algorithms.search;

import sun.misc.Queue;

import java.util.*;


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
        domain.getStartState().visit();

        while(!openList.isEmpty()){
            curr = popOpenList();

            //curr.done();
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
        }

        return sol;
    }

    protected AState popOpenList(){
        visitedNodes++;
        return openList.poll();
    }

}
