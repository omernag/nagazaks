package Indexer;

import java.util.ArrayList;

public class IndexEntryValue {
    private int docFq;
    private int totalFq;
    private String postingPath;

    public IndexEntryValue(int inDocFq, int inTotalFq, String inPostingPath) {
        docFq = inDocFq;
        totalFq = inTotalFq;
        postingPath = inPostingPath;
    }

    public IndexEntryValue(Term trm, String path) {
        //this.term = trm.getName();
        docFq = trm.getDocFq();
        totalFq = trm.getTotalFq();
        postingPath = path;
    }

    @Override
    public String toString() {
        return "docFq=" + docFq +
                        ", totalFq=" + totalFq +
                        ", postingPath='" + postingPath + '\'';
    }
}
