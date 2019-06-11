package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView, Initializable {

    @FXML
    private MyViewModel viewModel;
    private Scene mainScene;
    private Stage mainStage;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.Label lbl_currRow;
    public javafx.scene.control.Label lbl_currCol;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.layout.Pane mazePane;
    public javafx.scene.layout.Pane mainPane;

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public StringProperty MusicFileName = new SimpleStringProperty();

    public boolean newMaze;
    public boolean solved;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);
        NewGameController newGame = new NewGameController();
        newGame.setViewModel(viewModel);
        btn_solveMaze.setDisable(true);

    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_currRow.textProperty().bind(characterPositionRow);
        lbl_currCol.textProperty().bind(characterPositionColumn);

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            if(viewModel.isSolved()){
                solved=true;
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
                if(solved){
                    btn_generateMaze.setDisable(false);
                }else{
                    btn_generateMaze.setDisable(true);
                }
            }

            if(viewModel.isFinished()){
                btn_solveMaze.setDisable(true);
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
            solved = false;

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
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MAZE files (*.maze)", "*.maze");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(loadStage);
            if(file!=null) {
                if (!file.getName().endsWith(".maze")) {
                    showAlert("The file you chose is not a Maze!");
                } else {
                    try {
                        FileInputStream inputMaze = new FileInputStream(file);
                        ObjectInputStream objectIn = new ObjectInputStream(inputMaze);
                        Maze mazeInput = (Maze) objectIn.readObject();
                        newMaze = true;
                        btn_solveMaze.setDisable(false);
                        viewModel.generateMazeFromFile(mazeInput);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        showAlert("The file you chose is not a Maze!");
                    }


                }
            }

    }

    public void pressSave()  {
        try {
            if(mazeDisplayer.gotmaze) {
                Stage saveStage = new Stage();
                saveStage.setTitle("Save Maze");
                Maze sMaze = viewModel.getMaze();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("NewMaze");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MAZE files (*.maze)", "*.maze");
                fileChooser.getExtensionFilters().add(extFilter);
                File mazeFile = fileChooser.showSaveDialog(saveStage);
                if (mazeFile != null) {
                    FileOutputStream fileOut = new FileOutputStream(mazeFile);
                    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                    objectOut.flush();
                    objectOut.writeObject(sMaze);
                    objectOut.close();
                }
            }
            else{
                showAlert("No Maze to save");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void pressExit() throws FileNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to save before leaving?");
        ButtonType btn_save = new ButtonType("Save and Exit");
        ButtonType btn_exit = new ButtonType("Exit without saving");
        ButtonType btn_stay = new ButtonType("I want to stay");

        alert.getButtonTypes().setAll(btn_save, btn_exit, btn_stay);
        Stage stage = (Stage) btn_solveMaze.getScene().getWindow();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btn_save){
            pressSave();
            viewModel.stopServers();
            stage.close();
            alert.close();


        } else if(result.get() == btn_exit){

            stage.close();
            alert.close();
        }
        else{
            alert.close();
        }

    }

    public void pressHelp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MAzE SurVivor");
        alert.setHeaderText("Help");
        alert.setContentText("Game Rules:\nGet you'r character out of the maze\nYou'r location is marked with an image of you'r character\n" +
                "The exit point is marked with Yellow Stones\nMove freely with the NUMPAD\n\nGood luck. You'll need it");
        alert.show();
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





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mazePane.prefHeightProperty().bind(mainPane.heightProperty());
        mazePane.prefWidthProperty().bind(mainPane.widthProperty());

    }
}
