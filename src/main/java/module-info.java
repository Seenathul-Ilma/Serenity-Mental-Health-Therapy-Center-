module lk.ijse.gdse71.serenitytherapycenter {
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.mail;
    requires net.sf.jasperreports.core;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires jbcrypt;
    requires org.controlsfx.controls;

    opens lk.ijse.gdse71.serenitytherapycenter.entity to org.hibernate.orm.core;
    opens lk.ijse.gdse71.serenitytherapycenter.config to jakarta.persistence;

    opens lk.ijse.gdse71.serenitytherapycenter.controller to javafx.fxml;
    //opens lk.ijse.gdse71.serenitytherapycenter.dto.tm to java.base;
    opens lk.ijse.gdse71.serenitytherapycenter.dto.tm to javafx.base;
    exports lk.ijse.gdse71.serenitytherapycenter;
    opens lk.ijse.gdse71.serenitytherapycenter.dto to javafx.base;
}