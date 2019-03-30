package algorithms.mazeGenerators;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates mazes according to the Prim algorithm.
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */

public class MyMazeGenerator extends AMazeGenerator {
    private List <Position> walls;

    /**
     * constructor for this type of maze
     */
    public MyMazeGenerator() {
        walls = new<Position> ArrayList();
    }

    /**
     * This method generates mazes according to the Prim algorithm.
     *
     * @param lines Number of lines
     * @param columns  Number of columns
     * @return Maze This returns a solvable maze
     */
    @Override
    public Maze generate(int lines, int columns) {
       Maze primMaze = new Maze(lines,columns);
       primMaze.setAllWalls();
       int s_line = (int) ( Math.random() * lines);
       int s_column = (int) ( Math.random() * columns);
       Position curr_Pos = new Position(s_line,s_column);
       primMaze.setPath(curr_Pos);
       //walls.add(curr_Pos);

       int rand;
       Position up = new Position(curr_Pos.getLine() - 1, curr_Pos.getColumn());
       Position left = new Position(curr_Pos.getLine(), curr_Pos.getColumn() - 1);
       Position down = new Position(curr_Pos.getLine() + 1, curr_Pos.getColumn());
       Position right = new Position(curr_Pos.getLine(), curr_Pos.getColumn() + 1);
        if (primMaze.isInMaze(up))
            walls.add(up);
        if (primMaze.isInMaze(left))
            walls.add(left);
        if (primMaze.isInMaze(right))
            walls.add(right);
        if (primMaze.isInMaze(down))
            walls.add(down);


       while(!walls.isEmpty()) {
           rand = (int) (Math.random() * walls.size() );
           curr_Pos = walls.get(rand);
           boolean path = true;

           up = new Position(curr_Pos.getLine() - 1, curr_Pos.getColumn());
           left = new Position(curr_Pos.getLine(), curr_Pos.getColumn() - 1);
           down = new Position(curr_Pos.getLine() + 1, curr_Pos.getColumn());
           right = new Position(curr_Pos.getLine(), curr_Pos.getColumn() + 1);

           if(primMaze.isInMaze(up) && primMaze.isInMaze(down)){
               if(primMaze.getValue(up)==0 && primMaze.getValue(down)==0 ){
                   path= false;
               }
           }
           if(primMaze.isInMaze(left) && primMaze.isInMaze(right)){
               if(primMaze.getValue(left)==0 && primMaze.getValue(right)==0 ){
                   path= false;
               }
           }
           if(primMaze.isInMaze(up) && primMaze.isInMaze(left)){
               if(primMaze.getValue(up)==0 && primMaze.getValue(left)==0 ){
                   path= false;
               }
           }
           if(primMaze.isInMaze(left) && primMaze.isInMaze(down)){
               if(primMaze.getValue(left)==0 && primMaze.getValue(down)==0 ){
                   path= false;
               }
           }
           if(primMaze.isInMaze(up) && primMaze.isInMaze(right)){
               if(primMaze.getValue(up)==0 && primMaze.getValue(right)==0 ){
                   path= false;
               }
           }
           if(primMaze.isInMaze(down) && primMaze.isInMaze(right)){
               if(primMaze.getValue(down)==0 && primMaze.getValue(right)==0 ){
                   path= false;
               }
           }


           if(path==true){
                primMaze.setPath(curr_Pos);

               if (primMaze.isInMaze(up) && primMaze.getValue(up)==1)
                   walls.add(up);
               if (primMaze.isInMaze(left) && primMaze.getValue(left)==1)
                   walls.add(left);
               if (primMaze.isInMaze(right) && primMaze.getValue(right)==1)
                   walls.add(right);
               if (primMaze.isInMaze(down) && primMaze.getValue(down)==1)
                   walls.add(down);

           }
           walls.remove(rand);
       }
       Position entry = primMaze.Get_random_Frame();
       Position exit = primMaze.Get_random_Frame();
       boolean ready=false;
       while(!ready){
           if(primMaze.getValue(entry)==0 && primMaze.getValue(exit)==0 && !entry.equals(exit))
           {
               primMaze.setStartPosition(entry);
               primMaze.setGoalPosition(exit);
               ready=true;
           }
           else{
               if(primMaze.getValue(entry)==0) {
                   primMaze.setStartPosition(entry);
               }
               else
                   entry = primMaze.Get_random_Frame();

               if(primMaze.getValue(exit)==0) {
                   primMaze.setGoalPosition(exit);
               }
               else
                   exit = primMaze.Get_random_Frame();
           }
       }




       return primMaze;
    }

    private boolean canPlace(Maze primMaze, Position curr_Pos) {

        return true;
    }
}
