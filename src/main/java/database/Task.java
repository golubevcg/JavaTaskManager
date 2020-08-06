package database;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity
@Proxy(lazy=false)
@Table(name="tasks")
public class Task implements Comparable<Task>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String text;

    @Column
    private Integer rating;

    @Column //three types as string - quene, inwork, done
    private String tasktype;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "worker_id")
    public Worker worker;

    @Column
    private String color = "FFFFFF";

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }




    public Task(String text, Worker worker, String tasktype) {
        this.text = text;
        this.worker = worker;
        this.tasktype = tasktype;
    }

    public Task(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Task o) {
        return this.getText().compareTo(o.getText());
    }
}
