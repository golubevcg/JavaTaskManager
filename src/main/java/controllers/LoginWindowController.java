package controllers;

import Main.Main;
import animations.Shake;
import classes.SceneOpener;
import classes.WindowEffects;
import classes.UIColorAndStyleSettings;
import database.User;
import database.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;


public class LoginWindowController extends ControllerParent{

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

    @FXML
    private Button enterButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Button newUserButton;

    @FXML
    private Button fullScreenButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;

    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private String loginText;
    private String loginPassword;
    private SceneOpener sceneOpener = new SceneOpener();

    @FXML
    public void initialize() {



        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);

        Main.getStageObj().setResizable(false);

        this.setStylesToButtons();
        RegWindowController regWindowController = new RegWindowController();
        Stage regWindowStage = new Stage();

        newUserButton.setOnAction(event-> {
            sceneOpener.openNewScene("/fxml/regWindow.fxml", regWindowStage, (Stage) closeButton.getScene().getWindow(), regWindowController, true);
        });
        enterButton.setOnAction(event->{
            this.checkIfFieldsAreEmptyElseTryToLogin();
        });

        this.loginField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );
        this.pwdField.setStyle( uiColorAndStyleSettings.getDefaultStyleWithBorder() );

        this.setEnterHotkeyToLogin();

    }

    @Override
    public void min() {
        ((Stage)(anchorPane.getScene().getWindow())).setIconified(true);
    }

    @Override
    public void close() {
        System.exit(0);
    }

    public void checkIfFieldsAreEmptyElseTryToLogin(){
        if(loginField.getText().equals(("")) && pwdField.getText().equals("")){
            this.shakeField(loginField);
            this.shakeField(pwdField);
        }else{
            this.checkLoginIfTrueThenOpenNewSceneElseAnim();
        }
    }

    private void checkLoginIfTrueThenOpenNewSceneElseAnim() {
        loginText = loginField.getText().trim();
        loginPassword = DigestUtils.md5Hex(pwdField.getText().trim());
        UserService userService = new UserService();
        List<User> list = userService.returnUsersByLogin(loginText);

        if (list.size() == 1) {
            User user = list.get(0);
            if (list.get(0).getPassword().equals(loginPassword)) {
                MainWindowController mainWindowController = new MainWindowController(user);
                Stage mainWindowStage = new Stage();
                sceneOpener.openNewScene("/fxml/mainWindow.fxml", mainWindowStage, (Stage) closeButton.getScene().getWindow(), mainWindowController, true);
            } else {
                this.shakeField(loginField);
                this.shakeField(pwdField);
            }
        } else {
            this.shakeField(loginField);
            this.shakeField(pwdField);
        }

    }

    private void shakeField(Node node){
        Shake shake = new Shake(node);
        shake.playAnim();
    }

    @Override
    public void setStylesToButtons(){
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton, minimiseButton);
        uiColorAndStyleSettings.setButtonStyles(newUserButton, enterButton);
    }

    private void setEnterHotkeyToLogin(){
        KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER);
        Runnable rn = ()->{
            this.checkIfFieldsAreEmptyElseTryToLogin();
        };
        Platform.runLater(()->{
            anchorPane.getScene().getAccelerators().put(kc,rn);
        });
    }

}