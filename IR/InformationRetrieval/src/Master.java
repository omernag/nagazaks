import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Master {


    private HashMap<String, DocMD> docsMTs;
    private ArrayList<DocText> fileTexts;
    private ReadFile rf;
    private Parser parser;

    public Master() {
        docsMTs = new HashMap<>();
        fileTexts = new ArrayList<>();
        parser = new Parser();
    }

    public void run(String path){
        rf = new ReadFile(path);
        parser.stopwords = rf.readStopWords(new File(rf.getStopWordsPath()));
        for(String filePath : rf.filesPaths){
            //fileTexts.addAll(rf.handleFile(filePath));
            fileTexts= rf.handleFile(filePath);
            DocMD doc;
            for(DocText dt : fileTexts){
                doc = new DocMD(dt.getDocno());
                parser = new Parser();
              //  doc.words = parser.parse((dt.getInnerText()));
            }

            //ליצור מחלקה של doc, שתחזיק docMT וarraylist של docTerms

        }
    }
}
