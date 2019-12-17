package EngineUserInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;


public class Controller {

    @FXML
    public Button btn_ConnectToEn;
    public Button btn_loadDict;
    public Button btn_displayDict;
    public CheckBox cbx_stemming;
    public Button btn_clear;
    public Label lbl_status;
    public BorderPane mainPane;
    public Label tf_status;

    public MyModel model;
    public ConnectController connectC;
    public DisplayController dispC;
    public boolean isStemmer;
    private boolean corpusPathSet;
    private boolean postingPathSet;
    public String corpusPath;
    public String postingPath;

    public void pressSet(ActionEvent event) {
        Parent root;
        try {
            Stage stage = new Stage();
            stage.setTitle("Connect to Engine");
            FXMLLoader fxmlLoader = new FXMLLoader();
            root = fxmlLoader.load(getClass().getResource("/EngineUserInterface/ConnectView.fxml").openStream());
            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/EngineUserInterface/ConnectStyle.css").toExternalForm());
            stage.show();
            connectC = fxmlLoader.getController();
            connectC.setModel(model);
            tf_status.textProperty().setValue("Connected");
        } catch (IOException e) {
        }
    }

    public void pressConnect(ActionEvent event) throws IOException {
        if(model.getPostingPa()!=null&&model.getCorpusPa()!=null) {
            model.connectToCorpus(model.getCorpusPa(), model.getPostingPa());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(model.getInfoOnRun());
            alert.show();
        }
        else{
            showAlert("Please Insert Address");
        }
    }

    public void pressLoad(ActionEvent event) {
        try {
            model=connectC.getModel();
            model.bringUpDictionary();
            if (connectC.getModel().getDictionary() != null)
                tf_status.textProperty().setValue("Ready");
        } catch (IOException e) {
            showAlert("Cant find posting in this path");
            tf_status.textProperty().setValue("Failed to load");
        }
    }

    public void pressDisplay(ActionEvent event) {
        if(model.getDictionary()!=null) {
            Parent root;
            try {
                Stage stage = new Stage();
                stage.setTitle("Dictionary");
                FXMLLoader fxmlLoader = new FXMLLoader();
                root = fxmlLoader.load(getClass().getResource("/EngineUserInterface/DisplayView.fxml").openStream());
                Scene scene = new Scene(root, 500, 300);
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/EngineUserInterface/DisplayStyle.css").toExternalForm());
                stage.show();
                dispC = fxmlLoader.getController();
                dispC.setModel(model);
                dispC.displayDictionary(model);
            } catch (Exception e) {
            }
        }
        else{
            showAlert("Please Load a Dictionary first");
        }

    }

    public void pressStemming(ActionEvent event) {
        model.changeStemmerMode();
    }

    public void pressClear(ActionEvent event) throws IOException {
        if(model.getPostingPa()!=null) {
            model=connectC.getModel();
            File file;
            if(model.isStemmer) {
                 file = new File(model.getPostingPa()+"/Posting_s/");
            }
            else{
                 file = new File(model.getPostingPa()+"/Posting/");
            }
            if (file.isDirectory()) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                       entry.delete();
                    }
                }
            }
            file.delete();
            //Files.delete(Paths.get(file.getPath()));
            tf_status.textProperty().setValue("Connected. Posting and dictionary erased");
            model.forgetDictionary();
        }
        else showAlert("Please insert the path to the Posting files");
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setModel(MyModel model) {
        this.model = model;
    }
}
