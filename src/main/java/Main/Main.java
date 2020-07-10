package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private static Stage primaryStageObj;
    private static Parent root;


    @Override
    public void start(Stage stage) throws Exception{
        primaryStageObj = stage;
        root = FXMLLoader.load(getClass().getResource("/fxml/newMainTest.fxml"));
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static Stage getStageObj(){
        return primaryStageObj;
    }

    public static Parent getRootObj(){
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}