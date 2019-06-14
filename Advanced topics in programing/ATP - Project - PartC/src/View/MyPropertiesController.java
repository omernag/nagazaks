package View;

import Server.Configurations;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MyPropertiesController implements Initializable {
    @FXML
    public ChoiceBox box_generate = new ChoiceBox();
    public ChoiceBox box_solve = new ChoiceBox();
    public ChoiceBox box_character = new ChoiceBox();
    public String genAlgo;
    public String solveAlgo;
    public String characterName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //generate
        box_generate.setValue("MyMazeGenerator");
        box_generate.getItems().addAll(
                "MyMazeGenerator",
                "SimpleMazeGenerator",
                "EmptyMazeGenerator"
        );
        //solve MyMazeGenerator
        box_solve.setValue("BestFirstSearch");
        box_solve.getItems().addAll(
                "BestFirstSearch",
                "BreadthFirstSearch",
                "DepthFirstSearch"
        );
        //character
        box_character.setValue("Dorothy");
        box_character.getItems().addAll(
                "Dorothy",
                "The Cowardly Lion",
                "The Tin Man",
                "Scarecrow"
        );
    }



    public void updateCharachter(){
        characterName= (String) box_character.getValue();

    }

    public void updateGenerateAlgo(){
        genAlgo= (String) box_generate.getValue();
        Configurations.setGeneratorAlgorithm(genAlgo);
    }
    public void updateSolveAlgo(){
        solveAlgo = (String) box_solve.getValue();
        Configurations.setSearchAlgorithm(solveAlgo);
    }



}