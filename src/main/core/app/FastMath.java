package core.app;

import core.model.db.ExpressManager;
import core.model.mathlibrary.parser.Parser;
import core.model.mathlibrary.parser.util.ParserResult;
import core.model.mathlibrary.parser.util.Point;
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
        //exemple temporaire
        String f_x = "e^(2/3*i)";
        ParserResult result = Parser.eval(f_x);
        System.out.println(result.getComplexValue().getR() + " + " + result.getComplexValue().getI() + "i");

        final Point x = new Point("x", 2.0);
        final Point y = new Point("y", 5.0);
        f_x = "2.35*x+y";

        result = Parser.eval(f_x, x, y);
        System.out.println(result.getValue());


        //remplissage temporaire
        ExpressManager.addExpress("f","2.35*x");
        ExpressManager.addExpress("sinus","sin(x)");
        ExpressManager.addExpress("cosinus","cos(x)");

        ExpressManager.addExpressNames("f");

        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("file:src/main/resources/images/icon.png"));
        primaryStage.setTitle("FastMath");

        StageService.Holder.getInstance().setStage(primaryStage);
        StageService.Holder.loadScene("home");
    }
}
