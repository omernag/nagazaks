package EngineUserInterface;

import Indexer.IndexDictionary;
import Indexer.SegmentProcesses;
import Parser.DocMD;
import Parser.Master;
import javafx.scene.control.Alert;
//import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchRank.Ranker;
import searchRank.Searcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MyModel {
    private IndexDictionary dictionary;
    private String corpusPa;
    private String postingPa;
    public boolean isStemmer;
    private String infoOnRun;
    private HashMap<String, DocMD> docMD;
    private String resultLog;
    private Searcher searcher;
    private Ranker ranker;

    List<Map.Entry<String,String>> queries;
    private String trecEvalStr="";

    private boolean postingPathSet;
    private boolean corpusPathSet;
    private boolean isFileHandling;
    private int counter;
    private String queryFileName;

    public MyModel() {
        //dictionary=new IndexDictionary("",false);
        isStemmer=false;
        postingPathSet=false;
        corpusPathSet=false;
        isFileHandling=false;
        counter=0;
    }

    public void connectToCorpus(String corpusPath, String postingPath) {
        try {
            corpusPa = corpusPath;
            postingPa = postingPath;
            long startTimeIndex = System.nanoTime();
            Master m = new Master(isStemmer);
            m.run(isStemmer, corpusPath);
            SegmentProcesses sgm = new SegmentProcesses(isStemmer, postingPath);
            m.updateEntities(sgm.getTheDictionary().indexer);
            m.saveDocMD(postingPath);
            long finishTimeIndex = System.nanoTime();
            dictionary = sgm.getTheDictionary();
            infoOnRun = "RunTime Information:\n" + "Number of indexed docs: " + m.getDocAmount() + "\n" +
                    "Number of unique Terms in dictionary: " + dictionary.getNumOfUniqueTerms() + "\n" +
                    "Total time: " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec";
        }
        catch (IOException e){
            System.out.println("failed somehow");
        }
    }

    public void bringUpDictionary() throws IOException {
        dictionary.loadDictionary(postingPa,isStemmer);
        Master m = new Master(isStemmer);
        docMD=m.LoadDocMD(postingPa);

    }

    public String handleSingleQuery(String currentQuery, boolean findEntities, boolean semanticTreat) {
        //add semanticTreat to searcher contractor
        searcher = new Searcher(currentQuery,isStemmer,findEntities);
        ranker = new Ranker(dictionary,postingPa,isStemmer,docMD,semanticTreat,searcher.queryWords);
        searcher.rank(ranker);
        if(findEntities) {
            resultLog=searcher.getResultsStr() + "\n" + searcher.getEntitiesStr();
            return searcher.getResultsStr() + "\n" + searcher.getEntitiesStr();
        }
        //resultLog = searcher
        //missing the full searcher
        resultLog=searcher.getResultsStr();
        return searcher.getResultsStr();
    }


    public void handleQueryFile(String queryFilePath, boolean findEntities, boolean semanticTreat,boolean trecEval) {
        //missing the full searcher - get result and stuff
        //do some for loop here
        //but first load the file
        List<String> results = new ArrayList<>();
        queryFileName=new File(queryFilePath).getName();
        this.queries = new ArrayList<>();
        isFileHandling=true;
        Document docsFile;
        String res ="";
        try {
            docsFile = Jsoup.parse(new File(queryFilePath), "US-ASCII");
        }
        catch (IOException e){throw new RuntimeException("file opening error");} ;
        Elements queriesAsElements = docsFile.getElementsByTag("top");
        for(Element queryAsElement : queriesAsElements){
            String queryID="";
            String query ="";
            for(Element innerElement : queryAsElement.getAllElements()){
                if(innerElement.tagName().equals("title")){
                    query += innerElement.text() ;//+" ";
                }
                else if(innerElement.tagName().equals("num")){
                    queryID = ((innerElement.text()).split(" "))[1];
                }
                /*
                else if(innerElement.tagName().equals("desc")){
                    query += innerElement.text().substring(12) +" ";

                }*/
            }
            if(!query.equals("") && !queryID.equals("")){
                this.queries.add(new AbstractMap.SimpleEntry<>(queryID, query));
            }
        }
        for(Map.Entry ip : this.queries){
            res+="******************************************************************************************\n";
            res+="Query number: "+ip.getKey()+"\n";
            res+="Query: "+ip.getValue()+"\n";
            res+=handleSingleQuery(ip.getValue().toString(),findEntities,semanticTreat);
            res+="******************************************************************************************\n";
            if (trecEval){
                trecEval(ip);
            }
        }
        resultLog = res;
    }

    public void saveResult(String path) {
        try {
            File file;
            if(isFileHandling){
                if(isStemmer){
                    file = new File(path+"/"+queryFileName.substring(0,queryFileName.length()-4)+"_s");
                }
                else{
                    file = new File(path+"/"+queryFileName.substring(0,queryFileName.length()-4));
                }
            }
            else{
                if(isStemmer){
                    file = new File(path+"/query_"+counter+"_s");
                }
                else{
                    file = new File(path+"/query_"+counter);
                }
            }
            counter++;

            if(!trecEvalStr.equals("")) {
                Files.write(Paths.get(file.getPath()+"_TREC.txt"), trecEvalStr.getBytes());
            }
            else{
                Files.write(Paths.get(file.getPath()+".txt"), resultLog.getBytes());
            }
            showAlert("Successful save to \n"+file.getPath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Something went terribly wrong, Cant save the file", Alert.AlertType.ERROR);
        }

    }

    public String getTrecEvalStr() {
        return trecEvalStr;
    }

    public String getResultTOPrint() {
        return resultLog;
    }

    public void changeStemmerMode() {
        if(!isStemmer){
            isStemmer=true;
        }
        else{
            isStemmer=false;
        }
    }

    public void forgetDictionary() {
        dictionary=null;
    }

    public String getPostingPa() {
        return postingPa;
    }

    public String getDictionaryToPrint() {
        return dictionary.getIndexerPrint();
    }

    public String getCorpusPa() {
        return corpusPa;
    }

    public void setCorpusPa(String corpusPa) {
        this.corpusPa = corpusPa;
    }

    public boolean isStemmer() {
        return isStemmer;
    }

    public IndexDictionary getDictionary() {
        return dictionary;
    }

    public String getInfoOnRun() {
        return infoOnRun;
    }

    public void setPostingPa(String postingPa) {
        this.postingPa = postingPa;
    }

    public void initializeDictionary() {
        dictionary=new IndexDictionary(postingPa,isStemmer);
    }

    public String getResult() {
        return resultLog;
    }

    private void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void trecEval(Map.Entry ip){
        for(DocMD md : searcher.getOrderedDocs()) {
            this.trecEvalStr += ip.getKey().toString() + " 0 " + md.docno + " 1 42.38 mt\n";
        }
    }

    public void setPostingPathSet(boolean postingPathSet) {
        this.postingPathSet = postingPathSet;
    }

    public void setCorpusPathSet(boolean corpusPathSet) {
        this.corpusPathSet = corpusPathSet;
    }

    public boolean isPostingPathSet() {
        return postingPathSet;
    }

    public boolean isCorpusPathSet() {
        return corpusPathSet;
    }
}
