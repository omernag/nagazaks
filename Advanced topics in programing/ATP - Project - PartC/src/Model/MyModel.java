package Model;

import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.*;
import algorithms.mazeGenerators.Maze;
import Client.Client;
import Client.IClientStrategy;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
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

import static javafx.scene.input.KeyCode.*;
import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;


public class MyModel extends Observable implements IModel {


    private Maze currentMaze;
    public int characterPositionRow;
    public int characterPositionColumn;
    public boolean finished;
    public boolean moved;
    public boolean solved;
    public boolean wrongKey;

    public Solution mazeSolution;

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;


    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

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
            solved=false;
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
            finished = false;
            moved = true;
            setChanged();
            notifyObservers();
    }

    public void generateMazeFromFile(Maze mazeInput){
        solved=false;
        setcurrentMaze(mazeInput);
        finished = false;
        moved = true;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isMoved() {
        return moved;
    }

    public boolean isWrongKey() {
        return wrongKey;
    }

    public void setWrongKey(boolean wrongKey) {
        this.wrongKey = wrongKey;
    }

    @Override
    public boolean isSolved() {
        return solved;
    }

    public void SolveSearchProblem() {
        currentMaze.setStartPosition(new Position(characterPositionRow,characterPositionColumn));
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();


                        toServer.writeObject(currentMaze); //send maze to server
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        solved=true;
        setChanged();
        notifyObservers();
    }

    public void moveCharacter(KeyCode movement) {
            if(!finished && isLegalMove(movement)){
                solved = false;
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
                    case NUMPAD1:
                        characterPositionRow++;
                        characterPositionColumn--;
                        break;
                    case NUMPAD2:
                        characterPositionRow++;
                        break;
                    case NUMPAD3:
                        characterPositionRow++;
                        characterPositionColumn++;
                        break;
                    case NUMPAD4:
                        characterPositionColumn--;
                        break;
                    case NUMPAD6:
                        characterPositionColumn++;
                        break;
                    case NUMPAD7:
                        characterPositionRow--;
                        characterPositionColumn--;
                        break;
                    case NUMPAD8:
                        characterPositionRow--;
                        break;
                    case NUMPAD9:
                        characterPositionRow--;
                        characterPositionColumn++;
                        break;
                }
                if(currentMaze.getGoalPosition().getRowIndex()==characterPositionRow && currentMaze.getGoalPosition().getColumnIndex()==characterPositionColumn )
                {
                    finished=true;
                }
                moved = true;

            }
            else{
                moved = false;
                if(!movingKey(movement)){
                    wrongKey = true;
                }
            }
        setChanged();
        notifyObservers();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }



    public boolean isLegalMove(KeyCode movement){
        if(movingKey(movement)) {
            switch (movement) {
                case UP:
                    if (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn) == 0) {
                        return true;
                    }
                    break;
                case DOWN:
                    if (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn) == 0) {
                        return true;
                    }
                    break;
                case RIGHT:
                    if (currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn + 1) == 0) {
                        return true;
                    }
                    break;
                case LEFT:
                    if (currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn - 1) == 0) {
                        return true;
                    }
                    break;

                case NUMPAD1:
                    if (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn - 1) == 0
                            && ((currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn - 1) == 0)
                            || (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn) == 0))
                    ) {
                        return true;
                    }
                    break;
                case NUMPAD2:
                    if (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn) == 0) {
                        return true;
                    }
                    break;
                case NUMPAD3:
                    if (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn + 1) == 0
                            && ((currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn + 1) == 0)
                            || (currentMaze.isInMaze(new Position(characterPositionRow + 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow + 1, characterPositionColumn) == 0))
                    ) {
                        return true;
                    }
                    break;
                case NUMPAD4:
                    if (currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn - 1) == 0) {
                        return true;
                    }
                    break;
                case NUMPAD6:
                    if (currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn + 1) == 0) {
                        return true;
                    }
                    break;
                case NUMPAD7:
                    if (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn - 1) == 0
                            && ((currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn - 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn - 1) == 0)
                            || (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn) == 0))
                    ) {
                        return true;
                    }
                    break;
                case NUMPAD8:
                    if (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn) == 0) {
                        return true;
                    }
                    break;
                case NUMPAD9:
                    if (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn + 1) == 0
                            && ((currentMaze.isInMaze(new Position(characterPositionRow, characterPositionColumn + 1)) && currentMaze.getValueByInt(characterPositionRow, characterPositionColumn + 1) == 0)
                            || (currentMaze.isInMaze(new Position(characterPositionRow - 1, characterPositionColumn)) && currentMaze.getValueByInt(characterPositionRow - 1, characterPositionColumn) == 0))
                    ) {
                        return true;
                    }
                    break;
            }
            return false;
        }
        return false;
    }

    private boolean movingKey(KeyCode movement) {
        if(movement == UP || movement == DOWN || movement == RIGHT || movement==LEFT || movement == NUMPAD1||
                movement == NUMPAD2 || movement == NUMPAD3 || movement == NUMPAD4 || movement == NUMPAD6 ||
                movement == NUMPAD7 || movement == NUMPAD8 || movement == NUMPAD9){
            return true;
        }
        return false;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public Solution getSolution(){
        return mazeSolution;
    }
}
