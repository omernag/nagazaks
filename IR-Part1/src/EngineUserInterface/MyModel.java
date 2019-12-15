package EngineUserInterface;

import Indexer.IndexDictionary;
import Indexer.SegmentProcesses;
import Parser.Master;

import java.io.IOException;
import java.nio.file.Path;

public class MyModel {
    private IndexDictionary dictionary;
    private String corpusPa;
    private String postingPa;
    public boolean isStemmer;
    private String infoOnRun;

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

    public void connectToCorpus(String corpusPath, String postingPath) throws IOException {
        corpusPa=corpusPath;
        postingPa=postingPath;
        long startTimeIndex = System.nanoTime();
        Master m = new Master();
        m.run(isStemmer,corpusPath);
        SegmentProcesses sgm = new SegmentProcesses(isStemmer,postingPath);
        long finishTimeIndex = System.nanoTime();
        dictionary=sgm.getTheDictionary();
        infoOnRun="RunTime Information:\n"+"Number of indexed docs: "+m.getDocAmount()+"\n"+
                "Number of unique Terms in dictionary: "+dictionary.getNumOfUniqueTerms()+"\n"+
                "Total time: " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec";
    }

    public String getDictionaryToPrint() {
        return dictionary.getIndexerPrint();
    }

    public void bringUpDictionary() throws IOException {
        dictionary.loadDictionary(postingPa,isStemmer);
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
