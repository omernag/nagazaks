package Parser;

import Indexer.TermInDoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DocMD {

    public String docno;
    public int maxTf;
    int uniqueCount;
    public Map<String, TermInDoc> words;
    public String maxFreqTerm;

    public double getRank() {
        return rank;
    }

    public double rank;
    public int countEntities;
    public int docSize;
    public PriorityQueue<TermInDoc> entities;

    public DocMD(String docno) {
        Comparator<TermInDoc> comp = (o1, o2) -> (int) (o2.getTermfq() - o1.getTermfq());
        entities = new PriorityQueue<>(comp);
        this.docno = docno;

    }

    public DocMD(String[] parts) {
        Comparator<TermInDoc> comp = (o1, o2) -> (int) (o2.getTermfq() - o1.getTermfq());
        entities = new PriorityQueue<>(comp);
        words = new HashMap<>();
        docno = parts[0];
        String[] rest = parts[1].split("@");
        maxTf = Integer.parseInt(rest[0]);
        uniqueCount = Integer.parseInt(rest[1]);
        maxFreqTerm = rest[2];
        docSize = Integer.parseInt(rest[3]);
        countEntities = Integer.parseInt(rest[4]);///add to split
        for (int i = 5; i < 10 && i<rest.length; i++) { //add to split entities 5 best
            entities.add(new TermInDoc(rest[i]));
        }
        rank = 0;
    }

    @Override
    public String toString() {
        String toSave= maxTf + "@" + uniqueCount + "@" + maxFreqTerm + "@" + docSize + "@" + countEntities+"@";
        for (TermInDoc ent: entities
        ) {
            toSave+=ent.toString()+"@";
        }
        return toSave;
    }

    public void setEntities() {
        for (TermInDoc tid : words.values()) {
            if (tid.isEntity()) {
                entities.add(tid);
                countEntities += tid.getTermfq();
            }
        }
    }

    public int getMaxTf() {
        return maxTf;
    }
}
