package controllers;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.io.IOException;
import java.util.*;

public class OldMainWindowController {

    public static User rootUser;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private MenuButton settiingsMenuButton;

    @FXML
    private MenuItem logOut;


    @FXML
    private Button newWorkerButton;

    @FXML
    private Separator sep1;

    @FXML
    private Separator sep2;

    @FXML
    private Separator sep3;

//    @FXML
    private TextArea inputTextArea;

    private HashMap<Button, Worker> newTaskButtonsMap = new HashMap<>();
    private HashMap<Button, Task> editTuskButtonsMap = new HashMap<>();

    List<Button> deleteWorkerButtonList = new ArrayList<>();
    Map<Button, Task> switchToInWorkButtonMap = new HashMap<>();
    Map<Button, Task> delTaskButtonMap = new HashMap<>();

    List<Button> completeTaskButtonList = new ArrayList<>();

    List<Label> allLabelsList = new ArrayList<Label>();
    List<Button> allButtonsList = new ArrayList<Button>();
    Map<Button, Label> uiMap = new HashMap<Button, Label>();
    List<Rectangle> rectanglesList = new ArrayList<>();
    List<Button> rectangledList = new ArrayList<>();



    @FXML
    public void initialize() {

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> list = session.createQuery("FROM User WHERE login = '" + rootUser.getLogin() + "'").list();
        session.getTransaction();
        List<Worker> workersList1 = list.get(0).getWorkers();

        rootUser = list.get(0);
        this.mainGridPane.getChildren().remove(inputTextArea);
        inputTextArea = new TextArea();
        inputTextArea.setPrefWidth(282);
        inputTextArea.setPrefHeight(400);
        inputTextArea.setScaleY(0.975);
        inputTextArea.setTranslateX(-2);
        inputTextArea.setTranslateY(20);
        inputTextArea.setStyle("-fx-background-color:#ffffff");
        inputTextArea.setFont(new Font("System", 15));
        inputTextArea.setMaxWidth(282);
        inputTextArea.setText(list.get(0).getTextfield());
        mainGridPane.add(inputTextArea,1,0);
        inputTextArea.textProperty().addListener(((observableValue, oldvalue, newvalue) ->
                updateTextfield(this, newvalue)));

        mainGridPane.setValignment(inputTextArea,VPos.TOP);

        ContextMenu contextMenu = new ContextMenu();
        Menu menuItemNewTask = new Menu("Новая задача");
        Menu menuItemQuene = new Menu("в очередь");
        Menu menuItemWork = new Menu("в работе");
        menuItemNewTask.getItems().addAll(menuItemQuene, menuItemWork);
        contextMenu.getItems().addAll(menuItemNewTask);

        //менюшки в очередь
        for (int i = 0; i < workersList1.size(); i++) {
        MenuItem menuItem = new MenuItem(((Worker)workersList1.get(i)).getFirstname()
                + ((Worker)workersList1.get(i)).getLastname());
            Worker worker = workersList1.get(i);
            menuItem.setOnAction(e-> {
                String selectedText = inputTextArea.getSelectedText();
                if(newTaskFromSelected(selectedText, worker, "quene")){
                            inputTextArea.setText(inputTextArea.getText()
                                    .replace(selectedText, ""));
                            this.updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                            this.initialize();
                        }
            });
            menuItemQuene.getItems().addAll(menuItem);
        }

        //менюшки в работу
        for (int i = 0; i < workersList1.size(); i++) {
            MenuItem menuItem = new MenuItem(((Worker)workersList1.get(i)).getFirstname()
                    + ((Worker)workersList1.get(i)).getLastname());
            Worker worker = workersList1.get(i);
            menuItem.setOnAction(e->{
                String selectedText = inputTextArea.getSelectedText();
                if(newTaskFromSelected(selectedText, worker, "inwork")){
                    inputTextArea.setText(inputTextArea.getText()
                            .replace(selectedText, ""));
                    this.updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                    this.initialize();

                }
            });
            menuItemWork.getItems().addAll(menuItem);
        }

        inputTextArea.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                contextMenu.show(inputTextArea, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
        });
        session.close();



        updateSceneWorkers(allLabelsList, allButtonsList, uiMap);

        for (int i = 0; i < allLabelsList.size() ; i++) {
            allLabelsList.get(i).toFront();
        }

        for (int j = 0; j < rectanglesList.size() ; j++) {
            rectanglesList.get(j).toBack();
        }

