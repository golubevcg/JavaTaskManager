package controllers;

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
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class EditTaskWindowController {

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

    private Task task;

    public EditTaskWindowController(MainWindowController mainWindowController, Task task) {
        this.mainWindowController = mainWindowController;
        this.task = task;
    }


    @FXML
    private void initialize() {

        setButtonOnCoursorAction(createButton);

        if (task.getTasktype() != "quene") {
            inQueenRadioButton.fire();
        } else {
            inWorkRadioButton.fire();
        }

        taskTextfield.setText(task.getText());


        createButton.setOnAction(e -> {
            if (registerNewUser() == true) {
                mainWindowController.updateSceneWorkers(mainWindowController.allLabelsList,
                        mainWindowController.allButtonsList, mainWindowController.uiMap);
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
            Task task = new Task(taskText, this.task.getWorker(), tasktype);
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
                //если всё хорошо - то добавляем нового пользователя в базу
                Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                Transaction transaction1 = session2.beginTransaction();
                Query newQuery = session2.createQuery("DELETE Task WHERE id = " + this.task.getId());
                newQuery.executeUpdate();
                transaction1.commit();
                session2.close();

                this.task.setText(taskTextfield.getText());
                mainWindowController.addNewTask(this.task);
                mainWindowController.initialize();
                return true;
            }
        }
    }

    public void setButtonOnCoursorAction(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #0f79fa;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #ffffff"));

    }
}

