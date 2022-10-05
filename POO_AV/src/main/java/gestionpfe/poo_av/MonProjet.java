package gestionpfe.poo_av;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MonProjet extends Controller
{
    Integer cin;

    @FXML
    public Label lblSujet, lblBinome, lblEncadrant, lblRapporteur,path, lblNote,lblWarning, lblEtat;
    public Button btnChercher, btnOuvrirFichier;
    public ProgressBar progress;

    public void setCoordinates(String fn,String ln,int cin)
    {
        super.setCoordinates(fn,ln,cin);
        try
        {
            this.cin=cin;
            r = db.getStm().executeQuery("select * from project where etd1="+cin+"  or etd2="+cin);
            if(r.next())
            {
                int binome, encadrant =r.getInt("encadrant"), rapporteur =r.getInt("rapporteur");
                lblSujet.setText(r.getString("sujet"));
                lblNote.setText(r.getString("note")+"/20");
                lblEtat.setText(r.getString("etat"));

                switch (lblEtat.getText())
                {
                    case "en attente d'encadrement" : progress.setProgress(0.2); break;
                    case "encadré" : progress.setProgress(0.4); break;
                    case "en attente d'un rapporteur" : progress.setProgress(0.6); break;
                    case "en cours d'evaluation" : progress.setProgress(0.8); break;
                    case "evalué" : progress.setProgress(1); break;
                    default: progress.setProgress(0); break;
                }

                if(r.getInt("etd1")==cin)
                    binome =r.getInt("etd2");
                else
                    binome =r.getInt("etd1");

                r = db.getStm().executeQuery("select prenom||' '||nom from users where cin="+ encadrant);
                if(r.next())
                    lblEncadrant.setText(r.getString(1));

                r = db.getStm().executeQuery("select prenom||' '||nom from users where cin="+ rapporteur);
                if(r.next())
                    lblRapporteur.setText(r.getString(1));

                r = db.getStm().executeQuery("select prenom,nom from users where cin="+ binome);
                if(r.next())
                    lblBinome.setText(r.getString("prenom")+" "+r.getString("nom"));

                btnChercher.setDisable(!lblEtat.getText().equals("encadré"));
                btnOuvrirFichier.setDisable(progress.getProgress()>=0.6);
            }
        } catch (SQLException ex) { lblWarning.setText(ex.getMessage()); ex.printStackTrace(); }
    }

    public void chercher()
    {
        File f;
        FileChooser fc=new FileChooser();
        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("*.pdf","*.pdf");
        fc.getExtensionFilters().add(ef);
        f= fc.showOpenDialog(null);
        if (f!=null)
        {
            try
            {
                FileInputStream fis=new FileInputStream(f);
                PreparedStatement ps = db.getCnx().prepareStatement("update project set project_file=? where etd1="+cin+" or etd2="+cin);
                ps.setBinaryStream(1,fis,f.length());
                ps.executeUpdate();
                path.setText(f.getPath());
                db.getStm().execute("update project set etat='en attente d''un rapporteur' where etd1="+cin+" or etd2="+cin);
                lblEtat.setText("en attente d'un rapporteur");
                progress.setProgress(0.6);
                btnChercher.setDisable(true);
            }
            catch(IOException | SQLException ex){lblWarning.setText(ex.getMessage()); ex.printStackTrace(); }
        }
    }

    public void ouvrirFichier()
    {
        try
        {
            r = db.getStm().executeQuery("select project_file from project where etd1=" + cin + " or etd2=" + cin);
            File f = new File("C:\\Users\\Aridhi\\Desktop\\ISG\\projets\\2eme\\POO_AV\\PFE.pdf");
            if (r.next()) {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(r.getBytes("project_file"));
                fos.close();
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f);
            }
        }
        catch(IOException | SQLException ex){lblWarning.setText(ex.getMessage()); ex.printStackTrace(); }
    }
}