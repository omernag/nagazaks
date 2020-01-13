import java.util.Random;

public class QValueUpdate {

    public static double newQValue(double oldQValue, double reward, double nextStatesMaxQ, double discountFactor, double learningRate) {
        // TODO: Replace this with the Q-learning update rule
        return (1 - learningRate) * oldQValue + learningRate * (reward + discountFactor * nextStatesMaxQ);

    }

    public static CoffeeWorldAction chooseAction(CoffeeWorldState state, double epsilon, CoffeeEnvironment environment, AbstractQLearningAgent agent) {


        // TODO: Explore: choose a random action with epsilon probability

        // TODO: Exploit: get the best (highest Q value) action legal in this state

        // Helper values:
        //double exampleQValue = agent.getQValue(state, environment.getLegalActions(state).get(0));

        // TODO: replace with your chosen action
        if (environment.isTerminal(state))
            return null;

        double randomFactor = Math.random();

        if(randomFactor<epsilon){
            int randomIndex = (int)(Math.random()*environment.getLegalActions(state).size());
            return environment.getLegalActions(state).get(randomIndex);
        }
        else{
            CoffeeWorldAction maxQAction = null;
            double maxQValue = Double.NEGATIVE_INFINITY;
            double currQValue;
            for (CoffeeWorldAction action : environment.getLegalActions(state)) {
                currQValue = agent.getQValue(state, action);
                if (maxQValue < currQValue) {
                    maxQValue = currQValue;
                    maxQAction = action;
                }
            }
            return maxQAction;
        }
    }

}
