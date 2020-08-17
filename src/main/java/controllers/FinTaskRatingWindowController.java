package controllers;

import database.Task;
import database.services.TaskService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ResourceBundle;

public class FinTaskRatingWindowController extends ControllerParent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private AnchorPane moovableAnchorPane;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private Button confirmButton;

    @FXML
    private RadioButton buttonRating3;

    @FXML
    private RadioButton buttonRating2;

    @FXML
    private RadioButton buttonRating1;

    @FXML
    private Button minimiseButton;

    @FXML
    private Button closeButton;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML void close(ActionEvent event){
        stage.close();
        mainWindowController.initialize();
    }

    @FXML void min(ActionEvent event){
        ((Stage)(moovableAnchorPane.getScene().getWindow())).setIconified(true);
    }

    private Task task;
    private MainWindowController mainWindowController;
    private Stage stage;
    private double xOffset;
    private double yOffset;

    public FinTaskRatingWindowController(Task task, MainWindowController mainWindowController) {
        this.task = task;
        this.mainWindowController = mainWindowController;
    }

    @FXML
    public void initialize() {

        this.setDropShadow();
        this.makePaneMoovable(moovableAnchorPane);
        this.setImagesAndColorToButtons();


        ToggleGroup group = new ToggleGroup();
        buttonRating1.setToggleGroup(group);
        buttonRating2.setToggleGroup(group);
        buttonRating3.setToggleGroup(group);

        String pathToCss = getClass().getResource("/styles.css").toExternalForm();
        buttonRating1.getStylesheets().add(pathToCss);
        buttonRating2.getStylesheets().add(pathToCss);
        buttonRating3.getStylesheets().add(pathToCss);

        confirmButton.setOnAction(e->{

                if(group.getSelectedToggle()==(null)){
                    FXMLLoader loader = new FXMLLoader();
                    NoRatingErrorWindowController noRatingErrorWindowController = new NoRatingErrorWindowController();
                    loader.setLocation(getClass().getResource("/fxml/noRatingErrorWindow.fxml"));
                    try {
                        loader.setController(noRatingErrorWindowController);
                        loader.load();
                    } catch (IOException h) {
                        h.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                }else{
                    RadioButton rb = (RadioButton)group.getSelectedToggle();
                    updateTaskStatus(Integer.parseInt(rb.getText()));
                    mainWindowController.initialize();
                    confirmButton.getScene().getWindow().hide();
                }
        });

        confirmButton.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color:#FFFFFF;" +
                "-fx-background-insets: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-border-width: 1.5;");

        confirmButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                confirmButton.setStyle( "-fx-background-color: transparent;"+
                        "-fx-border-color:#91afc5;"+
                        "-fx-background-insets: transparent;"+
                        "-fx-faint-focus-color: transparent;"+
                        "-fx-border-radius: 5;"+
                        "-fx-background-radius: 5;"+
                        "-fx-border-width: 1.5;");
            }
        });

        confirmButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                confirmButton.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-color:#FFFFFF;" +
                        "-fx-background-insets: transparent;" +
                        "-fx-faint-focus-color: transparent;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-width: 1.5;");
            }
        });

    }

    private void setImagesAndColorToButtons() {
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

    private void makePaneMoovable(javafx.scene.layout.AnchorPane anchorPane) {
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    private void setDropShadow() {
        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());
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

    private void updateTaskStatus(int value){
        task.setTasktype("done");
        task.setRating(value);
        TaskService taskService = new TaskService();
        taskService.updateTask(task);
        this.initialize();
    }

}
