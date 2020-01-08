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
        /*//small main for my big friend
        String projPath = "C:\\Users\\onagar\\Desktop\\bgu\\nagazaks\\IR-Part1";
        Boolean stem = true;
        Master m = new Master(stem);
        m.run(stem,projPath);
        SegmentProcesses sp = new SegmentProcesses(stem,projPath);
        m.updateEntities(sp.getTheDictionary().indexer);
        m.saveDocMD(projPath);
        sp.getTheDictionary().saveToDisk();
        //change the posting path to yours
        HashMap<String, DocMD> docsMDs = m.LoadDocMD(projPath);
        IndexDictionary dictionary = new IndexDictionary(projPath,stem);
        dictionary.loadDictionary(projPath,stem);
        System.out.printf("");
        Searcher searcher = new Searcher("my query -63 media",stem,false);
        Ranker ranker = new Ranker(dictionary,projPath,stem,docsMDs);
        ranker.handleQuery(searcher.queryWords);
        //now you have dictionary and docsMDs
        //enjoy lov ya
        //System.out.println(WS4J.runWUP("milk","cheese"));*/
        Ranker r = new Ranker(null,"",false,null);
        r.fetchFromWeb("bank");
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
        primaryStage.setTitle("Enter a proper title");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/MyView.fxml").openStream());
        Scene scene = new Scene(root, 400, 400);
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





