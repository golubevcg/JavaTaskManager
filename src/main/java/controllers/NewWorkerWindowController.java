package controllers;

import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.User;
import database.Worker;
import database.services.UserService;
import database.services.WorkerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.jandex.Main;


import java.io.IOException;
import java.util.List;

public class NewWorkerWindowController {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField loginField;

    @FXML
    private Button registerButton;

    private MainWindowController mainWindowController;

    public NewWorkerWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @FXML
    void initialize(){

        setButtonCoursorEnterAction(registerButton);
        registerButton.setOnAction(e->{
            if(registerNewUser(mainWindowController)==true) {
                mainWindowController.updateSceneWorkers(mainWindowController.allLabelsList,
                        mainWindowController.allButtonsList, mainWindowController.uiMap);
                mainWindowController.initialize();
                registerButton.getScene().getWindow().hide();
            }
        });
    }

    public static void main(String[] args) {
        LoginWindowController lwc = new LoginWindowController();
        lwc.initialize();
    }

    private boolean registerNewUser(MainWindowController mainWindowController) {

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String login = loginField.getText();


        if(firstname.isEmpty() || lastname.isEmpty() || login.isEmpty()){
            if (firstname.isEmpty()){
                Shake firstNameShake = new Shake(firstnameTextField);
                firstNameShake.playAnim();
            }
            if (lastname.isEmpty()){
                Shake lastnameTextFieldShake = new Shake(lastnameTextField);
                lastnameTextFieldShake.playAnim();
            }
            if (login.isEmpty()){
                Shake loginFieldShake = new Shake(loginField);
                loginFieldShake.playAnim();
            }
            return false;

        }else {

            //формируем запрос, чтобы проверить, есть ли такой пользователь в базе данных
            WorkerService workerService = new WorkerService();
            Worker worker = new Worker(firstname, lastname, MainWindowController.rootUser, login);
            List<String> list = workerService.checkWorkerLogin(worker.getLogin());

            if (list.size() >= 1) {

                //popup window - такой пользователь уже существует
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/alertBoxWindow.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                return false;
            } else {
                //если всё хорошо - то добавляем нового пользователя в базу
                Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                Transaction transaction1 =  session2.beginTransaction();
                session2.save(worker);
                transaction1.commit();
                session2.close();
                return true;
            }
        }

        }

    public void openNewScene(String windowName, Button button){
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowName));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void setButtonCoursorEnterAction(Button button){
        button.setOnMouseEntered(e->button.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #0f79fa;" ));
        button.setOnMouseExited(e->button.setStyle("-fx-background-color:  #ffffff"));
    }
}