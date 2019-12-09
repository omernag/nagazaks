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
import java.util.Map;



@SuppressWarnings("ALL")
public class TermsInDocList {

    private ArrayList<TermInDoc> list;
    public static int COUNT=0;
    private int listNum;


    public TermsInDocList() {
        this.list = new ArrayList<>();
        COUNT++;
        this.listNum=COUNT;
    }

    public TermsInDocList(ArrayList<TermInDoc> list) {
        this.list = list;
    }

    public TermsInDocList(int i) {
        this.list = new ArrayList<>();
    }

    public ArrayList<TermInDoc> getList() {
        return list;
    }

    public void setList(ArrayList<TermInDoc> list) {
        this.list = list;
    }

    public void tidToJson() throws IOException {
        JSONObject tid = new JSONObject();
        int index = 0;
        for (TermInDoc term: list) {
            tid.put(index,term.toString());
            index++;
        }
        try{
            Files.write(Paths.get("tTj-" + listNum + ".txt"),tid.toJSONString().getBytes());

        }
        catch (IOException e){

        }
    }

    /* maybe a constructor */
    public ArrayList<TermInDoc> JsonToTid(String location) throws FileNotFoundException {
        try {
            FileReader reader = new FileReader(location);
            JSONParser parser = new JSONParser();
            ArrayList<TermInDoc> termList = new ArrayList<>();
            JSONObject fromMem = (JSONObject)parser.parse(reader);
            for ( Object term: fromMem.entrySet()) {
                TermInDoc newTerm = new TermInDoc((String)((Map.Entry)term).getKey(),(String)((Map.Entry)term).getValue());
                termList.add(newTerm);
            }
            reader.close();
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
        tidl.tidToJson();
        TermsInDocList nTidl=new TermsInDocList();
        nTidl.setList(JsonToTid("tTj-"+tidl.listNum));
        System.out.println(nTidl.list.toString());

    }
}