        for (int j = 0; j < allButtonsList.size() ; j++) {
            allButtonsList.get(j).toFront();
        }

        sep1.toFront();
        sep2.toFront();
        sep3.toFront();



        for (Map.Entry<Button, Worker> entry: newTaskButtonsMap.entrySet()) {
            entry.getKey().setOnAction(d->{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/newTaskWindow.fxml"));
                NewTaskWindowController newTaskWindowController =
                        new NewTaskWindowController(this, entry.getValue());
                loader.setController(newTaskWindowController);
                try {loader.load();
                } catch (IOException a) {
                    a.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            });
        }

        for (Map.Entry<Button, Task> entry: editTuskButtonsMap.entrySet()) {
            entry.getKey().setOnAction(d->{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/newTaskWindow.fxml"));
                EditTaskWindowController editTaskWindowController =
                        new EditTaskWindowController(this, entry.getValue());
                loader.setController(editTaskWindowController);
                try {loader.load();
                } catch (IOException a) {
                    a.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            });
        }


        logOut.setOnAction(e-> {
            settiingsMenuButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/loginWindow.fxml"));
            try {
                loader.load();
            } catch (IOException v) {
                v.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });


        newWorkerButton.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/newWorkerWindow.fxml"));
            try {
                NewWorkerWindowController newWorkerWindowController = new NewWorkerWindowController(this);
                loader.setController(newWorkerWindowController);
                loader.load();
            } catch (IOException d) {
                d.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            initialize();
            stage.show();
        });

        setButtonOnCoursorAction(newWorkerButton);

        if(rootUser.getWorkers().size()>3) {
            double maxYLabel = 0;
            for (int j = 0; j < allLabelsList.size(); j++) {
                if (allLabelsList.get(j).getTranslateY() > maxYLabel) {
                    maxYLabel = allLabelsList.get(j).getTranslateY();
                }
            }
            mainGridPane.setPrefHeight(maxYLabel + 40);
        }
    }

    public void updateSceneWorkers(List<Label> allLabelsList, List<Button> allButtonsList, Map<Button, Label> uiMap) {

        for (int i = 0; i < allLabelsList.size() ; i++) {
            this.mainGridPane.getChildren().remove(allLabelsList.get(i));
        }

        for (int j = 0; j < allButtonsList.size() ; j++) {
            this.mainGridPane.getChildren().remove(allButtonsList.get(j));
        }

        for (int j = 0; j < rectanglesList.size() ; j++) {
            this.mainGridPane.getChildren().remove(rectanglesList.get(j));
        }


        //запросом в бд возвращаем текущего юзера, из него получаем список работников
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> list = session.createQuery("FROM User WHERE login = '" + rootUser.getLogin() + "'").list();
        session.getTransaction();
        List<Worker> workersList = new ArrayList<>();

        workersList = list.get(0).getWorkers();
        Comparator<Worker> comparator = (o1, o2) -> Math.max(o1.getId(), o2.getId());

        List<Worker> independetCopyOfWorkersList = new ArrayList<>();
       TreeSet<Worker> workersTreeSet = new TreeSet<>();
        workersTreeSet.addAll(independetCopyOfWorkersList);

        independetCopyOfWorkersList.addAll(workersList);

        int insideInQueneTaskCounter = 0;
        int insideInWorkTaskCounter = 0;
        int workerLabelsCounter = 0;
        int additTranslateY = 0;

        for (int i = 0; i < independetCopyOfWorkersList.size(); i++) {
            workersTreeSet.add(independetCopyOfWorkersList.get(i));
        }

        for (Worker workerFromSet: workersTreeSet) {
            System.out.println(workerFromSet.getLogin()+workerFromSet.getId());
        }

        int i = 0;
        for (Worker workerFromSet: workersTreeSet) {
            //Лейблы с именем сотрудника
            //создаём задник цветной
            workerLabelsCounter++;



            //выставляю label сотрудника в колонке InQuen
            Label label = new Label();
            label.setTranslateX(300);
            label.setTranslateY(30 + (48 * i) + (37 * insideInQueneTaskCounter));
            label.setFont(new Font("System", 16));
            label.setText(workerFromSet.getFirstname() + " "
                                                + workerFromSet.getLastname());
            label.alignmentProperty();
            allLabelsList.add(label);
            mainGridPane.add(label,1,0);
            mainGridPane.setValignment(label, VPos.TOP);


            //выставляю label сотрудника в колонке inWork
            Label label2 = new Label();
            label2.setTranslateX(600);
            label2.setTranslateY(30 + (48 * i) + (37 * insideInWorkTaskCounter));
            label2.setFont(new Font("System", 16));
            label2.setText(workerFromSet.getFirstname()
                                     + " " + workerFromSet.getLastname());
            allLabelsList.add(label2);
            mainGridPane.add(label2,1,0);
            mainGridPane.setValignment(label2, VPos.TOP);

            if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                Rectangle rectangle1 = new Rectangle();
                rectangle1.setFill(Color.web("#eef6ff"));
                rectangle1.setStrokeWidth(0);
                rectangle1.setTranslateX(274);
                rectangle1.setTranslateY(label.getTranslateY());
                rectangle1.setWidth(288);
                rectangle1.setHeight(25);
                rectangle1.setOpacity(0.7);
                rectangle1.toBack();
                rectanglesList.add(rectangle1);
                mainGridPane.add(rectangle1, 1, 0);
                mainGridPane.setValignment(rectangle1, VPos.TOP);

                Rectangle rectangle2 = new Rectangle();
                rectangle2.setFill(Color.web("#eef6ff"));
                rectangle2.setStrokeWidth(0);
                rectangle2.setTranslateX(561);
                rectangle2.setTranslateY(label2.getTranslateY());
                rectangle2.setWidth(288);
                rectangle2.setHeight(25);
                rectangle2.setOpacity(0.7);
                rectangle2.toBack();
                rectanglesList.add(rectangle2);
                mainGridPane.add(rectangle2, 1, 0);
                mainGridPane.setValignment(rectangle2, VPos.TOP);

            }



            //кнопка для удаления сотрудника
            Button deleteWorkerButton = new Button();
            deleteWorkerButton.setTranslateX(274);
            deleteWorkerButton.setTranslateY(label.getTranslateY());
            deleteWorkerButton.setText("X");
            deleteWorkerButton.setFont(new Font("Liberation Sans", 14));
            if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                deleteWorkerButton.setStyle("-fx-background-color:#eef6ff");}
            else{deleteWorkerButton.setStyle("-fx-background-color:#ffffff");}
            deleteWorkerButton.setOpacity(0.5);
            double scale = 0.7;
            deleteWorkerButton.setScaleX(scale);
            deleteWorkerButton.setScaleY(scale);
            deleteWorkerButton.setScaleZ(scale);
            uiMap.put(deleteWorkerButton, label);
            mainGridPane.setValignment(deleteWorkerButton, VPos.TOP);

            Worker newworker2 = workerFromSet;
            setButtonOnCoursorAction(deleteWorkerButton);
            deleteWorkerButton.setOnAction(x -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/deleteWorkerConfirmationWindow.fxml"));
                loader.setController(new DeleteWorkerWindowController(this, newworker2, inputTextArea));
                try {loader.load();
                } catch (IOException d) {
                    d.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            });
            mainGridPane.add(deleteWorkerButton,1,0);
            allButtonsList.add(deleteWorkerButton);

            //кнопка для добавления новых задач
            Button addNewTask = new Button();
            addNewTask.setTranslateX(530);
            addNewTask.setTranslateY(label.getTranslateY());
            addNewTask.setText("+");
            addNewTask.setFont(new Font("Liberation Sans", 16));
            if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                addNewTask.setStyle("-fx-background-color:#eef6ff");}
            else{addNewTask.setStyle("-fx-background-color:#ffffff");}
            addNewTask.setOpacity(0.5);
            addNewTask.setScaleX(scale);
            addNewTask.setScaleY(scale);
            addNewTask.setScaleZ(scale);
            setButtonOnCoursorAction(addNewTask);
            uiMap.put(addNewTask, label);
            newTaskButtonsMap.put(addNewTask, newworker2);
            mainGridPane.add(addNewTask,1,0);
            mainGridPane.setValignment(addNewTask, VPos.TOP);
            allButtonsList.add(addNewTask);

