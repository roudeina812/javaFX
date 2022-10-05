package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnsgMenu extends Controller
{
    private Integer ensg;

    public void setEnsg(Integer ensg) { this.ensg = ensg; }

    @FXML
    public Button b1, b2;

    public void onClick(ActionEvent e)
    {
        try
        {
            System.out.println("ensg = " + ensg);
            AfficherListe sl = (AfficherListe) goTo("afficherListe.fxml",e);
            if (e.getSource().equals(b1))
            {
                r = db.getStm().executeQuery("select sujet,entreprise,etat from project where rapporteur=" + ensg + " and note is null");
                sl.btnNoter.setVisible(true);
            }
            else if(e.getSource().equals(b2))
                r = db.getStm().executeQuery("select sujet,entreprise,etat from project where encadrant=" + ensg + " or rapporteur=" + ensg + " and note is not null");
            List<ListElements> projects = new ArrayList<>();
            while (r.next())
            {
                ListElements p = new ListElements();
                p.setSujet(r.getString("sujet"));
                p.setEntreprise(r.getString("entreprise"));
                p.setEtat(r.getString("etat"));
                projects.add(p);
                System.out.println("projects = " + projects.size());
            }
            sl.list.getItems().addAll(projects);
            sl.setList("Projets");
        }
        catch(IOException | SQLException ex){ ex.printStackTrace(); }
    }
}
