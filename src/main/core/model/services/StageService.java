package core.model.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Gestionnaire auto-hébergé de contenu de fenêtre
 */
public class StageService {//NOPMD
    /**
     * unique instance auto hébergée de la fenêtre
     */
    private Stage stage;

    /**
     * getter : récupérer l'instance de fenêtre contrôlée
     * @return
     */
    protected Stage getStage() {
        return stage;
    }

    /**
     * setter : modifier la fenêtre affichée/contrôlée
     * @param newStage fenêtre à contrôler
     */
    public void setStage(final Stage newStage) {
        this.stage = newStage;
    }


    /**
     * partie statique : accès à l'instance
     */
    public static class Holder {//NOPMD
        /**
         * chemin d'accès aux fichiers fxml
         */
        private final static String RESOURCE_FOLDER_PATH = "/view/";

        /**
         * unique instance de la classe, accès à la scène
         */
        protected static final StageService INSTANCE = new StageService();

        /**
         * getter : accès à l'unique instance de scène
         *
         * @return unique instance de la classe
         */
        public static StageService getInstance() {
            return INSTANCE;
        }

        /**
         * met à jour la scène affichée dans la fenêtre
         *
         * @param pageName nom du fichier fxml à afficher
         */
        public static void loadScene(final String pageName) {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StageService.class.getResource(RESOURCE_FOLDER_PATH + pageName + ".fxml"));

            try {
                final VBox sceneRoot = loader.load();//VBox : rootNodeScene type
                INSTANCE.getStage().setScene(new Scene(sceneRoot));
                INSTANCE.getStage().show();
            } catch (IOException e) {
                e.printStackTrace();//passer en projet maven puis suivre https://www.baeldung.com/logback
            }
        }
    }
}
