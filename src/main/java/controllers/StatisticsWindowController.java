package controllers;

import classes.UIColorAndStyleSettings;
import classes.WindowEffects;
import database.Task;
import database.User;
import database.Worker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class StatisticsWindowController extends ControllerParent{

    @FXML
    private AnchorPane forDropShadowTopAnchorPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane anchorPainTodragWindow1;

    @FXML
    private AnchorPane movableAnchorPane;

    @FXML
    private AnchorPane anchorPaneForCharts;

    @FXML
    private PieChart pieChart;

    NumberAxis areaChartXAxis = new NumberAxis();
    NumberAxis areaChartYAxis = new NumberAxis();

    @FXML
    private AreaChart<Number,Number> areaChart = new AreaChart<Number, Number>(areaChartXAxis, areaChartYAxis);

    @FXML
    private MenuButton graphicsTimeStep;

    @FXML
    private RadioMenuItem days;

    @FXML
    private RadioMenuItem weeks;

    @FXML
    private RadioMenuItem months;

    @FXML
    private DatePicker firstDatePicker;

    @FXML
    private DatePicker secondDatePicker;

    @FXML
    private MenuButton workersMenuButton;

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

    private LocalDate firstDateValue = LocalDate.of(2020,01,01);
    private LocalDate secondDateValue = LocalDate.now().plusDays(1);
    private double finalSumForPieChart = 0;
    private ToggleGroup timeStepToggleGroup = new ToggleGroup();

    @FXML
    void initialize() {

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(movableAnchorPane);

        firstDatePicker.setValue(firstDateValue);
        secondDatePicker.setValue(secondDateValue);


        this.setStylesToButtons();

        this.setupTimeStepToggleGroup();

        this.setupDatePicker();

        this.createWorkersRadioMenusInWorkersMenu();

        this.setupPieChart();

        this.setupListenerToTimeStepForAreaChart();

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

    private double countFullSumForPieChart(List<Worker> workerList){
        int sumValueForWorker = 0;
        for (int i = 0; i < workerList.size(); i++) {
            sumValueForWorker += countFinalValueForWorker(workerList.get(i));
        }
        return sumValueForWorker;
    }

    private void createWorkersRadioMenusInWorkersMenu(){

        for (int i = 0; i < workersList.size(); i++) {

            Worker worker = workersList.get(i);

            CheckMenuItem checkMenuItem = new CheckMenuItem(worker.getFirstname() + worker.getLastname());

            if(countFinalValueForWorker(workersList.get(i))==0){
            }else
            {
                checkMenuItem.setSelected(true);
            }

            workersMenuButton.getItems().addAll(checkMenuItem);

            MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.put(checkMenuItem, worker);
            checkMenuItem.selectedProperty().addListener((observable, oldValue, selectedNow) -> {
                if (selectedNow) {
                    MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.put(checkMenuItem, worker);
                } else {
                    MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.remove(checkMenuItem);
                }
                this.updatePieChart();
            });
        }

    }

    private void updatePieChart() {
        pieChart.getData().clear();
        this.setupPieChart();
    }

    private void updateAreaChart(){
        this.setupAreaChart();
    }

    private void setupPieChart(){

        finalSumForPieChart = this.countFullSumForPieChart(rootUser.getWorkers());

        for (Map.Entry<CheckMenuItem, Worker> entry: MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.entrySet())
        {
            if( countFinalValueForWorker(entry.getValue())==0){
            }else {
                double valueForWorker = countFinalValueForWorker(entry.getValue());

                double persentage = 100 * (valueForWorker / finalSumForPieChart);

                ObservableList<PieChart.Data> pieChartData =
                        FXCollections.observableArrayList(new PieChart.Data(entry.getValue().getLastname()
                                + "\n" + new DecimalFormat("###.##").format(persentage) + "%",
                                countFinalValueForWorker(entry.getValue())));
                pieChart.getData().addAll(pieChartData);
            }
        }

        pieChart.setAnimated(false);
        pieChart.setLegendVisible(false);
        pieChart.setLabelLineLength(25);
    }

    private void setupDatePicker(){

        firstDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            firstDateValue = firstDatePicker.getValue();
            this.updatePieChart();
        });

        secondDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            secondDateValue = secondDatePicker.getValue();
            this.updatePieChart();
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

    private void setupAreaChart(){

        areaChartXAxis.setLowerBound(0);
        areaChartXAxis.setUpperBound(DAYS.between(firstDateValue, secondDateValue));
        areaChartXAxis.setTickUnit(1);

        areaChartYAxis.setLowerBound(0);
        areaChartYAxis.setUpperBound(5);
        areaChartYAxis.setTickUnit(1);


        List<Worker> workersList = rootUser.getWorkers();
        for (int i = 0; i < workersList.size(); i++) {
            Map<LocalDate, Integer> currentWorkerMap = this.countRatingFromDays(workersList.get(i));
            XYChart.Series<Number, Number> currentWorkerSeries = new XYChart.Series<>();
            currentWorkerSeries.setName(workersList.get(i).getLastname());
            int k = 0;
                    for (Map.Entry<LocalDate,Integer> entry: currentWorkerMap.entrySet()) {
                        currentWorkerSeries.getData().add(new XYChart.Data<Number, Number>(k, entry.getValue()));
                        k++;
                    }
            areaChart.getData().addAll(currentWorkerSeries);
        }

    }

    private void setupTimeStepToggleGroup(){
        days.setToggleGroup(timeStepToggleGroup);
        weeks.setToggleGroup(timeStepToggleGroup);
        months.setToggleGroup(timeStepToggleGroup);
    }

    private Map<LocalDate,Integer> countRatingFromDays(Worker worker){

        Map<LocalDate, Integer> ratingFromDaysMap = new TreeMap<>();

        long daysAmountBetweenTwoDates = DAYS.between(firstDateValue, secondDateValue);

        for (int i = 0; i <daysAmountBetweenTwoDates; i++) {

            int sum = 0;

            List<Task> tasksList = worker.getTasks();
            for (int k = 0; k < tasksList.size(); k++) {

                if( ("done").equals(tasksList.get(k).getTasktype()) &&
                        ( firstDateValue.plusDays(i) ).equals( tasksList.get(k).getDateOfFinishingTask() ))
                {
                        sum+=tasksList.get(k).getRating();
                }

            }

            if(sum!=0) {
                ratingFromDaysMap.put(firstDateValue.plusDays(i), sum);
            }

        }

        return ratingFromDaysMap;

    }

    private void setupListenerToTimeStepForAreaChart(){

        timeStepToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if(timeStepToggleGroup.getSelectedToggle()!=null){
                    updateAreaChart();
                }
            }
        });

    }

}
