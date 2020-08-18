package controllers;

import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.HibernateSessionFactoryUtil;
import database.Task;
import database.Worker;
import database.services.WorkerService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.Query;
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

    private MainWindowController mainWindowController;
    private Worker worker;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();


    public DeleteWorkerWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(AnchorPane);

        yesButton.setOnAction(e->{
            this.deleteTaskCloseWindowAndAddAllTasksToTextfield();
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

    private void deleteTaskCloseWindowAndAddAllTasksToTextfield(){
        yesButton.getScene().getWindow().hide();

        List<Task> tasks = worker.getTasks();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n" + worker.getFirstname() + " " + worker.getLastname() + ", оставшиеся задачи: \n");
        for (int i = 0; i <tasks.size() ; i++) {
            stringBuilder.append(" -" + tasks.get(i).getText() + ";\n");
        }

        mainWindowController.addToTextArea(stringBuilder.toString());

        Session session3 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session3.beginTransaction();
        Query newQuery = session3.createQuery("UPDATE User SET textfield = " + "'" + mainWindowController.getUserTextField() + "'" + "WHERE id = " + mainWindowController.getUser().getId());
        newQuery.executeUpdate();
        transaction.commit();
        session3.close();

        WorkerService workerService = new WorkerService();
        workerService.deleteWorker(worker);

        mainWindowController.getUser().removeWorker(worker);
        mainWindowController.initialize();
        yesButton.getScene().getWindow().hide();
    }


}
