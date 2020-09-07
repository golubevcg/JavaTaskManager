package controllers;

import additionalClasses.LanguageSwitcher;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Task;
import database.Worker;
import database.services.WorkerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class DeleteWorkerWindowController extends ControllerParent{


    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private Label areYouShureLabel;

    @FXML
    private Label workerLabel;

    private MainWindowController mainWindowController;
    private Worker worker;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private LanguageSwitcher languageSwitcher;


    public DeleteWorkerWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
        this.languageSwitcher = mainWindowController.getLanguageSwitcher();
    }

    @FXML
    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(AnchorPane);

        yesButton.setText(languageSwitcher.getYes());
        noButton.setText(languageSwitcher.getNo());
        areYouShureLabel.setText(languageSwitcher.getAreYouShureThatYouWantToDelete());
        workerLabel.setText(languageSwitcher.getWorker());



        yesButton.setOnAction(e->{
            this.addAllTasksTextToTextfield();
            this.deleteTaskCloseWindow();
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

    private void addAllTasksTextToTextfield(){
        List<Task> tasks = worker.getTasks();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n" + worker.getFirstname() + " " + worker.getLastname() + ", оставшиеся задачи: \n");
        for (int i = 0; i <tasks.size() ; i++) {

            if(!("done".equals(tasks.get(i).getTasktype()))){
                stringBuilder.append(" -" + tasks.get(i).getText() + ";\n");
            }

        }
        mainWindowController.addToTextArea(stringBuilder.toString());
    }

    private void deleteTaskCloseWindow(){
        yesButton.getScene().getWindow().hide();

        WorkerService workerService = new WorkerService();
        workerService.deleteWorker(worker);

        mainWindowController.getUser().removeWorker(worker);
        mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
        mainWindowController.initialize();
        yesButton.getScene().getWindow().hide();
    }


}
