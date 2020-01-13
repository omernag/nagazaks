import java.util.Random;

public class QValueUpdate {

    public static double newQValue(double oldQValue, double reward, double nextStatesMaxQ, double discountFactor, double learningRate) {
        // TODO: Replace this with the Q-learning update rule
        return 0;

    }

    public static CoffeeWorldAction chooseAction(CoffeeWorldState state, double epsilon, CoffeeEnvironment environment, AbstractQLearningAgent agent) {

        // TODO: Explore: choose a random action with epsilon probability

        // TODO: Exploit: get the best (highest Q value) action legal in this state

        // Helper values:
        double exampleQValue = agent.getQValue(state, environment.getLegalActions(state).get(0));

        // TODO: replace with your chosen action
        return environment.getLegalActions(state).get(0);

    }

}
