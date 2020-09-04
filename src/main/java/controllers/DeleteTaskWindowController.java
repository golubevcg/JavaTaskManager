package controllers;

import additionalClasses.LanguageSwitcher;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Task;
import database.services.TaskService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DeleteTaskWindowController extends ControllerParent {

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    private MainWindowController mainWindowController;

    private Task task;

    @FXML
    private Label areYouShureLabel;

    @FXML
    private Label taskLabel;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    LanguageSwitcher languageSwitcher;


    public DeleteTaskWindowController(MainWindowController mainWindowController, Task task) {
        this.mainWindowController = mainWindowController;
        this.task = task;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {

        languageSwitcher = mainWindowController.getLanguageSwitcher();

        areYouShureLabel.setText(languageSwitcher.getAreYouShureThatYouWantToDelete());
        taskLabel.setText(languageSwitcher.getTask());

        yesButton.setText(languageSwitcher.getYes());
        noButton.setText(languageSwitcher.getNo());

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(AnchorPane);

        yesButton.setOnAction(e->{
            TaskService taskService = new TaskService();
            taskService.deleteTask(task);
            task.getWorker().deleteTask(task);
            yesButton.getScene().getWindow().hide();
            mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
            mainWindowController.initialize();
        });

        noButton.setOnAction(e->{
            noButton.getScene().getWindow().hide();
        });

        uiColorAndStyleSettings.setButtonStyles(noButton,yesButton);

        AnchorPane.setStyle("-fx-background-color: "+ uiColorAndStyleSettings.getMainBGUiColor() + ";" +
                "-fx-border-color:" + uiColorAndStyleSettings.getMainUiBordersColor() + ";" +
                "-fx-background-insets: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;");

    }

}
