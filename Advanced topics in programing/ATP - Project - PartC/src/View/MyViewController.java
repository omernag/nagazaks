package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView{

    private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.Label lbl_currRow;
    public javafx.scene.control.Label lbl_currCol;
    public javafx.scene.control.Button btn_generateMaze;

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);
        NewGameController newGame = new NewGameController();
        newGame.setViewModel(viewModel);
        mazeDisplayer=new MazeDisplayer();
    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_currRow.textProperty().bind(viewModel.characterPositionRow);
        lbl_currCol.textProperty().bind(viewModel.characterPositionColumn);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            btn_generateMaze.setDisable(false);//?
        }
    }

    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
            }
        });
    }

    public void pressNew(){
        Parent root;
        try {
            Stage stage = new Stage();
            stage.setTitle("New Game");
            FXMLLoader fxmlLoader = new FXMLLoader();
            root = fxmlLoader.load(getClass().getResource("../View/NewGame.fxml").openStream());
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

    public void pressLoad(){
            Stage loadStage = new Stage();
            loadStage.setTitle("Load Maze");
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(loadStage);
            //Generate Maze from file
    }

    public void pressSave(){

            Stage saveStage = new Stage();
            saveStage.setTitle("Load Maze");
            File mazefile = null;
            FileChooser fileChooser = new FileChooser();
            mazefile = fileChooser.showSaveDialog(saveStage);
            // write to the file


    }
    public void pressAbout(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../View/MyViewStyle.css").toExternalForm());
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressSolveMaze(ActionEvent actionEvent) {
    }


    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }


}
