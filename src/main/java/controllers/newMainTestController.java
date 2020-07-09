package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class newMainTestController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private TextArea textArea;

    @FXML
    private AnchorPane AcnhorPaneInQuene;

    @FXML
    private AnchorPane AcnhorPaneInWork;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private Menu firstItemMenu;

    @FXML
    private Button closeButton;

    @FXML
    private Button addNewWorkerButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button fullScreenButton;



    @FXML void close(ActionEvent event){
        System.exit(0);
    }

    @FXML void min(ActionEvent event){
        ((Stage)(minimiseButton.getScene().getWindow())).setIconified(true);
    }

    private double xOffset;
    private double yOffset;


    @FXML
    void initialize() {

//        Stage mainStage = ((Stage)(minimiseButton.getScene().getWindow()));


//        mainAnchorPane.getScene().getRoot().setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                xOffset = mouseEvent.getSceneX();
//                yOffset = mouseEvent.getSceneY();
//            }
//        });
//
//        mainAnchorPane.getScene().getRoot().setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                mainAnchorPane.getScene().getWindow().setX(mouseEvent.getScreenX() - xOffset);
//                mainAnchorPane.getScene().getWindow().setY(mouseEvent.getScreenY() - yOffset);
//            }
//        });

//        Main.getRootObj()


//
//        mainAnchorPane.setOnMousePressed(e->{
//            xOffset = mainAnchorPane.getScene().getWindow().getX() - e.getSceneX();
//            yOffset = mainAnchorPane.getScene().getWindow().getY() - e.getSceneY();
//        });
//
//        mainAnchorPane.setOnMouseDragged(e->{
//            mainAnchorPane.getScene().getWindow().setX(e.getSceneX() + xOffset);
//            mainAnchorPane.getScene().getWindow().setY(e.getSceneY() + yOffset);
//        });



    }
}
