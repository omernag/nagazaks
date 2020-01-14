package Indexer;


/**
 * This class represent a single term in the corpus
 */
public class TermInDoc {

    private String term;
    private String docNo;
    public int termfq;
    private boolean isHeader;
    private boolean isEntity;


    /**
     * @param term
     * @param docNo
     * @param termfq
     * @param isHeader
     * @param isEntity
     */
    public TermInDoc(String term, String docNo, int termfq, boolean isHeader, boolean isEntity) {
        this.term = term;
        this.docNo = docNo;
        this.termfq = termfq;
        this.isHeader = isHeader;
        this.isEntity = isEntity;
    }

    /**
     * @param meta
     */
    public TermInDoc(String meta) {
        String[] metaData = meta.split("&");
        this.term = metaData[0];
        this.docNo = metaData[1];
        this.termfq = Integer.parseInt(metaData[2]);
        this.isHeader = (metaData[3].charAt(0) == 't');
        this.isEntity = (metaData[4].charAt(0) == 't');
    }

    /**
     * @param term
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Lowering the case
     */
    public void updateCapsToLower() {
        term = term.toLowerCase();
    }

    /**
     * @return Document id
     */
    public String getDocNo() {
        return docNo;
    }

    /**
     * @return Term freq in this doc
     */
    public int getTermfq() {
        return termfq;
    }

    /**
     * @return boolean
     */
    public boolean isHeader() {
        return isHeader;
    }

    /**
     * @return boolean
     */
    public boolean isEntity() {
        return isEntity;
    }

    /**
     * @return term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return String rep of this term
     */
    @Override
    public String toString() {
        return "" + term + "&" + docNo + "&" + termfq + "&" + isHeader + "&" + isEntity;
    }
}
