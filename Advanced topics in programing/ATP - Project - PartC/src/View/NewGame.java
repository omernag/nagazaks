package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGame {

    int NumOfRows=0;
    int NumOfCols=0;
    @FXML
    TextField rowsTextfield;
    @FXML
    TextField colsTextfield;




    @FXML
    public int getRowsNum(ActionEvent inputRows){
        String rows=rowsTextfield.getText();//this is the problem
        if ((rows != null && Integer.parseInt(rows)>0)) {
           // label.setText(name.getText() + " " + lastName.getText() + ", "
                 //   + "thank you for your comment!");
            return Integer.parseInt(rows);
        } else {
           // label.setText("You have not left a comment.");
        }
        return Integer.parseInt(rows);
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }
}
