package EngineUserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * This class is the controller of the Connect procedure
 */
public class ConnectController {

    @FXML
    public Label lbl_pathCorpus;
    public Label lbl_pathPosting;
    public TextField tf_pathCorpus;
    public TextField tf_pathPosting;
    public Button btn_browseCorpus;
    public Button btn_browsePosting;
    public AnchorPane pane;
    public Button btn_accept;

    private String corpusPath;
    private String postingPath;
    private MyModel model;
    private boolean corpusPathSet;
    private boolean postingPathSet;

    /**
     * Browse path to corpus directory
     * @param event
     */
    public void browseCorpus(ActionEvent event) {
        corpusPathSet=false;
        Stage saveStage = new Stage();
        saveStage.setTitle("Corpus folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //directoryChooser.setInitialDirectory(new File("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1"));
        File corpusDirectory = directoryChooser.showDialog(saveStage);
        if(corpusDirectory!=null) {
            tf_pathCorpus.textProperty().setValue(corpusDirectory.getPath());
            corpusPath = tf_pathCorpus.textProperty().get();
            corpusPathSet = true;
            model.setCorpusPa(corpusPath);
            model.setCorpusPathSet(true);
        }
        btn_accept.setDisable(false);
    }

    /**
     * Enter Corpus directory manually
     * @param event
     */
    public void typeCorpusPath(ActionEvent event) {
        corpusPathSet=false;
        corpusPath = tf_pathCorpus.textProperty().get();
        corpusPathSet = true;
        model.setCorpusPa(corpusPath);
        model.setCorpusPathSet(true);
        btn_accept.setDisable(false);
    }

    /**
     * Browse path to Posting directory
     * @param event
     */
    public void browsePosting(ActionEvent event) {
        postingPathSet=false;
        Stage saveStage = new Stage();
        saveStage.setTitle("Posting folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //directoryChooser.setInitialDirectory(new File("C:\\Users\\Asi Zaks\\Documents\\GitHub\\nagazaks\\IR-Part1\\Posting"));
        File postingDirectory = directoryChooser.showDialog(saveStage);
        if(postingDirectory!=null) {
            tf_pathPosting.textProperty().setValue(postingDirectory.getPath());
            postingPath = tf_pathPosting.textProperty().get();
            postingPathSet = true;
            model.setPostingPa(postingPath);
            model.setPostingPathSet(true);
        }
        btn_accept.setDisable(false);
    }

    /**
     * Enter Posting directory manually
     * @param event
     */
    public void typePostingPath(ActionEvent event) {
        postingPathSet=false;
        postingPath = tf_pathPosting.textProperty().get();
        postingPathSet = true;
        model.setPostingPa(postingPath);
        model.setPostingPathSet(true);
        btn_accept.setDisable(false);
    }

    /**
     * @return model object
     */
    public MyModel getModel() {
        return model;
    }

    /**
     * Closes the current window
     * @param event
     */
    public void pressAccept(ActionEvent event) {
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
    }

    /**
     * Initializes the model
     * @param model
     */
    public void setModel(MyModel model) {
        this.model = model;
        corpusPathSet=false;
        postingPathSet=false;
        btn_accept.setDisable(true);
    }

}
