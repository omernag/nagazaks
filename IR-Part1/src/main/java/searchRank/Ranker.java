package searchRank;

import Indexer.IndexDictionary;
import Indexer.Term;
import Indexer.TermInDoc;
import Parser.DocMD;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Ranker {
    IndexDictionary dictionary;
    String postingPath;
    boolean stem;
    HashMap<String, DocMD> relevantDocs;
    HashMap<String, Term> terms;
    HashMap<String, DocMD> docMDs;
    HashMap<String,Double> idf;
    HashMap<String,Double> okapiRank;
    Double avgLength;


    //////
    HashMap<String, TermInDoc> query;
    boolean semanticTreatment = true;
    //////

    public Ranker(IndexDictionary dictionary, String postingPath, boolean stem, HashMap<String, DocMD> docMDs) {
        this.dictionary = dictionary;
        this.postingPath = postingPath;
        this.stem = stem;
        relevantDocs = new HashMap<>();
        this.docMDs = docMDs;
        idf = new HashMap<>();
        this.postingPath=postingPath;
        this.stem=stem;
        this.relevantDocs = new HashMap<>();
        this.docMDs=docMDs;
        this.idf = new HashMap<>();
        this.avgLength = calcAvgLength();
        this.okapiRank = new HashMap<>();
    }

    private double calcAvgLength() {
        double ans = 0;
        for (DocMD doc : docMDs.values()) {
            ans+=doc.docSize;
        }
        return (ans/docMDs.size());
    }

    public double handleQuery(HashMap<String, TermInDoc> queryWords) {
        relevantDocs = new HashMap<>();
        terms = new HashMap<>();
        for(TermInDoc tid : queryWords.values()){
            String name = tid.getTerm();
            if (dictionary.indexer.containsKey(name)) {
                Term term = new Term(name);
                String[] termData = dictionary.indexer.get(name).split(",");
                String[] termLocation = termData[2].split("@");
                List<String> termList;
                int numDocs = Integer.parseInt(termData[0]);
                int termLine = Integer.parseInt(termLocation[1]);
                idf.put(name, new Double((docMDs.size() - numDocs + 0.5) / (numDocs + 0.5)));
                try {
                    if (stem) {
                        termList = Files.readAllLines(Paths.get(postingPath + "/Posting_s/" + termLocation[0] + ".txt"));
                    } else {
                        termList = Files.readAllLines(Paths.get(postingPath + "/Posting/" + termLocation[0] + ".txt"));
                    }
                    for (int i = 1; i <= numDocs; i++) {
                        String docTermDic = termList.get(i + termLine);
                        String[] arr = docTermDic.split(",");
                        String docno = arr[0];
                        term.addOccurrence(docno, Integer.parseInt(arr[1]), Boolean.parseBoolean(arr[2]), Boolean.parseBoolean(arr[3]));
                        DocMD dmd = docMDs.get(docno);
                        if (!relevantDocs.containsKey(docno)) {
                            relevantDocs.put(docno, dmd);
                        }
                        //dmd.words.put(name);
                    }
                    term.updateDocFq();
                    terms.put(term.getName(), term);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                }
        }
        return 0;
    }

    public void Bm25Rank(HashMap<String, TermInDoc> queryWords,double k,double b){
        for(DocMD doc :relevantDocs.values()){
            double docRank = 0;
            for(String termStr : queryWords.keySet()){
                Term term = terms.get(termStr);

                if (term.termDocs.containsKey(doc.docno)) {
                    TermInDoc tid = term.termDocs.get(doc.docno);
                    int termfq = tid.termfq;
                    docRank+= idf.get(termStr)*((termfq*(k+1))/(termfq+k*(1-b+(b*(doc.docSize/this.avgLength)))));
                }
            }
            okapiRank.put(doc.docno,docRank);
        }
    }

    /////
    private HashMap<String, TermInDoc> semanticTreat(HashMap<String, TermInDoc> queryWords) {
        for (Map.Entry word : queryWords.entrySet()
        ) {
            List<String> commonW = fetchFromWeb((String) word.getValue());
            if (commonW != null) {

            }
        }
        return null;
    }

    public List<String> fetchFromWeb(String word) {
        LinkedList<String> list = new LinkedList<>();
        URL datamuse = null;
        try {
            datamuse = new URL("https://api.datamuse.com/words?ml=" + word);
            URLConnection yc = datamuse.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            JSONParser parser = new JSONParser();
            JSONArray jArray = (JSONArray) parser.parse(in);
            int count = 0;
            for (int i = 0; i < jArray.size(); i++) {
                if (count < 4) {
                    JSONObject wordJson = (JSONObject) jArray.get(i);
                    if ((Long) wordJson.get("score") > 80000) {
                        list.add((String) wordJson.get("word"));
                        count++;
                    }
                }
                else break;
            }

            in.close();
            return list;
        } catch (IOException e) {
            System.out.println("Word not found or no internet connection");
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /////
}
