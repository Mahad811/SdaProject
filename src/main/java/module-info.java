module com.example.sda_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jdk.compiler;

    opens com.example.sda_project to javafx.fxml;
    opens com.example.sda_project.model to javafx.base;
    opens com.example.sda_project.db to javafx.base;

    exports com.example.sda_project;
    exports com.example.sda_project.model;
    exports com.example.sda_project.db;

}