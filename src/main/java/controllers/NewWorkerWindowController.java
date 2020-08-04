package controllers;

import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.User;
import database.Worker;
import database.services.UserService;
import database.services.WorkerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;


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

        registerButton.setOnAction(e->{
            if(registerNewUser(mainWindowController)==true) {
                mainWindowController.initialize();
                registerButton.getScene().getWindow().hide();
            }
        });
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

            WorkerService workerService = new WorkerService();
            Worker worker = new Worker(firstname, lastname, mainWindowController.getUser(), login);
            List<String> list = workerService.checkWorkerLogin(worker.getLogin());

            if (list.size() >= 1) {
                openNewScene("/fxml/alertBoxWindow.fxml", registerButton);
                return false;
            } else {
                workerService.saveWorker(worker);
                mainWindowController.getUser().addWorker(worker);
                UserService userService = new UserService();
                userService.updateUser(mainWindowController.getUser());

                Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                List<User> list1 = session.createQuery("FROM User WHERE id = '" + mainWindowController.getUser().getId() + "'").list();
                session.getTransaction();
                session.close();
                mainWindowController.setUser(list1.get(0));
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
}