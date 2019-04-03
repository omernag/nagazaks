package algorithms.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


/**
 * This class represent a solve of a Searchable problem according to the BestFS algorithm
 * In our case the problem is a maze
 *
 * This class is the same as BFS but uses prioritized list instead
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class BestFirstSearch extends BreadthFirstSearch {



    /**
     * Constructor for BestFS
     */
    public BestFirstSearch() {
        algorithmName = "BestFirstSearch";

        lStates = new ArrayList<>();
        Comparator<AState> byCost = (o1, o2) -> o1.compareTo(o2);
        openList = new PriorityQueue<>(byCost);
        openList.comparator();



    }

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
                    lStates.get(i).setCost(curr.getCost() + curr.getStateCost(lStates.get(i)));
                    openList.add(lStates.get(i));
                }
                else if( lVisitedStates.containsKey((lStates.get(i).toString())) &&  lStates.get(i).getCost()> (curr.getCost() + curr.getStateCost(lStates.get(i)))){
                   lStates.get(i).setPrevS(curr);
                    lStates.get(i).setCost(curr.getCost() + curr.getStateCost(lStates.get(i)));
                    openList.add(lStates.get(i));
                }
            }
        }

        return sol;
    }


}
