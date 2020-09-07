package additionalClasses;

public class LanguageSwitcherEng implements LanguageSwitcher{

    private String addTaskToWorker = "Add new task";
    private String editWorker = "Edit worker";
    private String deleteWorker = "Delete worker";
    private String editTask = "Edit task";
    private String deleteTask = "Delete task";
    private String returnToTextField = "Return task to notes";
    private String changeWorker = "Reassign worker";
    private String markByColor = "Mark with color";
    private String moveTaskToInWork = "Take task to work";
    private String cut = "Cut";
    private String copy = "Copy";
    private String paste = "Insert";
    private String delete = "Delete";
    private String selectAll = "Select all";
    private String createTaskFromSelected = "Create task from selected";
    private String inWork = "To work";
    private String inQueue = "In queue";
    private String addWorker = "Add worker";
    private String statistics = "Statistics";
    private String quit = "Quit";
    private String suchLoginAlreadyRegistered = "This login already registered,";
    private String pleasePickAnother = "please, pick another one.";
    private String suchTaskAlreadyExists = "Such task already exists.";
    private String AreYouShureThatYouWantToClose = "Are you sure that you want to close";
    private String app = "application?";
    private String yes = "Yes";
    private String no = "No";
    private String AreYouShureThatYouWantToDelete = "Are you sure that you want to delete";
    private String task = "task?";
    private String appreciateTaskLabel = "Estimate the complexity of the completed task,";
    private String higherValueHarderTaskLabel = "higher number means more difficult task";
    private String textOfTask = "Task text";
    private String inQueueTaskType = "in queue";
    private String inWorkTaskType = "in work";
    private String createNewTask = "Create new task";
    private String worker = "worker?";
    private String restTasks = "оставшиеся задачи:";

    @Override
    public String getRestTasks() {
        return restTasks;
    }

    @Override
    public String getWorker(){
        return worker;
    }

    @Override
    public String getTextOfTask() {
        return textOfTask;
    }

    @Override
    public String getInQueueTaskType() {
        return inQueueTaskType;
    }

    @Override
    public String getInWorkTaskType() {
        return inWorkTaskType;
    }

    @Override
    public String getCreateNewTask() {
        return createNewTask;
    }

    @Override
    public String getAppreciateTaskLabel() {
        return appreciateTaskLabel;
    }

    @Override
    public String getHigherValueHarderTaskLabel() {
        return higherValueHarderTaskLabel;
    }

    @Override
    public String getSuchTaskAlreadyExists(){
        return suchTaskAlreadyExists;
    }

    @Override
    public String getAreYouShureThatYouWantToClose() {
        return AreYouShureThatYouWantToClose;
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getYes() {
        return yes;
    }

    @Override
    public String getNo() {
        return no;
    }

    @Override
    public String getAreYouShureThatYouWantToDelete() {
        return AreYouShureThatYouWantToDelete;
    }

    @Override
    public String getTask() {
        return task;
    }

    @Override
    public String getSuchLoginAlreadyRegistered() {
        return suchLoginAlreadyRegistered;
    }

    @Override
    public String getPleasePickAnother() {
        return pleasePickAnother;
    }

    @Override
    public String getAddWorker() {
        return addWorker;
    }

    @Override
    public String getStatistics() {
        return statistics;
    }

    @Override
    public String getQuit() {
        return quit;
    }

    @Override
    public String getAddTaskToWorker() {
        return addTaskToWorker;
    }

    @Override
    public String getEditWorker() {
        return editWorker;
    }

    @Override
    public String getDeleteWorker() {
        return deleteWorker;
    }

    @Override
    public String getEditTask() {
        return editTask;
    }

    @Override
    public String getDeleteTask() {
        return deleteTask;
    }

    @Override
    public String getReturnToTextField() {
        return returnToTextField;
    }

    @Override
    public String getChangeWorker() {
        return changeWorker;
    }

    @Override
    public String getMarkByColor() {
        return markByColor;
    }

    @Override
    public String getMoveTaskToInWork() {
        return moveTaskToInWork;
    }

    @Override
    public String getCut() {
        return cut;
    }

    @Override
    public String getCopy() {
        return copy;
    }

    @Override
    public String getPaste() {
        return paste;
    }

    @Override
    public String getDelete() {
        return delete;
    }

    @Override
    public String getSelectAll() {
        return selectAll;
    }

    @Override
    public String getCreateTaskFromSelected() {
        return createTaskFromSelected;
    }

    @Override
    public String getInWork() {
        return inWork;
    }

    @Override
    public String getInQueue() {
        return inQueue;
    }
}
