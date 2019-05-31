package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.model = model;
    }

    public void update(Observable o, Object arg) {   //activated when the model changes. here the VM will ask for data from the Model

    }
}
