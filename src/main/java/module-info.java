module com.pharmacie.pharmacie {

    requires javafx.web;
    requires java.sql;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    requires itextpdf;
    requires java.desktop;

    opens com.pharmacie.pharmacie.components to javafx.base;
    opens com.pharmacie.pharmacie to javafx.fxml;
    opens com.pharmacie.pharmacie.controller to javafx.fxml;


    exports com.pharmacie.pharmacie;
    exports com.pharmacie.pharmacie.controller;
    exports com.pharmacie.pharmacie.controller.tab;
    exports com.pharmacie.pharmacie.controller.modif;

    opens com.pharmacie.pharmacie.controller.modif to javafx.fxml;
    opens com.pharmacie.pharmacie.controller.tab to javafx.fxml;

    exports com.pharmacie.pharmacie.model;
    opens com.pharmacie.pharmacie.model to javafx.fxml;

}