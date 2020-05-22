package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.function.FunctionX;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.ParserResult;
import core.services.mathLibrary.parser.util.Point;
import core.services.mathLibrary.util.Round;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Contrôleur de l'élément de fenêtre evaluate.fxml
 */
public class EvaluateController {
    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboEvaluateController;

    /**
     * Element du fxml : textfield contenant la valeur d'évaluation
     */
    @FXML
    private TextField valueFunction;

    /**
     * Element du fxml : texte affichant le résultat
     */
    @FXML
    private Label resultFunction;

    /**
     * onAction du fxml : évalue la fonction et affiche le résultat
     */
    @FXML
    private void executeEvaluation() {
        Parser.setDegree(ExpressManager.getDegree());

        String function = functionComboEvaluateController.getFunction();
        String userValue =  valueFunction.getCharacters().toString().replaceAll(FunctionX.getPI(), String.valueOf(Math.PI)).replaceAll(",", ".");

        ParserResult result = Parser.eval(userValue);//case of expression like 2*pi/3
        Point value;
        if(result.isComplex())
            value = new Point("x", result.getComplexValue());
        else
            value = new Point("x", result.getValue());

        result = Parser.eval(function, value);
        if(result.isComplex())
            resultFunction.setText(Round.rint(result.getComplexValue().getR(), 8) + " + " + result.getComplexValue().getI() + "i");
        else
            resultFunction.setText(Double.toString(Round.rint(result.getValue(), 8)));
    }
}
