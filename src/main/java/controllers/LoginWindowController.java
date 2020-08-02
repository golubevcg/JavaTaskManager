package controllers;

import Main.Main;
import animations.Shake;
import classes.FXResizeHelper;
import database.HibernateSessionFactoryUtil;
import database.User;
import database.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class LoginWindowController {

    @FXML
    private AnchorPane mainAnchorPane;

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


    @FXML void close(ActionEvent event){
        System.exit(0);
    }

    @FXML void min(ActionEvent event){
        ((Stage)(anchorPane.getScene().getWindow())).setIconified(true);
    }

    private double xOffset;
    private double yOffset;

    @FXML
    void initialize() {

//        mainAnchorPane.setPadding(new Insets(20,20,20,20));
//        mainAnchorPane.setEffect(new DropShadow());
//        anchorPane.getScene().setFill(Color.TRANSPARENT);


        Main.getStageObj().setResizable(false);
        this.setImagesAndColorToButtons();
        enterButtonAction();

        this.loginField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        this.pwdField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        this.makePaneMoovable(moovableAnchorPane);

        newUserButton.setOnAction(event-> {
            RegWindowController regWindowController = new RegWindowController();
            openNewScene("/fxml/regWindow.fxml", newUserButton, regWindowController);
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

    private void setImagesAndColorToButtons() {
        this.setImageToButton(closeButton, "cross.png", 11,20);
        this.setImageToButton(minimiseButton, "minimize.png", 13,40);
        this.setColorsToButtons();
    }

    public void openNewScene(String windowName, Button button, Object controller){

        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(windowName));
        loader.setController(controller);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        if(controller.getClass()==(MainWindowController.class)){
            ((MainWindowController)controller).setStage(stage);
            FXResizeHelper fxResizeHelper = new FXResizeHelper();
            fxResizeHelper.addResizeListener(stage);

        }
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
            if(list.get(0).getPassword().equals(loginPassword)) {
                enterButton.getScene().getWindow().hide();
                MainWindowController mainWindowController = new MainWindowController(user);
                openNewScene("/fxml/mainWindow.fxml", enterButton, mainWindowController);

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

    private void makePaneMoovable(AnchorPane anchorPane){
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            ((Stage)(anchorPane.getScene().getWindow())).setX(e.getScreenX() - xOffset);
            ((Stage)(anchorPane.getScene().getWindow())).setY(e.getScreenY() - yOffset);
        });
    }

    private void setImageToButton(Button button, String imageName, int width, int height){
        Image image = new Image(imageName);
        ImageView imageView = new ImageView(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        button.setGraphic(imageView);
    }

    private void setColorsToButtons(){
        String standartColorCursorOnButton = "cfdee9";

        closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeButton.setStyle("-fx-background-color:#F87272");
            }
        });
        closeButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeButton.setStyle("-fx-background-color: transparent");
            }
        });

        minimiseButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                minimiseButton.setStyle("-fx-background-color:#" + standartColorCursorOnButton);
            }
        });
        minimiseButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                minimiseButton.setStyle("-fx-background-color: transparent");
            }
        });


        newUserButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        enterButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        newUserButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newUserButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });
        newUserButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                newUserButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });

        enterButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                enterButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });
        enterButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                enterButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });


    }

}