package IO;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;
    private byte[] savedMazeBytes;

    public MyDecompressorInputStream(InputStream other) {
        in=other;
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        return 0;
    }

    /**
     * Reads some number of bytes from the input stream and stores them into
     * the buffer array <code>b</code>. The number of bytes actually read is
     * returned as an integer.  This method blocks until input data is
     * available, end of file is detected, or an exception is thrown.
     *
     * <p> If the length of <code>b</code> is zero, then no bytes are read and
     * <code>0</code> is returned; otherwise, there is an attempt to read at
     * least one byte. If no byte is available because the stream is at the
     * end of the file, the value <code>-1</code> is returned; otherwise, at
     * least one byte is read and stored into <code>b</code>.
     *
     * <p> The first byte read is stored into element <code>b[0]</code>, the
     * next one into <code>b[1]</code>, and so on. The number of bytes read is,
     * at most, equal to the length of <code>b</code>. Let <i>k</i> be the
     * number of bytes actually read; these bytes will be stored in elements
     * <code>b[0]</code> through <code>b[</code><i>k</i><code>-1]</code>,
     * leaving elements <code>b[</code><i>k</i><code>]</code> through
     * <code>b[b.length-1]</code> unaffected.
     *
     * <p> The <code>read(b)</code> method for class <code>InputStream</code>
     * has the same effect as: <pre><code> read(b, 0, b.length) </code></pre>
     *
     * @param unShrinkedMaze the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or
     * <code>-1</code> if there is no more data because the end of
     * the stream has been reached.
     * @throws IOException          If the first byte cannot be read for any reason
     *                              other than the end of the file, if the input stream has been closed, or
     *                              if some other I/O error occurs.
     * @throws NullPointerException if <code>b</code> is <code>null</code>.
     * @see InputStream#read(byte[], int, int)
     */
    @Override
    public int read(@NotNull byte[] unShrinkedMaze) throws IOException {
/*
        savedMazeBytes=new byte[unShrinkedMaze.length];
        for(int)
        ArrayList<Byte> mazeDeComp = new ArrayList<>();
        //makes a list from the byte array
        for (int i = 6; i < savedMazeBytes.length-1; i++) {
            int amountZ = savedMazeBytes[i];
            while(amountZ>0){
                mazeDeComp.add((byte) 0);
                amountZ--;
            }
            int amountO = savedMazeBytes[i+1];
            while(amountO>0){
                mazeDeComp.add((byte) 1);
                amountO--;
            }
        }

        for (int i = 0 ; i < 6; i++) {
            unShrinkedMaze[i] = mazeInArray[i];
        }

        for (int i = 6 ; i < unShrinkedMaze.length; i++) {
            unShrinkedMaze[i] = mazeDeComp.get(i-6);
        }

        return 0;
    }*/
return
        0;
}
}

/*
ArrayList<Integer> mazeDeComp = new ArrayList<>();
        //makes a list from the byte array
        for (int i = 6; i < savedMazeBytes.length-1; i++) {
            int amountZ = savedMazeBytes[i];
            while(amountZ>0){
                mazeDeComp.add( 0);
                amountZ--;
            }
            int amountO = savedMazeBytes[i+1];
            while(amountO>0){
                mazeDeComp.add(1);
                amountO--;
            }
        }
        int place = 0 ;
        for (int i = 0 ; i < mazeStruct.length; i++){
            for (int j = 0 ; j < mazeStruct[0].length; j++) {
                mazeStruct[i][j] = mazeDeComp.get(place);
                place++;
            }
        }
*/