package controllers;

import Main.Main;
import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.Task;
import database.Worker;
import database.services.TaskService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

public class NewTaskWindowController {

    @FXML
    private javafx.scene.layout.AnchorPane AnchorPane;

    @FXML
    private TextField taskTextfield;

    @FXML
    private Button createButton;

    @FXML
    private RadioButton inQueenRadioButton;

    @FXML
    private RadioButton inWorkRadioButton;

    @FXML
    private ToggleGroup tgroup;

    private MainWindowController mainWindowController;

    private Worker worker;

    public NewTaskWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;

    }


    @FXML
    private void initialize() {
        inQueenRadioButton.fire();


//        public Task(String text, Worker worker, String tasktype)

        createButton.setOnAction(e->{
                if(registerNewUser()==true) {
                    mainWindowController.initialize();
                    createButton.getScene().getWindow().hide();
                }
        });
    }

    public boolean registerNewUser() {
        String taskText;
        taskText = taskTextfield.getText();
        if (taskText.isEmpty()) {
            Shake taskTextfield = new Shake(this.taskTextfield);
            taskTextfield.playAnim();
            return false;
        } else {
            //формируем запрос, чтобы проверить, есть ли такая задача в базе данных
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
                //popup window - такая задача уже существует
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/alertTaskBoxWindow.fxml"));
                try {
                    loader.load();
                } catch (IOException а) {
                    а.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                return false;
            } else {
                //если всё хорошо - то добавляем новую задачу в базу
                Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                Transaction transaction1 = session2.beginTransaction();
                session2.save(task);
                transaction1.commit();
                session2.close();

                worker.addTask(task);
                return true;
            }
        }
    }
}

