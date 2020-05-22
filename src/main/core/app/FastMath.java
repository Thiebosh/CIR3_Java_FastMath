package core.app;

import core.app.data.ExpressManager;
import core.services.mathLibrary.parser.Parser;
import core.services.windowHolder.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Main class of the application
 */
public class FastMath extends Application {//NO PMD
    /**
     * Point de départ de l'application
     * @param args arguments du compilateur
     */
    public static final void main(final String[] args) { launch(args); }

    /**
     * chargement des données et Lancement de la fenêtre d'application
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public final void start(final Stage primaryStage) {
        ExpressManager.load();
        StageService.Holder.getInstance().setMainStage(primaryStage, "FastMath", new Image("file:src/main/resources/images/icon.png"));
        StageService.Holder.loadMainWindowsScene("home");

        StageService.Holder.openErrorWindows("messageOnly", "test de message d'erreur");
    }

    /**
     * fermeture de la fenêtre d'application et déchargement des données
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        ExpressManager.save();
        ExpressManager.clearAll();
        super.stop();
    }
}
