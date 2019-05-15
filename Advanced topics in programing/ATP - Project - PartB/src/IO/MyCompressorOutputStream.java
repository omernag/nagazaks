package IO;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;
    private HashMap<String,Integer> table;

    public MyCompressorOutputStream(OutputStream other)
    {
        out = other;
        table = new HashMap<>();
        for(int i = 0 ; i < 255; i++){
            table.put(""+(char)i,i);
        }
    }

    @Override
    public void write(int b) throws IOException {

    }

    public byte[] compress (byte[] bArray){
        int[] inputArr = toSingleByte(bArray);
        String inputS="";
        for(int i = 0 ; i < inputArr.length; i++){
            if (i > 29) {
                inputS+=inputArr[i]+"~";
            }
            else {
                inputS += inputArr[i];
            }
        }

        int spotInTable = 256;
        String curr = ""+inputArr[0];
        String next;
        ArrayList<String> output=new ArrayList<>();
        int index=1;
        while(index<(inputS.length())){
            next=""+inputS.charAt(index);
            index++;
            if(table.containsKey(""+curr+next)){
                curr=curr+next;
            }
            else{
                output.add(""+table.get(curr));
                table.put(curr+next,spotInTable);
                curr=next;
                spotInTable++;
            }
        }
        output.add(""+table.get(curr));

        byte[] toByte = new byte[output.size()*2];
        for(int i = 0 ; i < output.size();i++){
            splitToTwoByte(toByte,output.get(i),i);
        }

        return toByte;
    }


    @Override
    public void write(byte[] bArray) throws IOException {

        byte[] toByte = compress(bArray);

        //writing to output using out
        out.write(toByte);
    }

    private void splitToTwoByte(byte[] toByte, String num, int place){
        if(Integer.parseInt(num)<65025) {
            int whole = Integer.parseInt(num);
            byte first = (byte) (whole / 255);
            byte second = (byte) (whole % 255);

            toByte[place * 2] = first;
            toByte[place * 2 + 1] = second;
        }
        else splitToFourByte(toByte,num,place);
    }

    private void splitToFourByte(byte[] toByte, String num, int place) {
        int whole = Integer.parseInt(num);
        int firstDev = whole/255;
        int firstMod = whole%255;
        byte first = (byte) firstDev;
        byte second = (byte) firstMod;
        byte third = (byte) (firstDev/255);
        byte fourth = (byte) (firstDev % 255);

        toByte[place * 2] = first;
        toByte[place * 2 + 1] = second;
        toByte[place * 2 + 2] = third;
        toByte[place * 2 + 3] = fourth;
    }

    private int[] toSingleByte(byte[] bytesArr){//move to maze with omer
        ArrayList<Byte> shrinked = new ArrayList<>();
        Byte[] toShrink =  new Byte[8];
        int i=30;
        while(i < bytesArr.length){
            if(i+7>bytesArr.length-1){
                Byte[] last=new Byte[8];
                int zeroes = 8-(bytesArr.length-i);
                int place=0;
                while(place < zeroes){
                    last[7-place]=0;
                    place++;

                }
                while( i < bytesArr.length){
                    last[7-place]=bytesArr[i];
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
                while(place<8){
                    toShrink[7-place]=bytesArr[i];
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
        int[] result = new int[shrinked.size()+30];
        for (int j = 0 ; j < result.length ; j++){
            if(j<30){
                result[j]=bytesArr[j] & 0xFF;
            }
            else result[j]=shrinked.get(j-30) & 0xFF;
        }
        return result ;
    }
}