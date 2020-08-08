package controllers;

import Main.Main;
import animations.Shake;
import database.User;
import database.services.UserService;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class RegWindowController {

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

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

    private double xOffset;
    private double yOffset;

    @FXML void close(ActionEvent event){
        System.exit(0);
    }


    @FXML void min(ActionEvent event){
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }



    @FXML
    void initialize(){

        Main.getStageObj().setResizable(false);
        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());

        this.setImagesAndColorToButtons();

        this.makePaneMoovable(moovableAnchorPane);

        LoginWindowController loginWindowController = new LoginWindowController();
        backButton.setOnAction(e->{
            openNewScene("/fxml/loginWindow.fxml", closeButton, loginWindowController);
        });

        registerButton.setOnAction(event->{

            if(registerNewUser()==true) {
                registerButton.getScene().getWindow().hide();
                openNewScene("/fxml/loginWindow.fxml", closeButton, loginWindowController);
            }

        });

        this.firstnameTextField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        this.lastnameTextField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        this.loginField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        this.passwordField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");
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
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
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
                FXMLLoader loader = new FXMLLoader();
                AlertBoxController alertBoxController = new AlertBoxController();
                loader.setLocation(getClass().getResource("/fxml/alertBoxWindow.fxml"));
                try {
                    loader.setController(alertBoxController);
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initStyle(StageStyle.TRANSPARENT);
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

    private void setImagesAndColorToButtons() {
        this.setImageToButton(closeButton, "cross.png", 11,20);
        this.setImageToButton(minimiseButton, "minimize.png", 13,40);
        this.setImageToButton(backButton, "back.png", 18,16);

        this.setColorsToButtons();
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

        backButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        registerButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

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



        backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });
        backButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });

        registerButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                registerButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });
        registerButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                registerButton.setStyle("-fx-background-color: transparent;" +
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