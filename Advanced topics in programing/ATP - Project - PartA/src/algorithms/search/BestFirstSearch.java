package algorithms.search;

import java.util.ArrayList;
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

    PriorityQueue<AState> openList;

    /**
     * Constructor for BestFS
     */
    public BestFirstSearch() {
        openList = new PriorityQueue<>();
        lStates = new ArrayList<>();

    }
}
