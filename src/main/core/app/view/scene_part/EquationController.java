package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.equation.EquationX;
import core.services.mathLibrary.exception.CalculatorException;
import core.services.mathLibrary.util.Round;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class EquationController {
    @FXML
    private FunctionComboBoxController functionComboEquation1Controller;

    @FXML
    private FunctionComboBoxController functionComboEquation2Controller;

    @FXML
    private Label resultEquation;

    public void executeEquation() {
        String choiceA = functionComboEquation1Controller.getFunction();
        String choiceB = functionComboEquation2Controller.getFunction();

        EquationX eq = new EquationX((choiceA + "=" + choiceB));

        try {
            resultEquation.setText(Double.toString(Round.rint(eq.getRoot(0,0.1e-10), 6)));
        } catch (CalculatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
