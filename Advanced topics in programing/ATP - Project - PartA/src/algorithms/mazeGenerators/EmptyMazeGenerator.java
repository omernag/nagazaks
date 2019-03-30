package algorithms.mazeGenerators;


/**
 * This class represent a generator for empty maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class EmptyMazeGenerator extends AMazeGenerator{


    /**
     * This method generates a maze with no walls
     * @param lines
     * @param columns
     * @return Maze
     */
    @Override
    public Maze generate(int lines, int columns) {
        Maze emptyMaze = new Maze(lines, columns);
        return emptyMaze;
    }
}
