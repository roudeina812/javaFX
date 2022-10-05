package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Ajout extends Controller
{
    @FXML
    public Label lbl,path,lblWarning;
    public Button btnAjout;
    public TextField txtPrenom,txtNom,txtCIN,txtMail;
    public ImageView iv;

    private String elementAjout;
    private Image img;
    private File f;

    public void setElementAjout(String elementAjout) { this.elementAjout = elementAjout.toUpperCase(); }

    public void ajout(ActionEvent e)
    {
        try
        {
            r = db.getStm().executeQuery("select cin from users where cin=" + txtCIN.getText());
            if (r.next())
                lbl.setText("CIN appartient à une autre personne !!");
            else
            {
                db.getStm().execute("insert into users values ('" + txtPrenom.getText().toUpperCase() + "','" + txtNom.getText().toUpperCase() + "'," + txtCIN.getText() + ",'" + txtMail.getText() + "','" + elementAjout + "',null)");
                if (img != null)
                {
                    FileInputStream fis = new FileInputStream(f);
                    PreparedStatement ps = db.getCnx().prepareStatement("update users set photo=? where cin=" + txtCIN.getText());
                    ps.setBinaryStream(1, fis, f.length());
                    ps.executeUpdate();
                }
                showAlert("Ajout effectué avec Succes");
                goToPreviousPage(e);
            }
        }
        catch(IOException | SQLException ex){lblWarning.setText(ex.getMessage()); ex.printStackTrace(); }
    }

    public void chercher()
    {
        FileChooser fc=new FileChooser();
        f= fc.showOpenDialog(null);
        img = new Image("file:" + f.getPath());
        iv.setImage(img);
        path.setText(f.getPath());
    }
}
