package searchRank;
import Indexer.TermInDoc;
import Parser.Parser;
import Parser.DocMD;

import java.util.*;


/**
 * This class represent the logic of the dictionary searching
 */
public class Searcher {
    String query;
    Parser parser;
    public HashSet<String> queryWords;
    private PriorityQueue<DocMD> orderedDocs;
    boolean showEntities;
    boolean stem;
    String resultsStr;
    String entitiesStr;


    /**
     * @param query
     * @param stem
     * @param showEntities
     */
    public Searcher(String query, boolean stem,boolean showEntities){
        this.query=query;
        this.stem=stem;
        this.parser = new Parser (stem);
        this.showEntities =showEntities;
        parseQuery();

    }

    /**
     * Parsing a single query
     */
    private void parseQuery(){
        queryWords = new HashSet<>((parser.parse(query,false)).keySet());
    }

    /**
     * Ranking the query based on BM25 ranking
     * @param ranker
     */
    public void rank(Ranker ranker){
       // Comparator<DocMD> comp = (o1, o2) -> (int) (o2.getRank() - o1.getRank());
        ranker.handleQuery();
        orderedDocs = ranker.okapiRank;
        shrinkOrderedDocs();
        resultsStr = queryStringResults();
        if(showEntities){entitiesStr=this.stringEntities();}

    }

    /**
     * Lowering the num of returned docs to 50
     */
    private void shrinkOrderedDocs() {
        if(orderedDocs.size()>50){
            Comparator<DocMD> comp = (o1, o2) -> (int) (o2.getRank() - o1.getRank());
            PriorityQueue<DocMD> newOrderedDocs = new PriorityQueue<DocMD>(comp);
            for(int i =0;i<50;i++){
                newOrderedDocs.add(orderedDocs.poll());
            }
            this.orderedDocs=newOrderedDocs;
        }
    }

    /**
     * @return String rep of the 5 main entities of all docs
     */
    private String stringEntities() {
        List<DocMD> results = new LinkedList<DocMD>(orderedDocs);
        String ans ="Each document most relevant entities are:\n \n";
        int size = Math.min(results.size(),50);
        for(int i = 0; i<size;i++){
            DocMD md = results.get(i);
            int countEntities = md.countEntities;
            ans = ans + (i+1) + ". DocNumber: "+md.docno +".\n";
            List<TermInDoc> entities = new LinkedList<>(md.entities);
            int entitiesCount = Math.min(entities.size(),5);
            for(int j = 0; j<entitiesCount;j++){
                TermInDoc entity = entities.get(j);
                int fq = entity.getTermfq();
                double rank = Math.log(((entity.getTermfq())*100/countEntities)+1)/Math.log(2);
                if(fq!=1) {
                    ans = ans + "\t" + (j + 1) + ". " + entity.getTerm() + ". appeared " + fq + " times. Entity rank: "+(int)rank+".\n";
                }
                else{
                    ans = ans + "\t" + (j + 1) + ". " + entity.getTerm() + ". appeared once. Entity rank: "+(int)rank+".\n";
                }
            }
            ans=ans+"\n";
        }
        return ans;
    }

    /**
     * @return String rep of results
     */
    private String queryStringResults() {
        String ans ="The most relevant Docs are:\n";
        int i=1;
        for(DocMD md : orderedDocs){
            ans = ans + i + ". DocNumber: "+md.docno +". Rank: " + ((int)md.getRank())+". \n";
            i++;
        }
        return ans;
    }

    /**
     * @return result string
     */
    public String getResultsStr() {
        return resultsStr;
    }

    /**
     * @return entities string
     */
    public String getEntitiesStr() {
        return entitiesStr;
    }

    /**
     * @return relevent docs order
     */
    public PriorityQueue<DocMD> getOrderedDocs() {
        return orderedDocs;
    }
}
