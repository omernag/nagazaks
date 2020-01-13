package EngineUserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.*;


public class DisplayController {
    public ScrollPane sp_scroll;
    private MyModel model;

    public void displayDictionary(MyModel model){
        sp_scroll.setContent(new Text(model.getDictionaryToPrint()));
    }

    public void setModel(MyModel model) {
        this.model=model;
    }
}
