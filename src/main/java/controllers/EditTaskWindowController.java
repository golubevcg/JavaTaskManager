package controllers;

import animations.Shake;
import classes.WindowEffects;
import database.Task;
import database.services.TaskService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditTaskWindowController extends ControllerParent{


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private javafx.scene.layout.AnchorPane moovableAnchorPane;

    @FXML
    private TextField taskTextfield;

    @FXML
    private Button createButton;

    @FXML
    private RadioButton inQueenRadioButton;

    @FXML
    private ToggleGroup tgroup;

    @FXML
    private RadioButton inWorkRadioButton;

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

    private Task task;

    private double xOffset;
    private double yOffset;
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public EditTaskWindowController(MainWindowController mainWindowController, Task task) {
        this.mainWindowController = mainWindowController;
        this.task = task;
    }

    @FXML
    public void initialize() {

        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());

        WindowEffects.makePaneMoovable(moovableAnchorPane);


        String stylesheetRadioButtons = getClass().getResource("/styles.css").toExternalForm();
        inWorkRadioButton.getStylesheets().add(stylesheetRadioButtons);
        inQueenRadioButton.getStylesheets().add(stylesheetRadioButtons);

        String textFieldStylesheet = "-fx-background-color: transparent;\n" +
                "     -fx-border-color:#91afc5;\n" +
                "     -fx-background-insets: transparent;\n" +
                "     -fx-faint-focus-color: transparent;\n" +
                "     -fx-border-radius: 5;\n" +
                "     -fx-background-radius: 5;\n" +
                "     -fx-border-width: 1.5;";
        taskTextfield.getStylesheets().add(textFieldStylesheet);

        this.setImagesAndColorToButtons();

        if (task.getTasktype() != "quene") {
            inQueenRadioButton.fire();
        } else {
            inWorkRadioButton.fire();
        }

        taskTextfield.setText(task.getText());


        createButton.setOnAction(e -> {
            if (checkTaskRegisterNewTask() == true) {
                mainWindowController.initialize();
                createButton.getScene().getWindow().hide();
            }
        });
    }

    public boolean checkTaskRegisterNewTask() {
        String taskText;
        taskText = taskTextfield.getText();

        if (taskText.isEmpty()) {
            Shake taskTextfield = new Shake(this.taskTextfield);
            taskTextfield.playAnim();
            return false;
        } else {
            String tasktype;
            if (inQueenRadioButton.isSelected() == true) {
                tasktype = "quene";
            } else {
                tasktype = "inwork";
            }

            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(taskText);

            if (list.size() >= 1) {
                FXMLLoader loader = new FXMLLoader();
                AlertTaskWindowController alertTaskWindowController = new AlertTaskWindowController();
                loader.setLocation(getClass().getResource("/fxml/alertTaskBoxWindow.fxml"));
                try {
                    loader.setController(alertTaskWindowController);
                    loader.load();
                } catch (IOException а) {
                    а.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initStyle(StageStyle.TRANSPARENT);
                alertTaskWindowController.setStage(stage);
                stage.show();
                return false;
            } else {
                this.task.setText(taskTextfield.getText());
                this.task.setTasktype(tasktype);
                taskService.updateTask(task);
                mainWindowController.initialize();
                return true;
            }
        }
    }

    private void makePaneMoovable(AnchorPane anchorPane){
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
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

        createButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        createButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });

        createButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
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
}

