package Indexer;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SegmentProcesses {

    private HashMap<String, Term> termL;
    private IndexDictionary theDictionary;
    private String postingPath;
    private boolean stemmer;
    /*private Hashtable<String, Integer> termsDf = new Hashtable<>();*/

    public HashMap<String, Term> getTermL() {
        return termL;
    }

    public SegmentProcesses(boolean stemmed,String postingPath) throws IOException {
        this.termL = new HashMap<>();
        this.postingPath=postingPath;
        stemmer=stemmed;
        processCorpus();
    }

    public void processCorpus() throws IOException {

        TermsInDocList fromJson = new TermsInDocList(1);
        String path = ".";
        if(stemmer){
            File trmFile = new File(postingPath+"/Posting_s");
            if(!trmFile.exists()){
                trmFile.mkdir();
            }
        }
        else{
            File trmFile = new File(postingPath+"/Posting");
            if(!trmFile.exists()){
                trmFile.mkdir();
            }
        }
        File postingFolder = new File(path);
        theDictionary = new IndexDictionary(postingPath,stemmer);
        for (File json : postingFolder.listFiles()) {
            if (json.getName().contains("tTj-11")) {
                System.out.println("stating: " + json.getName());
                long sTime = System.nanoTime();
                fromJson.setList(fromJson.JsonToTid(json.getPath()));
                processSegment(fromJson);
                theDictionary.createIndexer(termL);
                termL.clear();
                long fTime = System.nanoTime();
                System.out.println("Time:  " + (fTime - sTime) / 60000000000.0 + "min");
            }
            //Files.delete(json.getPath());
        }
        theDictionary.saveToDisk();


    }

    private void processSegment(TermsInDocList termsList) {
        for (TermInDoc termIn : termsList.getList()) {
            if (termIn.getTerm().length() > 1 && termIn.getDocNo() != null) {
                if (termL.containsKey(termIn.getTerm())) {//segment in place
                    addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isHeader(), termIn.isEntity());

                } else if (termL.containsKey(termIn.getTerm().toLowerCase())) {
                    addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isHeader(), termIn.isEntity());
                    termIn.updateCapsToLower();

                } else if (termL.containsKey(termIn.getTerm().substring(0, 1).toUpperCase() + termIn.getTerm().substring(1))) {

                    addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isHeader(), termIn.isEntity());
                    termIn.updateCapsToLower();

                } else {
                    Term newTerm = new Term(termIn);
                    termL.put(newTerm.getName(), newTerm);
                }
            }
        }
        updateCaseToUpper();
        updateDocFreq();
    }


    public IndexDictionary getTheDictionary() {
        return theDictionary;
    }

    private void addOccurrenceToTerm(String name, String docNum, int termfq, boolean header, boolean entity) {
        if (termL.containsKey(name)) {
            termL.get(name).addOccurrence(docNum, termfq, header, entity);
        }
    }

    private void updateDocFreq() {
        for (Term trm : termL.values()) {
            trm.updateDocFq();
        }
    }

    private void updateCaseToUpper() {
        Set<String> keys = new HashSet<>(termL.keySet());
        for (String key : keys) {
            if (termL.get(key).updateToUpperCase()) {
                Term t = termL.remove(key);
                termL.put(t.getName(), t);
            }
        }
    }
}


