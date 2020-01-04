package Parser;

import EngineUserInterface.Controller;
import EngineUserInterface.MyModel;
import Indexer.IndexDictionary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        //small main for my big friend
        Master m = new Master(false);
        //change the posting path to yours
        HashMap docsMDs = m.LoadDocMD("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1");
        IndexDictionary dictionary = new IndexDictionary("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1",false);
        dictionary.loadDictionary("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1",false);

        //now you have dictionary and docsMDs
        //enjoy lov ya

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





