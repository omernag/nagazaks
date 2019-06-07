package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

public interface IModel {
    public int characterPositionRow=0;
    public int characterPositionColumn=0;

    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    void setcurrentMaze(Maze maze);
    Maze getMaze();

}
