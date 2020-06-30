package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.User;
import database.services.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;


public class LoginWindowController {

    public static User rootLWCUser;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button enterButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Button newUserButton;


//    @FXML
//    void Enter(KeyEvent event) {
//        enterButton.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
//            if(keyEvent.getCode()== KeyCode.ENTER){
//                enterButtonAction();
//            }
//        });
//    }

    @FXML
    void initialize() {

        enterButtonAction();

        setButtonCoursorEnterAction(newUserButton);
        setButtonCoursorEnterAction(enterButton);

        newUserButton.setOnAction(event-> {
            openNewScene("/fxml/regWindow.fxml", newUserButton);
        });

        KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER);
        Runnable rn = ()->{
            String loginText = loginField.getText().trim();
            String loginPassword = DigestUtils.md5Hex(pwdField.getText().trim());
            if(!loginText.equals("") && !loginPassword.equals("")){
                loginUser(loginText, loginPassword);
            }else {
                Shake loginShake = new Shake(loginField);
                Shake passwordShake = new Shake(pwdField);
                loginShake.playAnim();
                passwordShake.playAnim();
            }
        };
        Platform.runLater(()->{
        anchorPane.getScene().getAccelerators().put(kc,rn);
        });
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

    public void enterButtonAction(){
        enterButton.setOnAction(event->{
            String loginText = loginField.getText().trim();
            String loginPassword = DigestUtils.md5Hex(pwdField.getText().trim());


            if(!loginText.equals("") && !loginPassword.equals("")){
                loginUser(loginText, loginPassword);
            }else {
                Shake loginShake = new Shake(loginField);
                Shake passwordShake = new Shake(pwdField);
                loginShake.playAnim();
                passwordShake.playAnim();
            }
        });
    }


    private void loginUser(String loginText, String loginPassword) {

        UserService userService = new UserService();
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> list = session.createQuery("FROM User WHERE login = '" + loginText + "'").list();
        session.getTransaction();
        session.close();


        if (list.size() == 1) {
            User user = list.get(0);
            MainWindowController.rootUser = user;
            if(list.get(0).getPassword().equals(loginPassword)) {
                enterButton.getScene().getWindow().hide();
                openNewScene("/fxml/mainWindow.fxml", enterButton);
            }else{
                Shake loginShake = new Shake(loginField);
                Shake passwordShake = new Shake(pwdField);
                loginShake.playAnim();
                passwordShake.playAnim();
            }
        } else {
            Shake loginShake = new Shake(loginField);
            Shake passwordShake = new Shake(pwdField);
            loginShake.playAnim();
            passwordShake.playAnim();
        }
    }

    public static void main(String[] args) {
        LoginWindowController lwc = new LoginWindowController();
        lwc.initialize();
    }

    public void setButtonCoursorEnterAction(Button button){
        button.setOnMouseEntered(e->button.setStyle("-fx-background-color: #f5f9ff;" + "-fx-text-fill: #0f79fa;"));
        button.setOnMouseExited(e->button.setStyle("-fx-background-color:  #ffffff"));
    }
}