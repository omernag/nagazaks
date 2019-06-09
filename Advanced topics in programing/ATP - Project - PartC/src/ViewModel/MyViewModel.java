package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    private boolean finished;
    private boolean moved;
    private boolean solved;


    public MyViewModel(IModel model){
        this.model = model;
    }

    public void update(Observable o, Object arg) {   //activated when the model changes. here the VM will ask for data from the Model
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            finished = model.isFinished();
            moved = model.isMoved();
            solved = model.isSolved();
            setChanged();
            notifyObservers();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isSolved() {
        return solved;
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

    public void solveSearchProblem() { model.SolveSearchProblem(); }

    public void stopServers(){model.stopServers();}
}
