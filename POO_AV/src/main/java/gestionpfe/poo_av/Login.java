package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login extends Controller implements Initializable
{
    @FXML
    public TextField txtEmail;
    public PasswordField txtPassword;
    public Label lblWarning;

    public void login(ActionEvent e)
    {
        try
        {
            r = db.getStm().executeQuery("select * from users where email='" + txtEmail.getText() + "' and cin=" + txtPassword.getText());
            if (r.next())
            {
                Controller c;
                if (r.getString("role").equals("ETUDIANT"))//student
                {
                    int student = r.getInt("cin");
                    r = db.getStm().executeQuery("select * from project where etd1=" + student + " or etd2=" + student);
                    if (r.next())
                    {
                        c = goTo("monProjet.fxml",e);
                        r = db.getStm().executeQuery("select * from users where role='ETUDIANT' and cin=" + Integer.parseInt(txtPassword.getText()));
                        r.next();
                    }
                    else
                    {
                        c =  goTo("createProjet.fxml",e);
                        r = db.getStm().executeQuery("select * from users where role='ETUDIANT' and cin=" + Integer.parseInt(txtPassword.getText()));
                        r.next();
                        ((CreateProjet)c).setStudent1(r.getInt("cin"));
                    }
                }
                else if (r.getString("role").equals("ENSEIGNANT"))//professor
                {
                    c = goTo("ensgMenu.fxml",e);
                    ((EnsgMenu)c).setEnsg(r.getInt("cin"));
                }
                else//admin
                    c = goTo("adminMenu.fxml",e);
                c.setCoordinates(r.getString("prenom"), r.getString("nom"),Integer.parseInt(txtPassword.getText()));
                txtEmail.setText("");
                txtPassword.setText("");
                lblWarning.setText("");
            }
            else
                lblWarning.setText("Mail Ou Password Incorrect !!");
        }
        catch (SQLException | IOException ex) { lblWarning.setText(ex.getMessage()); ex.printStackTrace();}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        txtPassword.setStyle("-fx-font-size: 18px;");
        txtEmail.setStyle("-fx-font-size: 18px;");
    }
}
