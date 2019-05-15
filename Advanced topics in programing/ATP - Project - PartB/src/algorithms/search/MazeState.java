package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.Serializable;

/**
 * This class represent a class for a state in the maze
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class MazeState extends AState implements Serializable {

    private Position state;

    /**
     * This is a constructor for maze state
     */
    public MazeState(Position state) {
        this.state = state;
    }

    /**
     * This is a getter for cost of a state
     * @return cost as double
     */
    public double getStateCost(AState st){
        if(((state.getRowIndex() == ((MazeState)st).getState().getRowIndex()+1)||(state.getRowIndex() == ((MazeState)st).getState().getRowIndex()-1)) &&
                ((state.getColumnIndex() == ((MazeState)st).getState().getColumnIndex()+1)||(state.getColumnIndex() == ((MazeState)st).getState().getColumnIndex()-1))){
            return 15;
        }
        else return 10;
    }

    /**
     * This is a getter for cost of state
     * @return cost as double
     */
    public Position getState() {
        return state;
    }

    /**
     * This method override equals
     * @param o other state
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MazeState)) return false;
        MazeState mazeState = (MazeState) o;
        return state.equals(mazeState.state);
    }

    /**
     * This method override to string
     * @return string
     */
    @Override
    public String toString() {
        return state.toString();
    }

}
