package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionChoiceBoxController;
import core.services.mathLibrary.equation.EquationX;
import core.services.mathLibrary.exception.CalculatorException;
import core.services.mathLibrary.util.Round;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EquationController implements Initializable {
    @FXML
    private FunctionChoiceBoxController functionChoiceEquationController;

    @FXML
    private ComboBox functionComboBox;

    @FXML
    private Label resultEquation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComboBox.getItems().addAll(ExpressManager.getExpressNames());
    }

    public void executeEquation() {
        String choiceA = (String) functionChoiceEquationController.getValue();
        String choiceB = (String) functionComboBox.getValue();

        if (ExpressManager.containsExpress(choiceA)) choiceA = ExpressManager.getExpress(choiceA).getFunction();
        if (ExpressManager.containsExpress(choiceB)) choiceB = ExpressManager.getExpress(choiceB).getFunction();

        EquationX eq = new EquationX((choiceA + "=" + choiceB));

        try {
            resultEquation.setText(Double.toString(Round.rint(eq.getRoot(0,0.1e-10), 6)));
        } catch (CalculatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
