package database.dao;

import database.HibernateSessionFactoryUtil;
import database.User;
import database.Worker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class WorkerDao {
        public void save(Worker worker) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().getCurrentSession();
            Transaction transaction1 = session.beginTransaction();
            session.save(worker);
            transaction1.commit();
            session.close();
        }
        public void update(Worker worker) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction1 = session.beginTransaction();
            session.update(worker);
            transaction1.commit();
            session.close();
        }
        public void delete(Worker worker) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction transaction1 = session.beginTransaction();
            session.delete(worker);
            transaction1.commit();
            session.close();
        }
         public List<String> returnWorkersByUser(User user) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            List list = (List<String >)session.createQuery("FROM Worker WHERE user_id = '" + user.getId() + "'").list();
            session.getTransaction();
            session.close();
         return list;
         }
        public List<String> checkLogin(String login) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            List list = (List<String>)session.createQuery("SELECT login FROM Worker WHERE login = '" + login + "'").list();
            session.getTransaction();
            session.close();
        return list;
    }
}
