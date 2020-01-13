package Parser;

public class DocText {
    private String docno;
    private String innerText;
    private String title;

    public DocText(String docno, String innerText, String title) {
        this.docno = docno;
        this.innerText = innerText;
        this.title = title;
    }

    public String getHeader() {
        return title;
    }

    public String getDocno() {
        return docno;
    }

    public String getInnerText() {
        return innerText;
    }


}
