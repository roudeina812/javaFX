package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AfficherListe extends Controller implements Initializable
{
    @FXML
    public TableView<ListElements> list;
    public TableColumn<ListElements,String> col1,col2,col3;
    public Label lbl;
    public Button btnNoter;

    private String consultedElement;

    public void setList(String consultedElement)
    {
        this.consultedElement = consultedElement;
        lbl.setText(lbl.getText()+consultedElement);
        if(consultedElement.equals("Projets"))
        {
            col1.setCellValueFactory(new PropertyValueFactory<>("sujet"));    col1.setText("Sujet");
            col2.setCellValueFactory(new PropertyValueFactory<>("entreprise")); col2.setText("Entreprise");
            col3.setCellValueFactory(new PropertyValueFactory<>("etat"));     col3.setText("etat");
        }
        else
        {
            col1.setCellValueFactory(new PropertyValueFactory<>("prenom"));  col1.setText("Prenom");
            col2.setCellValueFactory(new PropertyValueFactory<>("nom"));     col2.setText("Nom");
            col3.setCellValueFactory(new PropertyValueFactory<>("email"));  col3.setText("email");
        }
    }

    public void consulter(ActionEvent e) throws IOException, SQLException
    {
        ListElements p = list.getSelectionModel().getSelectedItem();
        if (p != null)
        {
            if(consultedElement.equals("Projets"))
            {
                FicheDeProjet ps = (FicheDeProjet) goTo("ficheDeProjet.fxml", e);
                ps.setProjectData(p.getSujet(),p.getEntreprise());
            }
            else if (consultedElement.equals("Etudiants"))
            {
                EtdDashboard sd = (EtdDashboard) goTo("etdDashboard.fxml",e);
                sd.setCoordinates(p.getPrenom(),p.getNom(),Integer.parseInt(p.getCin()));
            }
            else
            {
                EnsgDashboard pd = (EnsgDashboard) goTo("ensgDashboard.fxml",e);
                pd.setCoordinates(p.getPrenom(),p.getNom(),Integer.parseInt(p.getCin()));
            }
        }
    }

    public void noter(ActionEvent e) throws IOException, SQLException
    {
        ListElements p = list.getSelectionModel().getSelectedItem();
        FicheDeProjet ps = (FicheDeProjet) goTo("ficheDeProjet.fxml",e);
        ps.setProjectData(p.getSujet(),p.getEntreprise());
        ps.btnNoter.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        btnNoter.setVisible(false);

    }
}
