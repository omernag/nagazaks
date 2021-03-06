package Server;
import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;



public class ServerStrategyGenerateMaze implements IServerStrategy {

    AMazeGenerator generatorAlgorithm;
    private static final Logger LOG = LogManager.getLogger(); //Log4j2

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(outToClient);

            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();
            int[] mazeParam = (int[])fromClient.readObject();
            generatorAlgorithm = getGeneratorAlgorithm();
            Maze maze = generatorAlgorithm.generate(mazeParam[0]/*rows*/, mazeParam[1]/*columns*/);
            byte [] mazeArray = maze.toByteArray();
            byte [] compressMazeArray = compressor.compress(mazeArray);
            toClient.writeObject(compressMazeArray);
            LOG.info(String.format("Maze size: "+mazeParam[0]+"X"+mazeParam[1]));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private AMazeGenerator getGeneratorAlgorithm(){
        String algorithm = Configurations.loadGeneratorAlgorithm();
        if(algorithm.equals("MyMazeGenerator")){
            return new MyMazeGenerator();
        }
        else if(algorithm.equals("SimpleMazeGenerator")){
            return new SimpleMazeGenerator();
        }
        else return new EmptyMazeGenerator();

    }
}
