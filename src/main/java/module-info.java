module grice.c195 {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens grice.c195 to javafx.fxml;
    opens grice.c195.controllers to javafx.fxml;
    opens grice.c195.helper to javafx.base;
    opens grice.c195.DAO to javafx.base;
    opens grice.c195.model to javafx.base;
    exports grice.c195;
    exports grice.c195.controllers;
    exports grice.c195.helper;
    exports grice.c195.DAO;
    exports grice.c195.model;


}