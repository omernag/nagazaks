package algorithms.search;

import java.util.ArrayList;

public class Solution {

    ArrayList<AState> solPath;

    public Solution() {
        this.solPath = new ArrayList<>();
    }

    public ArrayList<AState> getSolutionPath() {
        ArrayList<AState> tmp = new ArrayList<>();
        for(int i = solPath.size()-1; i >=0 ; i--)
            tmp.add(solPath.size()-i-1,solPath.get(i));
         return tmp;
    }
}
