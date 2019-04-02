package algorithms.search;

import java.util.*;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected String algorithmName;
    protected ArrayList<AState> lStates;
    protected PriorityQueue<AState> openList;
    protected Stack<AState> currPath;
    protected int visitedNodes;
    protected HashMap<String,Integer> lVisitedStates;



    protected AState popOpenList(){
        visitedNodes++;
        return openList.poll();
    }

    protected AState popStack(){
        visitedNodes++;
        AState pop = currPath.pop();
        return pop;
    }

    public Solution createSolution(AState curr, Solution sol, ISearchable domain){
        while(!curr.getState().equals(domain.getStartState().getState())) {
            sol.solPath.add(curr);
            curr = curr.getPrevS();
        }
        sol.solPath.add(curr);
        return sol;
    }

    protected void cleanVisites(ISearchable domain) {

    }

    /**
     * This method generates a maze
     *
     * @param domain@return Maze
     */////////////////
    @Override
    public abstract Solution solve(ISearchable domain) throws InterruptedException;

    /**
     * This method computes the time needed to generate a maze
     *
     * @return time in long
     *///////////////
    @Override
    public String getName() {
        return algorithmName;
    }



    /**
     * This method computes the time needed to generate a maze
     *
     * @return time in long
     */////////////
    @Override
    public int getNumberOfNodesEvaluated() {
        return visitedNodes;
    }
}
