package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.*;

/**
 * This class represent an abstract class for searching algorithms
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class SearchableMaze implements ISearchable {

    private AState startState;
    private AState goalState;
    private HashMap<String, ArrayList<AState>> m_dMaze;
    private MazeState[][] m_dMazeMap;

    /**
     * This method generates a ISearchable problem from a given maze
     */
    public SearchableMaze(Maze maze) {
        createMazeMap(maze);
        startState = getMazeMapState(maze.getStartPosition());
        goalState = getMazeMapState(maze.getGoalPosition());
        m_dMaze = new HashMap<>();
        for (int i = 0; i < maze.getLines(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                Position pos = new Position(i, j, maze.getValueByInt(i, j));
                if (maze.getValue(pos) == 0) {
                    pos.setValue(0);
                    ArrayList<AState> allNeighbors = new ArrayList<>();
                    addNeighbors(maze, allNeighbors, pos);
                    m_dMaze.put(pos.toString(), allNeighbors);
                }
            }
        }//first for
    }

    /**
     * This method creates a maze map from a given map from maze states
     * @param maze given maze
     */
    private void createMazeMap(Maze maze) {
        m_dMazeMap = new MazeState[maze.getLines()][maze.getColumns()];
        for (int i = 0; i < maze.getLines(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                Position pos = new Position(i, j, maze.getValueByInt(i, j));
                m_dMazeMap[i][j] = new MazeState(pos);
            }
        }
    }

    /**
     * This method pulls a maze state in the given position
     * @param pos current position
     * @return maze state
     */
    private MazeState getMazeMapState(Position pos) {
        if (pos.getColumnIndex() >= 0 && pos.getRowIndex() >= 0 && pos.getRowIndex() < m_dMazeMap.length && pos.getColumnIndex() < m_dMazeMap[0].length) {
            return m_dMazeMap[pos.getRowIndex()][pos.getColumnIndex()];
        } else return null;
    }

    /**
     * This method generates a maze
     * @param allNeighbors list of neighbors
     * @param maze         given maze
     * @param pos          curr position in maze
     */
    private void addNeighbors(Maze maze, ArrayList<AState> allNeighbors, Position pos) {

        Position[] dir = createDirections(pos, maze);

        for (int i = 0; i < 8; i++) {
            if (maze.isInMaze(dir[i]) && maze.getValue(dir[i]) == 0) {
                allNeighbors.add(getMazeMapState(dir[i]));
            }
        }

        for (int i = 1; i < 6; i += 2) {
            if (allNeighbors.contains(getMazeMapState(dir[i]))) {
                if (!allNeighbors.contains(getMazeMapState(dir[i - 1])) && !allNeighbors.contains(getMazeMapState(dir[i + 1]))) {
                    allNeighbors.remove(getMazeMapState(dir[i]));
                }
            }
        }

        if (allNeighbors.contains(getMazeMapState(dir[7]))) {
            if (!allNeighbors.contains(getMazeMapState(dir[6])) && !allNeighbors.contains(getMazeMapState(dir[0]))) {
                allNeighbors.remove(getMazeMapState(dir[7]));
            }
        }

    }

    /**
     * This method creates an array of Positions that are around the curr position
     * @param maze given maze
     * @param pos  curr position in maze
     */
    private Position[] createDirections(Position pos, Maze maze) {
        Position[] dir = new Position[8];
        Position UL = maze.getPositionAt(pos.getRowIndex() - 1, pos.getColumnIndex() - 1);
        Position U = maze.getPositionAt(pos.getRowIndex() - 1, pos.getColumnIndex());
        Position L = maze.getPositionAt(pos.getRowIndex(), pos.getColumnIndex() - 1);
        Position DL = maze.getPositionAt(pos.getRowIndex() + 1, pos.getColumnIndex() - 1);
        Position D = maze.getPositionAt(pos.getRowIndex() + 1, pos.getColumnIndex());
        Position DR = maze.getPositionAt(pos.getRowIndex() + 1, pos.getColumnIndex() + 1);
        Position R = maze.getPositionAt(pos.getRowIndex(), pos.getColumnIndex() + 1);
        Position UR = maze.getPositionAt(pos.getRowIndex() - 1, pos.getColumnIndex() + 1);
        dir[0] = U;
        dir[1] = UR;
        dir[2] = R;
        dir[3] = DR;
        dir[4] = D;
        dir[5] = DL;
        dir[6] = L;
        dir[7] = UL;
        return dir;
    }

    /**
     * This method return all the neighbor states from a given maze state
     *
     * @param s gives maze state
     * @return list of states
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState s) {
        Position curr = (Position) s.getState();
        return m_dMaze.get(curr.toString());
    }

    /**
     * This is a getter for start maze state
     * @return start state
     */
    @Override
    public AState getStartState() {
        return startState;
    }

    /**
     * This is a getter for goal maze state
     * @return goal state
     */
    @Override
    public AState getGoalState() {
        return goalState;
    }

}
