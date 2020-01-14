package Parser;

import Indexer.TermInDoc;
import Indexer.TermsInDocList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class controls and activates the ReadFile and Parser
 */
public class Master {


    public HashMap<String, DocMD> docsMDs;
    private TermsInDocList[] wordsToWrite;
    private ArrayList<DocText> fileTexts;
    private ReadFile rf;
    private Parser parser;
    private boolean stemmer;

    /**
     * @param stem
     */
    public Master(boolean stem) {
        docsMDs = new HashMap<>();
        fileTexts = new ArrayList<>();
        wordsToWrite = new TermsInDocList[20];
        for(int i = 0 ; i<wordsToWrite.length;i++){
            wordsToWrite[i] = new TermsInDocList();
        }
        stemmer=stem;
    }

    /**
     * The main method in this class, reads and parse the corpus
     * @param stem
     * @param path
     * @throws IOException
     */
    public void run(boolean stem,String path) throws IOException {
        int counter = 0;
        rf = new ReadFile(path);
        Parser.stopwords = rf.readStopWords(new File(rf.getStopWordsPath()));

        long startTimeIndex ;
        long finishTimeIndex;
        File f = new File("./Segment/");
        f.mkdir();

        for(String filePath : rf.filesPaths){

            //fileTexts.addAll(rf.handleFile(filePath));
            fileTexts= rf.handleFile(filePath);


            startTimeIndex = System.nanoTime();


            for(DocText dt : fileTexts){
                parser = new Parser(stem);
                DocMD doc = parser.handleDoc(dt);
                docsMDs.put(doc.docno,doc);
                for (TermInDoc tid : doc.words.values()){
                    int slot = Math.abs(tid.getTerm().toLowerCase().hashCode()) %20 ;
                    wordsToWrite[slot].getList().add(tid);
                }
                doc.words = null;
                dt = null;
            }
            finishTimeIndex = System.nanoTime();
            System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec to parse file path " + filePath.toString());
            startTimeIndex = System.nanoTime();
            counter++;
           // if(counter%50==0 || counter == rf.filesPaths.size()-1){

                for(int i = 0; i<20; i++){

                    wordsToWrite[i].tidToJson(i);
                    wordsToWrite[i]=new TermsInDocList();
                }
                finishTimeIndex = System.nanoTime();
                System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 1000000000.0 + "sec to save file");
           // }


        }
    }

    /**
     * @return num of docs in corpus
     */
    public int getDocAmount(){
        return docsMDs.size();
    }


    /**
     * Saves all the metadata of the documents to the memory
     * @param postingPath
     * @throws IOException
     */
    public void saveDocMD(String postingPath) throws IOException {
        FileWriter writer;
        if(stemmer){
            writer = new FileWriter(postingPath+"/Posting_s/docMD.txt");
        }
        else{
            writer = new FileWriter(postingPath+"/Posting/docMD.txt");
        }
        for (Map.Entry doc: docsMDs.entrySet()
        ) {
            writer.write(doc.getKey()+":"+doc.getValue().toString()+"\n");
        }
        writer.close();
    }

    /**
     * Load the Docs metadata from the memory to ram
     * @param postingPath
     * @return
     * @throws IOException
     */
    public HashMap LoadDocMD(String postingPath) throws IOException {/// find who call
        List<String> termList;
        if(stemmer){
            termList= Files.readAllLines(Paths.get(postingPath + "/Posting_s/docMD.txt"));
        }
        else{
            termList=Files.readAllLines(Paths.get(postingPath + "/Posting/docMD.txt"));
        }
        docsMDs=new HashMap<>();
        String[] parts;
        for (String trmS: termList
        ) {
            parts=trmS.split(":");
            docsMDs.put(parts[0],new DocMD(parts));
        }
        return docsMDs;
    }

    /**
     * This method update the priority queue of each docs and its entities prioritized by their frequency
     * @param index
     */
    public void updateEntities(TreeMap<String, String> index) {
        for(DocMD dmd:docsMDs.values()) {
            int entitiesCount = 0;
            Comparator<TermInDoc> comp = (o1, o2) -> (int) (o2.getTermfq() - o1.getTermfq());
            PriorityQueue<TermInDoc> newEntities = new PriorityQueue<>(comp);
            for (TermInDoc tid : dmd.entities) {
                if (index.containsKey(tid.getTerm())) {
                    entitiesCount += tid.termfq;
                    newEntities.add(tid);
                }
            }
            dmd.entities = newEntities;
            dmd.countEntities = entitiesCount;
        }
    }
}
