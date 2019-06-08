package Model;

import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.*;
import algorithms.mazeGenerators.Maze;
import Client.Client;
import Client.IClientStrategy;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MyModel extends Observable implements IModel {


    private static Maze currentMaze;
    public int characterPositionRow;
    public int characterPositionColumn;

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

        //CommunicateWithServer_MazeGenerating();
        //CommunicateWithServer_SolveSearchProblem();
    }

    public void stopServers() {
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();
    }

    public Maze getMaze() {
        return currentMaze;
    }

    public void setcurrentMaze(Maze maze) {
        currentMaze = maze;
        characterPositionRow = maze.getStartPosition().getRowIndex();
        characterPositionColumn = maze.getStartPosition().getColumnIndex();
    }

    public void generateMaze(int width, int height) {

            try {
                Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{width, height};
                            toServer.writeObject(mazeDimensions); //send maze dimensions to server
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[width*height+30 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                            is.read(decompressedMaze); //Fill decompressedMaze with bytes
                            setcurrentMaze(new Maze(decompressedMaze));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();

    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(50, 50);
                        maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void moveCharacter(KeyCode movement) {
        if(isLegalMove(movement)){
            switch (movement) {
                case UP:
                    characterPositionRow--;
                    break;
                case DOWN:
                    characterPositionRow++;
                    break;
                case RIGHT:
                    characterPositionColumn++;
                    break;
                case LEFT:
                    characterPositionColumn--;
                    break;
            }
            setChanged();
            notifyObservers();
        }
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public boolean isLegalMove(KeyCode movement){
        switch (movement) {
            case UP:
                if(currentMaze.getValueByInt(characterPositionRow-1,characterPositionColumn)==0){
                    return true;
                }
                break;
            case DOWN:
                if(currentMaze.getValueByInt(characterPositionRow+1,characterPositionColumn)==0){
                    return true;
                }
                break;
            case RIGHT:
                if(currentMaze.getValueByInt(characterPositionRow,characterPositionColumn+1)==0){
                    return true;
                }
                break;
            case LEFT:
                if(currentMaze.getValueByInt(characterPositionRow,characterPositionColumn-1)==0){
                    return true;
                }
                break;
        }
        return false;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
}
