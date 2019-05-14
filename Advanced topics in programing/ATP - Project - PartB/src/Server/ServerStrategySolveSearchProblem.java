package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;

public class ServerStrategySolveSearchProblem implements IServerStrategy  {

    ASearchingAlgorithm solveAlgorithm;

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();
            Maze maze=(Maze)fromClient.readObject();
            SearchableMaze sMaze = new SearchableMaze(maze);
            //BestFirstSearch bestFS = new BestFirstSearch();
            //Solution solution = bestFS.solve(sMaze);
            solveAlgorithm = getSearchingAlgorithm();
            Solution solution = solveAlgorithm.solve(sMaze);
            toClient.writeObject(solution);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ASearchingAlgorithm getSearchingAlgorithm(){
        String algorithm = Configurations.loadSearchAlgorithm();
        if(algorithm.equals("BestFirstSearch")){
            return new BestFirstSearch();
        }
        else if(algorithm.equals("BreadthFirstSearch")){
            return new BreadthFirstSearch();
        }
        else return new DepthFirstSearch();

    }
}
