package core.app;

import core.model.services.StageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the application
 */
public class FastMath extends Application {

    /**
     *
     * @param args
     */
    public static final void main(final String[] args) {
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("file:src/main/resources/images/icon.png"));
        primaryStage.setTitle("FastMath");

        //methode classique
        //final Parent root = FXMLLoader.load(getClass().getResource("/view/homePage.fxml"));
        //primaryStage.setScene(new Scene(root));
        //primaryStage.show();

        //methode contactapp
        StageService.getInstance().setCurrentStage(primaryStage);
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/homePage.fxml"));
        try {
            final VBox rootLayout = loader.load();
            final Scene scene = new Scene(rootLayout);
            StageService.getInstance().getCurrentStage().setScene(scene);
            StageService.getInstance().getCurrentStage().show();
        } catch (IOException e) {
            e.printStackTrace();// projet maven puis https://www.baeldung.com/logback
        }

        //autres methodes, plus simples ?
        /*
        https://openclassrooms.com/forum/sujet/javafx-switcher-entre-plusieurs-panels
        https://stackoverflow.com/questions/37200845/how-to-switch-scenes-in-javafx
        https://stackoverflow.com/questions/12804664/how-to-swap-screens-in-a-javafx-application-in-the-controller-class/12805134#12805134
        http://www.learningaboutelectronics.com/Articles/How-to-create-multiple-scenes-and-switch-between-scenes-in-JavaFX.php
        https://www.developpez.net/forums/d1416642/java/interfaces-graphiques-java/javafx/changer-dynamiquement-contenu-fenetre/
        https://gist.github.com/pethaniakshay/302072fda98098a24ce382a361bdf477
         */
    }


}
