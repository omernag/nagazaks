package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView{

    //private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.Label lbl_currRow;
    public javafx.scene.control.Label lbl_currCol;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public boolean newMaze;


    @FXML
    private MyViewModel viewModel;
    private Scene mainScene;
    private Stage mainStage;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);
        NewGameController newGame = new NewGameController();
        newGame.setViewModel(viewModel);

    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_currRow.textProperty().bind(characterPositionRow);
        lbl_currCol.textProperty().bind(characterPositionColumn);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            if(viewModel.isSolved()){
                displaySolution(viewModel.getSolution());
                btn_generateMaze.setDisable(false);
                btn_solveMaze.setDisable(true);
            }
            else if(viewModel.isFinished() && viewModel.isMoved()){ //just finished congratulations
                displayMaze(viewModel.getMaze());
                showAlert("Congratulations - you are the BEST!");
                btn_generateMaze.setDisable(false);

            }
            else if (viewModel.isFinished() && !viewModel.isMoved()){ //already finished
                showAlert("You've already finished \nyou can always start a new game!");
            }
            else if (!viewModel.isFinished() && !viewModel.isMoved()){ //illegal move
                showAlert("Cant do that :(");
            }
            else {
                displayMaze(viewModel.getMaze());
                btn_generateMaze.setDisable(true);
            }
        }
    }

    private void displaySolution(Solution solution) {
        mazeDisplayer.setSolution(solution);
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    @Override
    public void displayMaze(Maze maze) {


        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        if(newMaze){
            mazeDisplayer.setMaze(maze);
            mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);

        }
        else{
            mazeDisplayer.resetCharacterPosition(characterPositionRow,characterPositionColumn);
        }
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

    public void pressNew(ActionEvent event){
        newMaze=true;
        btn_solveMaze.setDisable(false);

        Parent root;
        try {
            Stage stage = new Stage();

            Region veil = new Region();
            veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3)");
            veil.setVisible(true);

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

    public void pressProperties(){

        Parent root;
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            root = fxmlLoader.load(getClass().getResource("../View/Properties.fxml").openStream());
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../View/MyViewStyle.css").toExternalForm());
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressExit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to save before leaving?");
        alert.showAndWait();

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
        viewModel.solveSearchProblem();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        newMaze=false;
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
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

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }


}
