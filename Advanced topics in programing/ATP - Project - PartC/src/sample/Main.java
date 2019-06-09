package sample;

import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {
    MyModel model;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.setTitle("MAzE SurViVoRS");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("../View/MyViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //--------------
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }


    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    model.stopServers();
                    // ... user chose OK
                    // Close program
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }

            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
