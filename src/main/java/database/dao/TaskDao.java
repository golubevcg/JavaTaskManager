package database.dao;

import database.HibernateSessionFactoryUtil;
import database.Task;
import database.Worker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TaskDao {

    public void save(Task task) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction2 = session.beginTransaction();
        session.save(task);
        transaction2.commit();
        session.close();
    }

    public void update(Task task) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction2 = session.beginTransaction();
        session.update(task);
        transaction2.commit();
        session.close();
    }

    public void delete(Task task) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction2 = session.beginTransaction();
        session.delete(task);
        transaction2.commit();
        session.close();
    }

    public List<String> checkTask(String text) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List list = (List<String>) session.createQuery("SELECT text FROM Task WHERE text = '" + text + "'").list();
        session.getTransaction().begin();
        session.close();
        return list;
    }
}