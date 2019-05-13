package Server;
import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(outToClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            int[] mazeParam = (int[])fromClient.readObject();
            MyMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(mazeParam[0]/*rows*/, mazeParam[1]/*columns*/);
            byte [] mazeArray = maze.toByteArray();
            byte [] compressMazeArray = compressor.compress(mazeArray);
            toClient.writeObject(compressMazeArray);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
