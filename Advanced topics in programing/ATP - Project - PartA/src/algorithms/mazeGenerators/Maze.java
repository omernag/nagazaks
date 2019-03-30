package algorithms.mazeGenerators;



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

    public Position getPositionAt(int line,int column){
        return new Position(line,column);
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
     * @param  startPosition
     */
    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
        setPath(startPosition);
    }

    /**
     * This method return a random position on the frame of the maze.
     * @return Position
     */
    public Position Get_random_Frame() {
        Position start_p = new Position(0,0);
        while(isCorner(start_p)) {
            int rand = (int) (Math.random() * 4);
            if (rand == 0) { //upper wall
                start_p = new Position(0, (int) (Math.random() * getColumns()));
            }
            else if (rand == 1) { //left wall
                start_p = new Position((int) (Math.random() * getLines()), 0);
            }
            else if (rand == 2) { //bottom wall
                start_p = new Position(getLines() - 1, (int) (Math.random() * getColumns()));
            }
            else { //right wall
                start_p = new Position((int) (Math.random() * getLines()), getColumns() - 1);
            }
        }//while
        return start_p;
    }

    /**
     * This method sets a random position on the frame of the maze as the start position.
     */
    public void Set_random_start() {
        Position start_p = Get_random_Frame();

        setStartPosition(start_p);
    }

    /**
     * This method checks if a position is on one of the maze corners.
     *
     * @param position
     * @return True if position is a corner of the maze, else false.
     */
    public boolean isCorner (Position position){
        if (position.getColumn()==0 || position.getColumn()==getColumns()-1){
            if(position.getLine()==0 || position.getLine()==getLines()-1){
                return true;
            }
        }
        return false;
    }

    /**
     * Getter for the goal position.
     * @return goalPosition
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

    /**
     * Setter for the goal position.
     * @param  endPosition
     */
    public void setGoalPosition(Position endPosition) {
        this.goalPosition = endPosition;
    }

    /**
     * This method returns if a position is a wall or path.
     * @param pointer
     * @return 1 or zero
     */
    public int getValue(Position pointer){
        return struct[pointer.getLine()][pointer.getColumn()];
    }

    /**
     * This method sets a wall on a given position.
     * @param position
     */
    public void setWall(Position position) {
        this.struct[position.getLine()][position.getColumn()] = 1;
    }

    /**
     * This method sets a path on a given position.
     * @param position
     */
    public void setPath(Position position) {
        this.struct[position.getLine()][position.getColumn()] = 0;
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
     * @param position
     * @return True if is inside, false otherwise
     */
    public boolean isInMaze(Position position) {
        if(position.getLine()>=0 &&position.getLine()<getLines() && position.getColumn() >=0 && position.getColumn() <getColumns() ){
            return true;
        }
        return false;
    }

    /**
     * This method return if a position is on the frame of the maze.
     * @param position
     *
     * @return True if is inside, false otherwise
     */
    public boolean is_Farme(Position position) {
        if(position.getLine()==0 ||position.getLine()==getLines()-1 || position.getColumn() ==0 | position.getColumn() ==getColumns()-1 ){
            return true;
        }
        return false;
    }

    /**
     * This method prints the maze to the console
     */
    public void print(){
        for (int i = 0; i < s_lines; i++) {
            for (int j = 0; j < s_columns; j++) {
                System.out.print(struct[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }
}//end of Maze
