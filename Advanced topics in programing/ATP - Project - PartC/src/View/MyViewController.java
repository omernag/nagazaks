package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyViewController implements IView{
    public void preesButton(ActionEvent event){
        System.out.println("Hellsssaa");
    }

    public void pressNew(ActionEvent newMaze){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../View/NewGame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("New Game");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
