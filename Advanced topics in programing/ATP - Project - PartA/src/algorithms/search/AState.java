package algorithms.search;

import java.util.Objects;

/**
 * This class represent an abstract class for a state in the problem
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public abstract class AState {

    private Object state;
    private AState prevS;
    private double cost;


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

    public double getStateCost(AState st){
        return 0;
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




    @Override
    public String toString() {
        return state.toString();
    }


    /////////
    public void visit(){}
    public void done(){}
    public boolean isVisited() {
        return false;
    }
    public boolean isDone() {
        return false;
    }

    public int compareTo(AState o2) {
        if(this.cost-o2.cost>0){
            return 1;
        }
        else if(this.cost-o2.cost<0){
            return -1;
        }
        return 0;
    }
}
