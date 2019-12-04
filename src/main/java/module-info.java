module edu.buffalo.cse442.nullterminators {
    requires java.base;
    requires java.sql;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens edu.buffalo.cse442.nullterminators;
    exports edu.buffalo.cse442.nullterminators;
}
