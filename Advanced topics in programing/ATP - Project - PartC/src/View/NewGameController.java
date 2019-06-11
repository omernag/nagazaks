package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class NewGameController {

    private static MyViewModel viewModel;
    int NumOfRows=0;
    int NumOfCols=0;
    @FXML
    TextField rowsTextfield;
    @FXML
    TextField colsTextfield;
    private Scene mainScene;
    private Stage mainStage;


    public void generateMaze(ActionEvent event){
        if(rowsTextfield==null || colsTextfield==null){
            showAlert("Wrong input, please enter a valid number!");
        }
        int rows = Integer.valueOf(rowsTextfield.getText());
        int columns = Integer.valueOf(colsTextfield.getText());
        if(rows<10 || columns<10){
            showAlert("Please insert Integers bigger than 9");
            ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();

        }
        //call maze generator
        else {
            ((Stage) ((Node) (event.getSource())).getScene().getWindow()).close();
            viewModel.generateMaze(rows, columns);
        }


    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }


}
