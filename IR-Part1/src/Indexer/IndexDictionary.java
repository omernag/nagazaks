package Indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;



public class IndexDictionary {

    private TreeMap<String, String> indexer;
    private String indexerPrint;
    private boolean stemmer;
    private String path;
    private LinkedList<Posting> toPost;


    public IndexDictionary(String postingPath, boolean stemmer) {
        this.stemmer = stemmer;
        if(stemmer){
            path = postingPath+"/Posting_s/";
        }
        else{
            path = postingPath+"/Posting/";
        }
        indexer = new TreeMap<>();
        toPost = new LinkedList<>();

    }

    public void createIndexer(HashMap<String, Term> termL) throws IOException {
        Posting postFile;
        int counter = 0;
        for (Term trm : termL.values()
        ) {
            if (counter < 200) {//1 is missing
                postFile = new Posting(trm);
                toPost.add(postFile.addToPostingList());
                indexer.put(trm.getName(), "" + trm.getTotalFq() + "," + trm.getDocFq() + "," + postFile.getPath());
                counter++;
            } else {
                outputPostList(path+"/"+Posting.postCounter+".txt");
                toPost.clear();
                Posting.postCounter++;
                Posting.placeInPost = 0;
                counter=0;
            }


        }

    }

    private void outputPostList(String path) throws IOException {
        FileOutputStream postingFile = new FileOutputStream(path);
        for (Posting pst: toPost
             ) {
            postingFile.write(pst.getMetaOfTerm().getBytes());
            for (String term : pst.getPosting()) {
                postingFile.write(term.getBytes());
            }
        }
        postingFile.close();

    }


    public String getIndexerPrint() {
        for (Map.Entry term : indexer.entrySet()
        ) {
            indexerPrint += term.getKey() + ":" + ((String) term.getValue()).substring(0, ((String) term.getValue()).indexOf(","));
        }
        return indexerPrint;
    }


    public void saveToDisk() throws IOException {
        FileWriter writer;
        if (stemmer) {
            writer = new FileWriter(path + "/dictionary.txt");
        } else {
            writer = new FileWriter(path + "/dictionary.txt");
        }
        for (Map.Entry term : indexer.entrySet()
        ) {
            writer.write(term.getKey() + ":" + term.getValue()+"\n");
        }
        writer.close();
    }

    public void loadDictionary(String postingPa, boolean isStemmed) throws IOException {
        List<String> termList;
        if (isStemmed) {
            termList = Files.readAllLines(Paths.get(postingPa + "Posting_s/dictionary.txt"));
        } else {
            termList = Files.readAllLines(Paths.get(postingPa + "Posting/dictionary.txt"));
        }
        indexer = new TreeMap<>(comp);
        String[] parts;
        for (String trmS : termList
        ) {
            parts = trmS.split(":");
            indexer.put(parts[0], parts[1]);
        }
    }

    public int getNumOfUniqueTerms() {
        return indexer.size();
    }

    private Comparator<String> comp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };

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
