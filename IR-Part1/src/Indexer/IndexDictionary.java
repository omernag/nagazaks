package Indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Boolean.parseBoolean;

public class IndexDictionary {

    private TreeMap<String, String> indexer;
    private String indexerPrint;
    private static int postCounter = 0;
    private boolean stemmer;
    private String path;

    public IndexDictionary(HashMap<String, Term> termL) throws IOException {
        indexer = new TreeMap<>(comp);
        createIndexer(termL);
    }

    private Comparator<String> comp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };

    public IndexDictionary(String postingPath, boolean stemmer) {
        path=postingPath;
        this.stemmer=stemmer;
        indexer = new TreeMap<>();
    }

    public void createIndexer(HashMap<String, Term> termL) throws IOException {
        Posting postFile;
        for (Term trm : termL.values()
        ) {
            postFile = new Posting(trm);
            indexer.put(trm.getName(), ""+trm.getTotalFq()+","+trm.getDocFq()+","+postCounter);
            postCounter++;

        }
        //indexerPrint=printDictionary();
    }


    public void setIndexerPrint(String indexerPrint) {
        this.indexerPrint = indexerPrint;
    }

    public String getIndexerPrint() {
        for (Map.Entry term: indexer.entrySet()
             ) {
            indexerPrint+=term.getKey()+":"+((String)term.getValue()).substring(0,((String) term.getValue()).indexOf(","));
        }
        return indexerPrint;
    }

    public String printDictionary() {
        String dict = "";
        for (Map.Entry ent : indexer.entrySet()
        ) {
            dict += ent.getKey() + ", " + ent.getValue().toString() + "\n";
        }
        return dict;
    }

    public void saveToDisk() throws IOException {
        FileWriter writer;
        if(stemmer){
             writer = new FileWriter(path+"Posting_s/dictionary.txt");
        }
        else{
             writer = new FileWriter(path+"Posting/dictionary.txt");
        }
        for (Map.Entry term: indexer.entrySet()
        ) {
            writer.write(term.getKey()+":"+term.getValue());
        }
    }

    public void loadDictionary(String postingPa,boolean isStemmed) throws IOException {
        List<String> termList;
        if(isStemmed){
            termList=Files.readAllLines(Paths.get(postingPa + "Posting_s/dictionary.txt"));
        }
        else{
            termList=Files.readAllLines(Paths.get(postingPa + "Posting/dictionary.txt"));
        }
        indexer=new TreeMap<>(comp);
        String[] parts;
        for (String trmS: termList
        ) {
            parts=trmS.split(":");
            indexer.put(parts[0],parts[1]);
        }
    }

    public int getNumOfUniqueTerms(){
        return indexer.size();
    }

    /*public IndexDictionary createIndexFromPosting(String path) throws IOException {
        IndexDictionary riseDictionary = new IndexDictionary();
        try {
            File postingFolder = new File(path);
            String termRep = "";
            BufferedReader reader;
            Term risedTerm;
            String[] split;
            for (File letterFolder : postingFolder.listFiles()
            ) {
                for (File termFile : letterFolder.listFiles()
                ) {
                    reader = new BufferedReader(new FileReader(termFile));
                    termRep = reader.readLine();
                    split = termRep.split(",");
                    IndexEntryValue nEntry = new IndexEntryValue(Integer.parseInt(split[1]), Integer.parseInt(split[2]), split[3]);
                    riseDictionary.indexer.put(split[0], nEntry);
                }
            }
        } catch (Exception e) {
            System.out.println("cant find folder");
        }

        return riseDictionary;

    }*/

    /*public Term getTermFromPosting(String termName) throws FileNotFoundException {
        Term termFromPosting = null;
        String path = "Posting/" + termName.charAt(0) + "/" + termName + ".txt";
        try {
            LinkedList<String> loadedPosting = new LinkedList<>(Files.readAllLines(Paths.get(path)));
            termFromPosting = new Term(termName);
            String[] split;
            for (String occ : loadedPosting
            ) {
                split = occ.split(",");
                termFromPosting.addOccurrence(split[0], Integer.parseInt(split[1]), parseBoolean(split[2]), parseBoolean(split[3]));

            }
            termFromPosting.updateDocFq();
        } catch (IOException e) {
            ///maybe change later
            System.out.println("Dictionary does not contain");
        }
        return termFromPosting;
    }*/
}
