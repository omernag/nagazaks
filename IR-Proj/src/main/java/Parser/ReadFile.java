package Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class reads the files that holds the docs in the corpus
 */
public class ReadFile {

    private String path;
    private String StopWordsPath;
    public List<String> filesPaths;

    /**
     * @return Stop words doc path
     */
    public String getStopWordsPath() {
        return StopWordsPath;
    }

    /**
     * @param path
     */
    public ReadFile(String path){
        this.path=path;
         filesPaths = new ArrayList<>();
        this.getAllPaths();
    }

    /**
     * Reading all the files in the corpus folder, and adding the paths of each file
     */
    public void getAllPaths(){
        File dir = new File(path);

        File [] dirArray = dir.listFiles();
        for(File f : dirArray){
            if(f.getName().contains("corpus")){
                readCorpus(f);
            }
            else if(f.getName().contains("stopwords")){
                StopWordsPath = f.getPath();
            }
        }
    }

    /**
     * Acting function for the above one
     * @param corpus
     */
    private void readCorpus(File corpus){
        if(corpus.isDirectory()) {
            for (File innerFolder : corpus.listFiles()) {
                if (innerFolder.isDirectory()) {
                    for (File file : innerFolder.listFiles()){
                       this.filesPaths.add(file.getPath());
                    }
                }
            }
        }
        else{
            throw new RuntimeException("corpus isn't a folder");
        }
    }

    /**
     * Reades the stop words from the stop words file
     * @param swFile
     * @return
     */
    public HashSet<String> readStopWords(File swFile){
        HashSet<String> stopwords = new HashSet<>();
        try{ BufferedReader br = new BufferedReader(new FileReader(swFile.getPath()));
            String word;
            while ((word=br.readLine())!=null) {
                stopwords.add(word);
            }
        }
        catch(Exception e){
            throw new RuntimeException("stopword file isn't legal");
        }
        return stopwords;
    }

    /**
     * Reads a single file and put it to a list
     * @param path
     * @return
     */
    public ArrayList<DocText> handleFile(String path){
        ArrayList<DocText> docsTexts = new ArrayList<>();
        Document docsFile;
        try {
            docsFile = Jsoup.parse(new File(path), "US-ASCII");
        }
        catch (IOException e){throw new RuntimeException("file opening error");} ;
        Elements docsAsEles = docsFile.getElementsByTag("DOC");
        String docno="";
        String title="";
        String text="";
        for(Element docAsEle : docsAsEles){
            Elements subEles=  docAsEle.getAllElements();
            docno="";
            title="";
            text="";
            for(Element subEle : subEles){

                if(subEle.tagName().equals("docno")){
                    docno = subEle.text();
                }
                else if(subEle.tagName().equals("text")) {
                    text = subEle.text();
                }
                else if(subEle.tagName().equals("ti")){
                    title = subEle.text();
                }
                else if(subEle.tagName().equals("headline")){
                    if(subEle.getAllElements().size()==1) {
                        title = subEle.text();
                    }
                    else{
                        for(Element subSubEle : subEle.children()) {
                            title = title + " " + subEle.text();
                        }
                    }

                }
            }
            if(!docno.equals("")&&!text.equals("")) {
                docsTexts.add(new DocText(docno, text, title));
            }

        }
        return docsTexts;
    }





}

