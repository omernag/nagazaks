package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState extends AState {

    private Position state;
    private boolean visit=false;
    private boolean done=false;



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

    public double getStateCost(AState st){
        if(((state.getRowIndex() == ((MazeState)st).getState().getRowIndex()+1)||(state.getRowIndex() == ((MazeState)st).getState().getRowIndex()-1)) &&
                ((state.getColumnIndex() == ((MazeState)st).getState().getColumnIndex()+1)||(state.getColumnIndex() == ((MazeState)st).getState().getColumnIndex()-1))){
            return 15;
        }
        else return 10;
    }

    public MazeState(Position state, int cost) {
        this.state = state;
        super.setCost(cost);
    }

    public MazeState(MazeState other) {
        this.state = other.state;
        this.visit = other.visit;
        this.done = other.done;
        super.setCost(other.getCost());
        super.setPrevS(other.getPrevS());

    }

    @Override
    public boolean isVisited() {

        return visit;
    }

    @Override
    public boolean isDone() {

        return done;
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

    @Override
    public void visit() {

        visit=true;
    }

    @Override
    public void done() {
    done=true;
    }


}
