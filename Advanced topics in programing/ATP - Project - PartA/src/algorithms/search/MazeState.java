package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState extends AState {

    private Position state;

    public MazeState(Position state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MazeState)) return false;

        MazeState mazeState = (MazeState) o;
        return state.equals(mazeState.state);
    }


    public MazeState(Position state, int cost) {
        this.state = state;
        super.setCost(cost);
    }

    public MazeState(MazeState other) {
        this.state = other.state;

    }

    public Position getState() {
        return state;
    }

    public void setState(Position state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
