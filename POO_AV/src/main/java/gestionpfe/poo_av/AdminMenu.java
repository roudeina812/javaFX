package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMenu extends Controller implements Initializable
{
    String selectedItem;

    @FXML
    ComboBox<String> cbAjout, cbConsulter, cbAffecter;
    public Label prenom,nom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        cbAjout.getItems().addAll("Etudiant","Enseignant");
        cbConsulter.getItems().addAll("Etudiants","Enseignants","Projets");
        cbAffecter.getItems().addAll("Encadrant","Rapporteur");
    }

    public void ajouter(ActionEvent e) throws IOException
    {
        Ajout add = (Ajout) goTo("ajout.fxml",e);
        selectedItem= cbAjout.getSelectionModel().getSelectedItem();
        add.setElementAjout(selectedItem);
        add.lbl.setText(add.lbl.getText()+selectedItem);
    }

    public void consulter(ActionEvent e) throws IOException, SQLException
    {
        AfficherListe sl = (AfficherListe) goTo("afficherListe.fxml",e);
        selectedItem= cbConsulter.getSelectionModel().getSelectedItem();

        List<ListElements> l= new ArrayList<>();
        if(selectedItem.equals("Projets"))
        {
            r = db.getStm().executeQuery("select sujet,entreprise,etat from project");
            while (r.next())
            {
                ListElements p = new ListElements();
                p.setSujet(r.getString("sujet"));
                p.setEntreprise(r.getString("entreprise"));
                p.setEtat(r.getString("etat"));
                l.add(p);
            }
        }
        else
        {
            if (selectedItem.equals("Etudiants"))
                r = db.getStm().executeQuery("select prenom,nom,cin,email from users where role='ETUDIANT'");
            else
                r = db.getStm().executeQuery("select prenom,nom,cin,email from users where role='ENSEIGNANT'");
            while (r.next())
            {
                ListElements p = new ListElements();
                p.setPrenom(r.getString("prenom"));
                p.setNom(r.getString("nom"));
                p.setEmail(r.getString("email"));
                p.setCin(r.getString("cin"));
                l.add(p);
            }
        }
        sl.list.getItems().addAll(l);
        sl.setList(selectedItem);
    }

    public void affecter(ActionEvent e) throws IOException, SQLException
    {
        selectedItem= cbAffecter.getSelectionModel().getSelectedItem();
        AffecterProjet ap = (AffecterProjet) goTo("affecterProjet.fxml", e);
        ap.setAssignedRole(selectedItem);
        if (selectedItem.equals("Encadrant"))
            r = db.getStm().executeQuery("select sujet,entreprise,etat from project where etat='en attente d''encadrement'");
        else
            r = db.getStm().executeQuery("select sujet,entreprise,etat from project where etat='en attente d''un rapporteur'");
        List<ListElements> projects = new ArrayList<>();
        while (r.next())
        {
            ListElements p = new ListElements();
            p.setSujet(r.getString("sujet"));
            p.setEntreprise(r.getString("entreprise"));
            projects.add(p);
        }
        ap.list1.getItems().addAll(projects);

        if(selectedItem.equals("Rapporteur"))
            r = db.getStm().executeQuery("select prenom,nom,cin from users where role='ENSEIGNANT' and cin not in(select nvl(encadrant,0) from project where etat='en attente d''un rapporteur')");
        else
            r = db.getStm().executeQuery("select prenom,nom,cin from users where role='ENSEIGNANT'");
        List<ListElements> l = new ArrayList<>();
        while(r.next())
        {
            ListElements prof = new ListElements();
            prof.setPrenom(r.getString("prenom"));
            prof.setNom(r.getString("nom"));
            prof.setCin(String.valueOf(r.getInt("cin")));
            l.add(prof);
        }
        ap.list2.getItems().addAll(l);
    }
}
