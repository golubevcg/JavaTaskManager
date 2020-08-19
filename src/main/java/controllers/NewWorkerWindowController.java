package controllers;

import animations.Shake;
import classes.SceneOpener;
import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.Worker;
import database.services.UserService;
import database.services.WorkerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.util.List;

public class NewWorkerWindowController extends ControllerParent{


    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField loginField;

    @FXML
    private Button registerButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;

    @FXML void close(ActionEvent event){
        stage.close();
        mainWindowController.initialize();
    }

    @FXML void min(ActionEvent event){
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }


    private MainWindowController mainWindowController;
    private Stage stage;
    UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();

    public NewWorkerWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void initialize(){

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(moovableAnchorPane);

        this.setStylesToButtons();


        registerButton.setOnAction(e->{
            if(registerNewUser(mainWindowController)==true) {
                mainWindowController.initialize();
                registerButton.getScene().getWindow().hide();
            }
        });

        firstnameTextField.setStyle(  uiColorAndStyleSettings.getDefaultStyleWithBorder() );
        lastnameTextField.setStyle(  uiColorAndStyleSettings.getDefaultStyleWithBorder() );
        loginField.setStyle(  uiColorAndStyleSettings.getDefaultStyleWithBorder() );
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
                AlertBoxController alertBoxController = new AlertBoxController();
                SceneOpener sceneOpener = new SceneOpener();
                sceneOpener.showAlertBox("/fxml/alertBoxWindow.fxml", (Stage) closeButton.getScene().getWindow(), alertBoxController);
                return false;
            } else {
                workerService.saveWorker(worker);
                mainWindowController.getUser().addWorker(worker);
                UserService userService = new UserService();
                userService.updateUser(mainWindowController.getUser());
                return true;
            }
        }

        }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setStylesToButtons(){
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
        uiColorAndStyleSettings.setButtonStyles(registerButton);
    }

}