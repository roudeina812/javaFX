package gestionpfe.poo_av;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class EnsgDashboard extends Controller
{
    @FXML
    Label lblMail,lblEncadre,lblRapporte,lblCIN;

    public void setCoordinates(String fn, String ln, int cin)
    {
        super.setCoordinates(fn,ln,cin);
        lblCIN.setText(String.valueOf(cin));
        try
        {
            r = db.getStm().executeQuery("select email from users where cin=" + cin);
            if(r.next())
                lblMail.setText(r.getString("email"));
            r = db.getStm().executeQuery("select count(*) from project where encadrant="+cin);
            r.next();
            lblEncadre.setText(r.getInt(1)+lblEncadre.getText());
            r = db.getStm().executeQuery("select count(*) from project where rapporteur="+cin);
            r.next();
            lblRapporte.setText(r.getInt(1)+lblRapporte.getText());
        }
        catch (SQLException ex) { ex.printStackTrace(); }
    }
}
