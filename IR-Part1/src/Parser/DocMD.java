package Parser;

import Indexer.TermInDoc;

import java.util.Map;

public class DocMD {

    String docno;
    int maxTf;
    int uniqueCount;
    Map<String, TermInDoc> words;

    public DocMD(String docno) {
        this.docno = docno;
    }
}
