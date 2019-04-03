package algorithms.search;

/**
 * This class represent an interface for searching algorithms
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public interface ISearchingAlgorithm {

    /**
     * This method solve a given problem
     * @param domain a problem that implements the ISearchable interface
     * @return a list as solution
     */
    Solution solve(ISearchable domain);

    /**
     * This method returns the name of the searching algorithm
     * @return name as string
     */
    String getName();

    /**
     * This method return the number of visited states during the search
     * @return number of states as int
     */
    int getNumberOfNodesEvaluated();

}