package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class hold a List of the path from the start state to the goal state
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class Solution implements Serializable {

    ArrayList<AState> solPath;

    /**
     * Constructor for the list
     */
    public Solution() {
        this.solPath = new ArrayList<>();
    }

    /**
     * This method return a list of the solution states
     * @return List of states
     */
    public ArrayList<AState> getSolutionPath() {
        ArrayList<AState> tmp = new ArrayList<>();
        for(int i = solPath.size()-1; i >=0 ; i--)
            tmp.add(solPath.size()-i-1,solPath.get(i));
        return tmp;
    }
}



