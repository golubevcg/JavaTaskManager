package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DeleteTaskWindowController {

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    private MainWindowController mainWindowController;

    private Task task;

    public TextArea textArea;

    public DeleteTaskWindowController(MainWindowController mainWindowController, Task task) {
        this.mainWindowController = mainWindowController;
        this.task = task;
    }

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    private double xOffset;
    private double yOffset;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void initialize() {

        this.setDropShadow();

        this.makePaneMoovable(AnchorPane);

        yesButton.setOnAction(e->{
            TaskService taskService = new TaskService();
            taskService.deleteTask(task);
            task.getWorker().deleteTask(task);
            yesButton.getScene().getWindow().hide();
            mainWindowController.initialize();
        });

        noButton.setOnAction(e->{
            noButton.getScene().getWindow().hide();
        });

        noButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        noButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                noButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });

        noButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                noButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });

        yesButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        yesButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                yesButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });

        yesButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                yesButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });

        AnchorPane.setStyle("-fx-background-color: #FFFFFF;" +
                "-fx-border-color:#91afc5;"+
                "-fx-background-insets: transparent;"+
                "-fx-border-radius: 5;"+
                "-fx-background-radius: 5;"+
                "-fx-border-width: 1.5;");

    }

    private void setDropShadow() {
        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        DropShadow dsh = new DropShadow();
        dsh.setColor(Color.web("#C6C6C6"));
        this.forDropShadowTopAnchorPane.setEffect(dsh);
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


}
