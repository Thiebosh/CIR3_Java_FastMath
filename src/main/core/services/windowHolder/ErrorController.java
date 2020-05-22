package core.services.windowHolder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.util.HashMap;

/**
 * Controleur de la fenêtre d"erreur
 * @see ContextControllerFactory
 */
public class ErrorController extends ContextControllerFactory {
    /**
     * Container du message
     */
    @FXML
    private AnchorPane messageLocation;


    /**
     * Met le message d'erreur dans le container
     * @param args map clé valeur contenant toutes les données initiales
     */
    @Override
    public void setInitialValues(final HashMap args) {
        Label message = new Label();
        message.setText("\t"+args.get("error")+"\t");
        messageLocation.getChildren().add(message);
    }


    /**
     * fxml onClic élément : bouton ok
     */
    @FXML
    private void okButton() {
        closeStage();
    }
}
