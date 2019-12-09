package Indexer;

import javafx.util.Pair;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

public class TermInDoc {

    private String term;
    private String docNo;
    private int termfq;
    private boolean isBold;
    private boolean isHeader;
    private boolean isEntity;


    //public static ArrayList<Map.Entry<String,Integer>> topTenTerm = new ArrayList<>();
    //public static ArrayList<Map.Entry<String,Integer>> bottomTenTerm = new ArrayList<>();//change to array list for sort
    //use Arrays.sort

    public TermInDoc(String term, String docNo, int termfq, boolean isBold, boolean isHeader, boolean isEntity) {
        this.term = term;
        this.docNo = docNo;
        this.termfq = termfq;
        this.isBold = isBold;
        this.isHeader = isHeader;
        this.isEntity = isEntity;
    }




    public String getDocNo() {
        return docNo;
    }

    public int getTermfq() {
        return termfq;
    }

    public boolean isBold() {
        return isBold;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public TermInDoc(String term, String meta) {
        String[] metaData = meta.split(",");
        this.term = metaData[0];
        this.docNo = metaData[1];
        try {
            this.termfq = Integer.parseInt(metaData[2]);
            this.isBold = Boolean.parseBoolean(metaData[3]);
            this.isHeader = Boolean.parseBoolean(metaData[4]);
            this.isEntity = Boolean.parseBoolean(metaData[5]);
        } catch (Exception e) {
            this.termfq = 0;
            this.isBold = false;
            this.isHeader = false;
            this.isEntity = false;
        }


    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "" + term + "," + docNo + "," + termfq + "," + isBold + "," + isHeader + "," + isEntity;
    }
}
