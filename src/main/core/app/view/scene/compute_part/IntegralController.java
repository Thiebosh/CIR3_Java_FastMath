package core.app.view.scene.compute_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.function.FunctionX;
import core.services.mathLibrary.integral.Integral;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.util.Round;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de l'onglet d'intégration dans compute.fxml
 */
public class IntegralController implements Initializable {
    /**
     * Chargement initial (après le constructeur) du fxml lié au contrôleur  : prépration des éléments du fxml
     * @param location paramètre par défaut
     * @param resources paramètre par défaut
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        method.setItems(FXCollections.observableArrayList("Trapèzes", "Simpson", "Romberg"));
    }


    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboTransformController;
    /**
     * Element du fxml : textfield contenant les valeurs d'évaluation
     */
    @FXML
    private TextField intervalA, intervalB;
    /**
     * Element du fxml : ChoiceBox listant les méthodes d'intégration
     */
    @FXML
    private ChoiceBox<String> method;
    /**
     * Element du fxml : label ou afficher le résultat
     */
    @FXML
    private Label resultFunction;


    /**
     * onAction du fxml : effectue la transformation et affiche le résultat
     */
    @FXML
    private void executeIntegral() {
        Parser.setDegree(ExpressManager.getDegree());

        //input secure
        String functionChoice = functionComboTransformController.getFunction();
        String methodChoice = method.getValue();

        String userValueA = intervalA.getCharacters().toString();
        if (userValueA.length() == 0) userValueA = "0";
        else userValueA = userValueA.replaceAll(FunctionX.getPI(), String.valueOf(Math.PI)).replaceAll(",", ".");

        String userValueB = intervalB.getCharacters().toString();
        if (userValueB.length() == 0) userValueB = "0";
        else userValueB = userValueB.replaceAll(FunctionX.getPI(), String.valueOf(Math.PI)).replaceAll(",", ".");

        //calcul & display
        Double a = Parser.eval(userValueA).getValue();
        Double b = Parser.eval(userValueB).getValue();

        Integral integral = new Integral(functionChoice);
        try {
            switch (methodChoice) {
                case "Trapèzes": resultFunction.setText(String.valueOf(Round.rint(integral.trapezoidal(a, b), 8)));
                    break;
                case "Simpson": resultFunction.setText(String.valueOf(Round.rint(integral.simpson(a, b), 8)));
                    break;
                case "Romberg": resultFunction.setText(String.valueOf(Round.rint(integral.Romberg(a, b, 20), 8)));
                    break;
                default:
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
