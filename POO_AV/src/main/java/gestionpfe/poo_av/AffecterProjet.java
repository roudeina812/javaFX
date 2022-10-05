package gestionpfe.poo_av;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AffecterProjet extends Controller implements Initializable
{
    @FXML
    public TableView<ListElements> list1,list2;
    public TableColumn<ListElements,String> col1,col2,col3,col4;

    private String assignedRole;

    public void setAssignedRole(String assignedRole) { this.assignedRole = assignedRole; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        col1.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        col2.setCellValueFactory(new PropertyValueFactory<>("entreprise"));
        col3.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col4.setCellValueFactory(new PropertyValueFactory<>("nom"));
    }

    public void affecter() throws SQLException
    {
        ListElements p = list1.getSelectionModel().getSelectedItem();
        ListElements prof = list2.getSelectionModel().getSelectedItem();
        String etat;
        if (assignedRole.equals("Encadrant"))
            etat="encadré";
        else etat = "en cours d''evaluation";
        db.getStm().execute("update project set "+assignedRole+"="+prof.getCin()+",etat='"+etat+"' where sujet='"+p.getSujet()+"' and entreprise='"+p.getEntreprise()+"'");
        showAlert(assignedRole+" Affecté Avec Succes");
    }
}
