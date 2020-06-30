package database.services;


import database.User;
import database.Worker;
import database.dao.WorkerDao;

import java.util.List;

public class WorkerService {

    private WorkerDao workerDao = new WorkerDao();

    public WorkerService() {}

    public void saveWorker(Worker worker){
        workerDao.save(worker);
    }

    public void updateWorker(Worker worker){
        workerDao.update(worker);
    }

    public void deleteWorker(Worker worker){
        workerDao.delete(worker);
    }

    public List<String> returnWorkersByUser(User user){ return workerDao.returnWorkersByUser(user); }

    public List<String> checkWorkerLogin(String login) {return workerDao.checkLogin(login); }


}
