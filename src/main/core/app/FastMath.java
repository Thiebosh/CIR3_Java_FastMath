package core.app;

import core.model.db.ExpressManager;
import core.model.mathlibrary.equation.EquationX;
import core.model.mathlibrary.equation.NonLinearSys;
import core.model.mathlibrary.exception.CalculatorException;
import core.model.mathlibrary.function.FunctionXs;
import core.model.mathlibrary.parser.Parser;
import core.model.mathlibrary.parser.util.ParserResult;
import core.model.mathlibrary.parser.util.Point;
import core.model.mathlibrary.polynomial.Polynomial;
import core.model.mathlibrary.util.Round;
import core.model.services.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


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
        /*String f_x = "e^(2/3*i)";
        ParserResult result = Parser.eval(f_x);
        System.out.println(result.getComplexValue().getR() + " + " + result.getComplexValue().getI() + "i");

        final Point x = new Point("x", 2.0);
        final Point y = new Point("y", 5.0);
        f_x = "2.35*x+y";

        result = Parser.eval(f_x, x, y);
        System.out.println(result.getValue());*/

        /*String f_x = EquationX.simplify("sin(x)=cos(x)");
        EquationX ec_x = new EquationX(f_x);
        try {
            System.out.println(Round.rint(ec_x.getRoot(0,0.1e-10), 5));
        } catch (CalculatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        /*double[] x ={1, 1, 1};

        try {

            Polynomial.rootCalc(x);
            System.out.println("***************");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

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
