import EngineUserInterface.Controller;
import EngineUserInterface.MyModel;
import Indexer.SegmentProcesses;
import Indexer.Term;
import Indexer.TermInDoc;
import Parser.Master;
import Parser.Parser;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;


import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
public class Main {
    public static void main(String[] args) {

       // Parser pars = new Parser(false);
       // Map<String, TermInDoc> s = pars.parse("%High-performance",false);
        double num = 0;
        try {
            num = NumberFormat.getNumberInstance(Locale.US).parse("+22").doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(num);
}
    }
*/


public class Main extends Application {
    MyModel model;

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new MyModel();
        //--------------
        primaryStage.setTitle("Enter a proper title");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/EngineUserInterface/MyView.fxml").openStream());
        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/EngineUserInterface/MyViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        Controller view = fxmlLoader.getController();
        view.setModel(model);
        //--------------
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
/*private static TermsInDocList[] addTerm() throws IOException {
        TermsInDocList test = new TermsInDocList();
        TermsInDocList test2 = new TermsInDocList();
        ArrayList<TermInDoc> list = new ArrayList<>();
        ArrayList<TermInDoc> list2 = new ArrayList<>();
        TermInDoc trm1 = new TermInDoc("bus", "doc1", 100, true, true, false);
        TermInDoc trm2 = new TermInDoc("car", "doc2", 1000, true, true, false);
        TermInDoc trm3 = new TermInDoc("bus", "doc1", 100, true, true, false);
        TermInDoc trm4 = new TermInDoc("car", "doc2", 1000, false, false, true);
        TermInDoc trm5 = new TermInDoc("bus", "doc3", 100, true, true, false);
        TermInDoc trm6 = new TermInDoc("car", "doc3", 1000, true, true, false);
        TermInDoc trm7 = new TermInDoc("bus", "doc4", 100, true, true, false);
        TermInDoc trm8 = new TermInDoc("car", "doc2", 1000, true, true, false);
        TermInDoc trm9 = new TermInDoc("bus", "doc4", 100, true, true, false);
        TermInDoc trm10 = new TermInDoc("car", "doc3", 1000, true, true, false);
        list.add(trm1);
        list.add(trm2);
        list.add(trm3);
        list.add(trm4);
        list.add(trm5);
        list.add(trm6);
        list.add(trm7);
        list.add(trm8);
        list.add(trm9);
        list.add(trm10);

        test.setList(list);

        TermInDoc trm11 = new TermInDoc("busf", "doc1", 100, true, true, false);
        TermInDoc trm12 = new TermInDoc("carg", "doc2", 1000, true, true, false);
        TermInDoc trm13 = new TermInDoc("busg", "doc3", 100, true, true, false);
        TermInDoc trm14 = new TermInDoc("carh", "doc4", 1000, true, true, false);
        TermInDoc trm15 = new TermInDoc("bush", "doc5", 100, true, true, false);
        TermInDoc trm16 = new TermInDoc("carj", "doc1", 1000, false, false, true);
        TermInDoc trm17 = new TermInDoc("busk", "doc2", 100, true, true, false);
        TermInDoc trm18 = new TermInDoc("carl", "doc3", 1000, true, true, false);
        TermInDoc trm19 = new TermInDoc("busc", "doc4", 100, true, true, false);
        TermInDoc trm20 = new TermInDoc("carv", "doc5", 1000, true, true, false);
        list2.add(trm11);
        list2.add(trm12);
        list2.add(trm13);
        list2.add(trm14);
        list2.add(trm15);
        list2.add(trm16);
        list2.add(trm17);
        list2.add(trm18);
        list2.add(trm19);
        list2.add(trm20);

        test2.setList(list2);

        TermsInDocList[] ans = new TermsInDocList[2];
        ans[0] = test;
        ans[1] = test2;
        return ans;
    }

    private static void testTheIndex() throws IOException {
        TermsInDocList test;
        ArrayList<TermInDoc> list = new ArrayList<>();
        String word = "aaaa";
        TermInDoc curr;
        int docNum;
        int termFq;
        int counter=1;


        char currC;
        while (word.charAt(0) <= 'a') {
                while (word.charAt(1) <= 'a') {
                    while (word.charAt(2) <= 'a') {
                        while (word.charAt(3) <= 'z') {
                            docNum = (int) (Math.random() * 100);
                            //termFq = (int) (Math.random() * 100) + 1;
                            if (counter % 5 == 0) {
                                curr = new TermInDoc(word.substring(0,1).toUpperCase()+word.substring(1), "" + docNum, counter, false, false, false);
                            }
                            else {
                                curr = new TermInDoc(word, "" + docNum, counter, false, false, false);
                            }
                            list.add(curr);
                            currC = word.charAt(3);
                            if(counter>20) {
                                currC++;
                                counter=1;
                            }
                            else{
                                counter++;
                            }
                            word = word.substring(0, 3) + currC;
                        }
                        currC = word.charAt(2);
                        currC++;
                        word = word.substring(0, 2) + currC + 'a';
                    }
                    currC = word.charAt(1);
                    currC++;
                    word = word.substring(0, 1) + currC + "aa";
                }
                currC = word.charAt(0);
                currC++;
                word = currC + "aaa";
                test = new TermsInDocList();
                list.add(new TermInDoc("Baaa","asd44",11,false,false,true));
                test.setList(list);
                test.tidToJson();
                list.clear();
            }

    }

    private static void simpleTestToLoad() throws IOException {
        TermsInDocList[] test = addTerm();
        test[0].tidToJson();
        test[1].tidToJson();
        SegmentProcesses sgm = new SegmentProcesses();
        System.out.println(sgm.getTheDictionary().getTermFromPosting("bus"));
    }

    private static void indexTest() throws IOException {
        testTheIndex();
        long startTimeIndex = System.nanoTime();
        SegmentProcesses sgm = new SegmentProcesses();
        long finishTimeIndex = System.nanoTime();
        long startTimeIR = System.nanoTime();
        //System.out.println(sgm.getTheDictionary().getTermFromPosting("abcd"));
        long finishTimeIR = System.nanoTime();
        System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 60000000000.0 + "min");
        System.out.println("Time IR:  " + (finishTimeIR - startTimeIR) / 1000000.0 + "ms");
        System.out.println("Bottom ten:\n");
        for (Term trm : Term.bottomTenTerm
        ) {
            System.out.println(trm.toString());
        }

        System.out.println("Top ten:\n");
        for (Term trm : Term.topTenTerm
        ) {
            System.out.println(trm.toString());
        }
        System.out.println(sgm.getTheDictionary().getIndexerPrint());
    }*//*






*/
/*
public class Main {

    public static void main(String[] args) throws IOException, IOException {
        // write your code here


       *//*

*/
/* long startTimeIndex = System.nanoTime();
        Master m = new Master(false);
        m.run(false,".");
        long finishTimeIndex = System.nanoTime();
        System.out.println("Time:  " + (finishTimeIndex - startTimeIndex) / 60000000000.0 + "min");*//*
*/
/*

        long startTimeIndex1 = System.nanoTime();
        //SegmentProcesses sgm = new SegmentProcesses(false,".");
        long finishTimeIndex1 = System.nanoTime();
        //System.out.println("Parse Time:  " + (finishTimeIndex - startTimeIndex) / 60000000000.0 + "min");
        System.out.println("Index Time:  " + (finishTimeIndex1 - startTimeIndex1) / 60000000000.0 + "min");
        //System.out.println("Total Time:" + (finishTimeIndex1 + finishTimeIndex - startTimeIndex - startTimeIndex1) / 60000000000.0 + "min");

        System.out.println(howMuchNumberTerm());
    }

    private static int howMuchNumberTerm() throws IOException {
        List<String> termList;
        termList = Files.readAllLines(Paths.get("./Posting/dictionary.txt"));

        HashMap indexer = new HashMap();
        String[] parts;
        for (String trmS : termList
        ) {
            parts = trmS.split(":");
            if(Character.isDigit(parts[0].charAt(0))) {
                indexer.put(parts[0], parts[1]);
            }
        }
        return indexer.size();
    }
}
*/




