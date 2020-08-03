package controllers;

import animations.Shake;
import database.HibernateSessionFactoryUtil;
import database.Task;
import database.Worker;
import database.services.TaskService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditTaskWindowController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private void initialize() {

        this.makePaneMoovable(moovableAnchorPane);

        String stylesheet = "-fx-background-color: transparent;\n" +
                "     -fx-border-color:#91afc5;\n" +
                "     -fx-background-insets: transparent;\n" +
                "     -fx-faint-focus-color: transparent;\n" +
                "     -fx-border-radius: 5;\n" +
                "     -fx-background-radius: 5;\n" +
                "     -fx-border-width: 1.5;";
        taskTextfield.getStylesheets().add(stylesheet);

        this.setImagesAndColorToButtons();
        this.setButtonOnCoursorAction(createButton);

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
        Worker worker = this.task.getWorker();

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
                loader.setLocation(getClass().getResource("/fxml/alertTaskBoxWindow.fxml"));
                try {
                    loader.load();
                } catch (IOException а) {
                    а.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                return false;
            } else {
                Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                Transaction transaction1 = session2.beginTransaction();
                Query newQuery = session2.createQuery("DELETE Task WHERE id = " + this.task.getId());
                newQuery.executeUpdate();
                transaction1.commit();
                session2.close();

                this.task.setText(taskTextfield.getText());
                mainWindowController.initialize();
                return true;
            }
        }
    }

    public void setButtonOnCoursorAction(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #0f79fa;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #ffffff"));

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

