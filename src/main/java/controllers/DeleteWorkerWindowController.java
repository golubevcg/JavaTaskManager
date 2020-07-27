package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DeleteWorkerWindowController {

    private OldMainWindowController mainWindowController;

    private Worker worker;

    public TextArea textArea;

    public DeleteWorkerWindowController(OldMainWindowController mainWindowController, Worker worker, TextArea textArea) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;



    @FXML
    void initialize() {

        setButtonCoursorEnterAction(yesButton);
        setButtonCoursorEnterAction(noButton);

        yesButton.setOnAction(e->{
            yesButton.getScene().getWindow().hide();
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction1 = session.beginTransaction();
            List<Task> tasks = worker.getTasks();

//            так же все задачи, которые были на сотруднике записать в textfield
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <tasks.size() ; i++) {
                stringBuilder.append(tasks.get(i).getText() + "\n");
            }
            System.out.println(stringBuilder.toString());
            Query newQuery = session.createQuery("DELETE Worker WHERE id = " + worker.getId());
            newQuery.executeUpdate();
            transaction1.commit();
//            session.close();
//            mainWindowController.rootUser.setTextfield(mainWindowController.rootUser.getTextfield() + "\n" + stringBuilder.toString());
//            System.out.println("================");
//            System.out.println(mainWindowController.rootUser.getTextfield());
//            System.out.println("================");

            Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction2 = session.beginTransaction();
            Query newQuery1 = session.createQuery("Update User set textfield = "
                    + "'" + mainWindowController.rootUser.getTextfield() + "'"
                    + " WHERE id = " + mainWindowController.rootUser.getId());
            newQuery1.executeUpdate();
            transaction2.commit();
            session2.close();

            yesButton.getScene().getWindow().hide();
            mainWindowController.updateSceneWorkers(mainWindowController.allLabelsList, mainWindowController.allButtonsList,
                    mainWindowController.uiMap);
            mainWindowController.initialize();
        });

        noButton.setOnAction(e->{
            noButton.getScene().getWindow().hide();
        });
    }

    public void setButtonCoursorEnterAction(Button button){
        button.setOnMouseEntered(e->button.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #0f79fa;" ));
        button.setOnMouseExited(e->button.setStyle("-fx-background-color:  #ffffff"));
    }



}
