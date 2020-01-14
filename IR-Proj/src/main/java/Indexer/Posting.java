package Indexer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * This class represent a single posting term
 */
public class Posting {

    private LinkedList<String> posting;
    private String path;
    private Term termToPost;
    public static int postCounter=0;
    public static int placeInPost=0;
    private boolean stemmer;

    /**
     * @param trm
     * @throws IOException
     */
    public Posting(Term trm) throws IOException {
        termToPost=trm;
        posting = trm.getOccurrence();
        path = "";
        stemmer=false;
    }

    /**
     * @return Posting term after calculation of its exact location
     * @throws IOException
     */
    public Posting addToPostingList() throws IOException {
        path=postCounter+"@"+placeInPost;
        placeInPost+=(1+posting.size());
        return this;
    }

    /**
     * @return Posting list
     */
    public LinkedList<String> getPosting() {
        return posting;
    }

    /**
     * @return String rep of metadata
     */
    public String getMetaOfTerm(){
        return "" + termToPost.getName() + "," + termToPost.getDocFq() + "," + termToPost.getTotalFq() + "," + path + "\n";
    }

    /**
     * @return exact path
     */
    public String getPath() {
        return path;
    }
}
