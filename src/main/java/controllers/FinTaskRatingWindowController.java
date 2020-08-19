package controllers;

import classes.SceneOpener;
import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.Task;
import database.services.TaskService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FinTaskRatingWindowController extends ControllerParent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private Button confirmButton;

    @FXML
    private RadioButton buttonRating3;

    @FXML
    private RadioButton buttonRating2;

    @FXML
    private RadioButton buttonRating1;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML void close(ActionEvent event){
        stage.close();
        mainWindowController.initialize();
    }

    @FXML void min(ActionEvent event){
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }

    private Task task;
    private MainWindowController mainWindowController;
    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();

    public FinTaskRatingWindowController(Task task, MainWindowController mainWindowController) {
        this.task = task;
        this.mainWindowController = mainWindowController;
    }

    @FXML
    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);
        this.setStylesToButtons();


        ToggleGroup group = new ToggleGroup();
        buttonRating1.setToggleGroup(group);
        buttonRating2.setToggleGroup(group);
        buttonRating3.setToggleGroup(group);

        String pathToCss = getClass().getResource("/styles.css").toExternalForm();
        buttonRating1.getStylesheets().add(pathToCss);
        buttonRating2.getStylesheets().add(pathToCss);
        buttonRating3.getStylesheets().add(pathToCss);

        confirmButton.setOnAction(e->{

                if(group.getSelectedToggle()==(null)){
                    NoRatingErrorWindowController noRatingErrorWindowController = new NoRatingErrorWindowController();
                    SceneOpener sceneOpener = new SceneOpener();
                    sceneOpener.showAlertBox("/fxml/noRatingErrorWindow.fxml", (Stage) closeButton.getScene().getWindow(), noRatingErrorWindowController);
                }else{
                    RadioButton rb = (RadioButton)group.getSelectedToggle();
                    updateTaskStatus(Integer.parseInt(rb.getText()));
                    mainWindowController.initialize();
                    confirmButton.getScene().getWindow().hide();
                }
        });


    }

    @Override
    public void setStylesToButtons() {
       uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
       uiColorAndStyleSettings.setButtonStyles(confirmButton);
    }

    private void updateTaskStatus(int value){
        task.setTasktype("done");
        task.setRating(value);
        TaskService taskService = new TaskService();
        taskService.updateTask(task);
        this.initialize();
    }

}
