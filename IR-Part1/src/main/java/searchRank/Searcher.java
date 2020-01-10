package searchRank;
import Indexer.TermInDoc;
import Parser.Parser;
import Parser.DocMD;

import java.util.*;


public class Searcher {
    String query;
    Parser parser;
    public HashSet<String> queryWords;
    private PriorityQueue<DocMD> orderedDocs;
    boolean showEntities;
    boolean stem;
    String resultsStr;
    String entitiesStr;




    public Searcher(String query, boolean stem,boolean showEntities){
        this.query=query;
        this.stem=stem;
        this.parser = new Parser (stem);
        this.showEntities =showEntities;
        parseQuery();

    }

    private void parseQuery(){
        queryWords = new HashSet<>((parser.parse(query,false)).keySet());
    }

    public void getRankedDocs(Ranker ranker){
        ranker.handleQuery();
        orderedDocs = ranker.okapiRank;
        resultsStr = queryStringResults();
        if(showEntities){entitiesStr=this.stringEntities();}

    }

    private String stringEntities() {
        List<DocMD> results = new LinkedList<DocMD>(orderedDocs);
        String ans ="Each document most relevant entities are:\n \n";
        int size = Math.min(results.size(),50);
        for(int i = 0; i<size;i++){
            DocMD md = results.get(i);
            int countEntities = md.countEntities;
            ans = ans + (i+1) + ". DocNumber: "+md.docno +".\n";
            List<TermInDoc> entities = new LinkedList<TermInDoc>(md.entities);
            int entitiesCount = Math.min(entities.size(),5);
            for(int j = 0; j<entitiesCount;j++){
                TermInDoc entity = entities.get(j);
                int fq = entity.getTermfq();
                int rank = (entity.getTermfq()/countEntities)*1000;
                if(fq!=1) {
                    ans = ans + "\t" + (j + 1) + ". " + entity.getTerm() + ". appeared " + fq + " times. Entity rank: "+rank+".\n";
                }
                else{
                    ans = ans + "\t" + (j + 1) + ". " + entity.getTerm() + ". appeared once.\n";
                }
            }
            ans=ans+"\n";
        }
        return ans;
    }

    private String queryStringResults() {
        List<DocMD> results = new LinkedList<DocMD>(orderedDocs);
        String ans ="The most relevant Docs are:\n";
        int size = Math.min(results.size(),50);
        for(int i = 0; i<size;i++){
            DocMD md = results.get(i);
            ans = ans + (i+1) + ". DocNumber: "+md.docno +". Rank: " + ((int)md.getRank())+". \n";
        }
        return ans;
    }
    public String getResultsStr() {
        return resultsStr;
    }

    public String getEntitiesStr() {
        return entitiesStr;
    }


}
