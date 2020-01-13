import java.util.List;
import java.util.Map;

public class BellmanUpdate {

    public static double getNewV(CoffeeWorldState state, OpenCoffeeEnvironment openEnvironment, double discountFactor, Map<CoffeeWorldState, Double> vValues) {

        // TODO: Compute the new V value for the given state.
        double newV =0;
        // Helper values:
        List<CoffeeWorldAction> legalActions = openEnvironment.getLegalActions(state);
        CoffeeWorldAction action = legalActions.get(0); // An example of one action
        List<OpenCoffeeEnvironment.Transition> transitions;// = openEnvironment.getLegalTransitions(state,action);
        double maxV = Double.NEGATIVE_INFINITY;
        for (CoffeeWorldAction cea: legalActions
             ) {
            transitions = openEnvironment.getLegalTransitions(state,cea);
            double summation=0;
            for (OpenCoffeeEnvironment.Transition trs: transitions
                 ) {
                summation+=trs.probability*vValues.get(trs.state);
            }
            newV=openEnvironment.getReward(state,cea)+discountFactor*summation;
            if(maxV<newV){
                maxV=newV;
            }
        }

        return maxV;
    }
    //V(state)←Reward(state,action)+γ∑_(s∈states)▒〖Pr⁡(s│state,action)⋅V(s)〗
}
