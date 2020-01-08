package searchRank;
import Indexer.IndexDictionary;
import Indexer.TermInDoc;
import Parser.Parser;
import Parser.DocMD;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;


public class Searcher {
    String query;
    Parser parser;
    public HashSet<String> queryWords;
    private PriorityQueue<DocMD> orderedDocs;
    boolean showEntities;
    HashMap<DocMD,HashMap<String,Double>> DocEntities;
    boolean stem;


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

    private void getRankedDocs(Ranker ranker){
        //call Ranker function

    }

    private void calcEntities() {
        DocEntities=new HashMap<DocMD, HashMap<String, Double>>();
        for (DocMD dmd:orderedDocs) {
            HashMap<String,Double> entities =  new HashMap<>();
            int x = dmd.countEntities;
            if(x>0){
                for(TermInDoc entity : dmd.entities){
                    entities.put(entity.getTerm(),new Double(entity.getTermfq()/x));
                }
            }
            DocEntities.put(dmd,entities);
        }
    }

}
