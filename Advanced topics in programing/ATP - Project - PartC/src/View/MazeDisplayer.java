package View;

import algorithms.mazeGenerators.Maze;
import com.sun.tracing.dtrace.Attributes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int prevRow = 1;
    private int prevCol = 1;

    private StringProperty NotBeenFileName = new SimpleStringProperty();

    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty BeenFileName = new SimpleStringProperty();



    public void setMaze(Maze maze) {
        this.maze = maze;

        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        int prevRow = characterPositionRow;
        int prevCol = characterPositionColumn;
        characterPositionRow = row;
        characterPositionColumn = column;

        redraw();

        //move(row,column,prevRow,prevCol);
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void move(int currRow,int currCol, int prevRow, int prevCol) {
        try {
            Image beenImage = new Image(new FileInputStream(BeenFileName.get()));
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getLines();
            double cellWidth = canvasWidth / maze.getColumns();

            GraphicsContext gc = getGraphicsContext2D();
            gc.drawImage(beenImage, prevRow * cellHeight, prevCol * cellWidth, cellHeight, cellWidth);
            gc.drawImage(characterImage, currRow * cellHeight, currCol * cellWidth, cellHeight, cellWidth);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getLines();
            double cellWidth = canvasWidth / maze.getColumns();

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image notBeenImage = new Image(new FileInputStream(NotBeenFileName.get()));
                Image BeenImage = new Image(new FileInputStream(BeenFileName.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.getLines(); i++) {
                    for (int j = 0; j < maze.getColumns(); j++) {
                        if (maze.getValueByInt(i,j) == 1) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                        else if (maze.getValueByInt(i,j) == 0) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(notBeenImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(BeenImage, maze.getGoalPosition().getRowIndex() * cellHeight, maze.getGoalPosition().getColumnIndex() * cellWidth, cellHeight, cellWidth);
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
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




    //endregion

}
