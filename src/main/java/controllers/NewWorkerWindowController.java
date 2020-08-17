package controllers;

import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.User;
import database.Worker;
import database.services.UserService;
import database.services.WorkerService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.io.IOException;
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
    private double xOffset;
    private double yOffset;

    public NewWorkerWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void initialize(){

        this.setDropShadow();

        this.setImagesAndColorToButtons();

        this.makePaneMoovable(moovableAnchorPane);

        registerButton.setOnAction(e->{
            if(registerNewUser(mainWindowController)==true) {
                mainWindowController.initialize();
                registerButton.getScene().getWindow().hide();
            }
        });

        registerButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");
        firstnameTextField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        lastnameTextField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

        loginField.setStyle( "-fx-background-color: transparent;"+
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-faint-focus-color: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;"+
                "-fx-padding: 10 10 10 10;");

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

    private void makePaneMoovable(AnchorPane anchorPane) {
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setImagesAndColorToButtons(){
        this.setImageToButton(closeButton, "cross.png", 11,20);
        this.setImageToButton(minimiseButton, "minimize.png", 13,40);

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

    private void setDropShadow(){
        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());
    }
}