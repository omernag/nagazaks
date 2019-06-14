package View;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Maze mazePointer;
    private Solution mazeSolution;
    public boolean gotmaze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int prevRow = 1;
    private int prevCol = 1;
    private GraphicsContext gc;

    //Image
    private StringProperty NotBeenFileName = new SimpleStringProperty();
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty BeenFileName = new SimpleStringProperty();
    private StringProperty SolutionFileName = new SimpleStringProperty();
    //endImage

    public MazeDisplayer() {
        widthProperty().addListener(event -> draw());
        heightProperty().addListener(event -> draw());
    }

    public void setMaze(Maze maze) {
        this.maze = new Maze(maze.toByteArray());
        mazePointer = maze;
        gotmaze = true;
        mazeSolution = null;

    }

    public void setCharacterPosition(int row, int column) {
        //  int prevRow = characterPositionRow;
        //  int prevCol = characterPositionColumn;
        characterPositionRow = row;
        characterPositionColumn = column;
        draw();


        //   move(row,column,prevRow,prevCol);
    }

    public void resetCharacterPosition(int row, int column) {
        int prevRow = characterPositionRow;
        int prevCol = characterPositionColumn;
        characterPositionRow = row;
        characterPositionColumn = column;


        move(row, column, prevRow, prevCol);
    }


    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void move(int currRow, int currCol, int prevRow, int prevCol) {

        try {
            Image beenImage = new Image(new FileInputStream(BeenFileName.get()));
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getLines();
            double cellWidth = canvasWidth / maze.getColumns();

            maze.setValueByInt(prevRow, prevCol, 2);
            gc.drawImage(beenImage, prevCol * cellWidth, prevRow * cellHeight, cellWidth, cellHeight);
            gc.drawImage(characterImage, currCol * cellWidth, currRow * cellHeight, cellWidth, cellHeight);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getLines();
            double cellWidth = canvasWidth / maze.getColumns();

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));

     //           String imageChar =  Configurations.getCharacter();

                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image notBeenImage = new Image(new FileInputStream(NotBeenFileName.get()));
                Image BeenImage = new Image(new FileInputStream(BeenFileName.get()));
                Image SolutionImage = new Image(new FileInputStream(SolutionFileName.get()));

                gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());


                //Draw Maze

                for (int i = 0; i < maze.getLines(); i++) {
                    for (int j = 0; j < maze.getColumns(); j++) {
                        if (maze.getValueByInt(i, j) == 1) {
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        } else if (maze.getValueByInt(i, j) == 0) {
                            gc.drawImage(notBeenImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        } else if (maze.getValueByInt(i, j) == 2) {
                            gc.drawImage(BeenImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        } else if (maze.getValueByInt(i, j) == 3) {
                            gc.drawImage(SolutionImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                this.requestFocus();


                gc.drawImage(BeenImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    //region Properties



    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getBeenFileName() {
        return BeenFileName.get();
    }

    public void setBeenFileName(String beenFileName) {
        this.BeenFileName.set(beenFileName);
    }

    public String getNotBeenFileName() {
        return NotBeenFileName.get();
    }

    public void setNotBeenFileName(String notBeenFileName) {
        this.NotBeenFileName.set(notBeenFileName);
    }


    public String getSolutionFileName() {
        return SolutionFileName.get();
    }

    public void setSolutionFileName(String solutionFileName) {
        this.SolutionFileName.set(solutionFileName);
    }


    public void setSolution(Solution solution) {
        mazeSolution = solution;
        for (AState mazeState : mazeSolution.getSolutionPath()) {
            Position p = ((MazeState) mazeState).getState();
            //  if(maze.getValueByInt(p.getRowIndex(),p.getColumnIndex())!=2){
            maze.setValueByInt(p.getRowIndex(), p.getColumnIndex(), 3);
            //   }
        }
        draw();
    }


    //////////////////////////////////////////
    //dragging and zooming

   /* public void zoomm(ScrollEvent event) {
        double zoomFactor = 1.05;
        double deltaY = event.getDeltaY();

        if (deltaY < 0) {
            zoomFactor = 0.95;
        }
        setScaleX(getScaleX() * zoomFactor);
        setScaleY(getScaleY() * zoomFactor);
        event.consume();
    }*/

    Delta initial_mouse_pos = new Delta();
    class Delta { double x, y; }

    public void zoom(ScrollEvent event) {
        initial_mouse_pos.x = event.getX();
        initial_mouse_pos.y = event.getY();
        double zoom_fac = 1.05;
        double delta_y = event.getDeltaY();

        if(delta_y < 0) {
            zoom_fac = 2.0 - zoom_fac;
        }

        Scale newScale = new Scale();
        newScale.setPivotX(initial_mouse_pos.x);
        newScale.setPivotY(initial_mouse_pos.y);
        newScale.setX( getScaleX() * zoom_fac );
        newScale.setY( getScaleY() * zoom_fac );

        getTransforms().add(newScale);

        event.consume();
    }
}





