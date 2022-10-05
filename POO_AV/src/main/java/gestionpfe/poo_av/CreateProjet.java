package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.SQLException;

public class CreateProjet extends Controller
{
    @FXML
    public Label lblWarning;
    public AnchorPane binome;
    public RadioButton btnBinome;
    public TextField txtSujet,txtEntreprise,txtPrenom,txtNom,txtCIN;

    private Integer student1,student2=null;

    public void setStudent1(int student1) { this.student1 = student1; }

    public void binome() { binome.setDisable(!btnBinome.isSelected()); }

    public void createProject(ActionEvent e)
    {
        try
        {
            if (btnBinome.isSelected())
            {
                r = db.getStm().executeQuery("select * from users where role='ETUDIANT' and prenom='" + txtPrenom.getText().toUpperCase() + "' and nom='" + txtNom.getText().toUpperCase() + "' and cin=" + txtCIN.getText());
                if (r.next())
                    student2 = r.getInt("cin");
                else
                    lblWarning.setText("invalid student !!");
            }
            if (btnBinome.isSelected() == (student2 != null))
            {
                db.getStm().execute("insert into project values ('" + txtSujet.getText() + "','" + txtEntreprise.getText() + "'," + student1 + "," + student2 + ",null,null,'en attente d''encadrement',null,null)");
                MonProjet mp = (MonProjet) goTo("monProjet.fxml",e);
                mp.setCoordinates(prenom.getText(),nom.getText(),student1);
                mp.source=source;
            }
        }
        catch (SQLException ex)
        {
            txtSujet.setText("");      txtSujet.setStyle("-fx-border-color: red");
            txtEntreprise.setText("");   txtEntreprise.setStyle("-fx-border-color: red");
            if (btnBinome.isSelected())
            {
                if (txtPrenom.getText().isEmpty() || txtNom.getText().isEmpty() || txtCIN.getText().isEmpty())
                {
                    txtPrenom.setStyle("-fx-border-color: red");
                    txtNom.setStyle("-fx-border-color: red");
                    txtCIN.setStyle("-fx-border-color: red");
                }
            }
            lblWarning.setText(ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ex) { lblWarning.setText(ex.getMessage());}
    }
}
