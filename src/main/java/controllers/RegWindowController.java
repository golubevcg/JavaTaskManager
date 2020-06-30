package controllers;

import animations.Shake;
import database.User;
import database.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RegWindowController {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField loginField;

    @FXML
    private Button registerButton;


    @FXML
    private Button backButton;


    @FXML
    void initialize(){

        setButtonOnCoursorAction(registerButton);
        setButtonOnCoursorAction(backButton);

        backButton.setOnAction(e->{
            registerButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/loginWindow.fxml"));
            try {
                loader.load();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });

        registerButton.setOnAction(event->{

            if(registerNewUser()==true) {

                registerButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/loginWindow.fxml"));
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
            setButtonOnCoursorAction(registerButton);
            setButtonOnCoursorAction(backButton);

        });


    }

    public void setButtonOnCoursorAction(Button button){
        button.setOnMouseEntered(e->button.setStyle("-fx-background-color: #f5f9ff;" + "-fx-text-fill: #0f79fa;" ));
        button.setOnMouseExited(e->button.setStyle("-fx-background-color:  #ffffff"));
    }

    private boolean registerNewUser() {


        //забираем текст из полей ввода
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();


        if(firstname.isEmpty() || lastname.isEmpty() || login.isEmpty() || password.isEmpty()){
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
            if (password.isEmpty()){
                Shake passwordFieldShake = new Shake(passwordField);
                passwordFieldShake.playAnim();
            }
            return false;

        }else {

            //формируем запрос, чтобы проверить, есть ли такой пользователь в базе данных
            UserService userService = new UserService();
            User user = new User(firstname, lastname ,login, password);
            List<String> list = userService.checkUserLogin(user.getLogin());

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
                userService.saveUser(user);
                return true;
            }
        }
//        return false;
    }

}