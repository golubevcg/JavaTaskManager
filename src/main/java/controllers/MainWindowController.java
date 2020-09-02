package controllers;

import classes.SceneOpener;
import classes.TextFieldCheckerEach30sec;
import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
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
import javafx.scene.control.*;
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
import org.hibernate.Session;

import java.net.URL;
import java.util.*;


public class MainWindowController extends ControllerParent{

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
    private AnchorPane movableAnchorPane;

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
    private Menu mainMenu;

    @FXML
    private MenuItem addWorkerMenu;

    @FXML
    private MenuItem statisticsMenu;

    @FXML
    private MenuItem settingsMenu;

    @FXML
    private MenuItem quitMenu;

    @FXML
    private Button closeButton;

    @FXML
    private Button minimiseButton;

    @FXML
    private AnchorPane anchorPaneForCards;

    @FXML
    private AnchorPane anchorPaneTextFieldBG;

    @FXML
    private Rectangle textFieldRectangle;

    private List<Label> labelsItemsList = new ArrayList<>();
    private List<Shape> shapesItemsList = new ArrayList<>();
    private List<Button> buttonsItemsList = new ArrayList<>();
    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private SceneOpener sceneOpener = new SceneOpener();
    CloseWindowConfirmationController closeWindowConfirmationController =
            new CloseWindowConfirmationController(this.stage);
    Stage closeWindowStage = new Stage();

