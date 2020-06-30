//какой модуль - т.е. имя корневой папки по сути
module test2 {

    //что требуется?
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.controls;
    requires java.sql;


    //где используется
    opens test2.src.main;
    opens test2.src.main.java;
    opens test2.src.main.java.animations;
    opens test2.src.main.java.controllers;
    opens test2.src.main.java.database;

//    opens test1.fxml;
}
