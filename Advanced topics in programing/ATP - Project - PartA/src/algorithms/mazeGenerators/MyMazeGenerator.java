package algorithms.mazeGenerators;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates mazes according to the Prim algorithm.
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */

public class MyMazeGenerator extends AMazeGenerator {
    private List<Position> walls;
    private Maze primMaze;

    /**
     * constructor for this type of maze
     */
    public MyMazeGenerator() {
        walls = new <Position>ArrayList();
    }

    /**
     * This method generates mazes according to the Prim algorithm.
     *
     * @param lines   Number of lines
     * @param columns Number of columns
     * @return Maze This returns a solvable maze
     */
    @Override
    public Maze generate(int lines, int columns) {
        primMaze = new Maze(lines, columns);
        primMaze.setAllWalls();
        int s_line = (int) (Math.random() * lines);
        int s_column = (int) (Math.random() * columns);
        Position curr_Pos = new Position(s_line, s_column);
        primMaze.setPath(curr_Pos);
        walls.add(curr_Pos);

        int rand;
        addNeighborsToWalls(curr_Pos);


        while (!walls.isEmpty()) {
            rand = (int) (Math.random() * walls.size());
            curr_Pos = walls.get(rand);
            if(primMaze.getValue(curr_Pos)==1) {
                TryPath(curr_Pos);
            }
            walls.remove(rand);
        }
        Position entry = primMaze.Get_random_Frame();
        Position exit = primMaze.Get_random_Frame();
        boolean ready = false;
        while (!ready) {
            if (primMaze.getValue(entry) == 0 && primMaze.getValue(exit) == 0 && !entry.equals(exit)) {
                primMaze.setStartPosition(entry);
                primMaze.setGoalPosition(exit);
                ready = true;
            }
            else {
                if (primMaze.getValue(entry) == 0) {
                    primMaze.setStartPosition(entry);
                }
                else {
                    entry = primMaze.Get_random_Frame();
                }
                if (primMaze.getValue(exit) == 0) {
                    primMaze.setGoalPosition(exit);
                }
                else{
                    exit = primMaze.Get_random_Frame();

                }
            }
        }


        return primMaze;
    }

    private boolean TryPath(Position curr_Pos) {

        boolean path ; // false --> if there are paths on 2 sides of him and this position needs to remain a wall.
        Position up = new Position(curr_Pos.getRowIndex() - 1, curr_Pos.getColumnIndex());
        Position left = new Position(curr_Pos.getRowIndex(), curr_Pos.getColumnIndex() - 1);
        Position down = new Position(curr_Pos.getRowIndex() + 1, curr_Pos.getColumnIndex());
        Position right = new Position(curr_Pos.getRowIndex(), curr_Pos.getColumnIndex() + 1);

        path = !areBothPaths(primMaze,up,down);
        if(path)
        path = !areBothPaths(primMaze,left,right);
        if(path)
        path = !areBothPaths(primMaze,left,up);
        if(path)
        path = !areBothPaths(primMaze,left,down);
        if(path)
        path = !areBothPaths(primMaze,up,right);
        if(path)
        path = !areBothPaths(primMaze,down,right);


        if (path == true) {
            primMaze.setPath(curr_Pos);
            addNeighborsToWalls(curr_Pos);
        }

        return path;
    }

    private void addNeighborsToWalls(Position curr_Pos) {
        tryAddWall(new Position(curr_Pos.getRowIndex() - 1, curr_Pos.getColumnIndex()));//up
        tryAddWall(new Position(curr_Pos.getRowIndex(), curr_Pos.getColumnIndex() - 1));//left
        tryAddWall(new Position(curr_Pos.getRowIndex() + 1, curr_Pos.getColumnIndex()));//down
        tryAddWall(new Position(curr_Pos.getRowIndex(), curr_Pos.getColumnIndex() + 1));//right
    }

    private boolean tryAddWall(Position curr_Pos) {
        if (primMaze.isInMaze(curr_Pos) && primMaze.getValue(curr_Pos) == 1) {
            walls.add(curr_Pos);
            return true;
        }
        return false;
    }


}

