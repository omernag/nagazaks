package EngineUserInterface;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * This class is the controller of the result display
 */
public class ResultController {

    public ScrollPane sp_scroll;
    private MyModel model;

    /**
     * Sets the Result print on displaying board
     * @param model
     */
    public void displayResult(MyModel model){
        sp_scroll.setContent(new Text(model.getResultTOPrint()));
    }

    /** Saves the result to memory
     * @param event
     */
    public void saveResult(ActionEvent event) {
        Stage saveStage = new Stage();
        saveStage.setTitle("Save folder");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File saveDic = directoryChooser.showDialog(saveStage);
        if(saveDic!=null) {
            model.saveResult(saveDic.getPath());
        }
        else showAlert("Bad path, please try again");
    }

    /**
     * @param alertMessage
     */
    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    /**
     * @param model
     */
    public void setModel(MyModel model) {
        this.model=model;
    }
}
