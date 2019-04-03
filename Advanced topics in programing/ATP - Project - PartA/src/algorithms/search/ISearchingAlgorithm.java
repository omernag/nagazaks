package algorithms.search;


import algorithms.mazeGenerators.Maze;

/**
 * This class represent a interface for maze generators
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 *///////////////////////////
public interface ISearchingAlgorithm {

    /**
     * This method generates a maze
     * @param
     * @param
     * @return Maze
     */////////////////////////
    Solution solve(ISearchable domain);

    /**
     * This method computes the time needed to generate a maze
     * @param
     * @param
     * @return time in long
     *//////////////////////
    String getName();

    /**
     * This method computes the time needed to generate a maze

     * @return time in long
     *//////////////////////
    int getNumberOfNodesEvaluated();


}
