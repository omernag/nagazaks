package algorithms.mazeGenerators;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a maze as 2 dimensions int array.
 * Contains the maze methods
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class Maze {

    private int[][] struct;
    private int s_lines;
    private int s_columns;
    private Position startPosition;
    private Position goalPosition;

    /**
     * constructor for a generic maze
     * @param lines number of lines
     * @param columns number of columns
     */
    public Maze(int lines, int columns) {
        s_lines = lines;
        s_columns = columns;
        struct = new int[lines][columns];
        startPosition = new Position(0,0);
        goalPosition = new Position(0,0);
    }


    public Maze(byte[] savedMazeBytes) {

        int rowsNum = savedMazeBytes[0];
        int columnsNum = savedMazeBytes[1];

        int startR = savedMazeBytes[2];
        int startC = savedMazeBytes[3];

        int goalR = savedMazeBytes[4];
        int goalC = savedMazeBytes[5];

        int[][] mazeStruct = new int[rowsNum][columnsNum];


        int place=6;
        for (int i = 0 ; i < mazeStruct.length; i++){
            for (int j = 0 ; j < mazeStruct[0].length; j++) {
                mazeStruct[i][j] = savedMazeBytes[place];
                place++;
            }
        }

        s_lines = rowsNum;
        s_columns = columnsNum;
        struct = mazeStruct;
        startPosition = new Position(startR,startC);
        goalPosition = new Position(goalR,goalC);

    }

    public Position getPositionAt(int line,int column){
        if(!isInMaze(new Position(line,column))){
            return new Position(line,column);
        }
        else
        {
            return new Position(line,column,struct[line][column]);
        }


    }

    /**
     * Getter for the number of lines.
     * @return num of lines
     */
    public int getLines() {
        return s_lines;
    }

    /**
     * Getter for the number of columns.
     * @return num of columns
     */
    public int getColumns() {
        return s_columns;
    }

    /**
     * Getter for the start position.
     * @return Position
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Setter for the start position.
     * @param  startPosition i,j of beginning
     */
    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
        //setPath(startPosition);
    }

    /**
     * Setter for the goal position.
     * @param  endPosition goal i,j
     */
    public void setGoalPosition(Position endPosition) {
        this.goalPosition = endPosition;
    }


    /**
     * This method sets a random position on the frame of the maze as the start position.
     */
    public void Set_random_start() {
        Position start_p = Get_random_Frame();

        setStartPosition(start_p);
    }



    /**
     * This method return a random position on the frame of the maze.
     * @return Position
     */
    public Position Get_random_Frame() {
        Position start_p = new Position(0,0);
        int location;
        while(isCorner(start_p)) {
            int rand = (int) (Math.random() * 4);
            if (rand == 0) { //upper wall
                location =  (int) (Math.random() * getColumns());
                start_p = new Position(0,location,struct[0][location]);
            }
            else if (rand == 1) { //left wall
                location =(int) (Math.random() * getLines());
                start_p = new Position(location, 0,struct[location][0]);
            }
            else if (rand == 2) { //bottom wall
                location =(int) (Math.random() * getColumns());
                start_p = new Position(getLines() - 1, location,struct[getLines()-1][location]);
            }
            else { //right wall
                location =(int) (Math.random() * getLines());
                start_p = new Position(location, getColumns() - 1,struct[location][getColumns()-1]);
            }
        }//while
        return start_p;
    }

    /**
     * This method checks if a position is on one of the maze corners.
     *
     * @param position i,j of corner
     * @return True if position is a corner of the maze, else false.
     */
    private boolean isCorner (Position position){
        if (position.getColumnIndex()==0 || position.getColumnIndex()==getColumns()-1){
            return (position.getRowIndex()==0 || position.getRowIndex()==getLines()-1);
        }
        return false;
    }

    /**
     * Getter for the goal position.
     * @return goalPosition goal i,j
     */
    public Position getGoalPosition() {
        return goalPosition;
    }


    /**
     * This method returns if a position is a wall or path.
     * @param pointer i,j
     * @return 1 or zero
     */
    public int getValue(Position pointer){
        return struct[pointer.getRowIndex()][pointer.getColumnIndex()];
    }

    /**
     * This method returns if a position is a wall or path.
     * @param  i,j
     * @return 1 or zero
     */
    public int getValueByInt(int i, int j){
        return struct[i][j];
    }

    /**
     * This method sets a wall on a given position.
     * @param position i, j
     */
    public void setWall(Position position) {
        this.struct[position.getRowIndex()][position.getColumnIndex()] = 1;
    }

    /**
     * This method sets a path on a given position.
     * @param position i, j
     */
    public void setPath(Position position) {
        this.struct[position.getRowIndex()][position.getColumnIndex()] = 0;
        position.setValue(0);
    }

    /**
     * This method sets all the positions in the maze as walls.
     */
    public void setAllWalls(){ //turn everything to 1
        for (int i = 0; i < s_lines; i++) {
            for (int j = 0; j < s_columns; j++) {
                setWall(new Position(i,j));
            }
        }
    }
    /**
     * This method sets random walls around the maze.
     */
    public void setRandomWalls(){ //set walls and paths random
        for (int i = 0; i < s_lines; i++) {
            for (int j = 0; j < s_columns; j++) {
                if(is_Farme(new Position(i,j))){
                    setWall(new Position(i, j));
                }
                else {
                    if (((int) (Math.random() * 4)) == 0) {
                        setPath(new Position(i, j));
                    } else {
                        setWall(new Position(i, j));
                    }
                }
            }//second for
        }//first for
    }

    /**
     * This method return if a position is in the maze.
     * @param position i , j
     * @return True if is inside, false otherwise
     */
    public boolean isInMaze(Position position) {
        return (position.getRowIndex()>=0 &&position.getRowIndex()<getLines() && position.getColumnIndex() >=0 && position.getColumnIndex() <getColumns() );
    }

    /**
     * This method return if a position is on the frame of the maze.
     * @param position i,j
     *
     * @return True if is inside, false otherwise
     */
    public boolean is_Farme(Position position) {
        return (position.getRowIndex()==0 ||position.getRowIndex()==getLines()-1 || position.getColumnIndex() ==0 | position.getColumnIndex() ==getColumns()-1 );
    }

    /**
     * This method prints the maze to the console
     */
    public void print(){
        for (int i = 0; i < s_lines; i++) {
            for (int j = 0; j < s_columns; j++) {
                if(i==startPosition.getRowIndex() && j==startPosition.getColumnIndex()){
                    System.out.print("S");
                }
                else if(i==goalPosition.getRowIndex() && j==goalPosition.getColumnIndex()){
                    System.out.print("E");
                }
                else {
                    System.out.print(struct[i][j]);
                }
                System.out.print(",");
            }
            System.out.println();
        }
    }

    public byte[] toByteArray() {//not commpresed

        ArrayList<Byte> mazeInArray = new ArrayList<>();
        for (int i = 0 ; i < struct.length; i++) {
            for (int j = 0; j < struct[0].length; j++) {
                mazeInArray.add((byte) struct[i][j]);
            }
        }

        byte[] toByte  = new byte[mazeInArray.size()+6];
        toByte[0] = (byte) s_lines;
        toByte[1] = (byte) s_columns;
        toByte[2] = (byte) startPosition.getRowIndex();
        toByte[3] = (byte) startPosition.getColumnIndex();
        toByte[4] = (byte) goalPosition.getRowIndex();;
        toByte[5] = (byte) goalPosition.getColumnIndex();

        for (int j = 6; j < toByte.length; j++) {
            toByte[j] = mazeInArray.get(j-6);
        }

        return toByte;
    }



/*
    public void print () {
        for (int i = 0; i < struct.length; i++) {
            for (int j = 0; j < struct[i].length; j++) {
                if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex()) {//startPosition
                    System.out.print(" " + "\u001B[44m" + " ");
                } else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex()) {//goalPosition
                    System.out.print(" " + "\u001B[44m" + " ");
                } else if (struct[i][j] == 1) System.out.print(" " + "\u001B[45m" + " ");
                else System.out.print(" " + "\u001B[107m" + " ");
            }
            System.out.println(" " + "\u001B[107m");
        }

    }
*/


}//end of Maze
