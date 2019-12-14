package Indexer;



import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



@SuppressWarnings("ALL")
public class TermsInDocList {

    private LinkedList<TermInDoc> list;
    public static int COUNT=0;
    private int listNum;
    public static int[] insideCounter=new int[20];


    public TermsInDocList() {
        this.list = new LinkedList<>();
        COUNT++;
        this.listNum=COUNT;
    }

    public TermsInDocList(LinkedList<TermInDoc> list) {
        this.list = list;
    }

    public TermsInDocList(int i) {
        this.list = new LinkedList<>();
    }

    public LinkedList<TermInDoc> getList() {
        return list;
    }

    public void setList(LinkedList<TermInDoc> list) {
        this.list = list;
    }

    public void tidToJson(int i) throws IOException {
        JSONObject tid = new JSONObject();

        for (TermInDoc term: list) {
            tid.put(insideCounter[i],term.toString());
            insideCounter[i]=insideCounter[i]+1;
        }
        try{

            Files.write(Paths.get("tTj-" + i + ".txt"),(tid.toJSONString()+"\n").getBytes(),Files.exists(Paths.get("tTj-" + i + ".txt")) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);

        }
        catch (IOException e){

        }
    }

    /* maybe a constructor */
    public LinkedList<TermInDoc> JsonToTid(String location) throws FileNotFoundException {
        try {
            JSONParser parser = new JSONParser();
            LinkedList<TermInDoc> termList = new LinkedList<>();
            List<String> jsonList = Files.readAllLines(Paths.get(location));
            JSONObject fromMem;
            for(String jsonObj : jsonList) {
                fromMem = (JSONObject)parser.parse(jsonObj);
                for (Object term : fromMem.entrySet()) {
                    TermInDoc newTerm = new TermInDoc((String) ((Map.Entry) term).getKey(), (String) ((Map.Entry) term).getValue());
                    termList.add(newTerm);
                }
            }

            return termList;
        }
        catch (IOException e){

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void testToSave(TermsInDocList tidl) throws IOException {
        System.out.println(tidl.list.toString());
        tidl.tidToJson(999);
        TermsInDocList nTidl=new TermsInDocList();
        nTidl.setList(JsonToTid("tTj-"+999));
        System.out.println(nTidl.list.toString());

    }
}
