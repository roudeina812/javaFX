package gestionpfe.poo_av;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class FicheDeProjet extends Controller implements Initializable
{
    @FXML
    Label lblSujet, lblEtd1, lblEtd2, lblEntreprise, lblEtat, lblEncadrant, lblRapporteur, lblNote;
    public Button btnNoter;
    public ProgressBar progress;

    int cin;

    public void setProjectData(String subject, String enterprise) throws SQLException
    {
        r = db.getStm().executeQuery("select * from project where sujet='"+subject+"' and entreprise='"+enterprise+"'");
        r.next();
        lblSujet.setText(subject);
        lblEntreprise.setText(enterprise);
        lblEtat.setText(r.getString("etat"));
        lblNote.setText(String.valueOf(r.getInt("note"))+"/20");

        switch (lblEtat.getText())
        {
            case "en attente d'encadrement" : progress.setProgress(0.2); break;
            case "encadré" : progress.setProgress(0.4); break;
            case "en attente d'un rapporteur" : progress.setProgress(0.6); break;
            case "en cours d'evaluation" : progress.setProgress(0.8); break;
            case "evalué" : progress.setProgress(1); break;
            default: progress.setProgress(0); break;
        }

        int student1=r.getInt("etd1"),
            student2=r.getInt("etd2"),
            encadrant =r.getInt("encadrant"),
            rapporteur =r.getInt("rapporteur");
        cin=student1;
        r = db.getStm().executeQuery("select prenom||' '||nom,cin from users where cin="+student1+" or cin="+student2+" or cin="+ encadrant +" or cin="+ rapporteur);
        while(r.next())
        {
            if (r.getInt(2)==student1)
                lblEtd1.setText(r.getString(1));
            if (r.getInt(2)==student2)
                lblEtd2.setText(r.getString(1));
            if (r.getInt(2)== encadrant)
                lblEncadrant.setText(r.getString(1));
            if (r.getInt(2)== rapporteur)
                lblRapporteur.setText(r.getString(1));
        }

    }

    public void ouvrirFichier()
    {
        try
        {
            r = db.getStm().executeQuery("select project_file from project where etd1=" + cin );
            File f = new File("C:\\Users\\Aridhi\\Desktop\\ISG\\projets\\2eme\\POO_AV\\PFE.pdf");
            if (r.next())
            {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(r.getBytes("project_file"));
                fos.close();
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f);
            }
        }
        catch(IOException | SQLException ex){ ex.printStackTrace(); }
    }

    public void noter()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Affecter Une Note");
        dialog.setHeaderText("Noter ce Projet :");
        dialog.setContentText("Entrer La Note :");

        Optional<String> result = dialog.showAndWait();
        if (!result.isEmpty())
        {
            try
            {
                System.out.println("result.get() = " + result.get());
                db.getStm().execute("update project set note="+result.get()+",etat='evalué' where etd1=(select cin from users where prenom||' '||nom='"+lblEtd1.getText()+"')");
                showAlert("La Note "+result.get()+" Est Affecté à Ce Projet");
                lblNote.setText(result.get()+"/20");
                progress.setProgress(1);
            }
            catch (SQLException ex) { showAlert(ex.getMessage()); ex.printStackTrace();}
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { btnNoter.setVisible(false); }
}
