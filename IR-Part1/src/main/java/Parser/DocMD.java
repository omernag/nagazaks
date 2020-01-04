package Parser;

import Indexer.TermInDoc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DocMD {

    String docno;
    int maxTf;
    int uniqueCount;
    public Map<String, TermInDoc> words;
    String maxFreqTerm;
    int rank;
    public Map<String, TermInDoc> entities = new HashMap<>();
    public int countEntities;


    public DocMD(String docno) {
        this.docno = docno;
    }

    public DocMD(String[] parts) {
        docno = parts[0];
        String[] rest = parts[1].split("@");
        maxTf = Integer.parseInt(rest[0]);
        uniqueCount = Integer.parseInt(rest[1]);
        maxFreqTerm = rest[2];
        countEntities = Integer.parseInt(rest[3]);///add to split
        for (int i = 4; i < 9; i++) { //add to split entitis 5 bes
            String[] kv = rest[i].split("#");
            entities.put(kv[0],new TermInDoc(kv[1]));
        }
        rank = 0;
    }

    @Override
    public String toString() {
        String toSave= maxTf + "@" + uniqueCount + "@" + maxFreqTerm + "@" + countEntities+"@";
        for (Map.Entry ent: entities.entrySet()
             ) {
            toSave+=ent.getKey()+"#"+ent.getValue()+"@";
        }
        return toSave;
    }

    public void setEntities() {
        Comparator<TermInDoc> comp = (o1, o2) -> (int) (o1.getTermfq() - o2.getTermfq());
        PriorityQueue<TermInDoc> pq = new PriorityQueue<>(comp);
        for (TermInDoc tid : words.values()) {
            if (tid.isEntity()) {
                pq.add(tid);
                countEntities += tid.getTermfq();
            }
        }
        int i = 0;
        while (!pq.isEmpty() && i < 5) {
            TermInDoc curr = pq.poll();
            entities.put(curr.getTerm(), curr);
            i++;
        }
    }
}
