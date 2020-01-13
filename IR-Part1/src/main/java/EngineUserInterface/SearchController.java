package EngineUserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.AbstractMap;

public class SearchController {

    public AnchorPane pane;
    public Label lbl_query;
    public Label lbl_pathQueryFile;
    public TextField tf_query;
    public TextField tf_pathQueries;
    public Button btn_goBig;
    public Button btn_go;
    public Button btn_browseQueries;
    public CheckBox cbox_findentities;
    public CheckBox cbox_semantic;
    public CheckBox cbox_trecEval;

    private String currentQuery;
    private MyModel model;
    private String queryFilePath;
    private boolean semanticTreat;
    private boolean findEntities;
    private boolean trecEval;
    private ResultController resultC;

    public void enterQuery(){
        currentQuery = tf_query.textProperty().get();
    }

    public void singleQuery(ActionEvent event){
        enterQuery();
        if(currentQuery!=null){
            if(currentQuery.length()==0){
                showAlert("Your query is empty, please don't..");
                return;
            }
            else{
                model.handleSingleQuery(new AbstractMap.SimpleEntry<>((""+111),currentQuery),findEntities,semanticTreat,trecEval);
                openResultWindow();
            }
        }
        else {
            showAlert("Please enter a query, and then press GO");
        }

    }

    public void typeQueryPath(){
        queryFilePath = tf_pathQueries.textProperty().get();
    }

    public void browseQueries(ActionEvent event){
        Stage browse = new Stage();
        browse.setTitle("Queries File");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Query Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = null;
        try {
            file = fileChooser.showOpenDialog(browse);
        }catch (Exception e){}
        if(file!=null && file.exists()) {
            tf_pathQueries.textProperty().setValue(file.getPath());
            queryFilePath = tf_pathQueries.textProperty().get();
        }
    }

    public void pressGoBig(ActionEvent event) {
        if(queryFilePath==null || queryFilePath.length()==0){
            typeQueryPath();
            if(queryFilePath==null || queryFilePath.length()==0){
                showAlert("Please choose a file by browsing or typing the path, and then press Go Big");
                return;
            }
        }
        model.handleQueryFile(queryFilePath,findEntities,semanticTreat,trecEval);
        openResultWindow();
    }

    private void openResultWindow() {
        if(model.getResult()!=null) {
            Parent root;
            try {
                Stage stage = new Stage();
                stage.setTitle("Result Log");
                FXMLLoader fxmlLoader = new FXMLLoader();
                root = fxmlLoader.load(getClass().getResource("/ResultView.fxml").openStream());
                Scene scene = new Scene(root, 500, 600);
                stage.setScene(scene);
                scene.getStylesheets().add(getClass().getResource("/ResultStyle.css").toExternalForm());
                stage.show();

                resultC = fxmlLoader.getController();
                resultC.setModel(model);
                resultC.displayResult(model);
            } catch (Exception e) {
            }
        }
    }

    public void closeWindow(ActionEvent event) {
        ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
    }

    public MyModel getModel() {
        return model;
    }

    public void setModel(MyModel model) {
        this.model = model;
        findEntities=false;
        semanticTreat=false;
        trecEval=false;
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void checkBoxEntities(ActionEvent event) {
        findEntities=!findEntities;
    }

    public void checkBoxSemantic(ActionEvent event) {
        semanticTreat=!semanticTreat;
    }

    public void checkBoxTrec(ActionEvent event) { trecEval=!trecEval; }
}
