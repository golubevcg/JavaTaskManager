import database.HibernateSessionFactoryUtil;
import database.Task;
import database.User;
import database.Worker;
import database.services.TaskService;
import database.services.UserService;
import database.services.WorkerService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.Root;
import java.util.List;

public class mainTest {
    public static void main(String[] args) {
//        UserService userService = new UserService();
//        WorkerService workerService = new WorkerService();
//        TaskService taskService = new TaskService();
//        User user = new User("Andrew", "Golubev", "agolubev", "123");
//        Worker ang = new Worker("Андрей","Голубев", user, "agol");
//        Worker alg = new Worker("Алина","Готовцева", user, "agotovts");
//        Worker ilba = new Worker("Пётр","Сергеевич", user, "IlBal");
//
//        user.setTextfield("mainTestTextField");
//
//        Task task1 = new Task("Проверка логина, его уникальность", ang, "quene");
//        Task task2 = new Task("Расположения интерфейса в время скейла", ang,"quene");
//        Task task3 = new Task("Выспаться, постирать всё, умыться, покушать", ang,"inwork");
//
//        Task task4 = new Task("Сходить погулять", ilba,"inwork");
//        Task task5 = new Task("Закончить документы", ilba,"quene");
//        Task task6 = new Task("Доделать отчёт", ilba,"quene");
//
//
//        Task task7 = new Task("Отправить письма заказчику", alg,"inwork");
//        Task task8 = new Task("Доделать модели в локации", alg,"quene");
//
//
//        taskService.saveTask(task1);
//        taskService.saveTask(task2);
//        taskService.saveTask(task3);
//        taskService.saveTask(task4);
//        taskService.saveTask(task5);
//        taskService.saveTask(task6);
//        taskService.saveTask(task7);
//        taskService.saveTask(task8);
        GridPane gridPane = new GridPane();



    }
}
