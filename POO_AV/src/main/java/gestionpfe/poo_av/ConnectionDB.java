package gestionpfe.poo_av;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB
{
    private Connection cnx;
    private Statement stm;

    public ConnectionDB()
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            cnx= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
            stm=cnx.createStatement();
        }
        catch(ClassNotFoundException | SQLException e)
        { e.printStackTrace(); }
    }

    public Connection getCnx() { return cnx; }

    public Statement getStm() { return stm; }
}


