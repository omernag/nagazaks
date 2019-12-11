package EngineUserInterface;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ConnectController {
    public Label lbl_pathCorpus;
    public Label lbl_pathPosting;
    public TextField tf_pathCorpus;
    public TextField tf_pathPosting;
    public Button btn_connect;
    public Button btn_browseCorpus;
    public Button btn_browsePosting;
    public AnchorPane pane;

    private String corpusPath;
    private String postingPath;
    private MyModel model;

    public void pressConnect(ActionEvent event) {
        model=new MyModel();
        model.connectToCorpus(corpusPath,postingPath);
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
    }

    public void setModel(MyModel model) {
        this.model = model;
    }

    public void browseCorpus(ActionEvent event) {
        Stage saveStage = new Stage();
        saveStage.setTitle("Corpus folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1"));
        tf_pathCorpus.textProperty().setValue(directoryChooser.getInitialDirectory().getPath());
        File corpusDirectory = directoryChooser.showDialog(saveStage);
        tf_pathCorpus.textProperty().setValue(corpusDirectory.getPath());
        corpusPath=tf_pathCorpus.textProperty().get();
    }

    public void browsePosting(ActionEvent event) {
        Stage saveStage = new Stage();
        saveStage.setTitle("Posting folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1\\Posting"));
        tf_pathPosting.textProperty().setValue(directoryChooser.getInitialDirectory().getPath());
        File postingDirectory = directoryChooser.showDialog(saveStage);
        tf_pathPosting.textProperty().setValue(postingDirectory.getPath());
        postingPath=tf_pathPosting.textProperty().get();
    }

    public MyModel getModel() {
        return model;
    }
}
