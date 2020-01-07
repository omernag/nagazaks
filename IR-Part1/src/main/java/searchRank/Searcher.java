package searchRank;
import Indexer.TermInDoc;
import Parser.Parser;
import Parser.DocMD;

import java.util.HashMap;
import java.util.PriorityQueue;


public class Searcher {
    String query;
    Parser parser;
    public HashMap<String, TermInDoc> queryWords;
    private PriorityQueue<DocMD> orderedDocs;
    boolean showEntitys;
    HashMap<DocMD,HashMap<String,Double>> DocEntities;
    boolean stem;


    public Searcher(String query, boolean stem,boolean showEntitys){
        this.query=query;
        this.stem=stem;
        this.parser = new Parser (stem);
        this.showEntitys=showEntitys;
        parseQuery();


    }

    private void parseQuery(){
        queryWords = new HashMap<>(parser.parse(query,false));
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
                for(TermInDoc entity : dmd.entities.values()){
                    entities.put(entity.getTerm(),new Double(entity.getTermfq()/x));
                }
            }
            DocEntities.put(dmd,entities);
        }
    }






}
