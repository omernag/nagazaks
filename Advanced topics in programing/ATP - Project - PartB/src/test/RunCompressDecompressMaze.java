package test;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Aviadjo on 3/26/2017.
 */
public class RunCompressDecompressMaze {
    public static void main(String[] args) {
        String mazeFileName = "savedMaze.txt";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(200, 200); //Generate new maze

        try {
            // save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte savedMazeBytes[] = new byte[0];
        try {
            //read maze from file
            InputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
            savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Maze loadedMaze = new Maze(savedMazeBytes);
        boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(),maze.toByteArray());
        System.out.println(String.format("Mazes equal: %s",areMazesEquals)); //maze should be equal to loadedMaze
    }

/***
        public static void main(String[] args) {

            Maze m1 = testMazeGenerator(new MyMazeGenerator());
            byte[] a1 = m1.toByteArray();
            for(byte b : a1){
             System.out.print(b);
            }

            System.out.println();

            Maze m2 = new Maze(a1);

            byte[] a2 = m2.toByteArray();

            for(byte b : a2){
                System.out.print(b);
            }
            System.out.println();

        }

        private static Maze testMazeGenerator(IMazeGenerator mazeGenerator) {

            Maze maze = mazeGenerator.generate(15, 12);

            // prints the maze
            maze.print();

            // get the maze entrance
            Position startPosition = maze.getStartPosition();

            // print the position
            System.out.println(String.format("Start Position: %s", startPosition)); // format "{row,column}"

            // prints the maze exit position
            System.out.println(String.format("Goal Position: %s", maze.getGoalPosition()));
            return maze;
        }
        ***/
}