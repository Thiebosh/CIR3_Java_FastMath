package core.app;

import core.model.db.Express;
import core.model.services.StageService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashSet;


/**
 * Main class of the application
 */
public class FastMath extends Application {//NOPMD
    //debut temporaire - créer gestionnaire
    private static LinkedHashSet<String> expressNames = new LinkedHashSet<String>();

    private static ArrayList<Express> expressList = new ArrayList<Express>();//map convertible en arrayList? accès immédiat à fonction

    public static ArrayList<Express> getExpressList() {
        return expressList;
    }

    public static ArrayList<String> getExpressNames() {
        return new ArrayList<String>(expressNames);
    }

    public static void update(Express expression, String oldName, String newName) {
        if (!newName.equals(oldName)) {
            if (expressNames.contains(newName) || newName.equals("fonction")) {//nom réservé pour ajout ligne
                System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
            } else {
                expressNames.add(newName);
                expressNames.remove(oldName);
                expression.setName(newName);
            }
        }
    }

    public static void addExpress(String name, String function) {
        if (expressNames.contains(name)) {
            System.out.println("créer une exception \"already used name\" et faire pop une fenêtre d'erreur?");
        }
        else {
            expressNames.add(name);
            expressList.add(new Express(name, function));
        }
    }
    //fin temporaire - créer gestionnaire

    /**
     * lanceur de l'application
     * @param args arguments
     */
    public static final void main(final String[] args) {
        //exemple temporaire - ancienne librairie
        /*
        String f_x = "e^(2/3*i)";
        ParserResult result = Parser.eval(f_x);
        System.out.println(result.getComplexValue().getR() + " + " + result.getComplexValue().getI() + "i");

        final Point x = new Point("x", 2.0);
        final Point y = new Point("y", 5.0);
        f_x = "2.35*x+y";

        result = Parser.eval(f_x, x, y);
        System.out.println(result.getValue());
*/

        //remplissage temporaire
        addExpress("f","2.35*x");
        addExpress("sinus","sin(x)");
        addExpress("cosinus","cos(x)");

        expressNames.add("f");

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
