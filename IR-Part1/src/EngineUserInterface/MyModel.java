package EngineUserInterface;

import Indexer.IndexDictionary;
import Indexer.SegmentProcesses;
import Parser.DocMD;
import Parser.Master;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class MyModel {
    private IndexDictionary dictionary;
    private String corpusPa;
    private String postingPa;
    public boolean isStemmer;
    private String infoOnRun;
    private HashMap<String, DocMD> docMD;

    public String getPostingPa() {
        return postingPa;
    }



    public MyModel() {
        dictionary=new IndexDictionary("",false);
        isStemmer=false;
    }

    public IndexDictionary getDictionary() {
        return dictionary;
    }

    public String getInfoOnRun() {
        return infoOnRun;
    }

    public void connectToCorpus(String corpusPath, String postingPath) {
        try {

            corpusPa = corpusPath;
            postingPa = postingPath;
            long startTimeIndex = System.nanoTime();
            Master m = new Master(isStemmer);
            m.run(isStemmer, corpusPath);
            SegmentProcesses sgm = new SegmentProcesses(isStemmer, postingPath);
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

    public String getDictionaryToPrint() {
        return dictionary.getIndexerPrint();
    }

    public void bringUpDictionary() throws IOException {
        dictionary.loadDictionary(postingPa,isStemmer);
        Master m = new Master(isStemmer);
        docMD=m.LoadDocMD(postingPa);

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
}
