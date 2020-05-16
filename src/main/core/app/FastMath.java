package core.app;

import core.model.data.ExpressManager;
import core.model.services.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


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
        ExpressManager.addToExpressList("f","2.35*x");
        ExpressManager.addToExpressList("sinus","sin(x)");
        ExpressManager.addToExpressList("cosinus","cos(x)");

        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws Exception {
        StageService.Holder.getInstance().setMainStage(primaryStage, "FastMath", new Image("file:src/main/resources/images/icon.png"));
        StageService.Holder.loadMainWindowsScene("home");
    }
}
