package IO;



import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;
    private HashMap<Integer, String> table;
    private HashMap<String, Integer> tableS;

    public MyDecompressorInputStream(InputStream other) {

        in = other;
        table = new HashMap<>();
        for (int i = 0; i < 255; i++) {
            table.put(i, "" + (char) i);
        }
        tableS = new HashMap<>();
        for (int i = 0; i < 255; i++) {
            tableS.put("" + (char) i, i);
        }
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(@NotNull byte[] deCompMaze) throws IOException {

        int size = in.read();
        byte[] inputStream = new byte[size];
        in.read(inputStream);


        ArrayList<String> toOpen = new ArrayList<>();
        for (int i = 0; i < inputStream.length; i += 2) {
            if (i == 0) {
                toOpen.add(combineToOneByte(inputStream[i], inputStream[i + 1]));
            }
            toOpen.add(combineToOneByte(inputStream[i], inputStream[i + 1]));
        }

        int spotInTable = 256;
        String curr = toOpen.get(0);
        String next;
        ArrayList<Integer> intList = new ArrayList<>();
        int index = 1;
        while (index < (toOpen.size())) {
            next = toOpen.get(index);
            index++;
            if (table.containsKey("" + curr + "," + next)) {
                curr = curr + next;
            }
            else {
                if (Integer.parseInt(curr) > 255 && Integer.parseInt(next) > 255) {
                    String[] toSplit = (table.get(Integer.parseInt(curr)).split(","));
                    for (int i = 0; i < toSplit.length; i++) {
                        intList.add(Integer.parseInt((toSplit[i])));//48,48 problem
                    }

                    String halfNext = getFirstPart(next);//??reqursive?
                    table.put(spotInTable, table.get(Integer.parseInt(curr)) + "," + halfNext);
                    tableS.put(table.get(curr) + "," + halfNext, spotInTable);
                }
                else if(Integer.parseInt(curr) > 255){
                    String[] toSplit = (table.get(Integer.parseInt(curr)).split(","));
                    for (int i = 0; i < toSplit.length; i++) {
                        intList.add(Integer.parseInt((toSplit[i])));//48,48 problem
                    }
                    table.put(spotInTable, table.get(Integer.parseInt(curr)) + "," + next);
                    tableS.put(table.get(curr) + "," + next, spotInTable);
                }
                else if(Integer.parseInt(next) > 255){
                    intList.add(Integer.valueOf(curr));
                    table.put(spotInTable, curr + "," + table.get(Integer.parseInt(next)));
                    tableS.put(curr + "," + table.get(next), spotInTable);
                }
                else{
                    intList.add(Integer.valueOf(curr));
                    table.put(spotInTable, curr + "," + next);
                    tableS.put(curr + "," + next, spotInTable);
                }
                curr = next;
                spotInTable++;
                ///////////////////////////////
                /*if (Integer.parseInt(curr) > 255) {
                    String[] toSplit = (table.get(Integer.parseInt(curr)).split(","));
                    for (int i = 0; i < toSplit.length; i++) {
                        intList.add(Integer.parseInt((toSplit[i])));//48,48 problem
                    }
                } else {
                    intList.add(Integer.valueOf(curr));
                }

                if (Integer.parseInt(next) > 255) {
                    String halfNext = getFirstPart(next);
                    table.put(spotInTable, curr + "," + halfNext);
                    tableS.put(curr + "," + next, spotInTable);
                }
                else {
                    table.put(spotInTable, curr + "," + next);
                    tableS.put(curr + "," + next, spotInTable);
                }*/

                //////////////////////////////////////
            }
        }

        ArrayList<Integer> intListBasic = new ArrayList<>();
        for (int i = 0; i < intList.size(); i++) {
            intListBasic.add(tableS.get(intList.get(i)));
        }


        return 0;
    }

    private String getFirstPart(String next){
        String halfNext = "" + table.get(Integer.parseInt(next));
        int firstPartSpot = 0;
        while (firstPartSpot < halfNext.length()) {
            if (halfNext.charAt(firstPartSpot) == ',') {
                break;
            }
            firstPartSpot++;
        }
        halfNext = halfNext.substring(0, firstPartSpot);
        if(Integer.parseInt(halfNext)>255){
            return getFirstPart(halfNext);
        }
        else return halfNext;
    }

    private String combineToOneByte(byte times, byte left){
        int first= (times*255) ;
        int second = left & 0xff;
        int whole= first+second ;
        return ""+whole;
    }

}