package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {
    public int characterPositionRow=0;
    public int characterPositionColumn=0;

    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    boolean isFinished();
    boolean isMoved();
    boolean isSolved();
    void setcurrentMaze(Maze maze);
    void SolveSearchProblem();
    Maze getMaze();
    Solution getSolution();
    void stopServers();




}
