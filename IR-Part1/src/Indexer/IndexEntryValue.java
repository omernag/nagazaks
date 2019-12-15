package Indexer;

import java.util.ArrayList;

public class IndexEntryValue {
    private int docFq;
    private int totalFq;
    private int postingPathName;

    public IndexEntryValue(int inDocFq, int inTotalFq, int inPostingPath) {
        docFq = inDocFq;
        totalFq = inTotalFq;
        postingPathName = inPostingPath;
    }

    public IndexEntryValue(Term trm, int pathName) {
        //this.term = trm.getName();
        docFq = trm.getDocFq();
        totalFq = trm.getTotalFq();
        postingPathName = pathName;
    }

    @Override
    public String toString() {
        return "docFq=" + docFq +
                        ", totalFq=" + totalFq +
                        ", postingPath='" + postingPathName + '\'';
    }
}
