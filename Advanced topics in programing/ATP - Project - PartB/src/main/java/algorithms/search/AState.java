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

    /**
     * This is a constructor for AState
     */
    public AState() {
        state=null;
        prevS=null;

    }

    /**
     * This is a getter for state
     * @return state as object
     */
    public Object getState() {
        return state;
    }

    /**
     * This is a getter for prev state
     * @return previous AState
     */
    public AState getPrevS() {
        return prevS;
    }

    /**
     * This is a setter for prev state
     * @param prevS previous AState
     */
    public void setPrevS(AState prevS) {
        this.prevS = prevS;
    }

    /**
     * This is a getter for cost of state
     * @return cost as double
     */
    public double getCost() {
        return cost;
    }

    /**
     * This is a setter for cost of state
     * @param cost of AState
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * This is a abstract getter for cost of a state
     * @return cost as double
     */
    public abstract double getStateCost(AState st);

    /**
     * This method override equals
     * @param o other state
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AState)) return false;
        AState aState = (AState) o;
        return Double.compare(aState.cost, cost) == 0 &&
                this.equals(aState.state) &&
                Objects.equals(prevS, aState.prevS);
    }

    /**
     * This method override to string
     * @return string
     */
    @Override
    public String toString() {
        return state.toString();
    }

    /**
     * This method override compare to
     * @param o2 other state
     * @return int
     */
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
