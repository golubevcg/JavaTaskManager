package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import database.services.UserService;
import database.services.WorkerService;
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

import javax.persistence.Query;
import java.util.List;

public class DeleteWorkerWindowController {


    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    private double xOffset;
    private double yOffset;
    private MainWindowController mainWindowController;
    private Worker worker;
    private Stage stage;

    public DeleteWorkerWindowController(MainWindowController mainWindowController, Worker worker) {
        this.mainWindowController = mainWindowController;
        this.worker = worker;
    }

    @FXML
    void initialize() {

        this.setDropShadow();

        this.makePaneMoovable(AnchorPane);

        yesButton.setOnAction(e->{
            yesButton.getScene().getWindow().hide();

            List<Task> tasks = worker.getTasks();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n" + worker.getFirstname() + " " + worker.getLastname() + ", оставшиеся задачи: \n");
            for (int i = 0; i <tasks.size() ; i++) {
                stringBuilder.append(" -" + tasks.get(i).getText() + ";\n");
            }

            mainWindowController.addToTextArea(stringBuilder.toString());

            Session session3 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction = session3.beginTransaction();
            Query newQuery = session3.createQuery("UPDATE User SET textfield = " + "'" + mainWindowController.getUserTextField() + "'" + "WHERE id = " + mainWindowController.getUser().getId());
            newQuery.executeUpdate();
            transaction.commit();
            session3.close();

            WorkerService workerService = new WorkerService();
            workerService.deleteWorker(worker);

            mainWindowController.getUser().removeWorker(worker);
            mainWindowController.initialize();
            yesButton.getScene().getWindow().hide();
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

    public void setStage(Stage stage) {
        this.stage = stage;
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
