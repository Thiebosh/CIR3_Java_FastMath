package core.services.windowHolder;

import core.app.error.ErrorCode;
import core.app.error.ErrorMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

/**
 * Gestionnaire auto-hébergé de contenu de fenêtre
 */
public class StageService {//NOPMD
    /**
     * unique instance auto hébergée de la fenêtre
     */
    private static Stage mainStage;

    /**
     * getter : récupérer l'instance de fenêtre contrôlée
     * @return
     */
    private Stage getMainStage() {
        return mainStage;
    }

    /**
     * setter : modifier la fenêtre affichée/contrôlée
     * @param newStage fenêtre à contrôler
     * @param title titre de la fenêtre
     * @param icon icone de la fenêtre
     */
    public void setMainStage(final Stage newStage, final String title, Image icon) {
        this.mainStage = newStage;
        if (title != null) this.mainStage.setTitle(title);
        if (icon != null) this.mainStage.getIcons().add(icon);
    }


    /**
     * partie statique (nécessairement dans classe non statique) : accès et pilotage de l'instance (regroupement)
     */
    public static class Holder {//NOPMD
        /**
         * unique instance de la classe, accès à la scène
         */
        protected static final StageService INSTANCE = new StageService();

        /**
         * chemin d'accès aux fichiers fxml
         */
        private final static String RESOURCE_FOLDER_PATH = "/view/scene/";

        /**
         * chemin d'accès aux fichiers fxml de fenêtre contextuelle
         */
        private final static String CONTEXTUAL_FOLDER_PATH = "/view/scene_contextual/";

        /**
         * chemin d'accès aux fichiers fxml de fenêtre d'erreur
         */
        private final static String ERROR_FOLDER_PATH = "/view/scene_error/";

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
        public static void loadMainWindowsScene(final String pageName) {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StageService.class.getResource(RESOURCE_FOLDER_PATH+pageName+".fxml"));

            try {
                final VBox sceneRoot = loader.load();//VBox : rootNodeScene type
                INSTANCE.getMainStage().setScene(new Scene(sceneRoot));
                INSTANCE.getMainStage().show();
            } catch (IOException e) {
                e.printStackTrace();//passer en projet maven puis suivre https://www.baeldung.com/logback
            }
        }

        private static Stage contextStage = new Stage();

        public static <T extends ContextControllerFactory> void openContextWindows(final String title, final String pageName, Modality config, T controller, final HashMap<String, ?> args) {
            if (!contextStage.isShowing()) contextStage.toFront();
            else {
                final FXMLLoader loader = new FXMLLoader();
                loader.setLocation(StageService.class.getResource(CONTEXTUAL_FOLDER_PATH + pageName + ".fxml"));

                try {
                    contextStage = new Stage();
                    final AnchorPane sceneRoot = loader.load();
                    contextStage.setScene(new Scene(sceneRoot));

                    contextStage.initModality(config);
                    contextStage.setTitle(title);
                    if (mainStage.getIcons().size() > 0) {
                        contextStage.getIcons().add(mainStage.getIcons().get(0));//share icon img
                    }

                    if (controller != null) {
                        controller = loader.getController();//Returns the controller associated with the root object.
                        controller.setStage(contextStage);
                        controller.setInitialValues(args);
                    }

                    contextStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static Stage errorStage = new Stage();

        public static <T extends ContextControllerFactory> void openErrorWindows(final String pageName, final ErrorCode error) {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StageService.class.getResource(ERROR_FOLDER_PATH + pageName + ".fxml"));

            final HashMap<String, String> args = new HashMap<>();
            args.put("error", ErrorMessage.getMessage(error));

            try {
                errorStage = new Stage();
                final AnchorPane sceneRoot = loader.load();
                errorStage.setScene(new Scene(sceneRoot));

                errorStage.initModality(Modality.APPLICATION_MODAL);
                errorStage.setTitle("Erreur");
                if (mainStage.getIcons().size() > 0) {
                    errorStage.getIcons().add(mainStage.getIcons().get(0));//share icon img
                }
                errorStage.setResizable(false);

                T controller = loader.getController();//Returns the controller associated with the root object.
                controller.setStage(errorStage);
                controller.setInitialValues(args);

                errorStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
