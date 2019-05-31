package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.mazeWriter;
import java.io.IOException;
import java.nio.file.Files;

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
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../View/NewGameStyle.css").toExternalForm());
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressLoad(ActionEvent LoadMaze){
            Stage loadStage = new Stage();
            loadStage.setTitle("Load Maze");
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(loadStage);
            //Generate Maze from file
    }

    private void pressSave(String content){

            Stage saveStage = new Stage();
            saveStage.setTitle("Load Maze");
            File mazefile = null;
            FileChooser fileChooser = new FileChooser();
            mazefile = fileChooser.showSaveDialog(saveStage);
            // write to the file


    }




}
