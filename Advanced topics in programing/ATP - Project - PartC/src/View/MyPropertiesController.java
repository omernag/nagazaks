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
    public CheckBox check_mute = new CheckBox();

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
        box_character.setValue("Doroty");
        box_character.getItems().addAll(
                "Highest",
                "High",
                "Normal",
                "Low",
                "Lowest"
        );
    }

    public void mutePressed(ActionEvent event){

    }

    public void updateCharachter(){}

    public void updateGenerateAlgo(){
        //Configurations.setGeneratorAlgorithm();
    }
    public void updateSolveAlgo(){
        //Configurations.setSearchAlgorithm();
    }



}