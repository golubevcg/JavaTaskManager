package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class MainWindowController {

    private User rootUser;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane anchorPainToDragWindow;

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

    private List<Label> labelsItemsList = new ArrayList<>();
    private List<Shape> shapesItemsList = new ArrayList<>();
    private List<Button> buttonsItemsList = new ArrayList<>();


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

        this.forDropShadowTopAnchorPane.setStyle("-fx-background-color: transparent;");
        this.forDropShadowTopAnchorPane.setPadding(new Insets(10,10,10,10));
        this.forDropShadowTopAnchorPane.setEffect(new DropShadow());

        this.cleanAllNodes();

        this.mainAnchorPane.setMinWidth(mainAnchorPane.getPrefWidth());
        this.mainAnchorPane.setMaxWidth(mainAnchorPane.getPrefHeight());

        this.makePaneMoovable(mainAnchorPane);

        this.textArea.setText(this.getUserTextField());
        this.textArea.setMinWidth(textArea.getPrefWidth());
        this.textArea.setMaxHeight(textArea.getPrefHeight());
        this.textArea.setWrapText(true);
        String stylesheet = getClass().getResource("/styles.css").toExternalForm();
        textArea.getStylesheets().add(stylesheet);
        this.createTextAreaContextMenus();

        this.setImagesAndColorToButtons();

        this.drawWorkerLabels();

        NewWorkerWindowController newWorkerWindowController = new NewWorkerWindowController(this);
        this.addNewWorkerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/newWorkerWindow.fxml"));
                loader.setController(newWorkerWindowController);
                try {loader.load();
                } catch (IOException a) {
                    a.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
//                stage.initStyle(StageStyle.UNDECORATED);
//                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root);
//                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        });

    }

    public void cleanAllNodes(){
        for (int i = 0; i < labelsItemsList.size() ; i++) {
            this.anchorPaneForCards.getChildren().remove(labelsItemsList.get(i));
        }
        for (int i = 0; i < shapesItemsList.size() ; i++) {
            this.anchorPaneForCards.getChildren().remove(shapesItemsList.get(i));
        }
        for (int i = 0; i < buttonsItemsList.size() ; i++) {
            this.anchorPaneForCards.getChildren().remove(buttonsItemsList.get(i));
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

    private List<MenuItem> createDefaultMenuItems(TextInputControl t) {
        MenuItem cut = new MenuItem("Cut");
        cut.setOnAction(e -> t.cut());
        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(e -> t.copy());
        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(e -> t.paste());
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> t.deleteText(t.getSelection()));
        MenuItem selectAll = new MenuItem("Select All");
        selectAll.setOnAction(e -> t.selectAll());

        BooleanBinding emptySelection = Bindings.createBooleanBinding(() ->
                        t.getSelection().getLength() == 0,
                t.selectionProperty());

        cut.disableProperty().bind(emptySelection);
        copy.disableProperty().bind(emptySelection);
        delete.disableProperty().bind(emptySelection);

        return Arrays.asList(cut, copy, paste, delete, new SeparatorMenuItem(), selectAll);
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

    private void updateRootUserFromDB(){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> list1 = session.createQuery("FROM User WHERE id = '" + this.getUser().getId() + "'").list();
        session.getTransaction();
        session.close();
        this.setUser(list1.get(0));
    }

    private void drawWorkerLabels(){

        this.updateRootUserFromDB();


        List<Worker> workerList = rootUser.getWorkers();

        double additionalHeight = 0;

        for (int i = 0; i < workerList.size(); i++) {

            Worker worker = workerList.get(i);

            double additionalRectangleHeight = 0;

            double rectanglesArcRadius = 5;
            double rectWidth = 302.5;
            double rectLX = 5;
            double rectLY = 5 + i * 55 + additionalHeight;
            double strokeWidth = 1.5;

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

            ContextMenu workerLabelContextMenu = new ContextMenu();
            MenuItem addTaskToWorker = new MenuItem("Добавить новую задачу");
            MenuItem editWorker = new MenuItem("Отредактировать сотрудника");
            MenuItem deleteWorker = new MenuItem("Удалить сотрудника");

            NewTaskWindowController newTaskWindowController = new NewTaskWindowController(this, worker);
            addTaskToWorker.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/newTaskWindow.fxml"));
                    loader.setController(newTaskWindowController);
                    try {loader.load();
                    } catch (IOException a) {
                        a.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    newTaskWindowController.setStage(stage);
                    stage.setResizable(false);
                    stage.show();
                }
            });

            DeleteWorkerWindowController deleteWorkerWindowController = new DeleteWorkerWindowController(this, worker);

            deleteWorker.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/fxml/deleteWorkerConfirmationWindow.fxml"));
                    loader.setController(deleteWorkerWindowController);
                    try {loader.load();
                    } catch (IOException a) {
                        a.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    deleteWorkerWindowController.setStage(stage);
                    stage.show();
                }
            });

            workerLabelContextMenu.getItems().addAll(addTaskToWorker,editWorker,deleteWorker);
            workerLabel.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent contextMenuEvent) {
                    workerLabelContextMenu.show(workerLabel, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                }
            });

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

            shapesItemsList.add(inQueueLine);
            shapesItemsList.add(inWorkLine);

            labelsItemsList.add(workerLabel);

            List<Task> workerTasks = workerList.get(i).getTasks();

            System.out.println("++++++++++++++++++++++++++");
            System.out.println(workerList.get(i).getFirstname() +" "+workerList.get(i).getLastname());
            System.out.println(workerList.get(i).getTasks().size());
            System.out.println("++++++++++++++++++++++++++");

            for (int j = 0; j < workerTasks.size(); j++) {

                Task task = workerTasks.get(j);
                double rectLY1 = i * 55 + additionalHeight;
                String taskType = workerTasks.get(j).getTasktype();

                if(taskType.equals("inwork") || taskType.equals("quene")){
                    Rectangle taskRectangle;
                    if(taskType.equals("quene")) {
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

                    MenuItem editTask = new MenuItem("Отредактировать задачу");
                    MenuItem deleteTask = new MenuItem("Удалить задачу");
                    MenuItem markByColor = new MenuItem("Пометить цветом");

                    EditTaskWindowController editTaskWindowController = new EditTaskWindowController(this,task);
                    editTask.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/newTaskWindow.fxml"));
                            loader.setController(editTaskWindowController);
                            try {loader.load();
                            } catch (IOException a) {
                                a.printStackTrace();
                            }
                            Parent root = loader.getRoot();
                            Stage stage = new Stage();
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            Scene scene = new Scene(root);
                            scene.setFill(Color.TRANSPARENT);
                            stage.setScene(scene);
                            editTaskWindowController.setStage(stage);
                            stage.setResizable(false);
                            stage.show();
                        }
                    });

                    DeleteTaskWindowController deleteTaskWindowController = new DeleteTaskWindowController(this, task);
                    deleteTask.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/fxml/deleteTaskConfirmationWindow.fxml"));
                            loader.setController(deleteTaskWindowController);
                            try {loader.load();
                            } catch (IOException a) {
                                a.printStackTrace();
                            }
                            Parent root = loader.getRoot();
                            Stage stage = new Stage();
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            Scene scene = new Scene(root);
                            scene.setFill(Color.TRANSPARENT);
                            stage.setScene(scene);
                            editTaskWindowController.setStage(stage);
                            stage.setResizable(false);
                            stage.show();                        }
                    });

                    markByColor.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                        }
                    });

                    TaskService taskservise = new TaskService();
                    MainWindowController thisMainWindowController = this;
                    if(taskType.equals("quene")){
                        ContextMenu taskRectangleContextMenu = new ContextMenu();
                        MenuItem moveTaskToInWork = new MenuItem("Сделать задачу в работе");

                        taskRectangleContextMenu.getItems().addAll(moveTaskToInWork,editTask,deleteTask,markByColor);
                        taskRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent contextMenuEvent) {
                                taskRectangleContextMenu.show(taskRectangle, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                            }
                        });

                        moveTaskToInWork.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                task.setTasktype("inwork");
                                taskservise.updateTask(task);
                                thisMainWindowController.initialize();
                            }
                        });
                    }

                    if(taskType.equals("inwork")){
                        ContextMenu taskRectangleContextMenu = new ContextMenu();
                        MenuItem moveTaskToInQueue = new MenuItem("Вернуть задачу в очередь");

                        taskRectangleContextMenu.getItems().addAll(moveTaskToInQueue,editTask,deleteTask,markByColor);
                        taskRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent contextMenuEvent) {
                                taskRectangleContextMenu.show(taskRectangle, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                            }
                        });

                        moveTaskToInQueue.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                task.setTasktype("quene");
                                taskservise.updateTask(task);
                                thisMainWindowController.initialize();
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
                    taskLabel.setTextOverrun(OverrunStyle.ELLIPSIS);

                    if(taskType.equals("quene")) {
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
                        buttonsItemsList.add(doneButton);
                    }

                    AnchorPane.setTopAnchor(taskLabel, rectLY1 + 50);
                    taskLabel.setWrapText(true);
                    taskLabel.setMouseTransparent(true);
                    anchorPaneForCards.getChildren().addAll(taskLabel);
                    additionalHeight+=28;
                    additionalRectangleHeight+=28;

                    labelsItemsList.add(taskLabel);
                    shapesItemsList.add(taskRectangle);
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

            shapesItemsList.add(queueRectangle);
            shapesItemsList.add(inWorkRectangle);
        }

    }

    public boolean addNewTaskFromText(String text, Worker worker, String tasktype){
        if (text.isEmpty()) {
            return false;
        } else {
            Task task = new Task(text, worker, tasktype);
            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(text);
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
                session2.save(task);
                transaction1.commit();
                session2.close();
                return true;
            }
        }
    }

    public void createTextAreaContextMenus(){
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createDefaultMenuItems(textArea));
        Menu createTaskFromSelectedMenuItem = new Menu("Создать задачу из выделенного");
        List<Menu> menuItemsList = new ArrayList<>();
        for (int i = 0; i < rootUser.getWorkers().size(); i++) {
            Worker worker = rootUser.getWorkers().get(i);
            Menu workerMenu = new Menu(worker.getFirstname()
                    + " " + worker.getLastname());
            menuItemsList.add(workerMenu);
            MenuItem inWorkMenuItem = new MenuItem("в работу");
            MenuItem inQueueMenuItem = new MenuItem("в очередь");
            workerMenu.getItems().addAll(inWorkMenuItem,inQueueMenuItem);

            inWorkMenuItem.setOnAction(d->{
                String selectedText = textArea.getSelectedText();
                if(addNewTaskFromText(selectedText, worker, "inwork")){
                    rootUser.setTextfield(textArea.getText()
                            .replace(selectedText, ""));
                    worker.addTask(new Task(textArea.getSelectedText(), worker, "inwork"));
                    this.initialize();
                }
            });

            inQueueMenuItem.setOnAction(d->{
                String selectedText = textArea.getSelectedText();
                if(addNewTaskFromText(selectedText, worker, "quene")){
                    rootUser.setTextfield(textArea.getText()
                            .replace(selectedText, ""));
                    worker.addTask(new Task(textArea.getSelectedText(), worker, "quene"));
                    this.initialize();
                }
            });

        }

        createTaskFromSelectedMenuItem.getItems().addAll(menuItemsList);
        contextMenu.getItems().addAll(createTaskFromSelectedMenuItem);
        this.textArea.setContextMenu(contextMenu);
    }

    public User getUser(){
        return this.rootUser;
    }

    public void setUser(User user){this.rootUser = user;}

    public void addToTextArea(String string){
        this.rootUser.setTextfield(this.textArea.getText() + "\n" + string);
    }

}
