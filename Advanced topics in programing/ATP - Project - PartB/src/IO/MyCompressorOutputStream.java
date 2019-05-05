package IO;

import com.sun.istack.internal.NotNull;
import java.io.*;
import java.util.ArrayList;


public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;


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

    private byte[] stringToByte(String from){
        byte[] to = new byte[from.length()];
        for(int j = 0 ; j < from.length(); j++){
            to[j] = (byte) from.charAt(j);
        }
        return to;
    }

    private String byteToString(byte[] from){
        String to = "";
        for(int j = 0 ; j < from.length; j++){
            to += from[j];
        }
        return to;
    }

    @Override
    public void write(@NotNull byte[] mazeInArray) throws IOException {

        int size = mazeInArray.length+2;

        byte[] allMaze = new byte[size];
        allMaze[0] = (byte) ('^');
        for(int i = 0 ; i < mazeInArray.length; i++){
            allMaze[i+1] = mazeInArray[i];
        }
        allMaze[allMaze.length-1] = (byte)('|');

        byte[][] table = new byte[size][size];
        table[0]=allMaze;
        int spot = 1;

        for(int i = 0 ; i < allMaze.length-1; i++){
            byte[] rotation = copyArray(table[i]);
            byte tmp = rotation[rotation.length-1];
            byte hold;
            byte prev = rotation[0];
            for(int j = 1 ; j < rotation.length; j++){
                hold = rotation[j];
                rotation[j] = prev;
                prev = hold;

            }
            rotation[0]=tmp;
            table[spot] = rotation;
            spot++;
        }

        selectionSort(table);

        byte[] toComp = new byte[table.length];
        for(int i = 0 ;  i < toComp.length; i++){
            toComp[i] = table[i][toComp.length-1];
        }

        //run length
        ArrayList<Byte> order = new ArrayList<>();
        int zeroCount = 0;
        int oneCount=0;
        int place=0;
        while(place<toComp.length){
            if(toComp[place]==1 || toComp[place]==0) {
                while (place < toComp.length && toComp[place] == 0) {
                    zeroCount++;
                    place++;
                }
                fixByteLimit(order,zeroCount);
                zeroCount = 0;
                oneCount++;
                place++;
                while (place < toComp.length && toComp[place] == 1) {
                    oneCount++;
                    place++;
                }
                fixByteLimit(order,oneCount);
                oneCount = 0;
                zeroCount++;
                place++;
            }
            else{
                order.add((byte) '}');
                fixByteLimit(order,toComp[place]);
                order.add((byte) '}');
                place++;
            }
        }





        //writing to output using out
        for(int i = 0 ;  i < order.size(); i++){
            out.write(order.get(i));
        }
    }


    private void fixByteLimit(ArrayList<Byte> order ,int num){
        if(num<=255){
            order.add((byte) num);
        }
        else {
            int sum = num;

            while (sum >= 255) {
                order.add((byte) 255);
                order.add((byte) '~');
                sum -= 255;
            }
            order.add((byte) sum);
        }

    }


    private byte[] copyArray(byte[] bytes) {
        byte[] newB = new byte[bytes.length];
        for(int i = 0 ; i < newB.length; i++){
            newB[i] = bytes[i];
        }
        return newB;
    }

    private int compareTo(byte[] left, byte[] right) {
        for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
            int a = (left[i] & 0xff);
            int b = (right[j] & 0xff);
            if (a != b) {
                return a - b;
            }
        }
        return left.length - right.length;
    }

    private void swap(byte[][] arr, int i, int j) {
        if (i != j) {
            byte[] temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public void selectionSort(byte[][] toSort) {
        for (int i = 0; i < toSort.length - 1; i++) {
            // find index of smallest element
            int smallest = i;
            for (int j = i + 1; j < toSort.length; j++) {
                if (compareTo(toSort[j],toSort[smallest])<=0) {
                    smallest = j;
                }
            }

            swap(toSort, i, smallest);  // swap smallest to front
        }
    }

}


/* int currNum = toComp[0] ;
        int numCount=1;
        for(int place = 1 ; place<toComp.length ; place++){
            if(toComp[place]!=currNum){
                order.add((byte) currNum);
                order.add((byte) numCount);
                currNum=toComp[place];
                numCount=1;
            }
            else{
                numCount++;
            }
        }*/