package Parser;

/**
 * This class represent the field of a single document
 */
public class DocText {
    private String docno;
    private String innerText;
    private String title;

    /**
     * @param docno
     * @param innerText
     * @param title
     */
    public DocText(String docno, String innerText, String title) {
        this.docno = docno;
        this.innerText = innerText;
        this.title = title;
    }

    /**
     * @return title
     */
    public String getHeader() {
        return title;
    }

    /**
     * @return document id
     */
    public String getDocno() {
        return docno;
    }

    /**
     * @return the text of document
     */
    public String getInnerText() {
        return innerText;
    }


}
