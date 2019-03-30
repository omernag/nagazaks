package algorithms.mazeGenerators;

/**
 * This class represent an abstract class for maze generators
 * implements the measure algorithm
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */

public abstract class AMazeGenerator implements IMazeGenerator{

    /**
     * This method generates a maze
     * @param lines
     * @param columns
     * @return Maze
     */
    public abstract  Maze generate(int lines,int columns);

    /**
     * This method computes the time needed to generate a maze
     * @param lines
     * @param columns
     * @return time in long
     */
    public long measureAlgorithmTimeMillis(int lines,int columns){
        long s_time = System.currentTimeMillis();
        generate(lines,columns);
        long f_time = System.currentTimeMillis();
        return f_time - s_time;
    }
}
