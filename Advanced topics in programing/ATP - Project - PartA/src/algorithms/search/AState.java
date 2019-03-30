package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.Objects;

public abstract class AState {

    private Object state;
    private AState prevS;
    private double cost;
    private boolean visit=false;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public AState() {
        state=null;
        prevS=null;

    }

    public AState(AState o) {
        this.state = o.state;
        this.prevS = o.prevS;
        this.cost = o.cost;
        this.visit = o.visit;

    }

    public AState getPrevS() {
        return prevS;
    }

    public void setPrevS(AState prevS) {
        this.prevS = prevS;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AState)) return false;
        AState aState = (AState) o;
        return Double.compare(aState.cost, cost) == 0 &&
                this.equals(aState.state) &&
                Objects.equals(prevS, aState.prevS);
    }


    public boolean isVisited() {
        return visit;
    }

    @Override
    public String toString() {
        return state.toString();
    }

    public void visit() {
        visit=true;
    }
}
