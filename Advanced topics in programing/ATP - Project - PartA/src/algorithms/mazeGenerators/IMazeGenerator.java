package algorithms.mazeGenerators;

/**
 * This class represent a interface for maze generators
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */

public interface IMazeGenerator {
    /**
     * This method generates a maze
     * @param lines
     * @param columns
     * @return Maze
     */
    Maze generate(int lines,int columns);

    /**
     * This method computes the time needed to generate a maze
     * @param lines
     * @param columns
     * @return time in long
     */
    long measureAlgorithmTimeMillis(int lines,int columns);
}
