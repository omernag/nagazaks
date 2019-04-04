package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.*;

public class SearchableMaze implements ISearchable {

    private AState startState;
    private AState goalState;
    private HashMap<String,ArrayList<AState>> m_dMaze;
    private int[][] doneTable;
    private MazeState[][] m_dMazeMap;

    /**
     * This method generates a maze
     *
     *  Maze
     */////////
    public SearchableMaze(Maze maze) {
        createMazeMap(maze);
        doneTable = new int[maze.getLines()][maze.getColumns()];////////////
        startState = getMazeMapState(maze.getStartPosition());
        goalState = getMazeMapState(maze.getGoalPosition());
        m_dMaze = new HashMap<>();
        for (int i = 0 ; i < maze.getLines() ; i++){
            for (int j = 0 ; j < maze.getColumns() ; j++){
                Position pos = new Position(i,j,maze.getValueByInt(i,j));
                if(maze.getValue(pos)==0) {
                    pos.setValue(0);
                    ArrayList<AState> allNeighbors = new ArrayList<>();
                    addNeighbors(maze, allNeighbors, pos);
                    m_dMaze.put(pos.toString(), allNeighbors);
                }
            }
        }

    }



    private void addNeighbors(Maze maze, ArrayList<AState> allNeighbors, Position pos) {

        Position[] dir = createDirections(pos, maze);

        for(int i = 0 ; i < 8 ; i++){
            if (maze.isInMaze(dir[i]) && maze.getValue(dir[i]) == 0) {
                allNeighbors.add(getMazeMapState(dir[i]));
            }
        }

        for(int i = 1 ; i < 6 ; i+=2){
            if (allNeighbors.contains(getMazeMapState(dir[i]))){
                if(!allNeighbors.contains(getMazeMapState(dir[i-1])) && !allNeighbors.contains(getMazeMapState(dir[i+1]))){
                    allNeighbors.remove(getMazeMapState(dir[i]));
                }
            }
        }

        if (allNeighbors.contains(getMazeMapState(dir[7]))){
            if(!allNeighbors.contains(getMazeMapState(dir[6])) && !allNeighbors.contains(getMazeMapState(dir[0]))){
                allNeighbors.remove(getMazeMapState(dir[7]));
            }
        }

        ///cost?
        /*for(int i = 0 ; i < 8 ; i++){
            if (allNeighbors.contains(getMazeMapState(dir[i]))) {
                if (i % 2 == 0) {
                    getMazeMapState(dir[i]).setCost(10);
                } else getMazeMapState(dir[i]).setCost(15);
            }
        }*/
    }

    private Position[] createDirections(Position pos, Maze maze) {
        Position[] dir = new Position[8];
        Position UL = maze.getPositionAt(pos.getRowIndex()-1,pos.getColumnIndex() - 1);
        Position U = maze.getPositionAt(pos.getRowIndex()-1,pos.getColumnIndex());
        Position L = maze.getPositionAt(pos.getRowIndex(),pos.getColumnIndex() - 1);
        Position DL = maze.getPositionAt(pos.getRowIndex()+1,pos.getColumnIndex() - 1);
        Position D = maze.getPositionAt(pos.getRowIndex()+1,pos.getColumnIndex());
        Position DR = maze.getPositionAt(pos.getRowIndex()+1,pos.getColumnIndex() + 1);
        Position R = maze.getPositionAt(pos.getRowIndex(),pos.getColumnIndex() + 1);
        Position UR = maze.getPositionAt(pos.getRowIndex()-1,pos.getColumnIndex() + 1);
        dir[0]=U;
        dir[1]=UR;
        dir[2]=R;
        dir[3]=DR;
        dir[4]=D;
        dir[5]=DL;
        dir[6]=L;
        dir[7]=UL;
        return dir;


    }

    private MazeState getMazeMapState(Position pos){
        if(pos.getColumnIndex()>=0 && pos.getRowIndex()>=0 && pos.getRowIndex()<m_dMazeMap.length && pos.getColumnIndex()<m_dMazeMap[0].length) {
            return m_dMazeMap[pos.getRowIndex()][pos.getColumnIndex()];
        }
        else return null;
    }

    private void createMazeMap(Maze maze){
        m_dMazeMap = new MazeState[maze.getLines()][maze.getColumns()];
        for (int i = 0 ; i < maze.getLines() ; i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                Position pos = new Position(i, j,maze.getValueByInt(i,j));
                m_dMazeMap[i][j]=new MazeState(pos);
            }
        }
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
        return m_dMaze.get(curr.toString());
    }

    @Override
    public AState getStartState() {
        return startState;
    }

    @Override
    public AState getGoalState() {
        return goalState;
    }

    public void setDone(MazeState st){
        doneTable[st.getState().getRowIndex()][st.getState().getColumnIndex()] = 1;
    }

    public void setVisit(MazeState st){
        doneTable[st.getState().getRowIndex()][st.getState().getColumnIndex()] = 1;
    }

    public int getDone(MazeState st){
        return doneTable[st.getState().getRowIndex()][st.getState().getColumnIndex()];
    }

    public int getVisit(MazeState st){
        return doneTable[st.getState().getRowIndex()][st.getState().getColumnIndex()];
    }
}
