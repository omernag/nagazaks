package Indexer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Posting {

    private LinkedList<String> posting;
    private String path;
    private Term termToPost;
    public static int postCounter=0;
    public static int placeInPost=0;
    private boolean stemmer;

    public Posting(Term trm) throws IOException {
        termToPost=trm;
        posting = trm.getOccurrence();
        path = "";
        stemmer=false;
    }

    public Posting addToPostingList() throws IOException {
        path=postCounter+"@"+placeInPost;
        placeInPost+=(1+posting.size());
        return this;
    }

    public LinkedList<String> getPosting() {
        return posting;
    }

    public String getMetaOfTerm(){
        return "" + termToPost.getName() + "," + termToPost.getDocFq() + "," + termToPost.getTotalFq() + "," + path + "\n";
    }

    public String getPath() {
        return path;
    }
}
