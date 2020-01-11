package Parser;

import EngineUserInterface.Controller;
import EngineUserInterface.MyModel;
import Indexer.IndexDictionary;
import Indexer.SegmentProcesses;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import searchRank.Ranker;
import searchRank.Searcher;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        //small main for my big friend
        Master m = new Master(false);
        IndexDictionary dic = new IndexDictionary("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1",false);
        dic.loadDictionary("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1",false);
        m.updateEntities(dic.indexer);
        MyModel model = new MyModel();
        model.setDictionary(dic);
        model.setPostingPa("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1");
        model.setDocMD(m.LoadDocMD("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1"));
        model.handleQueryFile(System.getProperty("user.dir")+"/queires.txt",false,false,true);
        model.saveResult("C:\\Users\\Asi Zaks\\Desktop\\08 Trec_eval");
        Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd C:\\Users\\Asi Zaks\\Desktop\\08 Trec_eval && treceval qrels.txt queires_TREC.txt\"");
        //change the posting path to yours
        //HashMap<String, DocMD> docsMDs = m.LoadDocMD(projPath);
        //  IndexDictionary dictionary = new IndexDictionary(projPath,stem);
        //dictionary.loadDictionary(projPath,stem);
        //    System.out.printf("");
        //  Searcher searcher = new Searcher("my query -63 media",stem,false);
        // Ranker ranker = new Ranker(dictionary,projPath,stem,docsMDs,semanticTreatment,searcher.queryWords);
        //ranker.handleQuery();
        //System.out.println("");
        //now you have dictionary and docsMDs
        //enjoy lov ya
        //System.out.println(WS4J.runWUP("milk","cheese"));

    }
}


//public class main.java.Parser.Main {
//    public static void main(String[] args) {


//        Master m = new Master(false);
//        try {
//            m.run(false,".");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
/*
        Parser pars = new Parser(false);
        Map<String, TermInDoc> s = pars.parse("May 1994, MAY 1994",false);
        System.out.println(" ");
        for(TermInDoc tid : s.values()){
            System.out.println(tid.getTerm());
        }


        Pattern isNumericPat = Pattern.compile("([+]|[-])?(\\d{1,3}[,])*\\d(\\d+)?([.]\\d+)?");
        String x = "-2,220,160";

        System.out.println(isNumericPat.matcher(x).matches());


    }
}*/

/*public class Main extends Application {
    MyModel model;

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new MyModel();
        //--------------
        primaryStage.setTitle("Omer's & Asi's Search Engine");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/MyView.fxml").openStream());
        Scene scene = new Scene(root, 450, 400);
        scene.getStylesheets().add(getClass().getResource("/MyViewStyle.css").toExternalForm());
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
}*/





