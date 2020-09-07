package controllers;

import additionalClasses.LanguageSwitcher;
import animations.Shake;
import additionalClasses.SceneOpener;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Task;
import database.services.TaskService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.language.bm.Lang;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditTaskWindowController extends ControllerParent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private javafx.scene.layout.AnchorPane moovableAnchorPane;

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

    private Task task;

    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private SceneOpener sceneOpener = new SceneOpener();
    private LanguageSwitcher languageSwitcher;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public EditTaskWindowController(MainWindowController mainWindowController, Task task) {
        this.mainWindowController = mainWindowController;
        this.task = task;
        this.languageSwitcher = mainWindowController.getLanguageSwitcher();
    }

    @FXML
    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);

        taskTextfield.setPromptText(languageSwitcher.getTextOfTask());
        inQueenRadioButton.setText(languageSwitcher.getInQueueTaskType());
        inWorkRadioButton.setText(languageSwitcher.getInWorkTaskType());
        createButton.setText(languageSwitcher.getCreateNewTask());

        String stylesheetRadioButtons = getClass().getResource("/styles.css").toExternalForm();
        inWorkRadioButton.getStylesheets().add(stylesheetRadioButtons);
        inQueenRadioButton.getStylesheets().add(stylesheetRadioButtons);

        taskTextfield.getStylesheets().add(uiColorAndStyleSettings.getDefaultStyleWithBorder());

        this.setStylesToButtons();

        if (task.getTasktype() != "quene") {
            inQueenRadioButton.fire();
        } else {
            inWorkRadioButton.fire();
        }

        taskTextfield.setText(task.getText());


        createButton.setOnAction(e -> {
            if (checkTaskRegisterNewTask() == true) {
                mainWindowController.initialize();
                createButton.getScene().getWindow().hide();
            }
        });
    }

    public boolean checkTaskRegisterNewTask() {
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

            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(taskText);

            if (list.size() >= 1) {
                AlertTaskWindowController alertTaskWindowController =
                        new AlertTaskWindowController(this.mainWindowController);
                Stage alertTaskWindowStage = new Stage();
                sceneOpener.openNewScene("/fxml/alertTaskBoxWindow.fxml", alertTaskWindowStage,
                        (Stage) closeButton.getScene().getWindow(), alertTaskWindowController, false);
                return false;
            } else {
                this.task.setText(taskTextfield.getText());
                this.task.setTasktype(tasktype);
                taskService.updateTask(task);
                mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
                mainWindowController.initialize();
                return true;
            }
        }
    }

    @Override
    public void setStylesToButtons(){
        uiColorAndStyleSettings.setImageToButton(closeButton, "cross.png", 11,20);
        uiColorAndStyleSettings.setImageToButton(minimiseButton, "minimize.png", 13,40);
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
        uiColorAndStyleSettings.setButtonStyles(createButton);
    }
}

