package IO;



import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;
    private HashMap<Integer,String> table;
    private HashMap<String,Integer> tableS;

    public MyDecompressorInputStream(InputStream other) {

        in=other;
        table = new HashMap<>();
        for(int i = 0 ; i < 127; i++){
            table.put(i,""+(char)i);
        }
        tableS = new HashMap<>();
        for(int i = 0 ; i < 127; i++){
            tableS.put(""+(char)i,i);
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
        for(int i = 0 ; i < inputStream.length ;i+=2){
            toOpen.add(combineToOneByte(inputStream[i],inputStream[i+1]));
        }

        int spotInTable = 128;
        String curr = toOpen.get(0);
        String next;
        ArrayList<Integer> intList=new ArrayList<>();
        int index=1;
        while(index<(toOpen.size())){
            next=toOpen.get(index);
            index++;
            if(table.containsKey(""+curr+next)){
                curr=curr+next;
            }
            else{
                if(Integer.parseInt(curr)>127){
                    intList.add(tableS.get(Integer.parseInt(curr)));
                    if(Integer.parseInt(next)>127){
                        String halfNext = ""+table.get(Integer.parseInt(next));
                        for(int i = 1; i < halfNext.length(); i++){
                            String first = halfNext.substring(0,i);
                            String second = halfNext.substring(i);
                            if(table.containsKey(Integer.parseInt(first)) && table.containsKey(Integer.parseInt(second))){
                                halfNext = first;
                                break;
                            }
                        }
                        table.put(spotInTable,curr+halfNext);
                        tableS.put(curr+next,spotInTable);
                    }
                    else{
                        table.put(spotInTable,curr+next);
                        tableS.put(curr+next,spotInTable);
                    }
                }
                else{
                    intList.add(Integer.valueOf(table.get(Integer.valueOf(curr))));
                    table.put(spotInTable,curr+next);
                    tableS.put(curr+next,spotInTable);
                }

                curr=next;
                spotInTable++;
            }
        }

        return 0;
    }

    private String combineToOneByte(byte times, byte left){
        int first= (times*255) ;
        int second = left & 0xff;
        int whole= first+second ;
        return ""+whole;
    }

}