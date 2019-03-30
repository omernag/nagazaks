package algorithms.search;
import java.util.ArrayList;

/**
 * This class represent a interface for maze generators
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 *///////////////////////////
public interface ISearchable {

    /**
     * This method generates a maze
     * @param
     * @param
     * @return Maze
     */////////////////////////
    ArrayList<AState> getAllPossibleStates(AState s) ;
    AState getStartState();
    AState getGoalState();

}
