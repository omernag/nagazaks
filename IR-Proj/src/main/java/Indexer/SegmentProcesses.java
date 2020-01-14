package Indexer;

import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class take segments of terms from the corpus and iteratively indexing them
 */
public class SegmentProcesses {

    private HashMap<String, Term> termL;
    private IndexDictionary theDictionary;
    private String postingPath;
    private boolean stemmer;

    /**
     * @param stemmed
     * @param postingPath
     * @throws IOException
     */
    public SegmentProcesses(boolean stemmed,String postingPath) throws IOException {
        this.termL = new HashMap<>();
        this.postingPath=postingPath;
        stemmer=stemmed;
        processCorpus();
    }

    /**
     * The main function of this class
     * Indexing each segment
     * @throws IOException
     */
    public void processCorpus() throws IOException {

        TermsInDocList fromJson = new TermsInDocList();
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
        File segmentFolder = new File(path+"/Segment/");
        theDictionary = new IndexDictionary(postingPath,stemmer);
        for (File json : segmentFolder.listFiles()) {
            if (json.getName().contains("tTj")) {
                System.out.println("stating: " + json.getName());
                long sTime = System.nanoTime();
                fromJson.setList(fromJson.JsonToTid(json.getPath()));
                processSegment(fromJson);
                theDictionary.createIndexer(termL);
                termL.clear();
                long fTime = System.nanoTime();
                System.out.println("Time:  " + (fTime - sTime) / 60000000000.0 + "min");
                Files.delete(Paths.get(json.getPath()));
            }

        }
        theDictionary.saveToDisk();

    }

    /**
     * Index a single Segment file
     * @param termsList
     */
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
        updateEntities();
    }

    /**
     * Check that a entity occurs more than once in the corpus
     */
    private void updateEntities() {
        Set<String> keys = new HashSet<>(termL.keySet());
        for (String key : keys) {
            if(termL.get(key).isEntity() && termL.get(key).getDocFq()==1){
                termL.remove(key);
            }
        }
    }

    /**
     * add an observed occurrence to the term list
     * @param name
     * @param docNum
     * @param termfq
     * @param header
     * @param entity
     */
    private void addOccurrenceToTerm(String name, String docNum, int termfq, boolean header, boolean entity) {
        if (termL.containsKey(name)) {
            termL.get(name).addOccurrence(docNum, termfq, header, entity);
        }
    }

    /**
     * Calculate the total doc frequency of all terms
     */
    private void updateDocFreq() {
        for (Term trm : termL.values()) {
            trm.updateDocFq();
        }
    }

    /**
     * Check if a term always show up with a capital first letter,
     * and if so turns it to all caps
     */
    private void updateCaseToUpper() {
        Set<String> keys = new HashSet<>(termL.keySet());
        for (String key : keys) {
            if (termL.get(key).updateToUpperCase()) {
                Term t = termL.remove(key);
                termL.put(t.getName(), t);
            }
        }
    }

    /**
     * @return Dictionary
     */
    public IndexDictionary getTheDictionary() {
        return theDictionary;
    }
}


