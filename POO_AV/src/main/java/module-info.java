module gestionpfe.poo_av
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.oracle.database.jdbc;

    opens gestionpfe.poo_av to javafx.fxml;
    exports gestionpfe.poo_av;
}