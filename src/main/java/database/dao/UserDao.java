package database.dao;

import database.HibernateSessionFactoryUtil;
import database.User;
import database.Worker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.ObjectInputFilter;
import java.lang.module.Configuration;
import java.util.List;

public class UserDao {

    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction1 = session.beginTransaction();
        session.save(user);
        transaction1.commit();
        session.close();
    }
    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction1 = session.beginTransaction();
        session.update(user);
        transaction1.commit();
        session.close();
    }
    public void delete(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction1 = session.beginTransaction();
        session.delete(user);
        transaction1.commit();
        session.close();

    }
    public List<String> checkLogin(String login) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List list = (List<String>)session.createQuery("SELECT login FROM User WHERE login = '" + login + "'").list();
        session.getTransaction().begin();
        session.close();
        return list;
    }
    public List<String> returnWorkersFromUser(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List list = (List<String >)session.createQuery("FROM User u WHERE u.product as product = '" + user.getId() + "'").list();
        session.getTransaction();
        session.close();
        return list;
    }
}
