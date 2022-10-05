package gestionpfe.poo_av;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.sql.SQLException;

public class EtdDashboard extends Controller
{
    @FXML
    Label lblCIN,lblSujet,lblMail,lblEntreprise, lblEtat,lblNote, lblBinome;
    public ProgressBar progress;

    public void setCoordinates(String fn, String ln, int cin)
    {
        super.setCoordinates(fn,ln,cin);
        lblCIN.setText(String.valueOf(cin));
        try
        {
            r = db.getStm().executeQuery("select email from users where cin="+cin);
            r.next();
            lblMail.setText(r.getString("email"));
            r = db.getStm().executeQuery("select sujet,entreprise,etd1,etd2,etat,note from project where etd1="+cin+" or etd2="+cin);
            if(r.next())
            {
                lblSujet.setText(r.getString("sujet"));
                lblEntreprise.setText(r.getString("entreprise"));
                lblEtat.setText(r.getString("etat"));
                lblNote.setText(r.getString("note")+"/20");

                switch (lblEtat.getText())
                {
                    case "en attente d'encadrement" : progress.setProgress(0.2); break;
                    case "encadré" : progress.setProgress(0.4); break;
                    case "en attente d'un rapporteur" : progress.setProgress(0.6); break;
                    case "en cours d'evaluation" : progress.setProgress(0.8); break;
                    case "evalué" : progress.setProgress(1); break;
                    default: progress.setProgress(0); break;
                }

                int binome = 0;
                if (r.getInt("etd1") == cin)
                        binome = r.getInt("etd2");
                else binome = r.getInt("etd1");
                r = db.getStm().executeQuery("select prenom||' '||nom from users where cin=" + binome);
                if (r.next())
                    lblBinome.setText(r.getString(1));
            }
        }
        catch (SQLException ex) { ex.printStackTrace(); }
    }
}
