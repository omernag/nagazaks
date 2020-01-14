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

/**
 * This class is the model in the MVC, the back-end of the program
 */
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
    private HashSet<String> descWords;

    /**
     * Constactor for MyModel
     */
    public MyModel() {
        //dictionary=new IndexDictionary("",false);
        isStemmer=false;
        postingPathSet=false;
        corpusPathSet=false;
        isFileHandling=false;
        counter=0;
        descWords = new HashSet<>();
    }

    /**
     * Creates Postings from the Courpus
     * @param corpusPath
     * @param postingPath
     */
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
            this.docMD=m.docsMDs;
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

    /**
     * Load Dictionary from memory
     * @throws IOException
     */
    public void bringUpDictionary() throws IOException {
        dictionary.loadDictionary(postingPa,isStemmer);
        Master m = new Master(isStemmer);
        docMD=m.LoadDocMD(postingPa);

    }

    /**
     * Return the output result of documents retrieval from a single query
     * @param ip
     * @param findEntities
     * @param semanticTreat
     * @param trecEval
     * @return
     */
    public String handleSingleQuery(AbstractMap.SimpleEntry<String,String> ip, boolean findEntities, boolean semanticTreat,boolean trecEval) {
        //add semanticTreat to searcher contractor
        searcher = new Searcher(ip.getValue(),isStemmer,findEntities);
        ranker = new Ranker(dictionary,postingPa,isStemmer,docMD,semanticTreat,searcher.queryWords,descWords);
        searcher.rank(ranker);
        if (trecEval){
            trecEval(ip);
        }
        if(findEntities) {
            resultLog=searcher.getResultsStr() + "\n" + searcher.getEntitiesStr();
            return searcher.getResultsStr() + "\n" + searcher.getEntitiesStr();
        }
        //resultLog = searcher
        //missing the full searcher
        resultLog=searcher.getResultsStr();

        return searcher.getResultsStr();

    }


    /**
     * Return the output result of documents retrieval from query file
     * @param queryFilePath
     * @param findEntities
     * @param semanticTreat
     * @param trecEval
     */
    public void handleQueryFile(String queryFilePath, boolean findEntities, boolean semanticTreat,boolean trecEval) {
        //missing the full searcher - get result and stuff
        //do some for loop here
        //but first load the file
        trecEvalStr="";
        List<String> results = new ArrayList<>();
        queryFileName=new File(queryFilePath).getName();
        this.queries = new ArrayList<>();
        isFileHandling=true;
        Document docsFile;
        String res ="";
        try {
            docsFile = Jsoup.parse(new File(queryFilePath), "US-ASCII");
        }
        catch (IOException e){throw new RuntimeException("file opening error");}
        Elements queriesAsElements = docsFile.getElementsByTag("top");
        for(Element queryAsElement : queriesAsElements){
            String queryID="";
            String query ="";
            for(Element innerElement : queryAsElement.getAllElements()){
                if(innerElement.tagName().equals("title")){
                    query += innerElement.text() +" ";
                }
                else if(innerElement.tagName().equals("num")){
                    queryID = ((innerElement.text()).split(" "))[1];
                }
/*
                else if(innerElement.tagName().equals("desc")){
                    query += innerElement.childNodes().get(0).toString().substring(12) +" ";
                    descWords.clear();
                    descWords.addAll(Arrays.asList(innerElement.text().substring(12).split(" ")));
                }
*/

            }
            if(!query.equals("") && !queryID.equals("")){
                this.queries.add(new AbstractMap.SimpleEntry<>(queryID, query));
            }
        }
        for(Map.Entry ip : this.queries){
            res+="******************************************************************************************\n";
            res+="Query number: "+ip.getKey()+"\n";
            res+="Query: "+ip.getValue()+"\n";
            res+=handleSingleQuery((AbstractMap.SimpleEntry<String, String>)ip,findEntities,semanticTreat,trecEval);
            res+="******************************************************************************************\n";

        }
        resultLog = res;
    }

    /**
     * Save the output to a file in memory
     * @param path
     */
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
            //showAlert("Successful save to \n"+file.getPath(), Alert.AlertType.INFORMATION);/////////////////////////////////////////////////////////////////////////////////////////////
        } catch (IOException e) {
            showAlert("Something went terribly wrong, Cant save the file", Alert.AlertType.ERROR);
        }

    }

    /**
     * getter for the result
     * @return
     */
    public String getResultTOPrint() {
        return resultLog;
    }

    /**
     * isStremmer changer for check box
     */
    public void changeStemmerMode() {
        if(!isStemmer){
            isStemmer=true;
        }
        else{
            isStemmer=false;
        }
    }

    /**
     * Resets the Dictionary
     */
    public void forgetDictionary() {
        dictionary=null;
    }

    /**
     * getter for posting path
     * @return postingPa
     */
    public String getPostingPa() {
        return postingPa;
    }

    /**
     * @return the string representation of a dictionary
     */
    public String getDictionaryToPrint() {
        return dictionary.getIndexerPrint();
    }

    /**
     * @return corpus saved path
     */
    public String getCorpusPa() {
        return corpusPa;
    }

    /**
     * @param corpusPa
     */
    public void setCorpusPa(String corpusPa) {
        this.corpusPa = corpusPa;
    }

    /**
     * @return boolean status
     */
    public boolean isStemmer() {
        return isStemmer;
    }

    /**
     * @return IndexDictionary
     */
    public IndexDictionary getDictionary() {
        return dictionary;
    }

    /**
     * @return valuable information on the system run
     */
    public String getInfoOnRun() {
        return infoOnRun;
    }

    /**
     * @param postingPa
     */
    public void setPostingPa(String postingPa) {
        this.postingPa = postingPa;
    }

    /**
     * InitializeDictionary
     */
    public void initializeDictionary() {
        dictionary=new IndexDictionary(postingPa,isStemmer);
    }

    /**
     * @return the result of the retrivieval
     */
    public String getResult() {
        return resultLog;
    }

    /**
     * @param alertMessage
     * @param alertType
     */
    private void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(alertMessage);
        alert.show();
    }

    /**
     * Creates trev eval proper input from result
     * @param ip
     */
    private void trecEval(Map.Entry ip){
        for(DocMD md : searcher.getOrderedDocs()) {
            this.trecEvalStr += ip.getKey().toString() + " 0 " + md.docno + " 1 42.38 mt\n";
        }
    }

    /**
     * @param postingPathSet
     */
    public void setPostingPathSet(boolean postingPathSet) {
        this.postingPathSet = postingPathSet;
    }

    /**
     * @param corpusPathSet
     */
    public void setCorpusPathSet(boolean corpusPathSet) {
        this.corpusPathSet = corpusPathSet;
    }


    /**
     * @param dictionary
     */
    public void setDictionary(IndexDictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * @param stemmer
     */
    public void setStemmer(boolean stemmer) {
        isStemmer = stemmer;
    }
}
