package IO;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;
    private byte[] shrinkedMaze;

    public MyCompressorOutputStream(OutputStream other) {
        out = other;
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *                     an <code>IOException</code> may be thrown if the
     *                     output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(@NotNull byte[] mazeInArray) throws IOException {
        Writer file = new FileWriter(out.toString());

        ArrayList<Byte> order = new ArrayList<>();
        int zeroCount=0;
        int oneCount=0;
        int place=0;
        while(place<mazeInArray.length){
            while(place<mazeInArray.length && mazeInArray[place] == 0) {
                zeroCount++;
                place++;
            }
            order.add((byte) zeroCount);
            zeroCount = 0;
            oneCount++;
            place++;
            while(place<mazeInArray.length && mazeInArray[place] == 1 ) {
                oneCount++;
                place++;
            }
            order.add((byte) oneCount);
            oneCount = 0;
            zeroCount++;
            place++;
        }
        for(int i = 0 ; i < 6 ; i ++){
            file.write( mazeInArray[i]);
        }
        //file.write(mazeInArray);

        //for (int j = 6; j < shrinkedMaze.length; j++) {
          //  shrinkedMaze[j] = mazeInArray[j-6];
        //}


    }
}
