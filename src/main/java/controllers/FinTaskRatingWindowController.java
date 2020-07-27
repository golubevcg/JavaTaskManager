package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.io.IOException;

public class FinTaskRatingWindowController {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button confirmButton;

    @FXML
    private ToggleGroup tgroup11;

    @FXML
    private ToggleGroup tgroup111;

    @FXML
    private ToggleGroup tgroup1111;



    @FXML
    private RadioButton buttonRaing1;

    @FXML
    private RadioButton buttonRaing2;

    @FXML
    private RadioButton buttonRaing3;

    @FXML
    private RadioButton buttonRaing4;




    @FXML
    private ToggleGroup tgroup1112;

    private Task task;

    private OldMainWindowController mainWindowController;

    public TextArea textArea;


    public FinTaskRatingWindowController(Task task, OldMainWindowController mainWindowController, TextArea textArea) {
        this.task = task;
        this.mainWindowController = mainWindowController;
        this.textArea = textArea;
    }

    @FXML
    public void initialize() {

        ToggleGroup group = new ToggleGroup();
        buttonRaing1.setToggleGroup(group);
        buttonRaing2.setToggleGroup(group);
        buttonRaing3.setToggleGroup(group);
        buttonRaing4.setToggleGroup(group);

        group.getSelectedToggle();

        confirmButton.setOnAction(e->{

                if(group.getSelectedToggle()==(null)){
                    //вывезти лейбл что нужно выбрать хотя бы одно значение рейтинга
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/noRatingErrorWindow.fxml"));
                    try {loader.load();
                    } catch (IOException h) {
                        h.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                }else{
                    //если эта кнопка нажата тогда обновить задачу в базе данных и вернуть main window
                    RadioButton rb = (RadioButton)group.getSelectedToggle();

                    updateTaskStatus(Integer.parseInt(rb.getText()));
                    //закрыть окно и вернуть main
                    mainWindowController.initialize();
                    confirmButton.getScene().getWindow().hide();
                }
        });


    }

    private void updateTaskStatus(int value){
        Session session3 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session3.beginTransaction();

        Query newQuery = session3.createQuery("UPDATE Task SET " +
                "tasktype = 'done' WHERE id = " + task.getId());
        newQuery.executeUpdate();

        Query newQuery1 = session3.createQuery("UPDATE Task SET rating = " + value + " WHERE id= " + task.getId());
        newQuery1.executeUpdate();

        transaction.commit();
        session3.close();
        this.initialize();
    }

}
