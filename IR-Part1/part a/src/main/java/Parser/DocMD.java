package Parser;

import Indexer.TermInDoc;

import java.util.Map;

public class DocMD {

    String docno;
    int maxTf;
    int uniqueCount;
    Map<String, TermInDoc> words;
    String maxFreqTerm;

    public DocMD(String docno) {
        this.docno = docno;
    }

    public DocMD(String[] parts) {
        docno=parts[0];
        String[] rest = parts[1].split("&");
        maxTf=Integer.parseInt(rest[0]);
        uniqueCount=Integer.parseInt(rest[1]);
        maxFreqTerm=rest[2];
    }

    @Override
    public String toString() {
        return "" + maxTf + "," + uniqueCount + "," + maxFreqTerm;
    }
}
