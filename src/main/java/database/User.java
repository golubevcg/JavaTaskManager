package database;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Entity
@Proxy(lazy=false)
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Worker> workers;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private String textfield;

    public User() {}

    public User(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = DigestUtils.md5Hex(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public String getTextfield() {
        return textfield;
    }

    public void setTextfield(String textfield) {
        this.textfield = textfield;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void addWorker(Worker worker){
        this.workers.add(worker);
        worker.setUser(this);
    }

    public void removeWorker(Worker worker){
        this.workers.remove(worker);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = DigestUtils.md5Hex(password);
    }

    public int getId() {
        return id;
    }

}