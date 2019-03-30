package algorithms.mazeGenerators;

/**
 * This class represent a generator for simple maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    /**
     * This method generates a simple maze with random wall.
     * this method grantees that the maze is solvable.
     * @param lines
     * @param columns
     * @return Maze
     */
    @Override
    public Maze generate(int lines, int columns) {

        Maze simpleMaze = new Maze(lines, columns);
        simpleMaze.setRandomWalls();
        simpleMaze.Set_random_start();
        Position pointer = simpleMaze.getStartPosition();
        Position tmp;
        boolean exitFound=false;
        boolean moved = false;
        while(!exitFound){
            moved = false;
            int rand = (int) (Math.random() * 4);
            if(rand==0){
                tmp = new Position(pointer.getLine() -1 , pointer.getColumn());
            }
            else if(rand==1){
                tmp = new Position(pointer.getLine() , pointer.getColumn()-1);

            }else if(rand==2 ){
                tmp = new Position(pointer.getLine()+1 , pointer.getColumn());
            }
            else{
                tmp = new Position(pointer.getLine() , pointer.getColumn()+1);
            }
            if(simpleMaze.isInMaze(tmp) && (!simpleMaze.is_Farme(pointer)|| !simpleMaze.is_Farme(tmp))){
                pointer = tmp;
                simpleMaze.setPath(pointer);
                moved = true;
            }
            if (moved && simpleMaze.is_Farme(pointer) && !pointer.equals(simpleMaze.getStartPosition())){
                exitFound = true;
                simpleMaze.setGoalPosition(pointer);
            }
        }//while
        return simpleMaze;
    }
}
