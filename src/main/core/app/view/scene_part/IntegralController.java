package core.app.view.scene_part;

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

public class IntegralController implements Initializable {
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
    private TextField intervalA, intervalB;

    @FXML
    private ChoiceBox<String> method;

    @FXML
    private Label resultFunction;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        method.setItems(FXCollections.observableArrayList("Trapèzes", "Simpson", "Romberg"));
    }
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
