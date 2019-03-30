package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.*;

public class SearchableMaze implements ISearchable {

    private AState startState;
    private AState goalState;
    private HashMap<Position,ArrayList<AState>> m_dMaze;

    /**
     * This method generates a maze
     *
     * @return Maze
     */////////
    public SearchableMaze(Maze maze) {
        startState = new MazeState(maze.getStartPosition());
        goalState = new MazeState(maze.getGoalPosition());
        m_dMaze = new HashMap<>();
        for (int i = 0 ; i < maze.getLines() ; i++){
            for (int j = 0 ; j < maze.getColumns() ; j++){
                Position pos = new Position(i,j);
                if(maze.getValue(pos)==0) {
                    pos.setValue(0);
                    ArrayList<AState> allNeighbors = new ArrayList<>();
                    addNeighbors(maze, allNeighbors, pos);
                    m_dMaze.put(pos, allNeighbors);
                }
            }
        }
    }

    private void addNeighbors(Maze maze, ArrayList<AState> allNeighbors, Position pos) {

        Position UL = maze.getPositionAt(pos.getLine()-1,pos.getColumn() - 1);
        Position U = maze.getPositionAt(pos.getLine()-1,pos.getColumn());
        Position L = maze.getPositionAt(pos.getLine(),pos.getColumn() - 1);
        Position DL = maze.getPositionAt(pos.getLine()+1,pos.getColumn() - 1);
        Position D = maze.getPositionAt(pos.getLine()+1,pos.getColumn());
        Position DR = maze.getPositionAt(pos.getLine()+1,pos.getColumn() + 1);
        Position R = maze.getPositionAt(pos.getLine(),pos.getColumn() + 1);
        Position UR = maze.getPositionAt(pos.getLine()-1,pos.getColumn() + 1);

        if (maze.isInMaze(U) && maze.getValue(U) == 0) {
            allNeighbors.add(new MazeState(U));
        }

        if (maze.isInMaze(R) && maze.getValue(R) == 0) {
            if (maze.isInMaze(UR) && maze.getValue(UR) == 0) {
                if (allNeighbors.contains(U))
                    allNeighbors.add(new MazeState(UR));
            }
            allNeighbors.add(new MazeState(R));
        }

        if (maze.isInMaze(D) && maze.getValue(D) == 0) {
            if (maze.isInMaze(DR) && maze.getValue(DR) == 0) {
                if (allNeighbors.contains(R))
                allNeighbors.add(new MazeState(DR));
            }
            allNeighbors.add(new MazeState(D));
        }

        if (maze.isInMaze(L) && maze.getValue(L) == 0) {
            if (maze.isInMaze(DL) && maze.getValue(DL) == 0) {
                if (allNeighbors.contains(D) || allNeighbors.contains(L))
                    allNeighbors.add(new MazeState(DL));
            }
            allNeighbors.add(new MazeState(L));
        }


        if (maze.isInMaze(UL) && maze.getValue(UL) == 0) {
            if(allNeighbors.contains(U) || allNeighbors.contains(L))
                allNeighbors.add(new MazeState(UL));
        }








        /*Position currPos;

        for (int i = pos.getState().getLine()-1 ; i <= maze.getLines()+1 ; i++) {
            for (int j = pos.getState().getColumn() - 1; j <= maze.getColumns() + 1; j++) {
                if(i != pos.getState().getLine() || j != maze.getColumns()) {
                    currPos = maze.getPositionAt(i, j);

                    if (maze.isInMaze(currPos) && maze.getValue(currPos) == 0) {
                        if(maze.getValue(maze.getPositionAt(i-1,j))==0 || maze.getValue(maze.getPositionAt(i,j-1))==0 || ){

                        }
                        allNeighbors.add(new MazeState(currPos));
                    }
                }
            }
        }*/
    }



    /**
     * This method generates a maze
     *
     * @param s gives state
     * @return Maze
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState s) {
        Position curr = (Position)s.getState();
        for (Position tmp : m_dMaze.keySet()) {
            if(tmp.equals(curr)){
                return m_dMaze.get(tmp);
            }
        }
        return null;
    }

    @Override
    public AState getStartState() {
        return startState;
    }

    @Override
    public AState getGoalState() {
        return goalState;
    }
}
