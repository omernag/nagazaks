package EngineUserInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;


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
    public Button btn_Set;
    public Button btn_search;

    public MyModel model;
    public ConnectController connectC;
    public DisplayController dispC;
    private SearchController searchC;

    public boolean isStemmer;
    private boolean corpusPathSet;
    private boolean postingPathSet;
    private boolean postingErased;
    public String corpusPath;
    public String postingPath;


    public void pressSet(ActionEvent event) {
        Parent root;
        try {
            Stage stage = new Stage();
            stage.setTitle("Connect to Engine");
            FXMLLoader fxmlLoader = new FXMLLoader();
            root = fxmlLoader.load(getClass().getResource("/ConnectView.fxml").openStream());
            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/ConnectStyle.css").toExternalForm());
            stage.show();
            connectC = fxmlLoader.getController();
            connectC.setModel(model);
            tf_status.textProperty().setValue("Connected");

            //if(model.isPostingPathSet()){
                btn_loadDict.setDisable(false);
                btn_clear.setDisable(false);
            //}
            //if(model.isCorpusPathSet()) {
                btn_ConnectToEn.setDisable(false);
            //}
            cbx_stemming.setDisable(false);
        } catch (Exception e) {
        }
    }

    public void pressConnect(ActionEvent event) throws IOException {
        if(model.getPostingPa()!=null&&model.getCorpusPa()!=null) {
            model.connectToCorpus(model.getCorpusPa(), model.getPostingPa());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(model.getInfoOnRun());
            alert.show();
            postingErased=false;
            btn_search.setDisable(false);
            btn_displayDict.setDisable(false);
            btn_clear.setDisable(false);
        }
        else{
            showAlert("Please Insert Address",Alert.AlertType.ERROR);
        }
    }

    public void pressLoad(ActionEvent event) {
        try {
            model=connectC.getModel();
            if(model.getDictionary()==null){
                model.initializeDictionary();
            }
            model.bringUpDictionary();
            if (model.getDictionary() != null) {
                tf_status.textProperty().setValue("Ready");
            }
            btn_search.setDisable(false);
            btn_displayDict.setDisable(false);
        } catch (Exception e) {
            showAlert("Cant find posting in this path",Alert.AlertType.ERROR);
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
                root = fxmlLoader.load(getClass().getResource("/DisplayView.fxml").openStream());
                Scene scene = new Scene(root, 500, 300);
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/DisplayStyle.css").toExternalForm());
                stage.show();
                dispC = fxmlLoader.getController();
                dispC.setModel(model);
                dispC.displayDictionary(model);
            } catch (Exception e) {
            }
        }
        else{
            showAlert("Please Load a Dictionary first",Alert.AlertType.ERROR);
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
            model.setPostingPa(null);
            postingErased=true;
            showAlert("Posting and dictionary erased ",Alert.AlertType.INFORMATION);
        }
        else if(postingErased){
            showAlert("Posting has already been erased", Alert.AlertType.ERROR);
        }
        else showAlert("Please insert the path to the Posting files",Alert.AlertType.ERROR);
    }
    
    public void openSearchWindow(ActionEvent event){
        Parent root;
        try {
            Stage stage = new Stage();
            stage.setTitle("Search the Corpus");
            FXMLLoader fxmlLoader = new FXMLLoader();
            root = fxmlLoader.load(getClass().getResource("/SearchView.fxml").openStream());
            Scene scene = new Scene(root, 500, 400);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/SearchStyle.css").toExternalForm());
            stage.show();
            searchC = fxmlLoader.getController();
            searchC.setModel(model);
            //tf_status.textProperty().setValue("Connected");
        } catch (Exception e) {
        }
    }

    private void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setModel(MyModel model) {
        this.model = model;
        postingErased=false;
        btn_ConnectToEn.setDisable(true);
        btn_clear.setDisable(true);
        btn_displayDict.setDisable(true);
        btn_search.setDisable(true);
        btn_loadDict.setDisable(true);
        cbx_stemming.setDisable(true);
    }
}
