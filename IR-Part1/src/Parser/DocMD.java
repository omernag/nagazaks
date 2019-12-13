package Parser;

import java.util.Map;

public class DocMD {

    String docno;
    String maxTf;
    String uniqueCount;
    Map<String,Integer> words;

    public DocMD(String docno) {
        this.docno = docno;
    }
}
