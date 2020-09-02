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

        public void openNewScene(String windowName, Stage newStage, Stage currentStage, ControllerParent controller, boolean CloseInitialWindow){

        double currentWindowXCenter = currentStage.getX() + (currentStage.getWidth()/2);
        double currentWindowYCenter = currentStage.getY() + (currentStage.getHeight()/2);

        if(CloseInitialWindow) {
            currentStage.hide();
        }

        if(!newStage.isShowing()) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(windowName));
            loader.setController(controller);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
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

            newStage.show();

            newStage.setX(currentWindowXCenter - newStage.getWidth()/2);
            newStage.setY(currentWindowYCenter - newStage.getHeight()/2);

        }else{
            newStage.toFront();
            newStage.setX(currentWindowXCenter - newStage.getWidth()/2);
            newStage.setY(currentWindowYCenter - newStage.getHeight()/2);
        }

    }


}
