package Indexer;


public class TermInDoc {

    private String term;
    private String docNo;
    public int termFq;
    private boolean isHeader;
    private boolean isEntity;

    public TermInDoc(String term, String docNo, int termFq, boolean isHeader, boolean isEntity) {
        this.term = term;
        this.docNo = docNo;
        this.termFq = termFq;
        this.isHeader = isHeader;
        this.isEntity = isEntity;
    }

    public TermInDoc(String meta) {
        String[] metaData = meta.split(",");
        this.term = metaData[0];
        this.docNo = metaData[1];
        this.termFq = Integer.parseInt(metaData[2]);
        this.isHeader = (metaData[3].charAt(0) == 't');
        this.isEntity = (metaData[4].charAt(0) == 't');
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void updateCapsToLower() {
        term = term.toLowerCase();
    }

    public String getDocNo() {
        return docNo;
    }

    public int getTermfq() {
        return termFq;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "" + term + "," + docNo + "," + termFq + "," + isHeader + "," + isEntity;
    }
}
