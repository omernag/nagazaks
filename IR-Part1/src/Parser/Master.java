package Parser;

import Indexer.TermInDoc;
import Indexer.TermsInDocList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Master {


    private HashMap<String, DocMD> docsMDs;
    private TermsInDocList[] wordsToWrite;
    private ArrayList<DocText> fileTexts;
    private ReadFile rf;
    private Parser parser;

    public Master() {
        docsMDs = new HashMap<>();
        fileTexts = new ArrayList<>();
        parser = new Parser();
        wordsToWrite = new TermsInDocList[20];
        for(int i = 0 ; i<wordsToWrite.length;i++){
            wordsToWrite[i] = new TermsInDocList();
        }
    }

    public void run() throws IOException {
        int counter = 0;
        rf = new ReadFile(".");
        parser.stopwords = rf.readStopWords(new File(rf.getStopWordsPath()));

        long startTimeIndex ;
        long finishTimeIndex;
        for(String filePath : rf.filesPaths){

            //fileTexts.addAll(rf.handleFile(filePath));
            fileTexts= rf.handleFile(filePath);


            startTimeIndex = System.nanoTime();


            for(DocText dt : fileTexts){
                parser = new Parser();
                DocMD doc = parser.handleDoc((dt.getInnerText()),dt.getDocno());
                docsMDs.put(doc.docno,doc);
                for (TermInDoc tid : doc.words.values()){
                    int slot = Math.abs(tid.getTerm().toLowerCase().hashCode()) %20 ;
                    wordsToWrite[slot].getList().add(tid);
                }
                doc.words = null;
                dt = null;
            }
            finishTimeIndex = System.nanoTime();
            System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec to parse file path " + filePath.toString());
            startTimeIndex = System.nanoTime();
            counter++;
           // if(counter%50==0 || counter == rf.filesPaths.size()-1){

                for(int i = 0; i<20; i++){

                    wordsToWrite[i].tidToJson(i);
                    wordsToWrite[i]=new TermsInDocList();
                }
                finishTimeIndex = System.nanoTime();
                System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec to save file");
           // }


        }
    }
}
