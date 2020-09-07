package controllers;

import additionalClasses.LanguageSwitcher;
import additionalClasses.UIColorAndStyleSettings;
import additionalClasses.WindowEffects;
import database.Task;
import database.User;
import database.Worker;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    CategoryAxis lineChartXAxis = new CategoryAxis();
    NumberAxis lineChartYAxis = new NumberAxis();

    @FXML
    private LineChart<String,Number> lineChart = new LineChart<String, Number>(lineChartXAxis, lineChartYAxis);

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

    @FXML
    private Label firstDateLabel;

    @FXML
    private Label lastDateLabel;

    @FXML
    private Label numbersInGraphicsLabel;

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

    private MainWindowController mainWindowController;
    private Stage stage;
    private UIColorAndStyleSettings uiColorAndStyleSettings = new UIColorAndStyleSettings();
    private User rootUser = new User();
    private List<Worker> workersList = rootUser.getWorkers();
    private ObservableMap<CheckMenuItem,Worker>
            MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu = FXCollections.observableHashMap();
    private LanguageSwitcher languageSwitcher;

    private LocalDate firstDateValue =
            LocalDate.of(2020, LocalDate.now().getMonth().minus(1).getValue(), 01);
    private LocalDate secondDateValue = LocalDate.now().plusDays(1);
    private double finalSumForPieChart = 0;
    private ToggleGroup timeStepToggleGroup = new ToggleGroup();
    private ArrayList<String> daysList = new ArrayList<>();

    public StatisticsWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        languageSwitcher=mainWindowController.getLanguageSwitcher();
    }

    @FXML
    void initialize() {

        firstDateLabel.setText(languageSwitcher.getFirstDateTextLabel());
        lastDateLabel.setText(languageSwitcher.getLastDateTextLabel());
        numbersInGraphicsLabel.setText(languageSwitcher.getNumbersInGraphicsNotice());
        workersMenuButton.setText(languageSwitcher.getWorkersTextLabel());

        WindowEffects.setDropShadowToWindow(forDropShadowTopAnchorPane);
        WindowEffects.makePaneMoovable(movableAnchorPane);

        firstDatePicker.setValue(firstDateValue);
        secondDatePicker.setValue(secondDateValue);

        this.setStylesToButtons();

        this.setupDatePicker();

        this.createWorkersRadioMenusInWorkersMenu();

        this.setupPieChart();

        this.setupLineChart();

    }

    @Override
    public void setStylesToButtons() {
        uiColorAndStyleSettings.setCloseAndMinimizeButtonStylesAndIcons(closeButton,minimiseButton);
    }

    private void setupDatePicker(){
        firstDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            firstDateValue = firstDatePicker.getValue();
            this.updatePieChart();
            this.updateLineChart();

        });
        secondDatePicker.valueProperty().addListener((observableValue, oldValue, newValue)->{
            secondDateValue = secondDatePicker.getValue();
            this.updatePieChart();
            this.updateLineChart();
        });
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
                this.updateLineChart();
            });
        }

    }

    private void setupPieChart(){

        anchorPaneForCharts.layout();

        finalSumForPieChart = this.countFullSumForPieChart(rootUser.getWorkers());

        for (Map.Entry<CheckMenuItem, Worker> entry: MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.entrySet())
        {
            double valueForWorker = countFinalValueForWorker(entry.getValue());
            if( valueForWorker==0){
            }else {
                double persentage = 100 * (valueForWorker / finalSumForPieChart);

                ObservableList<PieChart.Data> pieChartData =
                        FXCollections.observableArrayList(new PieChart.Data(entry.getValue().getLastname()
                                + "\n" + new DecimalFormat("###.##").format(persentage) + "%",
                                valueForWorker));

                pieChart.getData().addAll(pieChartData);
            }
        }

        pieChart.setAnimated(false);
        pieChart.setLabelLineLength(15);

    }

    private void setupLineChart(){

        for (int i = 0; i < DAYS.between(firstDateValue, secondDateValue); i++) {
            daysList.add(firstDateValue.plusDays(i).getDayOfMonth() + "."
                    + String.format("%02d", firstDateValue.plusDays(i).getMonthValue()));
        }

        lineChartXAxis.getCategories().clear();

        ObservableList<String> observableList = FXCollections.observableList(daysList);

        lineChartXAxis.setCategories(observableList);


        List<Worker> workersList = rootUser.getWorkers();

        for (Map.Entry<CheckMenuItem, Worker> entry: MapOfSelectedCheckBoxesOfWorkersInStatisticsMenu.entrySet()) {
            TreeMap<LocalDate, Integer> currentWorkerMap = this.countRatingFromDays(entry.getValue());
            XYChart.Series<String, Number> currentWorkerSeries = new XYChart.Series<>();
            currentWorkerSeries.setName(entry.getValue().getLastname());

            for (Map.Entry<LocalDate,Integer> newEntry: currentWorkerMap.entrySet()) {
                currentWorkerSeries.getData().add(
                        new XYChart.Data<String, Number>(newEntry.getKey().getDayOfMonth()
                                + "." + String.format("%02d", newEntry.getKey().getMonthValue()),
                                newEntry.getValue()));
            }

            lineChart.getData().addAll(currentWorkerSeries);
        }

        lineChart.setAnimated(false);
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

    private void updatePieChart() {
        this.pieChart.getData().clear();
        this.setupPieChart();
    }

    private void updateLineChart(){
        this.lineChart.getData().clear();
        this.setupLineChart();
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

    private TreeMap<LocalDate,Integer> countRatingFromDays(Worker worker){

        TreeMap<LocalDate, Integer> ratingFromDaysMap = new TreeMap<>();

        long daysAmountBetweenTwoDates = DAYS.between(firstDateValue, secondDateValue);

        for (int i = 0; i <daysAmountBetweenTwoDates; i++) {

            int sum = 0;

            List<Task> tasksList = worker.getTasks();
            for (int k = 0; k < tasksList.size(); k++) {

                if( ("done").equals(tasksList.get(k).getTasktype()) &&
                        ( firstDateValue.plusDays(i) ).equals( tasksList.get(k).getDateOfFinishingTask())
                        && tasksList.get(k).getDateOfFinishingTask()!=null)
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

}
