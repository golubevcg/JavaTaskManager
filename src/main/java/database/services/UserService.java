package database.services;

import database.User;
import database.Worker;
import database.dao.UserDao;

import java.util.List;

public class UserService {

    private UserDao userDao = new UserDao();

    public UserService() {
    }

    public void saveUser(User user){
        userDao.save(user);
    }

    public void updateUser(User user){
        userDao.update(user);
    }

    public void deleteUser(User user){ userDao.delete(user); }

    public List<String> checkUserLogin(String login) {return userDao.checkLogin(login); }

    public List<String> returnWorkersFromUser(User user){ return userDao.returnWorkersFromUser(user); }

    public List<User> returnUsersByLogin(String login) {return userDao.returnUsersByLogin(login);}

}
