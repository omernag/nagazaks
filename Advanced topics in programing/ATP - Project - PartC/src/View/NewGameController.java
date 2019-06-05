package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


public class NewGameController {

    private static MyViewModel viewModel;
    int NumOfRows=0;
    int NumOfCols=0;
    @FXML
    TextField rowsTextfield;
    @FXML
    TextField colsTextfield;



    public void generateMaze(){
        if(rowsTextfield==null || colsTextfield==null){
            showAlert("Wrong input, please enter a valid number!");
        }
        int rows = Integer.valueOf(rowsTextfield.getText());
        int columns = Integer.valueOf(colsTextfield.getText());
        if(rows<=0 || columns<=0){
            showAlert("One of the numbers you've entered is not positive, please try again");
        }
        //call maze generator
        viewModel.generateMaze(rows,columns);
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
