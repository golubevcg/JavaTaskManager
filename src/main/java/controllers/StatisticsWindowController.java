package controllers;

import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.Task;
import database.User;
import database.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsWindowController extends ControllerParent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab statistics1;

    @FXML
    private Tab statistics2;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private DatePicker firstDatePicker;

    @FXML
    private DatePicker secondDatePicker;

    @FXML
    private MenuButton workersMenuButton;

    @FXML
    private AnchorPane anchorPainTodragWindow1;

    @FXML
    private AnchorPane movableAnchorPane;

    @FXML
    private Button closeButton;

    @FXML
    private Button minimiseButton;

    @Override
    public void min() {
        ((Stage)(movableAnchorPane.getScene().getWindow())).setIconified(true);
    }

    @Override
    public void close() {
        stage.close();
        mainWindowController.getUser().setTextfield(mainWindowController.getTextField());
        mainWindowController.initialize();
    }

    MainWindowController mainWindowController;
    Stage stage;
    UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    User rootUser = new User();
    List<Worker> workersList = rootUser.getWorkers();
    ObservableMap<CheckMenuItem,Worker> MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu = FXCollections.observableHashMap();
    XYChart.Series barChartSeries = new XYChart.Series();

    private LocalDate firstDateValue = LocalDate.of(2020,01,01);
    private LocalDate secondDateValue = LocalDate.now().plusDays(1);


    @FXML
    void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(movableAnchorPane);

        firstDatePicker.setValue(firstDateValue);
        secondDatePicker.setValue(secondDateValue);

        this.setStylesToButtons();

        this.setupDatePicker();

        this.createWorkersRadioMenusInWorkersMenu();

        this.setupBarChart();

    }

    @Override
    public void setStylesToButtons() {
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
        workersList = rootUser.getWorkers();

    }

    private int countFinalValueForWorker(Worker worker){

        List<Task> tasksList = worker.getTasks();
        int sum = 0;

        for (int i = 0; i < tasksList.size(); i++) {

            Task task = tasksList.get(i);

            if (("done").equals(task.getTasktype()) && this.checkTaskDate(task.getDateOfFinishingTask())) {
                sum += task.getRating();
            }

        }
        return sum;
    }

    private void createWorkersRadioMenusInWorkersMenu(){

        for (int i = 0; i < workersList.size(); i++) {

            Worker worker = workersList.get(i);

            CheckMenuItem checkMenuItem = new CheckMenuItem(worker.getFirstname() + worker.getLastname());
            checkMenuItem.setSelected(true);
            workersMenuButton.getItems().addAll(checkMenuItem);

            MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.put(checkMenuItem, worker);
            checkMenuItem.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
                if (selectedNow) {
                    MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.put(checkMenuItem, worker);
                } else {
                    MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.remove(checkMenuItem);
                }
                this.updateStatistics();
            });
        }

    }

    private void updateStatistics() {
        this.cleanBarChart();
        this.setupBarChart();
    }

    private int counter = 0;

    private void setupBarChart(){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Сотрудник");
        yAxis.setLabel("Показатели");
        for (Map.Entry<CheckMenuItem, Worker> entry: MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.entrySet())
        {
            Worker worker = entry.getValue();
            barChartSeries.getData().add(new XYChart.Data(worker.getFirstname().charAt(0) + "." + worker.getLastname(), this.countFinalValueForWorker(worker)));
        }
        barChart.getData().addAll(barChartSeries);


        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
    }

    private void cleanBarChart(){
        barChartSeries.getData().clear();
        barChart.getData().clear();
    }

    private void setupDatePicker(){

        firstDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            firstDateValue = firstDatePicker.getValue();
            this.updateStatistics();
        });

        secondDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            secondDateValue = secondDatePicker.getValue();
            this.updateStatistics();
        });
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private boolean checkTaskDate(LocalDate date){

        if(firstDateValue==null || secondDateValue==null || date==null){
            return false;
        }else{
            return date.isAfter(firstDateValue)&&date.isBefore(secondDateValue);
        }

    }

    private void setupLineChart(){

    }

}
