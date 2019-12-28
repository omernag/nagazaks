package EngineUserInterface;

import Indexer.IndexDictionary;
import Indexer.SegmentProcesses;
import Parser.DocMD;
import Parser.Master;


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

    public MyModel() {
        //dictionary=new IndexDictionary("",false);
        isStemmer=false;
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

    public void bringUpDictionary() throws IOException {
        dictionary.loadDictionary(postingPa,isStemmer);
        //Master m = new Master(isStemmer);
        //docMD=m.LoadDocMD(postingPa);

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
}
