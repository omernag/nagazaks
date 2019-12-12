package Indexer;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class SegmentProcesses {

    private ArrayList<Term> termL;
    private IndexDictionary theDictionary;
    /*private Hashtable<String, Integer> termsDf = new Hashtable<>();*/

    public ArrayList<Term> getTermL() {
        return termL;
    }

    public SegmentProcesses() throws IOException {
        this.termL = new ArrayList<>();
        processCorpus();
    }

    public void processCorpus() throws IOException {
        int i = 1;
        TermsInDocList fromJson = new TermsInDocList(1);
        while (i <= TermsInDocList.COUNT) {
            fromJson.setList(fromJson.JsonToTid("tTj-" + i + ".txt"));
            processSegment(fromJson);
            Files.delete(Paths.get("tTj-" + i + ".txt"));
            i++;
        }
        theDictionary = new IndexDictionary(termL);

    }

    private void processSegment(TermsInDocList termsList) {
        ArrayList<String> markTerms = new ArrayList<>();
        for (TermInDoc termIn : termsList.getList()) {
            if (markTerms.contains(termIn.getTerm())) {//segment in place
                addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isBold(), termIn.isHeader(), termIn.isEntity());

            } else if (markTerms.contains(termIn.getTerm().toLowerCase())) {
                addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isBold(), termIn.isHeader(), termIn.isEntity());
                termIn.updateCapsToLower();
            }
            else if(markTerms.contains(termIn.getTerm().substring(0,1).toUpperCase()+termIn.getTerm().substring(1))){
                addOccurrenceToTerm(termIn.getTerm(), termIn.getDocNo(), termIn.getTermfq(), termIn.isBold(), termIn.isHeader(), termIn.isEntity());
                termIn.updateCapsToLower();
            }
            else {
                Term newTerm = new Term(termIn);
                termL.add(newTerm);
                markTerms.add(newTerm.getName());
            }
        }
        updateCaseToUpper();
        updateDocFreq();
    }



    public IndexDictionary getTheDictionary() {
        return theDictionary;
    }

    private void addOccurrenceToTerm(String name, String docNum, int termfq, boolean bold, boolean header, boolean entity) {
        for (Term term : termL) {
            if (term.getName().equals(name)) {
                term.addOccurrence(docNum, termfq, bold, header, entity);
            }
        }
    }

    private void updateDocFreq() {
        for (Term trm : termL) {
            trm.updateDocFq();
        }
    }

    private void updateCaseToUpper() {
        for (Term trm : termL) {
            trm.updateToUpperCase();
        }
    }
}


