package core.app.view.scene_part;

import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.Point;
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
        String function = functionComboEvaluateController.getFunction();
        Point value = new Point("x", Double.parseDouble(valueFunction.getCharacters().toString()));

        resultFunction.setText(Double.toString(Parser.eval(function, value).getValue()));
    }
}
