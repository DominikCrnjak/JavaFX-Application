module hr.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    exports hr.project.application;
    opens hr.project.application to javafx.fxml;
    exports hr.project.application.admin;
    opens hr.project.application.admin to javafx.fxml;
}