            //запросом в бд получаем список задач для этого работника и создаём лейблы на каждую задачу
            List<Worker> list1 = session.createQuery("FROM Worker WHERE login = '"
                    + workerFromSet.getLogin() + "'").list();
            session.getTransaction();
            List<Task> tasksList = list1.get(0).getTasks();

            List<Task> inQueneTasksList = new ArrayList<>();
            List<Task> inWorkTasksList = new ArrayList<>();

            if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                rectangledList.add(addNewTask);
                rectangledList.add(deleteWorkerButton);
            }

            for (int k = 0; k < tasksList.size(); k++) {
                if("quene".equals(tasksList.get(k).getTasktype())) {
                    inQueneTasksList.add(tasksList.get(k));
                }else{
                    if("inwork".equals(tasksList.get(k).getTasktype()))
                        inWorkTasksList.add(tasksList.get(k));
                }
            }

            //создаём task лейблы в очереди
            if (inQueneTasksList.size() > 0) {
                for (int j = 0; j < inQueneTasksList.size(); j++) {

                    int shift;

                    if(j==0){
                        shift = 40 * (j + 1);
                    }else {
                        shift = 35 * (j + 1);
                    }

                    Label label1 = new Label();
                    label1.setTranslateX(310);

                    if(inQueneTasksList.get(j).getText().length()>15){
                        label1.setTranslateY(label.getTranslateY() + shift - 20 );
                    }else {
                        label1.setTranslateY(label.getTranslateY() + shift - 10 );
                    }

                    label1.setFont(new Font("System", 14));
                    label1.setOpacity(0.7);
                    if(inQueneTasksList.get(j).getText().length()>15) {
                        additTranslateY +=20;
                        StringBuilder task = new StringBuilder(inQueneTasksList.get(j).getText());
                        String[] strArray = inQueneTasksList.get(j).getText().split(" ");
                        int charcounter = 0;
                        int iternum = 0;
                        StringBuilder pt1 = new StringBuilder();
                        StringBuilder pt2 = new StringBuilder();
                        for (int k = 0; k < strArray.length; k++) {
                            if (charcounter < 15) {
                                charcounter = charcounter + strArray[k].length();
                                pt1.append(" " + strArray[k]);
                            } else {
                                pt2.append(" " + strArray[k]);
                            }
                        }
                        label1.setText(pt1.toString() + "\n" + pt2.toString());

                    }else{
                        label1.setText(inQueneTasksList.get(j).getText());
                    }


                    mainGridPane.add(label1,1,0);
                    mainGridPane.setValignment(label1, VPos.TOP);
                    allLabelsList.add(label1);

                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        Rectangle rectangle2 = new Rectangle();
                        rectangle2.setFill(Color.web("#eef6ff"));
                        rectangle2.setStrokeWidth(0);
                        rectangle2.setTranslateX(274);
                        rectangle2.setTranslateY(label1.getTranslateY()-20+10);
                        rectangle2.setWidth(288);
                        rectangle2.setHeight(55);
                        rectangle2.setOpacity(0.7);
                        mainGridPane.add(rectangle2, 1, 0);
                        mainGridPane.setValignment(rectangle2, VPos.TOP);
                        rectanglesList.add(rectangle2);
                    }


                    //кнопка для переключения задачи в раздел в работе
                    Button buttonMoveTaskToInWork = new Button();
                    buttonMoveTaskToInWork.setTranslateX(525);
                    buttonMoveTaskToInWork.setTranslateY(label1.getTranslateY()-4);
                    buttonMoveTaskToInWork.setText("->");
                    buttonMoveTaskToInWork.setFont(new Font("System", 15));
                    buttonMoveTaskToInWork.setOpacity(0.5);
                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        buttonMoveTaskToInWork.setStyle("-fx-background-color:#eef6ff");}
                    else{buttonMoveTaskToInWork.setStyle("-fx-background-color:#ffffff");}
                    double scale1 = 1.1;
                    buttonMoveTaskToInWork.setScaleX(scale1);
                    buttonMoveTaskToInWork.setScaleY(scale1);
                    Task newtask = inQueneTasksList.get(j);
                    allButtonsList.add(buttonMoveTaskToInWork);
                    setButtonOnCoursorAction(buttonMoveTaskToInWork);
                    buttonMoveTaskToInWork.setOnAction(e -> {
                        Session session1 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                        Transaction transaction = session1.beginTransaction();
                        Query newQuery = session1.createQuery("UPDATE Task SET " +
                                "tasktype = 'inwork' WHERE id = " + newtask.getId());
                        newQuery.executeUpdate();
                        transaction.commit();
                        updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                        initialize();
                        session1.close();
                    });
                    this.mainGridPane.add(buttonMoveTaskToInWork,1,0);
                    this.switchToInWorkButtonMap.put(buttonMoveTaskToInWork, inQueneTasksList.get(j));
                    mainGridPane.setValignment(buttonMoveTaskToInWork, VPos.TOP);


                    //кнопка редактирования задачи
                    Button editTask = new Button();
                    editTask.setTranslateX(508);
                    editTask.setTranslateY(label1.getTranslateY()-4);
                    editTask.setText("/");
                    editTask.setFont(new Font("System", 15));
                    editTask.setStyle("Bold");
                    editTask.setOpacity(0.5);
                    setButtonOnCoursorAction(editTask);
                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        editTask.setStyle("-fx-background-color:#eef6ff");}
                    else{editTask.setStyle("-fx-background-color:#ffffff");}
                    double scale3 = 0.8;
                    editTask.setScaleX(scale3);
                    editTask.setScaleY(scale3);
                    allButtonsList.add(editTask);
                    editTuskButtonsMap.put(editTask, newtask);
                    this.mainGridPane.add(editTask,1,0);
                    mainGridPane.setValignment(editTask, VPos.TOP);


                    Button buttonDeleteTask = new Button();
                    buttonDeleteTask.setTranslateX(489);
                    buttonDeleteTask.setTranslateY(label1.getTranslateY()-1);
                    buttonDeleteTask.setText("X");
                    setButtonOnCoursorAction(buttonDeleteTask);
                    buttonDeleteTask.setFont(new Font("Liberation Sans", 14));
                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        buttonDeleteTask.setStyle("-fx-background-color:#eef6ff");}
                    else{buttonDeleteTask.setStyle("-fx-background-color:#ffffff");}
                    buttonDeleteTask.setOpacity(0.5);
                    double scale2 = 0.7;
                    buttonDeleteTask.setScaleX(scale2);
                    buttonDeleteTask.setScaleY(scale2);
                    allButtonsList.add(buttonDeleteTask);
                    mainGridPane.setValignment(buttonDeleteTask, VPos.TOP);


                    buttonDeleteTask.setOnAction(e->{
                        Session session1 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                        Transaction transaction1 = session1.beginTransaction();
                        Query newQuery = session1.createQuery("DELETE Task WHERE id = " +  + newtask.getId());
                        newQuery.executeUpdate();
                        transaction1.commit();
                        updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                        initialize();
                        session.close();
                    });
                    delTaskButtonMap.put(buttonDeleteTask, newtask);

                    this.mainGridPane.add(buttonDeleteTask,1,0);

                    insideInQueneTaskCounter++;

                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        rectangledList.add(buttonMoveTaskToInWork);
                        rectangledList.add(editTask);
                        rectangledList.add(buttonDeleteTask);
                    }
                }
            }

            //создаём task label'ы в работе
            if (inWorkTasksList.size() > 0) {
                for (int f = 0; f < inWorkTasksList.size(); f++) {

                    int shift;
                    if(f==0){
                        shift = 40 * (f + 1);
                    }else {
                        shift = 35 * (f + 1);
                    }
                    Label label1 = new Label();
                    label1.setTranslateX(625);

                    if(inWorkTasksList.get(f).getText().length()>25){
                        label1.setTranslateY(label2.getTranslateY() + shift - 20);
                    }else {
                        label1.setTranslateY(label2.getTranslateY() + shift - 10);
                    }
                    label1.setFont(new Font("System", 14));
                    label1.setOpacity(0.7);

                    label1.setFont(new Font("System", 14));
                    label1.setOpacity(0.7);
                    if(inWorkTasksList.get(f).getText().length()>15) {
                        StringBuilder task = new StringBuilder(inWorkTasksList.get(f).getText());
                        String[] strArray = inWorkTasksList.get(f).getText().split(" ");
                        int charcounter = 0;
                        int iternum = 0;
                        for (int k = 0; k < strArray.length; k++) {
                            if (charcounter < 15) {
                                charcounter = charcounter + strArray[k].length();
                            } else {
                                iternum = k;
                            }
                        }
                        StringBuilder pt1 = new StringBuilder();
                        StringBuilder pt2 = new StringBuilder();
                        for (int g = 0; g < strArray.length; g++) {
                            if (g < iternum) {
                                pt1.append(" " + strArray[g]);
                            } else {
                                pt2.append(" " + strArray[g]);
                            }
                        }
                        label1.setText(pt1.toString() + "\n" + pt2.toString());
                    }else{
                        label1.setText(inWorkTasksList.get(f).getText());
                    }
                    mainGridPane.add(label1,1,0);
                    mainGridPane.setValignment(label1, VPos.TOP);

                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        Rectangle rectangle2 = new Rectangle();
                        rectangle2.setFill(Color.web("#eef6ff"));
                        rectangle2.setStrokeWidth(0);
                        rectangle2.setTranslateX(562);
                        rectangle2.setTranslateY(label1.getTranslateY()-20+15);
                        rectangle2.setWidth(288);
                        rectangle2.setHeight(50);
                        rectangle2.setOpacity(0.7);

                        mainGridPane.add(rectangle2, 1, 0);
                        rectanglesList.add(rectangle2);
                        mainGridPane.setValignment(rectangle2, VPos.TOP);

                    }

                    allLabelsList.add(label1);
                    Button buttonTaskDone = new Button();
                    buttonTaskDone.setTranslateX(823);
                    buttonTaskDone.setTranslateY(label1.getTranslateY()-4);
                    buttonTaskDone.setText("v");
                    buttonTaskDone.setFont(new Font("System", 16));
                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                    buttonTaskDone.setStyle("-fx-background-color:#eef6ff");}
                    else{buttonTaskDone.setStyle("-fx-background-color:#ffffff");}
                    buttonTaskDone.setOpacity(0.5);
                    double scale1 = 1;
                    buttonTaskDone.setScaleX(scale1);
                    buttonTaskDone.setScaleY(scale1);
                    buttonTaskDone.setScaleZ(scale1);
                    mainGridPane.add(buttonTaskDone,1,0);
                    mainGridPane.setValignment(buttonTaskDone, VPos.TOP);

                    Task newtask = inWorkTasksList.get(f);
                    setButtonOnCoursorAction(buttonTaskDone);
                    buttonTaskDone.setOnAction(d->{
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/fxml/finTaskRatingWindow.fxml"));
                        loader.setController(new FinTaskRatingWindowController(newtask, this, inputTextArea));
                        try {loader.load();
                        } catch (IOException h) {
                            h.printStackTrace();
                        }
                        Parent root = loader.getRoot();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    });
                    allButtonsList.add(buttonTaskDone);

                    Button buttonTaskBackToQuene = new Button();
                    buttonTaskBackToQuene.setTranslateX(563);
                    buttonTaskBackToQuene.setTranslateY(label1.getTranslateY()-4);
                    buttonTaskBackToQuene.setText("<-");
                    buttonTaskBackToQuene.setFont(new Font("System", 15));
                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        buttonTaskBackToQuene.setStyle("-fx-background-color:#eef6ff");}
                    else{buttonTaskBackToQuene.setStyle("-fx-background-color:#ffffff");}
                    double scale2 = 1.1;
                    buttonTaskBackToQuene.setScaleX(scale2);
                    buttonTaskBackToQuene.setScaleY(scale2);
                    buttonTaskBackToQuene.setScaleZ(scale2);
                    buttonTaskBackToQuene.setOpacity(0.5);
                    setButtonOnCoursorAction(buttonTaskBackToQuene);
                    mainGridPane.add(buttonTaskBackToQuene,1,0);
                    mainGridPane.setValignment(buttonTaskBackToQuene, VPos.TOP);

                    allButtonsList.add(buttonTaskBackToQuene);

                    Button editTask2 = new Button();
                    editTask2.setTranslateX(585);
                    editTask2.setTranslateY(label1.getTranslateY()-4);
                    editTask2.setText("/");
                    editTask2.setFont(new Font("System", 15));
                        if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        editTask2.setStyle("-fx-background-color:#eef6ff");}
                        else{editTask2.setStyle("-fx-background-color:#ffffff");}
                    double scale3 = 0.8;
                    editTask2.setOpacity(0.5);
                    editTask2.setScaleX(scale3);
                    editTask2.setScaleY(scale3);
                    editTask2.setScaleZ(scale3);
                    allButtonsList.add(editTask2);
                    setButtonOnCoursorAction(editTask2);
                    editTuskButtonsMap.put(editTask2, newtask);
                    this.mainGridPane.add(editTask2,1,0);
                    mainGridPane.setValignment(editTask2, VPos.TOP);


                    buttonTaskBackToQuene.setOnAction(e -> {
                        Session session1 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                        Transaction transaction = session1.beginTransaction();
                        Query newQuery = session1.createQuery("UPDATE Task " +
                                "SET tasktype = 'quene' WHERE id = " + newtask.getId());
                        newQuery.executeUpdate();
                        transaction.commit();
                        updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                        initialize();
                        session1.close();
                    });
                    insideInWorkTaskCounter++;


                    Button buttonDeleteTask1 = new Button();
                    buttonDeleteTask1.setTranslateX(598);
                    buttonDeleteTask1.setTranslateY(label1.getTranslateY()-1);
                    buttonDeleteTask1.setText("X");
                    buttonDeleteTask1.setFont(new Font("Liberation Sans", 14));
                            if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                                buttonDeleteTask1.setStyle("-fx-background-color:#eef6ff");
                            }else{buttonDeleteTask1.setStyle("-fx-background-color:#ffffff");
                            }
                    buttonDeleteTask1.setOpacity(0.5);
                    double scale4 = 0.7;
                    buttonDeleteTask1.setScaleX(scale4);
                    buttonDeleteTask1.setScaleY(scale4);
                    buttonDeleteTask1.setScaleZ(scale4);
                    allButtonsList.add(buttonDeleteTask1);
                    setButtonOnCoursorAction(buttonDeleteTask1);
                    mainGridPane.add(buttonDeleteTask1,1,0);
                    mainGridPane.setValignment(buttonDeleteTask1, VPos.TOP);

                    buttonDeleteTask1.setOnAction(e->{
                        Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                        Transaction transaction1 = session2.beginTransaction();
                        Query newQuery = session2.createQuery("DELETE Task " +
                                "WHERE id = " +  + newtask.getId());
                        newQuery.executeUpdate();
                        transaction1.commit();
                        updateSceneWorkers(allLabelsList, allButtonsList, uiMap);
                        initialize();
                        session2.clear();
                        session2.close();
                    });

                    if(workerLabelsCounter==1 || workerLabelsCounter%2!=0) {
                        rectangledList.add(buttonTaskBackToQuene);
                        rectangledList.add(editTask2);
                        rectangledList.add(buttonDeleteTask1);
                        rectangledList.add(buttonTaskDone);
                    }
                }


            }
            i++;
        }
        session.getTransaction();
        session.close();
    }


    public static void updateTextfield(OldMainWindowController mainWindowController, String string){
        Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction2 = session2.beginTransaction();
        Query newQuery1 = session2.createQuery("UPDATE User SET textfield = "
                + "'" + string + "'"
                + " WHERE id = " + mainWindowController.rootUser.getId());
        newQuery1.executeUpdate();
        transaction2.commit();
        session2.close();
        rootUser.setTextfield(string);
    }

    public static void addNewTask(Task task){
        Session session3 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction2 = session3.beginTransaction();
        session3.save(task);
        transaction2.commit();
        session3.close();
    }
    
    public void setButtonOnCoursorAction(Button button){
//        String style = button.getStyle();
        button.getBackground();
        button.setOnMouseEntered(e->{
            if(rectangledList.contains(button)) {
                button.setStyle("-fx-background-color: #e8f1fa;" + "-fx-text-fill: #0032bd;" + "-fx-text-weight: bold;");
            }else{
                button.setStyle("-fx-background-color: #f5f9ff;" + "-fx-text-fill: #0032bd;" + "-fx-text-weight: bold;");
            }
            button.setOpacity(1);
            button.setFont(new Font("Liberation Sans", 15));
            }
            );

        button.setOnMouseExited(e->{
            if(rectangledList.contains(button)) {
                button.setStyle("-fx-background-color: #eef6ff;");
            }else{
                button.setStyle("-fx-background-color: #ffffff;");
            }
            button.setOpacity(0.5);
            button.setFont(new Font("Liberation Sans", 14));
        });
    }


    public boolean newTaskFromSelected(String text, Worker worker, String tasktype){
        if (text.isEmpty()) {
            return false;
        } else {
            //формируем запрос, чтобы проверить, есть ли такая задача в базе данных
            Task task = new Task(text, worker, tasktype);
            TaskService taskService = new TaskService();
            List<String> list = taskService.checkTask(text);
            if (list.size() >= 1) {
                //popup window - такая задача уже существует
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
                //если всё хорошо - то добавляем новую задачу в базу
                Session session2 = HibernateSessionFactoryUtil.getSessionFactory().openSession();
                Transaction transaction1 = session2.beginTransaction();
                session2.save(task);
                transaction1.commit();
                session2.close();
                return true;
            }
        }
    }
}