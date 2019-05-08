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
            MyCompressorOutputStream toClient = new MyCompressorOutputStream(outToClient);
            toClient.flush();
            int[] mazeParam = (int[])fromClient.readObject();
            fromClient.close();
            MyMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(mazeParam[0]/*rows*/, mazeParam[1]/*columns*/);
            byte [] mazeArray = maze.toByteArray();
            toClient.write(mazeArray);
            toClient.flush();
            toClient.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
