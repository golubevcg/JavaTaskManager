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

        double additionalHeight=0;

        for (int i = 0; i < workerList.size(); i++) {



            double rectWidth = 302.5;
            double rectHeight = 20*2.5;
            double rectLX = 5;
            double rectLY = 5 + i * 50 + additionalHeight;
            System.out.println("rectLY" + rectLY);
//
//            Rectangle rectangle = new Rectangle(rectWidth,rectHeight);
//            rectangle.setFill(Color.WHITE);
//            rectangle.setLayoutX(rectLX);
//            AnchorPane.setLeftAnchor(rectangle, rectLX);
//            AnchorPane.setTopAnchor(rectangle, rectLY);
//            anchorPaneForCards.getChildren().addAll(rectangle);
//
//            Rectangle rectangle1 = new Rectangle(rectWidth,rectHeight);
//            rectangle1.setFill(Color.WHITE);
//            AnchorPane.setRightAnchor(rectangle1, rectLX);
//            AnchorPane.setTopAnchor(rectangle1, rectLY);
//            anchorPaneForCards.getChildren().addAll(rectangle1);

            Label label = new Label();
            label.setTranslateZ(2);
            Font font = new Font("Arial", 16);
            label.setFont(font);
            label.setText(workerList.get(i).getFirstname() + " " + workerList.get(i).getLastname());
            AnchorPane.setLeftAnchor(label, rectLX + 3);
            AnchorPane.setTopAnchor(label, rectLY + 3);
            label.setMaxWidth(rectWidth);
            label.setWrapText(true);
            anchorPaneForCards.getChildren().addAll(label);

            Separator separator = new Separator();
            AnchorPane.setLeftAnchor(separator, rectLX+1);
            separator.setLayoutY(rectLY+21+3);
            separator.setPrefWidth(301);
            separator.setMaxWidth(separator.getPrefWidth());
            anchorPaneForCards.getChildren().addAll(separator);

            Separator separator1 = new Separator();
            AnchorPane.setRightAnchor(separator1, rectLX);
            separator1.setLayoutY(rectLY+21+3);
            separator1.setPrefWidth(301);
            separator1.setMaxWidth(separator1.getPrefWidth());
            anchorPaneForCards.getChildren().addAll(separator1);


            List<Task> workerTasks = workerList.get(i).getTasks();

            for (int j = 0; j < workerTasks.size(); j++) {

                double rectLY1 = 5 + i * 50 + additionalHeight + 2;

                String taskText = workerTasks.get(j).getTasktype();

                if(taskText.equals("inwork") || taskText.equals("quene")){
                    System.out.println("iteration"+j + " needs to be drawn");
                    String myString = workerTasks.get(j).getText();
                    Label label1 = new Label();
                    Font font1 = new Font("Arial", 13);
                    label1.setFont(font1);
                    label1.setText(myString);
                    label1.setPrefWidth(253);
                    label1.setMaxWidth(label1.getPrefWidth());
                    label1.setPrefHeight(20);
                    label1.setWrapText(true);

                        if(taskText.equals("quene")) {
                            AnchorPane.setLeftAnchor(label1, 50.0);
                            System.out.println("iteration"+j + " is quene");
                        }else{
                            AnchorPane.setRightAnchor(label1, rectLX+3);
                            System.out.println("iteration"+j + " is in work");
                        }

                    AnchorPane.setTopAnchor(label1, rectLY1 + 40);
                    anchorPaneForCards.getChildren().addAll(label1);
                    additionalHeight+=20;
                }
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
