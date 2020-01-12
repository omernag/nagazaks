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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.Searcher;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class Ranker {
    IndexDictionary dictionary;
    String postingPath;
    boolean stem;
    HashMap<String, DocMD> relevantDocs;
    HashMap<String, Term> terms;
    //HashMap<String, Double> termsFromSemantic;
    ArrayList<String> termsFromSemantic;
    HashMap<String, DocMD> docMDs;
    HashMap<String,Double> idf;
    public PriorityQueue<DocMD> okapiRank;
    public PriorityQueue<DocMD> cosineSimilarityRank;
    Double avgLength;
    boolean semanticTreatment ;
    HashSet<String> queryWords;
    HashSet<String> descWords;


    public Ranker(IndexDictionary dictionary, String postingPath, boolean stem, HashMap<String, DocMD> docMDs, boolean semanticTreatment, HashSet<String> queryWords, HashSet<String> descWords) {
        Comparator<DocMD> comp = (o1, o2) -> (int) (o2.getRank() - o1.getRank());

        this.dictionary = dictionary;
        this.postingPath = postingPath;
        this.stem = stem;
        this.relevantDocs = new HashMap<>();
        this.termsFromSemantic=new ArrayList<>();
        this.docMDs=docMDs;
        this.idf = new HashMap<>();
        this.okapiRank = new PriorityQueue<>(comp);
        this.cosineSimilarityRank = new PriorityQueue<>(comp);
        this.avgLength = calcAvgLength();
        this.semanticTreatment = semanticTreatment;
        this.queryWords=queryWords;
        this.descWords=descWords;

    }

    private void updatequerywords() {
        HashSet<String> updatedWords = new HashSet<>(queryWords);
        updatedWords.addAll(termsFromSemantic);
        for(String word:queryWords){
            updatedWords.add(word.toLowerCase());
           // updatedWords.add(word.toUpperCase());
        }
        queryWords=updatedWords;
    }

    private double calcAvgLength() {
        double ans = 0;
        for (DocMD doc : docMDs.values()) {
            ans+=doc.docSize;
        }
        return (ans/docMDs.size());
    }

    public void handleQuery() {
        relevantDocs = new HashMap<>();
        terms = new HashMap<>();
        if(semanticTreatment){semanticTreat();}
        updatequerywords();
        for(String name : queryWords){
            if (dictionary.indexer.containsKey(name)) {
                Term term = new Term(name);
                String[] termData = dictionary.indexer.get(name).split(",");
                String[] termLocation = termData[2].split("@");
                List<String> termList;
                int numDocs = Integer.parseInt(termData[0]);
                int termLine = Integer.parseInt(termLocation[1]);
                double idfcalc=Math.log((docMDs.size() - numDocs + 0.5) / (numDocs + 0.5))/Math.log(2);
                if(idfcalc<0){idfcalc=0;}
                idf.put(name,idfcalc);
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
        Bm25Rank( 0.9,0.7);
        //saveRelevantDocs();
    }



    public void Bm25Rank(double k,double b){
        for(DocMD doc :relevantDocs.values()){
            double docRank = 0;
            int numOfMatchedTerm = 0;
            for(Term term : terms.values()){

                if (term.termDocs.containsKey(doc.docno)) {
                    TermInDoc tid = term.termDocs.get(doc.docno);
                    int termfq = tid.termfq;
                    double docTermRank = (idf.get(term.getName()))*((termfq*(k+1))/(termfq+(k*(1-b+(b*(doc.docSize/avgLength))))));
                    if(tid.isHeader()){
                        docTermRank=docTermRank*1.5;
                    }
                    if(tid.isEntity()){
                        docTermRank=docTermRank*1.5;
                    }
                    if(termsFromSemantic.contains(term.getName())){
                        docTermRank=docTermRank*0.01;
                    }
                    if(descWords.contains(term.getName())){
                        docTermRank=docTermRank*0.5;
                    }
                    docRank+= docTermRank;
                    numOfMatchedTerm++;
                }
            }

            doc.rank=docRank*numOfMatchedTerm;
            okapiRank.add(doc);
        }
    }


    private void semanticTreat() {
        List<String> commonW = new LinkedList<>();
        for (String word : queryWords
        ) {
            List<String> curr =fetchFromWord2Vec(word);
            if (curr != null) {
                commonW.addAll(curr);
            }
        }
        termsFromSemantic.addAll(commonW);
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
                    if ((Long) wordJson.get("score") > 68000) {
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

    public List<String> fetchFromWord2Vec(String word){
        try {
            List<String> returnList = new LinkedList<>();
            Word2VecModel word2vec = Word2VecModel.fromTextFile(new File((System.getProperty("user.dir")+("/target/classes/word2vec.txt"))));
            Searcher  word2vecSearcher = word2vec.forSearch();

            List<Searcher.Match> commons = word2vecSearcher.getMatches(word,3);

            for (Searcher.Match commonWord: commons
            ) {
                if(!commonWord.match().equals(word) && commonWord.distance()>0.8) {
                    returnList.add(commonWord.match());
                }
            }
            return returnList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Searcher.UnknownWordException e) {
        }
        return null;
    }
    /////

    public void saveRelevantDocs(){
        try {
            StringBuilder sb = new StringBuilder();
            for (String st: relevantDocs.keySet()
                 ) {
                sb.append(st+"\n");
            }
            Files.write(Paths.get("C:/Users/Asi Zaks/Desktop/08 Trec_eval/releventdocs.txt"),sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
