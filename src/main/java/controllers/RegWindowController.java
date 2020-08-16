package controllers;

import Main.Main;
import animations.Shake;
import classes.SceneOpener;
import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.User;
import database.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class RegWindowController implements ControllerInterface{

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane anchorPane;

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
    private AnchorPane moovableAnchorPane;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;

    private SceneOpener sceneOpener = new SceneOpener();
    private LoginWindowController loginWindowController = new LoginWindowController();
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();

    @FXML
    public void initialize(){
        Main.getStageObj().setResizable(false);

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);

        this.setColorAndStylesToButtons();
        this.setFieldStyles();

        this.setEnterHotekeyToRegister();

        backButton.setOnAction(e->{
            sceneOpener.openNewScene("/fxml/loginWindow.fxml", closeButton, loginWindowController);
        });
        registerButton.setOnAction(event->{
            if(registerNewUser()) {
                sceneOpener.openNewScene("/fxml/loginWindow.fxml", closeButton, loginWindowController);
            }

        });
    }

    @Override
    public void min() {
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }

    @Override
    public void close() {
        System.exit(0);
    }

    private boolean registerNewUser() {

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();


        if(firstname.isEmpty() || lastname.isEmpty() || login.isEmpty() || password.isEmpty()){

            if (firstname.isEmpty()){
                this.shakeField(firstnameTextField);
            }
            if (lastname.isEmpty()){
                this.shakeField(lastnameTextField);
            }
            if (login.isEmpty()){
                this.shakeField(loginField);
            }
            if (password.isEmpty()){
                this.shakeField(passwordField);
            }
            return false;

        }else {

            UserService userService = new UserService();
            User user = new User(firstname, lastname ,login, password);
            List<String> list = userService.checkUserLogin(user.getLogin());

             if (list.size() >= 1) {
                AlertBoxController alertBoxController = new AlertBoxController();
                sceneOpener.showAlertBox("/fxml/alertBoxWindow.fxml", alertBoxController);
                return false;
            } else {
                userService.saveUser(user);
                return true;
            }
        }
    }

    public void setColorAndStylesToButtons(){

        uiColorAndStyleSettings.setImageToButton(backButton, "back.png", 18,16);

        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);

        uiColorAndStyleSettings.setDefaultStylesToButtonsAndOnMouseEnteredAndExited(backButton);

        uiColorAndStyleSettings.setDefaultStylesToButtonsAndOnMouseEnteredAndExited(registerButton);

    }
    
    private void setFieldStyles(){
        this.firstnameTextField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );

        this.lastnameTextField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );

        this.loginField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );

        this.passwordField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );
    }

    private void shakeField(Node node){
        Shake shake = new Shake(node);
        shake.playAnim();
    }

    private void setEnterHotekeyToRegister(){
        KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER);
        Runnable rn = ()->{
            this.registerNewUser();
        };
        Platform.runLater(()->{
            anchorPane.getScene().getAccelerators().put(kc,rn);
        });
    }

}