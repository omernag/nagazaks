package EngineUserInterface;

import Indexer.IndexDictionary;

import java.io.IOException;
import java.nio.file.Path;

public class MyModel {
    private IndexDictionary dictionary;
    private String corpusPa;
    private String postingPa;

    public String getPostingPa() {
        return postingPa;
    }



    public MyModel() {
        dictionary=new IndexDictionary();
    }

    public IndexDictionary getDictionary() {
        return dictionary;
    }

    public void connectToCorpus(String corpusPath, String postingPath) {

        //activate omers part
        corpusPa=corpusPath;
        postingPa=postingPath;
    }

    public String getDictionaryToPrint() {
        return dictionary.getIndexerPrint();
    }

    public void bringUpDictionary() throws IOException {
        //dictionary=dictionary.createIndexFromPosting(postingPa);
        dictionary.setIndexerPrint(dictionary.printDictionary());
    }
}
