package EngineUserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.*;


/**
 * This class is the controller for the dictionary display
 */
public class DisplayController {
    public ScrollPane sp_scroll;
    private MyModel model;

    /**
     * Puts the Dictionary on the Scrolling pane
     * @param model
     */
    public void displayDictionary(MyModel model){
        sp_scroll.setContent(new Text(model.getDictionaryToPrint()));
    }

    /**
     * @param model
     */
    public void setModel(MyModel model) {
        this.model=model;
    }
}
