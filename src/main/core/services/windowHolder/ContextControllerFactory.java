package core.services.windowHolder;

import javafx.stage.Stage;
import java.util.HashMap;

/**
 * Contrôleur abstrait de fenêtre contextuelle : sert de modèle générique
 * @param <T> Type générique : instance de contrôleur de fichier fxml
 * @see StageService méthode openContextWindows
 */
public abstract class ContextControllerFactory<T> {
    /**
     * Instance de la fenêtre
     */
    private Stage stage;
    /**
     * Setter du stage
     * @param stage le stage de la fenêtre contextuelle
     */
    public void setStage(final Stage stage) {
        this.stage = stage;
    }
    /**
     * Fermeture de la fenêtre contextuelle
     */
    public void closeStage() {
        stage.close();
    }


    /**
     * Setter des données nécessaires à l'initalisation de la fenêtre du contrôleur
     * @param args map clé valeur contenant toutes les données initiales
     */
    public abstract void setInitialValues(final HashMap<String, Object> args);
}
