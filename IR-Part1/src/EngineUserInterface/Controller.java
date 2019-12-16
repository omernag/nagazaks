package EngineUserInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    public void pressConnect(ActionEvent event) {
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
            e.printStackTrace();
        }
    }

    public void pressLoad(ActionEvent event) {
        try {
            model=connectC.getModel();
            model.bringUpDictionary();
            if (connectC.getModel().getDictionary() != null)
                tf_status.textProperty().setValue("Ready");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Cant find posting in this path");
            tf_status.textProperty().setValue("Failed to load");
        }
    }

    public void pressDisplay(ActionEvent event) {
        //if(tf_status.textProperty().getValue().equals("Ready")) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
        //else{
          //  showAlert("Please Load a Dictionary first");
        //}

    }

    public void pressStemming(ActionEvent event) {
        model.changeStemmerMode();
    }

    public void pressClear(ActionEvent event) throws IOException {
        if(!tf_status.textProperty().getValue().equals("Waiting")) {
            model=connectC.getModel();
            File file = new File(model.getPostingPa());
            if (file.isDirectory()) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        Files.walk(Paths.get(model.getPostingPa()))
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                    }
                }
            }
            Files.delete(Paths.get(model.getPostingPa()));
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
