package core.app.view.scene_part;

import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.derivative.DerivativeX;
import core.services.mathLibrary.function.FunctionX;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.util.Round;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class TransformController {
    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboTransformController;

    /**
     * Element du fxml : textfield contenant la valeur d'évaluation
     */
    @FXML
    private TextField valueFunction;

    @FXML
    private Spinner<Integer> degreeSpinner;

    @FXML
    private Label resultFunction;

    /**
     * onAction du fxml : effectue la transformation et affiche le résultat
     */
    @FXML
    private void action() {
        String functionChoice = functionComboTransformController.getFunction();
        Double xo = Parser.eval(valueFunction.getText().replaceAll(FunctionX.getPI(), String.valueOf(Math.PI))).getValue();

        DerivativeX der = new DerivativeX(functionChoice);

        try {
            switch(degreeSpinner.getValue())
            {
                case 1: resultFunction.setText(String.valueOf(Round.rint(der.getDerivative_xo_accurate(xo), 8)));
                break;
                case 2: resultFunction.setText(String.valueOf(Round.rint(der.getDerivativeOrderTwo_xo_accurate(xo), 8)));
                break;
                case 3: resultFunction.setText(String.valueOf(Round.rint(der.getDerivativeOrderThree_xo_accurate(xo), 8)));
                break;
                default: resultFunction.setText(String.valueOf(Round.rint(der.getDerivativeOrderFour_xo_accurate(xo), 8)));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
