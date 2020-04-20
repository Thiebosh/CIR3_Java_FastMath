package core.app;

import core.model.services.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import core.model.expression.parser.Parser;
import core.model.expression.parser.util.ParserResult;
import core.model.expression.parser.util.Point;


/**
 * Main class of the application
 */
public class FastMath extends Application {//NOPMD

    /**
     * lanceur de l'application
     * @param args arguments
     */
    public static final void main(final String[] args) {
        String f_x = "e^(2/3*i)";
        ParserResult result = Parser.eval(f_x);
        System.out.println(result.getComplexValue().getR() + " + " + result.getComplexValue().getI() + "i");

        final Point x = new Point("x", 2.0);
        final Point y = new Point("y", 5.0);
        f_x = "2.35*x+y";

        result = Parser.eval(f_x, x, y);
        System.out.println(result.getValue());

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
