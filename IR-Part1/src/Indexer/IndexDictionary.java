package Indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Boolean.parseBoolean;

public class IndexDictionary {

    HashMap<String, IndexEntryValue> indexer;

    public IndexDictionary(ArrayList<Term> termL) throws IOException {
        indexer=new HashMap<>();
        createIndexer(termL);
    }

    public IndexDictionary() {
        indexer=new HashMap<>();
    }

    private void createIndexer(ArrayList<Term> termL) throws IOException {
        IndexEntryValue entry;
        for (Term trm: termL
             ) {
            Posting postFile = new Posting(trm);
            entry = new IndexEntryValue(trm,postFile.getPath());
            indexer.put(trm.getName(),entry);
        }
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

    public IndexDictionary createIndexFromPosting(String path) throws IOException {
        IndexDictionary riseDictionery=new IndexDictionary();
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
                    riseDictionery.indexer.put(split[0],nEntry);
                }
            }
        }
        catch (Exception e){
            System.out.println("cant find folder");
        }
        return riseDictionery;

    }
}
