package gestionpfe.poo_av;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller
{
    ConnectionDB db=new ConnectionDB();
    ResultSet r;

    ActionEvent source;

    @FXML
    public Label prenom,nom;
    public ImageView iv;

    @FXML
    public Controller goTo(String fxmlFile,ActionEvent e) throws IOException
    {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root=loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        ((Controller) loader.getController()).source=e;
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        return loader.getController();
    }

    public void goToPreviousPage(ActionEvent e)
    {
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        ((Stage) ((Node)source.getSource()).getScene().getWindow()).show();
    }

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPhoto(byte[] bytes,ImageView iv) throws IOException
    {
            FileOutputStream fos = new FileOutputStream("images.png");
            fos.write(bytes);
            fos.close();
            iv.setImage(new Image("file:images.png"));
    }

    public void setCoordinates(String fn, String ln,int cin)
    {
        prenom.setText(fn);
        nom.setText(ln);
        try
        {
            r = db.getStm().executeQuery("select photo from users where cin="+cin);
            if(r.next())
                setPhoto(r.getBytes(1),iv);
        }
        catch (SQLException | IOException e) {e.printStackTrace();}
    }
}
