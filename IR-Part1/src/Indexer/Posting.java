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
    private static int postCounter=0;
    private boolean stemmer;

    public String getPath() {
        return path;
    }

    public Posting(Term trm) throws IOException {
        termToPost=trm;
        posting = trm.getOccurrence();
        path=createFolder();
        outputPosting();
    }

    private String createFolder() {
        /*File trmFile = new File("Posting");
        if(!trmFile.exists()){
            trmFile.mkdir();
        }*/
        /*trmFile=new File("Posting/"+trm.getName().charAt(0));
        if(!trmFile.exists()){
            trmFile.mkdir();
        }*/

        String pathCreator = "Posting/"+postCounter+".txt";
        postCounter++;
        return pathCreator;
        /*"+trm.getName().charAt(0)+"/"*/
    }

    private void outputPosting() throws IOException {

        FileOutputStream postingFile = new FileOutputStream(path);
        String meta=""+termToPost.getName()+","+termToPost.getDocFq()+","+termToPost.getTotalFq()+","+path+"\n";
        postingFile.write(meta.getBytes());
        try{
            for (String term: posting) {
                //term+="\n";
                postingFile.write(term.getBytes());
            }
        }
        catch (IOException e){
            System.out.println("problem with creating post");
        }
        postingFile.close();
    }
}
