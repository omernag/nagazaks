package View;

import ViewModel.MyViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class NewGameController implements Initializable {

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
        else if(rows>500 || columns>500){
            showAlert("Wow that is big! maybe try a bit smaller maze");
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rowsTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    rowsTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        colsTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    colsTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

    }
}
