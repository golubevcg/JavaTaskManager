module test2 {

    requires javafx.fxml;
    requires javafx.base;
    requires javafx.controls;
    requires java.sql;

    opens test2.src.main;
    opens test2.src.main.java;
    opens test2.src.main.java.animations;
    opens test2.src.main.java.controllers;
    opens test2.src.main.java.database;
}
