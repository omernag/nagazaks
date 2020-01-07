package searchRank;

import Indexer.IndexDictionary;
import Indexer.Term;
import Indexer.TermInDoc;
import Parser.DocMD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Ranker {
    IndexDictionary dictionary;
    String postingPath;
    boolean stem;
    HashMap<String,DocMD> relevantDocs;
    HashMap<String,Term> terms;
    HashMap<String, DocMD> docMDs;
    HashMap<String,Double> idf;

    public Ranker(IndexDictionary dictionary,String postingPath,boolean stem ,HashMap<String, DocMD> docMDs){
        this.dictionary = dictionary;
        this.postingPath=postingPath;
        this.stem=stem;
        relevantDocs = new HashMap<>();
        this.docMDs=docMDs;
        idf = new HashMap<>();
    }

    public double handleQuery(HashMap<String, TermInDoc> queryWords){
        relevantDocs = new HashMap<>();
        terms = new HashMap<>();
        for(TermInDoc tid : queryWords.values()){
            String name = tid.getTerm();
                if(dictionary.indexer.containsKey(name)) {
                    Term term = new Term(name);
                    String[] termData = dictionary.indexer.get(name).split(",");
                    String[] termLocation = termData[2].split("@");
                    List<String> termList;
                    int numDocs =Integer.parseInt(termData[0]);
                    int termLine = Integer.parseInt(termLocation[1]);
                    idf.put(name,new Double((docMDs.size()-numDocs+0.5)/(numDocs+0.5)));
                    try{
                        if (stem) {
                            termList = Files.readAllLines(Paths.get(postingPath + "/Posting_s/"+termLocation[0]+".txt"));
                        } else {
                            termList = Files.readAllLines(Paths.get(postingPath + "/Posting/"+termLocation[0]+".txt"));
                        }
                        for(int i=1;i<=numDocs;i++){
                            String docTermDic = termList.get(i+termLine);
                            String[] arr = docTermDic.split(",");
                            String docno = arr[0];
                            term.addOccurrence(docno,Integer.parseInt(arr[1]),Boolean.parseBoolean(arr[2]),Boolean.parseBoolean(arr[3]));
                            DocMD dmd = docMDs.get(docno);
                            if(!relevantDocs.containsKey(docno)){
                                relevantDocs.put(docno,dmd);
                            }
                            //dmd.words.put(name);
                        }
                        term.updateDocFq();
                        terms.put(term.getName(),term);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
        return 0;
    }

    public double Bm25Rank(HashMap<String, TermInDoc> queryWords){
        double docRank = 0;
        for(DocMD doc :relevantDocs.values()){
            for(String termStr : queryWords.keySet()){
                Term term = terms.get(termStr);

                if(term.termDocs.containsKey(doc.docno)){
                    TermInDoc tid = term.termDocs.get(doc.docno);
                    int termfq = tid.termfq;
                    docRank+= idf.get(termStr)*((termfq*3)/(termfq+2*(0.25+(0.75))));
                }
            }
        }
        return docRank;
    }
}
