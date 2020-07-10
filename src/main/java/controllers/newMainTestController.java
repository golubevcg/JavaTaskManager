package controllers;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


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

    //event to make window closable (connected to button in fxml)
    @FXML void close(ActionEvent event){
        System.exit(0);
    }

    //event to make window collapsable (connected to button in fxml)
    @FXML void min(ActionEvent event){
        ((Stage)(minimiseButton.getScene().getWindow())).setIconified(true);
    }

    private double xOffset;
    private double yOffset;

    @FXML
    void initialize() {

        //make window movable during pressing on top main Anchor Pane
        mainAnchorPane.setOnMousePressed(e->{
           xOffset = e.getSceneX();
           yOffset = e.getSceneY();
        });
        mainAnchorPane.setOnMouseDragged(e->{
            Main.getStageObj().setX(e.getScreenX() - xOffset);
            Main.getStageObj().setY(e.getScreenY() - yOffset);
        });



    }
}