    public MainWindowController(User rootUser) {
        this.rootUser = rootUser;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private int counter = 0;

    public void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(movableAnchorPane);

        this.cleanAllNodes();

        this.editMenuBarMenus();

        this.mainAnchorPane.setMinWidth(mainAnchorPane.getPrefWidth());
        this.mainAnchorPane.setMaxWidth(mainAnchorPane.getPrefHeight());

        this.setupTextArea();

        TextFieldCheckerEach30sec.initialize(this);
        TextFieldCheckerEach30sec.start();

        this.setStylesToButtons();

        this.setupUI();

        NewWorkerWindowController newWorkerWindowController = new NewWorkerWindowController(this);
        Stage newWorkerStage = new Stage();
        addWorkerMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/newWorkerWindow.fxml",
                        newWorkerStage, stage,newWorkerWindowController, false);
            }
        });

    }

    private void setupTextArea() {
        this.textArea.setText(this.rootUser.getTextfield());
        this.textArea.setMinWidth(textArea.getPrefWidth());
        this.textArea.setMaxHeight(textArea.getPrefHeight());
        this.textArea.setWrapText(true);
        String stylesheet = getClass().getResource("/styles.css").toExternalForm();
        textArea.getStylesheets().add(stylesheet);
        this.createTextAreaContextMenus();
        textArea.textProperty().addListener(((observableValue, oldvalue, newvalue) ->
                {
                    textArea.setText(newvalue);
                    rootUser.setTextfield(newvalue);
                })
        );
    }

    private void cleanAllNodes(){
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

    private void setupUI() {
        this.updateRootUserFromDB();
        this.drawWorkersUILabels();
    }

    double additionalHeight = 0;
    double strokeWidth = uiColorAndStyleSettings.getStrokeWidth();
    double rectanglesArcRadius = 5;
    double rectWidth = 302.5;
    double rectLX = 5;
    double additionalRectangleHeight = 0;

    private void drawWorkersUILabels(){
        additionalHeight = 0;
        List<Worker> workerList = rootUser.getWorkers();
        Collections.sort(workerList);
        for (int i = 0; i < workerList.size(); i++) {
            additionalRectangleHeight = 0;
            Worker currentWorker = workerList.get(i);
            double rectLY = 5 + i * 55 + additionalHeight;
            Label workerLabel = this.setupWorkerLabel(currentWorker, i);
            this.setupWorkerLabelContextMenus(currentWorker, workerLabel);
            this.setupWorkerLines(i);
            this.setupWorkerTasksUI(currentWorker.getTasks(), i);
            this.setupWorkerRectanglesUI( i,rectLY);
        }
    }

    private void setupWorkerRectanglesUI(int i, double rectLY) {

        double rectHeight = 50 + additionalRectangleHeight;

        Rectangle queueRectangle = new Rectangle(rectWidth-5.5,rectHeight);
        queueRectangle.setFill(Color.web( uiColorAndStyleSettings.getMainBGUiColor() ));
        queueRectangle.setStrokeWidth(strokeWidth);
        queueRectangle.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
        queueRectangle.setViewOrder(3);
        queueRectangle.setArcWidth(rectanglesArcRadius);
        queueRectangle.setArcHeight(rectanglesArcRadius);
        AnchorPane.setLeftAnchor(queueRectangle, rectLX+3.5);
        AnchorPane.setTopAnchor(queueRectangle, rectLY+i*2);
        anchorPaneForCards.getChildren().addAll(queueRectangle);

        Rectangle inWorkRectangle = new Rectangle(rectWidth-5.5+3.5,rectHeight);
        inWorkRectangle.setFill(Color.web( uiColorAndStyleSettings.getMainBGUiColor() ));
        inWorkRectangle.setStrokeWidth(strokeWidth);
        inWorkRectangle.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
        inWorkRectangle.setViewOrder(4);
        inWorkRectangle.setArcWidth(rectanglesArcRadius);
        inWorkRectangle.setArcHeight(rectanglesArcRadius);
        AnchorPane.setRightAnchor(inWorkRectangle, rectLX);
        AnchorPane.setTopAnchor(inWorkRectangle, rectLY+i*2);
        anchorPaneForCards.getChildren().addAll(inWorkRectangle);

        shapesItemsList.add(queueRectangle);
        shapesItemsList.add(inWorkRectangle);
    }

    private void setupWorkerLines(int i) {
        double rectLY = 5 + i * 55 + additionalHeight;
        Line inQueueLine = new Line();
        inQueueLine.setStartX(rectLX+12);
        inQueueLine.setEndX(299);
        inQueueLine.setStartY(rectLY+i*2+6+25);
        inQueueLine.setEndY(rectLY+i*2+6+25);
        inQueueLine.setStrokeWidth(strokeWidth);
        inQueueLine.setOpacity(0.7);
        inQueueLine.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
        anchorPaneForCards.getChildren().addAll(inQueueLine);

        Line inWorkLine = new Line();
        inWorkLine.setStartX(rectLX+315);
        inWorkLine.setEndX(604);
        inWorkLine.setStartY(rectLY+i*2+31);
        inWorkLine.setEndY(rectLY+i*2+31);
        inWorkLine.setStrokeWidth(strokeWidth);
        inWorkLine.setOpacity(0.7);
        inWorkLine.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
        anchorPaneForCards.getChildren().addAll(inWorkLine);

        shapesItemsList.add(inQueueLine);
        shapesItemsList.add(inWorkLine);
    }

    private void setupWorkerLabelContextMenus(Worker currentWorker, Label workerLabel) {
        ContextMenu workerLabelContextMenu = new ContextMenu();
        MenuItem addTaskToWorker = new MenuItem("Добавить новую задачу");
        MenuItem editWorker = new MenuItem("Отредактировать сотрудника");
        MenuItem deleteWorker = new MenuItem("Удалить сотрудника");

        addTaskToWorker.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        editWorker.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        deleteWorker.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );

        NewTaskWindowController newTaskWindowController = new NewTaskWindowController(this, currentWorker);
        Stage newTaskWindowStage = new Stage();
        addTaskToWorker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/newTaskWindow.fxml", newTaskWindowStage,
                        (Stage) closeButton.getScene().getWindow(), newTaskWindowController, false);
            }
        });

        DeleteWorkerWindowController deleteWorkerWindowController = new DeleteWorkerWindowController(this, currentWorker);
        Stage deleteWorkerConfirmWStage = new Stage();
        deleteWorker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/deleteWorkerConfirmationWindow.fxml",
                        deleteWorkerConfirmWStage, (Stage) closeButton.getScene().getWindow(),
                        deleteWorkerWindowController, false);
            }
        });

        workerLabelContextMenu.getItems().addAll(addTaskToWorker,editWorker,deleteWorker);
        workerLabel.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                workerLabelContextMenu.show(workerLabel, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
        });
    }

    private Label setupWorkerLabel(Worker currentWorker, int i) {
        double rectLY = 5 + i * 55 + additionalHeight;
        Label workerLabel = new Label();
        Font workerFont = new Font("Arial", 16);
        workerLabel.setFont(workerFont);
        workerLabel.setText(currentWorker.getFirstname() + " " + currentWorker.getLastname());
        AnchorPane.setLeftAnchor(workerLabel, rectLX + 12);
        AnchorPane.setTopAnchor(workerLabel, rectLY+i*2+6);
        workerLabel.setMaxWidth(rectWidth);
        workerLabel.setWrapText(true);
        anchorPaneForCards.getChildren().addAll(workerLabel);
        labelsItemsList.add(workerLabel);

        return workerLabel;
    }

    String color1ToMarkTask = uiColorAndStyleSettings.getColor1ToMarkTask();
    String strokeColor1ToMarkTask = uiColorAndStyleSettings.getStrokeColor1ToMarkTask();
    String color1HighlightedToMarkTask = uiColorAndStyleSettings.getColor1HighlightedToMarkTask();

    String color2ToMarkTask = uiColorAndStyleSettings.getColor2ToMarkTask();
    String strokeColor2ToMarkTask = uiColorAndStyleSettings.getStrokeColor2ToMarkTask();
    String color2HighlightedToMarkTask = uiColorAndStyleSettings.getColor2HighlightedToMarkTask();

    String color3ToMarkTask = uiColorAndStyleSettings.getColor3ToMarkTask();
    String strokeColor3ToMarkTask = uiColorAndStyleSettings.getStrokeColor3ToMarkTask();
    String color3HighlightedToMarkTask = uiColorAndStyleSettings.getColor3HighlightedToMarkTask();

    private void setupWorkerTasksUI(List<Task> workerTasks, int i){

        Collections.sort(workerTasks);

        for (int j = 0; j < workerTasks.size(); j++) {

            Task task = workerTasks.get(j);

            double rectLY1 = i * 55 + additionalHeight;

            String taskType = task.getTasktype();

            if(!taskType.equals("done")){

                Rectangle taskRectangle = this.setupTaskRectangle(task, rectLY1);

                this.setupTaskMenuItems(task, taskRectangle);

                String myString = "- " + task.getText();
                Label taskLabel = new Label();
                Font font1 = new Font("Arial", 14);
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
                    AnchorPane.setRightAnchor(taskLabel, rectLX+33);
                    AnchorPane.setRightAnchor(taskRectangle, rectLX+6);
                    Button doneButton = new Button();
                    AnchorPane.setRightAnchor(doneButton, 8.0);
                    AnchorPane.setTopAnchor(doneButton, rectLY1 + 48);
                    doneButton.setStyle("-fx-background-color: transparent;");
                    int imgWidth = 18;
                    int imgHeight = 18;
                    uiColorAndStyleSettings.setImageToButton(
                            doneButton, "checkboxundone1.png", imgWidth, imgHeight);

                    doneButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            uiColorAndStyleSettings.setImageToButton(
                                    doneButton, "checkboxdone1.png", imgWidth, imgHeight);

                        }
                    });

                    doneButton.setOnMouseExited(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            uiColorAndStyleSettings.setImageToButton(
                                    doneButton, "checkboxundone1.png", imgWidth, imgHeight);
                        }
                    });

                    Stage finTaskRatingWindowStage = new Stage();
                    doneButton.setOnAction(d->{
                        FinTaskRatingWindowController finTaskRatingWindowController = new FinTaskRatingWindowController(task, this);
                        sceneOpener.openNewScene("/fxml/finTaskRatingWindow.fxml",
                                finTaskRatingWindowStage, (Stage) closeButton.getScene().getWindow(), finTaskRatingWindowController, false);
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
    }

    private void setupTaskMenuItems(Task task, Rectangle taskRectangle) {
        MenuItem editTask = new MenuItem("Отредактировать задачу");
        MenuItem deleteTask = new MenuItem("Удалить задачу");
        MenuItem returnToTextfield = new MenuItem("Вернуть задачу в записи");
        Menu markByColor = new Menu("Пометить цветом");
        MenuItem yellow = new MenuItem("#e2e0c8");
        MenuItem blue = new MenuItem("#c8d7e2");
        MenuItem red = new MenuItem("#e2c8ca");
        MenuItem defaultColor = new MenuItem("default");

        uiColorAndStyleSettings.setImageToMenuItem(yellow, "e2e0c8.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(blue, "c8d7e2.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(red, "e2c8ca.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(editTask, "editTask.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(deleteTask, "taskCross.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(markByColor, "paintbrush.png", 16,16);
        uiColorAndStyleSettings.setImageToMenuItem(returnToTextfield, "returnToTextfield.png", 16,16);

        editTask.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        deleteTask.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        returnToTextfield.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        markByColor.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );

        yellow.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        blue.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        red.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        defaultColor.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );

        markByColor.getItems().addAll(yellow,blue,red,defaultColor);

        returnToTextfield.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removeTaskAddTaskTextToTextfield(task);
            }
        });

        TaskService taskService = new TaskService();

        yellow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                task.setColor(color1ToMarkTask);
                taskService.updateTask(task);
                taskRectangle.setFill(Color.web(color1ToMarkTask));
                taskRectangle.setStroke(Paint.valueOf(strokeColor1ToMarkTask));

                taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(strokeWidth);
                        taskRectangle.setFill(Color.web(color1HighlightedToMarkTask));
                    }
                });

                taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(0);
                        taskRectangle.setFill(Color.web(color1ToMarkTask));
                    }
                });
            }
        });

        blue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                task.setColor(color2ToMarkTask);
                taskService.updateTask(task);
                taskRectangle.setFill(Color.web(color2ToMarkTask));
                taskRectangle.setStroke(Paint.valueOf(strokeColor2ToMarkTask));

                taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(strokeWidth);
                        taskRectangle.setFill(Color.web(color2HighlightedToMarkTask));
                    }
                });

                taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(0);
                        taskRectangle.setFill(Color.web(color2HighlightedToMarkTask));
                    }
                });
            }
        });

        red.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                task.setColor(color3ToMarkTask);
                taskService.updateTask(task);
                taskRectangle.setFill(Color.web(color3ToMarkTask));
                taskRectangle.setStroke(Paint.valueOf(strokeColor3ToMarkTask));

                taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(strokeWidth);
                        taskRectangle.setFill(Color.web(color3HighlightedToMarkTask));
                    }
                });

                taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(0);
                        taskRectangle.setFill(Color.web(color3ToMarkTask));

                    }
                });
            }
        });

        defaultColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                taskRectangle.setFill(Color.web( uiColorAndStyleSettings.getMainBGUiColor() ));
                task.setColor( uiColorAndStyleSettings.getMainBGUiColor() );
                taskService.updateTask(task);
                taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(strokeWidth);
                        taskRectangle.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
                    }
                });

                taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        taskRectangle.setStrokeWidth(0);
                    }
                });
            }
        });

        EditTaskWindowController editTaskWindowController = new EditTaskWindowController(this,task);
        Stage editTaskWindowStage = new Stage();
        editTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/newTaskWindow.fxml", editTaskWindowStage,
                        (Stage) closeButton.getScene().getWindow(), editTaskWindowController, false);
            }
        });

        DeleteTaskWindowController deleteTaskWindowController =
                new DeleteTaskWindowController(this, task);
        Stage deleteTaskConfirmStage = new Stage();
        deleteTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/deleteTaskConfirmationWindow.fxml",
                        deleteTaskConfirmStage, (Stage) closeButton.getScene().getWindow(),
                        deleteTaskWindowController, false);
            }
        });

        TaskService taskservice = new TaskService();
        MainWindowController thisMainWindowController = this;

        if(("quene".equals(task.getTasktype()))){
            ContextMenu taskRectangleContextMenu = new ContextMenu();
            MenuItem moveTaskToInWork = new MenuItem("Сделать задачу в работе");
            moveTaskToInWork.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
            uiColorAndStyleSettings.setImageToMenuItem(moveTaskToInWork, "moveToWork.png", 16,16);

            taskRectangleContextMenu.getItems()
                    .addAll(moveTaskToInWork,editTask,markByColor, returnToTextfield, deleteTask);
            taskRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent contextMenuEvent) {
                    taskRectangleContextMenu
                            .show(taskRectangle, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                }
            });

            moveTaskToInWork.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    task.setTasktype("inwork");
                    taskservice.updateTask(task);
                    thisMainWindowController.initialize();
                }
            });
        }

        if(("inwork").equals(task.getTasktype())){
            ContextMenu taskRectangleContextMenu = new ContextMenu();
            MenuItem moveTaskToInQueue = new MenuItem("Вернуть задачу в очередь");
            moveTaskToInQueue.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
            uiColorAndStyleSettings.setImageToMenuItem(moveTaskToInQueue, "moveToQuene.png",16,16);

            taskRectangleContextMenu.getItems()
                    .addAll(moveTaskToInQueue,editTask,markByColor, returnToTextfield, deleteTask);
            taskRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent contextMenuEvent) {
                    taskRectangleContextMenu
                            .show(taskRectangle, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
                }
            });

            moveTaskToInQueue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    task.setTasktype("quene");
                    taskservice.updateTask(task);
                    thisMainWindowController.initialize();
                }
            });
        }
    }

    private Rectangle setupTaskRectangle(Task task, double rectLY1) {
        Rectangle taskRectangle;

        if(task.getTasktype().equals("quene")) {
            taskRectangle = new Rectangle(257,25);
        }else{
            taskRectangle = new Rectangle(284,25);
        }

        taskRectangle.setFill( Paint.valueOf( uiColorAndStyleSettings.getMainBGUiColor() ));
        taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                taskRectangle.setStrokeWidth(strokeWidth);
                taskRectangle.setStroke(Paint.valueOf( uiColorAndStyleSettings.getMainUiBordersColor() ));
            }
        });
        taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                taskRectangle.setStrokeWidth(0);
            }
        });

        if( color1ToMarkTask.equals(task.getColor())){
            taskRectangle.setFill(Color.web(color1ToMarkTask));
            taskRectangle.setStroke(Paint.valueOf(strokeColor1ToMarkTask));

            taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(strokeWidth);
                    taskRectangle.setFill(Color.web(color1HighlightedToMarkTask));
                }
            });

            taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(0);
                    taskRectangle.setFill(Color.web(color1ToMarkTask));
                }
            });
        }

        if(color2ToMarkTask.equals(task.getColor())){
            taskRectangle.setFill(Color.web(color2ToMarkTask));
            taskRectangle.setStroke(Paint.valueOf(strokeColor2ToMarkTask));

            taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(strokeWidth);
                    taskRectangle.setFill(Color.web(color2HighlightedToMarkTask));
                }
            });

            taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(0);
                    taskRectangle.setFill(Color.web(color2ToMarkTask));
                }
            });
        }


        if(color3ToMarkTask.equals(task.getColor())){
            taskRectangle.setFill(Color.web(color3ToMarkTask));
            taskRectangle.setStroke(Paint.valueOf(strokeColor3ToMarkTask));

            taskRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(strokeWidth);
                    taskRectangle.setFill(Color.web(color3HighlightedToMarkTask));
                }
            });

            taskRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    taskRectangle.setStrokeWidth(0);
                    taskRectangle.setFill(Color.web(color3ToMarkTask));
                }
            });
        }

        taskRectangle.setViewOrder(2);
        taskRectangle.setArcHeight(rectanglesArcRadius*1.5);
        taskRectangle.setArcWidth(rectanglesArcRadius*1.5);
        AnchorPane.setTopAnchor(taskRectangle, rectLY1 + 48);
        anchorPaneForCards.getChildren().addAll(taskRectangle);

        return taskRectangle;
    }

    private void editMenuBarMenus(){
        Image image = new Image("settings.png");
        ImageView imageView = new ImageView(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(34);
        imageView.setFitHeight(17);
        mainMenu.setGraphic(imageView);

        addWorkerMenu.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        statisticsMenu.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        settingsMenu.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );
        quitMenu.setStyle( uiColorAndStyleSettings.getFontStyleToMenuItems() );

        Stage loginWindowStage = new Stage();
        quitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/loginWindow.fxml", loginWindowStage,
                        (Stage) closeButton.getScene().getWindow(), new LoginWindowController(), true);
            }
        });

        StatisticsWindowController statisticsWindowController = new StatisticsWindowController();
        statisticsWindowController.setRootUser(rootUser);
        statisticsWindowController.setMainWindowController(this);
        Stage statisticsWindowStage = new Stage();
        statisticsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneOpener.openNewScene("/fxml/statisticsWindow.fxml",
                        statisticsWindowStage, (Stage) closeButton.getScene().getWindow(), statisticsWindowController, false);
            }
        });

    }

    private List<MenuItem> createDefaultMenuItems(TextInputControl t) {
        String fontStyleToMenuItems = uiColorAndStyleSettings.getFontStyleToMenuItems();

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(e -> t.cut());
        MenuItem copy = new MenuItem("Скопировать");
        copy.setOnAction(e -> t.copy());
        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(e -> t.paste());
        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(e -> t.deleteText(t.getSelection()));
        MenuItem selectAll = new MenuItem("Выделить всё");
        selectAll.setOnAction(e -> t.selectAll());

        cut.setStyle(fontStyleToMenuItems);
        copy.setStyle(fontStyleToMenuItems);
        paste.setStyle(fontStyleToMenuItems);
        delete.setStyle(fontStyleToMenuItems);
        selectAll.setStyle(fontStyleToMenuItems);

        BooleanBinding emptySelection = Bindings.createBooleanBinding(() ->
                        t.getSelection().getLength() == 0,
                t.selectionProperty());

        cut.disableProperty().bind(emptySelection);
        copy.disableProperty().bind(emptySelection);
        delete.disableProperty().bind(emptySelection);

        return Arrays.asList(cut, copy, paste, delete, new SeparatorMenuItem(), selectAll);
    }

    @Override
    public void setStylesToButtons(){
        this.uiColorAndStyleSettings.setImageToButton(closeButton, "cross.png", 11,20);
        this.uiColorAndStyleSettings.setImageToButton(minimiseButton, "minimize.png", 13,40);
        this.uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
    }

    @Override
    public void min() {
        ((Stage)(mainAnchorPane.getScene().getWindow())).setIconified(true);
    }

    @Override
    public void close() {
        sceneOpener.openNewScene("/fxml/closeWindowConfirmation.fxml", closeWindowStage, stage,
                closeWindowConfirmationController, false);
    }

    public String getTextField() {
        if(textArea.getText().isEmpty()){
            return "";
        }else {
            return this.textArea.getText();
        }
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

    private boolean addNewTaskFromText(String text, Worker worker, String tasktype){
        if (text.isEmpty()) {
            return false;
        } else {
            Task task = new Task(text, worker, tasktype);
            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(text);
            if (list.size() >= 1) {
                Stage alertBoxStage = new Stage();
                sceneOpener.openNewScene("/fxml/alertTaskBoxWindow.fxml", alertBoxStage,
                        (Stage) closeButton.getScene().getWindow(),new AlertBoxController(), false);
                return false;
            } else {
                taskService.saveTask(task);
                return true;
            }
        }
    }

    private void createTextAreaContextMenus(){

        String fontStyleToMenuItems = uiColorAndStyleSettings.getFontStyleToMenuItems();

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(createDefaultMenuItems(textArea));
        Menu createTaskFromSelectedMenuItem = new Menu("Создать задачу из выделенного");
        createTaskFromSelectedMenuItem.setStyle(fontStyleToMenuItems);

        List<Menu> menuItemsList = new ArrayList<>();
        for (int i = 0; i < rootUser.getWorkers().size(); i++) {
            Worker worker = rootUser.getWorkers().get(i);
            Menu workerMenu = new Menu(worker.getFirstname()
                    + " " + worker.getLastname());
            menuItemsList.add(workerMenu);
            MenuItem inWorkMenuItem = new MenuItem("в работу");
            MenuItem inQueueMenuItem = new MenuItem("в очередь");

            inWorkMenuItem.setStyle(fontStyleToMenuItems);
            inQueueMenuItem.setStyle(fontStyleToMenuItems);
            workerMenu.setStyle(fontStyleToMenuItems);

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
        this.textArea.setText(this.textArea.getText() + "\n" + string);
    }

    private void removeTaskAddTaskTextToTextfield(Task task){
        textArea.setText( textArea.getText() + "\n" + "\n" + task.getText());
        TaskService taskService = new TaskService();
        taskService.deleteTask(task);
        this.initialize();
    }

}
