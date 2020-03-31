package core.app;

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
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("file:src/main/resources/images/icon.png"));
        primaryStage.setTitle("FastMath");

        StageService.Holder.getInstance().setStage(primaryStage);
        StageService.Holder.loadScene("homePage");
    }

}
