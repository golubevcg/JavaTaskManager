package database.services;

import database.Task;
import database.User;
import database.dao.TaskDao;
import database.dao.UserDao;

import java.util.List;

public class TaskService {


    private TaskDao taskDao = new TaskDao();

    public TaskService() {
    }

    public void saveTask(Task task){
        taskDao.save(task);
    }

    public void updateTask(Task task){
        taskDao.update(task);
    }

    public void deleteTask(Task task){
        taskDao.delete(task);
    }

    public List<String> checkTask(String text){return taskDao.checkTask(text);}
}
