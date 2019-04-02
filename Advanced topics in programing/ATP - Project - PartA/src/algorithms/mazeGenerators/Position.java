package algorithms.mazeGenerators;

import java.util.Objects;

/**
 * This class represent a single position
 *
 * @author  Asaf Zaks, Omer Nagar
 *
 */
public class Position {
    private int line;
    private int column;
    private int value;

    public void setValue(int value) {
        if(value == 0 || value == 1)
            this.value = value;
    }

    /**
     * constructor for a single position.
     * @param line
     * @param column
     */
    public Position(int line, int column) {
        this.line = line;
        this.column = column;
        this.value=1;
    }

    public Position(int line, int column, int value) {
        this.line = line;
        this.column = column;
        this.value = value;
    }

    /**
     * Getter for the line param of the position.
     * @return line
     */
    public int getRowIndex() {
        return line;
    }

    /**
     * Setter for the line param of the position.
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * Getter for the columns param of the position.
     * @return column
     */
    public int getColumnIndex() {
        return column;
    }

    /**
     * Setter for the columns param of the position.
     */
    public void setColumn(int column) {
        this.column = column;
    }


    /**
     * This methods check if 2 positions are the same.
     * @return true if same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return line == position.line &&
                column == position.column;
    }

    /**
     * This methods print to string a position.
     * @return string as {line,columns}
     */
    @Override
    public String toString() {
        return "{" +line +","+column+"}";
    }
}
