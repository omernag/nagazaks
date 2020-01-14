package Indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * This class represent the Dictionary of all the terms from the corpus
 */
public class IndexDictionary {

    public TreeMap<String, String> indexer;
    private String indexerPrint;
    private boolean stemmer;
    private String path;
    private LinkedList<Posting> toPost;


    /**
     * @param postingPath
     * @param stemmer
     */
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

    /**
     * Writes all the term and occurrences to posting files
     * Each file contains 200 terms
     * @param termL
     * @throws IOException
     */
    public void createIndexer(HashMap<String, Term> termL) throws IOException {
        Posting postFile;
        int counter = 0;
        for (Term trm : termL.values()
        ) {
            if (counter < 200) {//1 is missing
                postFile = new Posting(trm);
                toPost.add(postFile.addToPostingList());
                indexer.put(trm.getName(), "" + trm.getDocFq() + "," + trm.getTotalFq() + "," + postFile.getPath());
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

    /**
     * Does the actual writing of the posting
     * Helper function for the one above
     * @param path
     * @throws IOException
     */
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


    /**
     * @return String representation of the dictionary
     */
    public String getIndexerPrint() {
        StringBuilder stb = new StringBuilder();
        for (Map.Entry term : indexer.entrySet()
        ) {
            stb.append(term.getKey() + ":" + ((String) term.getValue()).substring(0, ((String) term.getValue()).indexOf(","))+"\n");
        }
        indexerPrint=stb.toString();
        return stb.toString();
    }


    /**
     * Saves the Dictionary entries to the disk
     * @throws IOException
     */
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

    /**
     * Loads the Dictionary from the file to RAM
     * @param postingPa
     * @param isStemmed
     * @throws IOException
     */
    public void loadDictionary(String postingPa, boolean isStemmed) throws IOException {
        List<String> termList;
        if (isStemmed) {
            termList = Files.readAllLines(Paths.get(postingPa + "/Posting_s/dictionary.txt"));
        } else {
            termList = Files.readAllLines(Paths.get(postingPa + "/Posting/dictionary.txt"));
        }
        indexer = new TreeMap<>(comp);
        String[] parts;
        for (String trmS : termList
        ) {
            parts = trmS.split(":");
            indexer.put(parts[0], parts[1]);
        }
    }

    /**
     * @return size of dictionary
     */
    public int getNumOfUniqueTerms() {
        return indexer.size();
    }

    /**
     * Comparer by name lexicographically
     */
    private Comparator<String> comp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };
}
