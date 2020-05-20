package core.app;

import core.app.data.ExpressManager;
import core.services.windowHolder.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Main class of the application
 */
public class FastMath extends Application {//NOPMD
    /**
     * lanceur de l'application
     * @param args arguments
     */
    public static final void main(final String[] args) {
        //charger expressions
        try {
            ExpressManager.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public final void start(final Stage primaryStage) throws Exception {
        StageService.Holder.getInstance().setMainStage(primaryStage, "FastMath", new Image("file:src/main/resources/images/icon.png"));
        StageService.Holder.loadMainWindowsScene("home");
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        try {
            ExpressManager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExpressManager.clearAll();
        super.stop();
    }
}
