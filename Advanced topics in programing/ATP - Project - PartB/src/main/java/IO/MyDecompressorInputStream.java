package IO;




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
    public int read(byte[] deCompMaze) throws IOException {

        ArrayList<Integer> readerList = new ArrayList<>();
        int reader = 0 ;
        int readerSize = in.available();
        int index=0;
        while(index<readerSize){
            reader = in.read();
            readerList.add(reader);
            index++;
        }

        byte[] inputStream = new byte[readerList.size()];
        for(int i = 0 ; i < inputStream.length; i++){
            inputStream[i]=readerList.get(i).byteValue();
        }

        byte[] tmpResult=new byte[deCompMaze.length];
        decompressLZW(tmpResult,inputStream);
        for(int i = 0 ; i < deCompMaze.length; i++){
            deCompMaze[i]=tmpResult[i];
        }

        return 0;
    }


    public void decompressLZW(byte[] result, byte[] inputStream){
        ArrayList<String> toOpen = new ArrayList<>();
        for (int i = 0; i < inputStream.length; i += 2) {
            toOpen.add(combineToOneByte(inputStream[i], inputStream[i + 1]));
        }

        String translation;
        int spotInTable = 256;
        String curr = toOpen.get(0);
        String next;
        String S;
        String C = "0";

        translation=table.get(Integer.parseInt(curr));
        int index = 1;
        while (index < (toOpen.size())) {
            next = toOpen.get(index);
            index++;
            if(!table.containsKey(Integer.parseInt(next))){
                S = table.get(Integer.parseInt(curr));
                S = S + C;
            }
            else{
                S = table.get(Integer.parseInt(next));
            }

            translation+=S;
            C = ""+S.charAt(0);
            table.put(spotInTable,table.get(Integer.parseInt(curr))+C);
            tableS.put(curr+C,spotInTable);
            curr=next;
            spotInTable++;
        }

        ArrayList<Integer> list = new ArrayList<>();
        String[] finalS = translation.substring(30).split("~");
        for (int i = 0; i < 30+finalS.length; i ++) {
            if(i<30) {
                list.add(Integer.parseInt("" + translation.charAt(i)));
            }
            else{
                list.add(Integer.parseInt(finalS[i-30]));
            }
        }
        ArrayList<Integer> fixed;
        fixed=toEightByte(list,result.length);
        for(int i = 0 ; i < fixed.size() && i < result.length; i ++){
            result[i] = fixed.get(i).byteValue();
        }
    }


    private ArrayList<Integer> toEightByte(ArrayList<Integer> list,int byteArraySize){
        ArrayList <Integer> ans = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){
            if(i<30){
                ans.add((int) list.get(i).byteValue());
            }
            else{
                String binaryRep = Integer.toBinaryString(list.get(i));
                if(i==list.size()-1){
                    int leftToFill = byteArraySize-ans.size();
                    int addZero = leftToFill-binaryRep.length();

                    for(int j = 0; j < leftToFill; j++ ) {
                        if (j < addZero) {
                            ans.add(0);
                        } else {
                            ans.add(Integer.parseInt("" + binaryRep.charAt(j-addZero)));
                        }
                    }
                    break;
                }
                else {
                    String binary8 = "";
                    if (binaryRep.length() < 8) {
                        int miss = 8 - binaryRep.length();
                        for (int k = 0; k < miss; k++) {
                            binary8 += 0;
                        }
                        for (int k = miss; k < 8; k++) {
                            binary8 += binaryRep.charAt(k - miss);
                        }
                        for (int j = 0; j < binary8.length(); j++) {
                            ans.add(Integer.parseInt("" + binary8.charAt(j)));
                        }
                    }
                    else{
                        for (int j = 0; j < binaryRep.length(); j++) {
                            ans.add(Integer.parseInt("" + binaryRep.charAt(j)));
                        }
                    }
                }
            }
        }
        return ans;
    }


    private String combineToOneByte(byte times, byte left){
        int first= (times & 0xff)*255 ;
        int second = left & 0xff;
        int whole= first+second ;
        return ""+whole;
    }

    private String byteToString(byte[] from){
        String to = "";
        for(int j = 0 ; j < from.length; j++){
            to += (char)from[j];
        }
        return to;
    }

}