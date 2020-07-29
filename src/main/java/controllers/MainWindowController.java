package controllers;

import database.Task;
import database.User;
import database.Worker;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MainWindowController {

    private User rootUser;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane anchorPainTodragWindow;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private TextArea textArea;

    @FXML
    private AnchorPane AcnhorPaneInQuene;

    @FXML
    private AnchorPane AcnhorPaneInWork;

    @FXML
    private MenuBar mainMenuBar;

    @FXML
    private Button closeButton;

    @FXML
    private Button addNewWorkerButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private AnchorPane anchorPaneForCards;

    @FXML void close(ActionEvent event){
        System.exit(0);
    }

    @FXML void min(ActionEvent event){
        ((Stage)(mainAnchorPane.getScene().getWindow())).setIconified(true);
    }

    private double xOffset;
    private double yOffset;
    private Stage stage;

    public MainWindowController(User rootUser) {
        this.rootUser = rootUser;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    void initialize() {

        this.mainAnchorPane.setMinWidth(mainAnchorPane.getPrefWidth());
        this.mainAnchorPane.setMaxWidth(mainAnchorPane.getPrefHeight());

        this.makePaneMoovable(anchorPainTodragWindow);

        this.textArea.setText(this.getUserTextField());
        this.textArea.setMinWidth(textArea.getPrefWidth());
        this.textArea.setMaxHeight(textArea.getPrefHeight());
        textArea.setWrapText(true);

        this.setImagesAndColorToButtons();

        this.fixCaretView();


        this.drawWorkerLabels();


    }

    private void makePaneMoovable(AnchorPane anchorPane){
        anchorPane.setOnMousePressed(e->{
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        anchorPane.setOnMouseDragged(e->{
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
            textArea.setPrefWidth(textArea.getPrefWidth()-xOffset);
        });
    }

    private void setImagesAndColorToButtons(){
        this.setImageToButton(closeButton, "cross.png", 11,20);
        this.setImageToButton(minimiseButton, "minimize.png", 13,40);
        this.setImageToButton(addNewWorkerButton, "plus.png", 38,15);

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

        addNewWorkerButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addNewWorkerButton.setStyle("-fx-background-color:#" + standartColorCursorOnButton);
            }
        });
        addNewWorkerButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addNewWorkerButton.setStyle("-fx-background-color: transparent");
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

    public String getUserTextField() {
        return  this.rootUser.getTextfield();
    }

    public int getUserId() {
        return this.rootUser.getId();
    }

    private void drawWorkerLabels(){
        List<Worker> workerList = rootUser.getWorkers();

        double additionalHeight = 0;

        for (int i = 0; i < workerList.size(); i++) {

            double additionalRectangleHeight = 0;


            double rectWidth = 302.5;
            double rectLX = 5;
            double rectLY = 5 + i * 55 + additionalHeight;


            Label workerLabel = new Label();
            Font workerFont = new Font("Arial", 16);
            workerLabel.setFont(workerFont);
            workerLabel.setText(workerList.get(i).getFirstname() + " " + workerList.get(i).getLastname());
            AnchorPane.setLeftAnchor(workerLabel, rectLX + 3);
            AnchorPane.setTopAnchor(workerLabel, rectLY + 3);
            workerLabel.setMaxWidth(rectWidth);
            workerLabel.setWrapText(true);
            anchorPaneForCards.getChildren().addAll(workerLabel);

            Separator inQueueSeparator = new Separator();
            AnchorPane.setLeftAnchor(inQueueSeparator, rectLX+1);
            inQueueSeparator.setLayoutY(rectLY+21+3);
            inQueueSeparator.setPrefWidth(301);
            inQueueSeparator.setMaxWidth(inQueueSeparator.getPrefWidth());
            anchorPaneForCards.getChildren().addAll(inQueueSeparator);

            Separator inWorkSeparator = new Separator();
            AnchorPane.setRightAnchor(inWorkSeparator, rectLX);
            inWorkSeparator.setLayoutY(rectLY+21+3);
            inWorkSeparator.setPrefWidth(301);
            inWorkSeparator.setMaxWidth(inWorkSeparator.getPrefWidth());
            anchorPaneForCards.getChildren().addAll(inWorkSeparator);


            List<Task> workerTasks = workerList.get(i).getTasks();
            for (int j = 0; j < workerTasks.size(); j++) {

                double rectLY1 = i * 55 + additionalHeight;

                String taskText = workerTasks.get(j).getTasktype();

                if(taskText.equals("inwork") || taskText.equals("quene")){

                    Rectangle taskRectangle = new Rectangle(253,23);

                    String standartColorCursorOnButton = "51abed";
                    taskRectangle.setFill(Color.WHITE);
                    taskRectangle.setViewOrder(2);
                    taskRectangle.setArcHeight(5);
                    taskRectangle.setArcWidth(5);
                    AnchorPane.setTopAnchor(taskRectangle, rectLY1 + 40);
                    anchorPaneForCards.getChildren().addAll(taskRectangle);
                    taskRectangle.setOpacity(0.75);


                    taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            taskRectangle.setFill(Color.web(standartColorCursorOnButton));
                        }
                    });

                    if(i%2!=0){
                        taskRectangle.setFill((Color.web("f2f6f9")));
                        taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                    taskRectangle.setFill((Color.web("f2f6f9")));

                            }
                        });
                    }else{
                        taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                           @Override
                           public void handle(MouseEvent mouseEvent) {
                               taskRectangle.setFill(Color.WHITE);

                           }
                    });
                    }

                    String myString = "- " + workerTasks.get(j).getText();
                    Label taskLabel = new Label();
                    Font font1 = new Font("Arial", 13);
                    taskLabel.setFont(font1);
                    taskLabel.setText(myString);
                    taskLabel.setPrefWidth(253);
                    taskLabel.setMaxWidth(taskLabel.getPrefWidth());
                    taskLabel.setPrefHeight(20);
                    taskLabel.setViewOrder(1);
                        if(taskText.equals("quene")) {
                            AnchorPane.setLeftAnchor(taskLabel, 50.0);
                            AnchorPane.setLeftAnchor(taskRectangle, 50.0);
                        }else{
                            AnchorPane.setRightAnchor(taskLabel, rectLX+3+20);
                            AnchorPane.setRightAnchor(taskRectangle, rectLX+3+20);
                            //add done button here
                        }

                    AnchorPane.setTopAnchor(taskLabel, rectLY1 + 40);
                    taskLabel.setWrapText(true);
                    taskLabel.setMouseTransparent(true);
                    anchorPaneForCards.getChildren().addAll(taskLabel);
                    additionalHeight+=25;
                    additionalRectangleHeight+=25;



                }
            }

            double rectHeight = 25*2 + additionalRectangleHeight;

            Rectangle queueRectangle = new Rectangle(rectWidth,rectHeight);
            queueRectangle.setFill(Color.WHITE);
            queueRectangle.setLayoutX(rectLX);
            queueRectangle.setViewOrder(3);
            AnchorPane.setLeftAnchor(queueRectangle, rectLX);
            AnchorPane.setTopAnchor(queueRectangle, rectLY);
            anchorPaneForCards.getChildren().addAll(queueRectangle);

            Rectangle inWorkRectangle = new Rectangle(rectWidth,rectHeight);
            inWorkRectangle.setFill(Color.WHITE);
            inWorkRectangle.setViewOrder(3);
            AnchorPane.setRightAnchor(inWorkRectangle, rectLX);
            AnchorPane.setTopAnchor(inWorkRectangle, rectLY);
            anchorPaneForCards.getChildren().addAll(inWorkRectangle);

            if(i%2!=0){
                queueRectangle.setFill(Color.web("f2f6f9"));
                inWorkRectangle.setFill(Color.web("f2f6f9"));
            }


        }



    }

    private void fixCaretView(){
        textArea.getCaretPosition();
        ObjectProperty<Rectangle> caretShape = new SimpleObjectProperty<>();
        textArea.caretPositionProperty().addListener((src, ov, nv ) -> {
            TextInputControlSkin<TextArea> skin = (TextInputControlSkin<TextArea>) textArea.getSkin();
            if (skin != null) {
                Rectangle2D bounds = skin.getCharacterBounds(nv.intValue());
                caretShape.set(new Rectangle(bounds.getMinX(), bounds.getMinY()-1,
                        4, bounds.getHeight()+2));
            }
        });
        caretShape.addListener((src, ov, r) -> {
            Skin<?> skin = textArea.getSkin();
            if (skin instanceof SkinBase) {
                if (ov != null) {
                    ((SkinBase<?>) skin).getChildren().remove(ov);
                }
                if (r != null) {
                    Color color = Color.DODGERBLUE;
                    r.setStroke(color);
                    r.setFill(color);
                    r.setMouseTransparent(true);
                    ((SkinBase<?>) skin).getChildren().add(r);
                }
            }
        });
    }
}
