package Main;

import controllers.FinTaskRatingWindowController;
import controllers.newMainTestController;
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



        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginWindow.fxml"));

//        root = FXMLLoader.load(getClass().getResource("/fxml/newMainTest.fxml"));

//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/fxml/newMainTest.fxml"));
//        loader.setController(new newMainTestController(stage));

        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        System.out.println("+++++++++++++++++");
        System.out.println(this.getClass().getPackage());
        System.out.println("+++++++++++++++++");

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