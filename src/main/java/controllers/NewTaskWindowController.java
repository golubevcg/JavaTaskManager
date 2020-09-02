package controllers;

import animations.Shake;
import additionalClasses.SceneOpener;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Task;
import database.Worker;
import database.services.TaskService;
import database.services.WorkerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewTaskWindowController extends ControllerParent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private javafx.scene.layout.AnchorPane AnchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

    @FXML
    private TextField taskTextfield;

    @FXML
    private Button createButton;

    @FXML
    private RadioButton inQueenRadioButton;

    @FXML
    private ToggleGroup tgroup;

    @FXML
    private RadioButton inWorkRadioButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;

    @FXML void close(ActionEvent event){
        stage.close();
        mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
        mainWindowController.initialize();
    }

    @FXML void min(ActionEvent event){
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }


    private MainWindowController mainWindowController;
    private double xOffset;
    private double yOffset;
    private Worker worker;
    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();

    public NewTaskWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);
        this.setStylesToButtons();

        inQueenRadioButton.fire();
        inQueenRadioButton.getStylesheets().add( getClass().getResource("/styles.css").toExternalForm() );

        createButton.setOnAction(e->{
                if(checkTask()==true) {
                    mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
                    mainWindowController.initialize();
                    createButton.getScene().getWindow().hide();
                }
        });
    }

    public boolean checkTask() {
        String taskText;
        taskText = taskTextfield.getText();
        if (taskText.isEmpty()) {
            Shake taskTextfield = new Shake(this.taskTextfield);
            taskTextfield.playAnim();
            return false;
        } else {
            String tasktype;
            if (inQueenRadioButton.isSelected() == true) {
                tasktype = "quene";
            } else {
                tasktype = "inwork";
            }
            Task task = new Task(taskText, this.worker, tasktype);
            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(taskText);

            if (list.size() >= 1) {
                AlertTaskWindowController alertTaskWindowController = new AlertTaskWindowController();
                SceneOpener sceneOpener = new SceneOpener();
                Stage alertTaskWindowStage = new Stage();
                sceneOpener.openNewScene("/fxml/alertTaskBoxWindow.fxml", alertTaskWindowStage,
                        (Stage) closeButton.getScene().getWindow(),
                        alertTaskWindowController, false);
                return false;
            } else {
                taskService.saveTask(task);
                worker.addTask(task);
                WorkerService workerService = new WorkerService();
                workerService.updateWorker(worker);
                return true;
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setStylesToButtons(){
        uiColorAndStyleSettings.setImageToButton(closeButton, "cross.png", 11,20);
        uiColorAndStyleSettings.setImageToButton(minimiseButton, "minimize.png", 13,40);
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
        uiColorAndStyleSettings.setButtonStyles(createButton);
    }

}

