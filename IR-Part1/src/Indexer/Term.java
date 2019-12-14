package Indexer;


import java.util.*;

public class Term {

    private String term;
    private int totalFq;
    private int docFq;
    private LinkedList<String> occurrence;
    private HashSet<String> docsNames;
    //public static Term[] topTenTerm= new Term[11];
    //public static Term[] bottomTenTerm= new Term[11];
    private static int termCounter=0;

    public LinkedList<String> getOccurrence() {
        return occurrence;
    }
    public int getDocFq() {
        return docFq;
    }


    public Term(String term) {
        this.term = term;
        this.docFq = 0;
        this.occurrence = new LinkedList<>();
        this.docsNames = new HashSet<>();

    }

    public Term(TermInDoc termIn) {
        this.term = termIn.getTerm();
        this.docFq = 0;
        this.totalFq=0;
        this.occurrence = new LinkedList<>();
        this.docsNames = new HashSet<>();
        addOccurrence(termIn.getDocNo(), termIn.getTermfq(), termIn.isHeader(),termIn.isEntity());
    }

    public void addOccurrence(String docNum, int termfq, boolean isHeader, boolean isEntity) {
        totalFq+=termfq;
        docsNames.add(docNum);
        this.occurrence.add(docNum+","+termfq +","+isHeader+","+isEntity+"\n");
    }

    public void setDocFq(int docFq) {
        this.docFq = docFq;
    }

    public int getTotalFq() {
        return totalFq;
    }

    public String getName() {
        return term;
    }

    public void updateDocFq(){

        docFq=docsNames.size();
        docsNames=null;
        /*addToTopBottom(this);*/
    }

    @Override
    public String toString() {
        String termToPrint;
        return
                "term='" + term + '\'' +
                ", docFq=" + docFq +", totalFq="+totalFq+
                ", occurrence=" + occurrence +
                '}';
    }

   /* private void addToTopBottom(Term trm) {
        if(termCounter<11){
            topTenTerm[termCounter]=trm;
            bottomTenTerm[termCounter]=trm;
            termCounter++;

        }
        else{
            Arrays.sort(topTenTerm,compTop);
            Arrays.sort(bottomTenTerm,compBottom);
            if (trm.getTotalFq() > topTenTerm[9].getTotalFq()) {
                topTenTerm[10]=trm;
                Arrays.sort(topTenTerm,compTop);
            }
            else if(trm.getTotalFq() < bottomTenTerm[9].getTotalFq()){
                bottomTenTerm[10]=trm;
                Arrays.sort(bottomTenTerm,compBottom);
            }
        }
    }*/


    private static Comparator<Term> compTop = new Comparator<Term>() {
        @Override
        public int compare(Term o1, Term o2) {
            return o2.getTotalFq()-o1.getTotalFq();
        }
    };
    private static Comparator<Term> compBottom = new Comparator<Term>() {
        @Override
        public int compare(Term o1, Term o2) {
            return o1.getTotalFq()-o2.getTotalFq();
        }
    };

    public boolean updateToUpperCase() {
        if(Character.isUpperCase(term.charAt(0))){
            term = term.toUpperCase();
            return true;
        }
        return false;
    }
}
