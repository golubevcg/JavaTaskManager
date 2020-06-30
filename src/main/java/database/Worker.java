package database;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Entity
@Proxy(lazy=false)
@Table(name="workers")
public class Worker implements Comparable<Worker>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String login;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    public Worker() {
    }

    public Worker(String firstname, String lastname, User user, String login) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.user = user;
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String secondname) {
        this.lastname = secondname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task){
        task.setWorker(this);
        this.tasks.add(task);
    }

    public void deleteTask(Task task){
        this.tasks.remove(task);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +

                ", login='" + login + '\'';
    }


    @Override
    public int compareTo(Worker o) {
        return this.getLastname().compareTo(o.getLastname());

    }
}
