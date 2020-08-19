package classes;

import controllers.ControllerParent;
import controllers.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SceneOpener {

    public void openNewScene(String windowName, Stage currentStage, ControllerParent controller, boolean CloseInitialWindow){
        if(CloseInitialWindow) {
            currentStage.hide();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowName));
        loader.setController(controller);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        newStage.setScene(scene);
        controller.setStage(newStage);

        if(controller.getClass()==(MainWindowController.class)){
            UndecoratedWindowsResizer fxResizeHelper = new UndecoratedWindowsResizer();
            fxResizeHelper.addResizeListener(newStage);
        }
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initStyle(StageStyle.TRANSPARENT);

//        double centerXPositionOfMainScene = newStage.getX() - newStage.getWidth()/2d;
//        double centerYPositionOfMainScene = newStage.getY() - newStage.getHeight()/2d;
//        newStage.setOnShown(ev -> {
//            newStage.setX(centerXPositionOfMainScene - newStage.getWidth()/2d);
//            newStage.setY(centerYPositionOfMainScene - newStage.getHeight()/2d);
//            newStage.show();
//        });
        newStage.show();

    }

    public void showAlertBox(String windowName, Stage currentStage, ControllerParent controller){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowName));
        loader.setController(controller);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        newStage.setScene(scene);
        controller.setStage(newStage);

        double centerXPositionOfMainScene = currentStage.getX() - currentStage.getWidth()/2d;
        double centerYPositionOfMainScene = currentStage.getY() - currentStage.getHeight()/2d;
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setOnShown(ev -> {
            newStage.setX(centerXPositionOfMainScene - newStage.getWidth()/2d);
            newStage.setY(centerYPositionOfMainScene - newStage.getHeight()/2d);
            newStage.show();
        });
        newStage.show();
    }
}
