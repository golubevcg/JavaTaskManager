package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import database.services.UserService;
import database.services.WorkerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class DeleteWorkerWindowController {

    private MainWindowController mainWindowController;

    private Worker worker;

    private Stage stage;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    public DeleteWorkerWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    void initialize() {

        yesButton.setOnAction(e->{
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
        });

        noButton.setOnAction(e->{
            noButton.getScene().getWindow().hide();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
