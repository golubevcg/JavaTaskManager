package Main;

import controllers.LoginWindowController;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new LoginWindowController());
        loader.setLocation(getClass().getResource("/fxml/loginWindow.fxml"));
        loader.load();
        root = loader.getRoot();
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