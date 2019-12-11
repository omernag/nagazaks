package Indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Boolean.parseBoolean;

public class IndexDictionary {

    private TreeMap<String, IndexEntryValue> indexer;
    private String indexerPrint;

    public IndexDictionary(ArrayList<Term> termL) throws IOException {
        indexer=new TreeMap<>();
        createIndexer(termL);
    }

    public IndexDictionary() {
        indexer=new TreeMap<>();
    }

    private void createIndexer(ArrayList<Term> termL) throws IOException {
        IndexEntryValue entry;
        for (Term trm: termL
             ) {
            Posting postFile = new Posting(trm);
            entry = new IndexEntryValue(trm,postFile.getPath());
            indexer.put(trm.getName(),entry);
        }
        indexerPrint=printDictionary();
    }

    public Term getTermFromPosting(String termName) throws FileNotFoundException {
        Term termFromPosting=null;
        String path = "Posting/"+termName.charAt(0)+"/"+termName+".txt";
        try {
            ArrayList<String> loadedPosting = new ArrayList<>(Files.readAllLines(Paths.get(path)));
            termFromPosting = new Term(termName);
            String[] split;
            for (String occ:loadedPosting
                 ) {
                split=occ.split(",");
                termFromPosting.addOccurrence(split[0], Integer.parseInt(split[1]), parseBoolean(split[2]),parseBoolean(split[3]),parseBoolean(split[4]));

            }
            termFromPosting.updateDocFq();
        }
        catch (IOException e){
            ///maybe change later
            System.out.println("Dictionary does not contain");
        }
        return termFromPosting;
    }

    public void setIndexerPrint(String indexerPrint) {
        this.indexerPrint = indexerPrint;
    }

    public IndexDictionary createIndexFromPosting(String path) throws IOException {
        IndexDictionary riseDictionary=new IndexDictionary();
        try{
            File postingFolder = new File(path);
            String termRep="";
            BufferedReader reader;
            Term risedTerm;
            String[] split;
            for (File letterFolder: postingFolder.listFiles()
            ) {
                for (File termFile: letterFolder.listFiles()
                     ) {
                    reader=new BufferedReader(new FileReader(termFile));
                    termRep=reader.readLine();
                    split=termRep.split(",");
                    IndexEntryValue nEntry= new IndexEntryValue(Integer.parseInt(split[1]),Integer.parseInt(split[2]),split[3]);
                    riseDictionary.indexer.put(split[0],nEntry);
                }
            }
        }
        catch (Exception e){
            System.out.println("cant find folder");
        }

        return riseDictionary;

    }

    public String getIndexerPrint() {
        return indexerPrint;
    }

    public String printDictionary(){
        String dict="";
        for (Map.Entry ent:indexer.entrySet()
             ) {
            dict+=ent.getKey()+", "+ent.getValue().toString()+"\n";
        }
        return dict;
    }
}
