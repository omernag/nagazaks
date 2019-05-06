package IO;

import com.sun.istack.internal.NotNull;
import java.io.*;
import java.util.*;


public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;
    private HashMap<String,Integer> table;

    public MyCompressorOutputStream(OutputStream other)
    {
        out = other;
        table = new HashMap<>();
        for(int i = 0 ; i < 127; i++){
            table.put(""+(char)i,i);
        }
    }


    @Override
    public void write(int b) throws IOException {

    }



    @Override
    public void write(@NotNull byte[] bArray) throws IOException {

        Byte[] inputArr = toSingleByte(bArray);
        int spotInTable = 128;
        String curr = inputArr[0].toString();
        String next;
        ArrayList<String> output=new ArrayList<>();
        int index=1;
        while(curr!=inputArr[inputArr.length-1].toString()){
            next=inputArr[index].toString();
            index++;
            if(table.containsValue(""+curr+next)){
                curr=curr+next;
            }
            else{
                output.add(""+table.get(next));
                table.put(curr+next,spotInTable);
                curr=next;
                spotInTable++;
            }
        }

        ArrayList<Byte> toByte = new ArrayList<>();
        //Byte[] ans = new Byte[output.length()];
        for(int i = 0 ; i < output.size();i++){
            index = Integer.parseInt(output.get(i));
            fixByteLimit(toByte,index);
            /*if(index>255){
                while(index>255){
                    toByte.add((byte) 255);
                    toByte.add((byte) '~');
                    index-=255;
                }
                toByte.add((byte) index);
            }
            else{
                toByte.add((byte) index);
            }*/
        }

        //writing to output using out
        for(int i = 0 ;  i < toByte.size(); i++){
            out.write(toByte.get(i));
        }

    }
       /* int size = mazeInArray.length+2;

        byte[] allMaze = new byte[size];
        allMaze[0] = (byte) ('^');
        for(int i = 0 ; i < mazeInArray.length; i++){
            allMaze[i+1] = mazeInArray[i];
        }
        allMaze[allMaze.length-1] = (byte)('|');

        byte[][] table = new byte[size][size];
        LinkedList<byte[]> tablel = new LinkedList<>();
        //HashMap<Integer,byte[]> table = new HashMap<>();
        table[0]=allMaze;
        //table.put(0,allMaze);
        int spot = 1;

        for(int i = 0 ; i < allMaze.length-1; i++){
            byte[] rotation = copyArray(table[i]);
            //byte[] rotation = copyArray(table.get(i));
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
            //table.put(spot,rotation);
            spot++;
        }

        selectionSort(table);
        //selectionSort(table.values());

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
*/


    private Byte[] toSingleByte(byte[] bytesArr){//move to maze with omer
        ArrayList<Byte> shrinked = new ArrayList<>();
        Byte[] toShrink =  new Byte[8];
        for(int i=30 ; i < bytesArr.length;i++){
            if(i+7<bytesArr.length){
                Byte[] last=new Byte[8];
                int zeroes = 8-(bytesArr.length-i);
                int place=0;
                while(place < zeroes){
                    last[place]=0;
                    place++;

                }
                while( i < bytesArr.length){
                    last[place]=bytesArr[i];
                    place++;
                    i++;
                }
                double sum=0;
                for (int j = 0 ; j < 8 ; j++){
                    sum+=last[j]*Math.pow(2,j);
                }
                shrinked.add((byte) sum);
            }
            else{
                int place=0;
                while(i<i+8){
                    toShrink[place]=bytesArr[i];
                    place++;
                    i++;
                }
                double sum=0;
                for (int j = 0 ; j < 8 ; j++){
                    sum+=toShrink[j]*Math.pow(2,j);
                }
                shrinked.add((byte) sum);
            }
        }
        Byte[] result = new Byte[shrinked.size()+30];
        for (int j = 0 ; j < result.length ; j++){
            if(j<30){
                result[j]=bytesArr[j];
            }
            else result[j]=shrinked.get(j-30);
        }
        return result;
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