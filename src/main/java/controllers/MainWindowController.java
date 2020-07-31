package controllers;

import database.Task;
import database.User;
import database.Worker;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
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

    @FXML
    private AnchorPane anchorPaneTextFieldBG;

    @FXML
    private Rectangle textFieldRectangle;


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
        this.textArea.setWrapText(true);
        this.textArea.setStyle( "-fx-background-color: transparent;"+
                                "-fx-border-color:#91afc5;"+
                                "-fx-background-insets: transparent;"+
                                "-fx-faint-focus-color: transparent;"+
                                "-fx-border-radius: 5;"+
                                "-fx-background-radius: 5;"+
                                "-fx-border-width: 1.5;");
        this.setImagesAndColorToButtons();

//        this.fixCaretView();

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

            double rectanglesArcRadius = 5;
            double rectWidth = 302.5;
            double rectLX = 5;
            double rectLY = 5 + i * 55 + additionalHeight;
            double strokeWidth = 1.5;

            String standartColorCursorOnButton = "51abed";
            String standartRectanglesColor = "FFFFFF";

            Label workerLabel = new Label();
            Font workerFont = new Font("Arial", 16);
            workerLabel.setFont(workerFont);
            workerLabel.setText(workerList.get(i).getFirstname() + " " + workerList.get(i).getLastname());
            AnchorPane.setLeftAnchor(workerLabel, rectLX + 12);
            AnchorPane.setTopAnchor(workerLabel, rectLY+i*2+6);

            workerLabel.setMaxWidth(rectWidth);
            workerLabel.setWrapText(true);
            anchorPaneForCards.getChildren().addAll(workerLabel);

            Line inQueueLine = new Line();
            inQueueLine.setStartX(rectLX+12);
            inQueueLine.setEndX(299);
            inQueueLine.setStartY(rectLY+i*2+6+25);
            inQueueLine.setEndY(rectLY+i*2+6+25);
            inQueueLine.setStrokeWidth(strokeWidth);
            inQueueLine.setOpacity(0.7);
            inQueueLine.setStroke(Paint.valueOf("91afc5"));
            anchorPaneForCards.getChildren().addAll(inQueueLine);

            Line inWorkLine = new Line();
            inWorkLine.setStartX(rectLX+1+4+2+5+303);
            inWorkLine.setEndX(301-6-4+8+305);
            inWorkLine.setStartY(rectLY+i*2+6+25);
            inWorkLine.setEndY(rectLY+i*2+6+25);
            inWorkLine.setStrokeWidth(strokeWidth);
            inWorkLine.setOpacity(0.7);
            inWorkLine.setStroke(Paint.valueOf("91afc5"));
            anchorPaneForCards.getChildren().addAll(inWorkLine);

            List<Task> workerTasks = workerList.get(i).getTasks();

            for (int j = 0; j < workerTasks.size(); j++) {

                Task task = workerTasks.get(j);

                double rectLY1 = i * 55 + additionalHeight;

                String taskText = workerTasks.get(j).getTasktype();

                if(taskText.equals("inwork") || taskText.equals("quene")){

                    Rectangle taskRectangle;
                    if(taskText.equals("quene")) {
                        taskRectangle = new Rectangle(253+4,23+2);
                    }else{
                        taskRectangle = new Rectangle(253+4+4+8+15,23+2);
                    }
                    taskRectangle.setFill(Color.web(standartRectanglesColor));
                    taskRectangle.setViewOrder(2);
                    taskRectangle.setArcHeight(rectanglesArcRadius*1.5);
                    taskRectangle.setArcWidth(rectanglesArcRadius*1.5);

                    AnchorPane.setTopAnchor(taskRectangle, rectLY1 + 48);
                    anchorPaneForCards.getChildren().addAll(taskRectangle);

                    taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            taskRectangle.setStrokeWidth(strokeWidth);
                            taskRectangle.setStroke(Paint.valueOf("91afc5"));
                        }
                    });

                    taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            taskRectangle.setStrokeWidth(0);
                        }
                    });

                    String myString = "- " + workerTasks.get(j).getText();
                    Label taskLabel = new Label();
                    Font font1 = new Font("Arial", 13);
                    taskLabel.setFont(font1);
                    taskLabel.setText(myString);
                    taskLabel.setPrefWidth(253);
                    taskLabel.setMaxWidth(taskLabel.getPrefWidth());
                    taskLabel.setPrefHeight(20);
                    taskLabel.setViewOrder(1);
                    taskLabel.setTextOverrun(OverrunStyle.ELLIPSIS);

                    if(taskText.equals("quene")) {
                        AnchorPane.setLeftAnchor(taskLabel, 50.0);
                        AnchorPane.setLeftAnchor(taskRectangle, 43.0);
                    }else{
                        AnchorPane.setRightAnchor(taskLabel, rectLX+3+20+10);
                        AnchorPane.setRightAnchor(taskRectangle, rectLX+3+20+10-4-8-15);
                        Button doneButton = new Button();
                        AnchorPane.setRightAnchor(doneButton, 8.0);
                        AnchorPane.setTopAnchor(doneButton, rectLY1 + 48);
                        doneButton.setStyle("-fx-background-color: transparent;");
                        int imgWidth = 18;
                        int imgHeight = 18;
                        this.setImageToButton(doneButton, "checkboxundone1.png", imgWidth, imgHeight);

                        doneButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                Image image = new Image("checkboxdone1.png");
                                ImageView imageView = new ImageView(image);
                                imageView.setPickOnBounds(true);
                                imageView.setPreserveRatio(true);
                                imageView.setFitWidth(imgWidth);
                                imageView.setFitHeight(imgHeight);
                                doneButton.setGraphic(imageView);
                            }
                        });

                        doneButton.setOnMouseExited(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                Image image = new Image("checkboxundone1.png");
                                ImageView imageView = new ImageView(image);
                                imageView.setPickOnBounds(true);
                                imageView.setPreserveRatio(true);
                                imageView.setFitWidth(imgWidth);
                                imageView.setFitHeight(imgHeight);
                                doneButton.setGraphic(imageView);
                            }
                        });


                        doneButton.setOnAction(d->{
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/finTaskRatingWindow.fxml"));
                            loader.setController(new FinTaskRatingWindowController(task, this, textArea));
                            try {loader.load();
                            } catch (IOException h) {
                                h.printStackTrace();
                            }
                            Parent root = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                        });


                        anchorPaneForCards.getChildren().addAll(doneButton);
                    }

                    AnchorPane.setTopAnchor(taskLabel, rectLY1 + 50);
                    taskLabel.setWrapText(true);
                    taskLabel.setMouseTransparent(true);
                    anchorPaneForCards.getChildren().addAll(taskLabel);
                    additionalHeight+=28;
                    additionalRectangleHeight+=28;

                }
            }

            double rectHeight = 25*2 + additionalRectangleHeight;

            Rectangle queueRectangle = new Rectangle(rectWidth-5.5,rectHeight);
            queueRectangle.setFill(Color.web(standartRectanglesColor));
            queueRectangle.setStrokeWidth(strokeWidth);
            queueRectangle.setStroke(Paint.valueOf("91afc5"));
            queueRectangle.setViewOrder(3);
            queueRectangle.setArcWidth(rectanglesArcRadius);
            queueRectangle.setArcHeight(rectanglesArcRadius);
            AnchorPane.setLeftAnchor(queueRectangle, rectLX+3.5);
            AnchorPane.setTopAnchor(queueRectangle, rectLY+i*2);
            anchorPaneForCards.getChildren().addAll(queueRectangle);

            Rectangle inWorkRectangle = new Rectangle(rectWidth-5.5+3.5,rectHeight);
            inWorkRectangle.setFill(Color.web(standartRectanglesColor));
            inWorkRectangle.setStrokeWidth(strokeWidth);
            inWorkRectangle.setStroke(Paint.valueOf("91afc5"));
            inWorkRectangle.setViewOrder(4);
            inWorkRectangle.setArcWidth(rectanglesArcRadius);
            inWorkRectangle.setArcHeight(rectanglesArcRadius);
            AnchorPane.setRightAnchor(inWorkRectangle, rectLX);
            AnchorPane.setTopAnchor(inWorkRectangle, rectLY+i*2);
            anchorPaneForCards.getChildren().addAll(inWorkRectangle);


        }



    }

    private void fixCaretView(){
        textArea.getCaretPosition();
        ObjectProperty<Rectangle> caretShape = new SimpleObjectProperty<>();
        textArea.caretPositionProperty().addListener((src, ov, nv ) -> {
            TextInputControlSkin<TextArea> skin = (TextInputControlSkin<TextArea>) textArea.getSkin();
            if (skin != null) {
                Rectangle2D bounds = skin.getCharacterBounds(nv.intValue());
                caretShape.set(new Rectangle(bounds.getMinX()+12.5, bounds.getMinY()-1+12.5,
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
