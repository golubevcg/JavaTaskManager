package controllers;

import animations.Shake;
import database.Task;
import database.Worker;
import database.services.TaskService;
import database.services.WorkerService;
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

public class NewTaskWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private javafx.scene.layout.AnchorPane AnchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

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
    private double xOffset;
    private double yOffset;
    private Worker worker;
    private Stage stage;

    public NewTaskWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    private void initialize() {
        this.setDropShadow();
        this.makePaneMoovable(moovableAnchorPane);
        this.setImagesAndColorToButtons();

        inQueenRadioButton.fire();

        createButton.setOnAction(e->{
                if(checkTask()==true) {
                    mainWindowController.initialize();
                    createButton.getScene().getWindow().hide();
                }
        });
    }

    private void setDropShadow(){
        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());
    }

    public boolean checkTask() {
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
            Task task = new Task(taskText, this.worker, tasktype);
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
                alertTaskWindowController.setStage(stage);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
                return false;
            } else {
                taskService.saveTask(task);
                worker.addTask(task);
                WorkerService workerService = new WorkerService();
                workerService.updateWorker(worker);
                return true;
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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

