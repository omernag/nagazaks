package Indexer;


import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class TermsInDocList {

    private LinkedList<TermInDoc> list;
    public static int[] insideCounter = new int[20];

    public TermsInDocList() {
        this.list = new LinkedList<>();
    }

    public void tidToJson(int i) throws IOException {
        JSONObject tid = new JSONObject();
        for (TermInDoc term : list) {
            tid.put(insideCounter[i], term.toString());
            insideCounter[i] = insideCounter[i] + 1;
        }
        Files.write(Paths.get("./Segment/tTj-" + i + ".txt"), (tid.toJSONString() + "\n").getBytes(), Files.exists(Paths.get("./Segment/tTj-" + i + ".txt")) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }

    public LinkedList<TermInDoc> JsonToTid(String location) throws IOException {
        JSONParser parser = new JSONParser();
        LinkedList<TermInDoc> termList = new LinkedList<>();
        List<String> jsonList = Files.readAllLines(Paths.get(location));
        JSONObject fromMem;
        for (String jsonObj : jsonList) {
            try {
                fromMem = (JSONObject) parser.parse(jsonObj);
                for (Object term : fromMem.entrySet()) {
                    termList.add(new TermInDoc((String) ((Map.Entry) term).getValue()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return termList;

    }

    public LinkedList<TermInDoc> getList() {
        return list;
    }

    public void setList(LinkedList<TermInDoc> list) {
        this.list = list;
    }
}
