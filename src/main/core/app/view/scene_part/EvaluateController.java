package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionChoiceBoxEvalController;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.Point;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EvaluateController {
    @FXML
    private TextField valueFunction;

    @FXML
    private Label resultFunction;

    private static String functionChoice;

    public static void setFunctionChoice(final String choice) { functionChoice = choice; }

    @FXML
    private void executeEvaluation() {
        FunctionChoiceBoxEvalController.requireFunctionChoiceUpdate();

        if (ExpressManager.containsExpress(functionChoice)) {
            String function = ExpressManager.getExpress(functionChoice).getFunction();
            Point value = new Point("x", Double.parseDouble(valueFunction.getCharacters().toString()));

            resultFunction.setText(Double.toString(Parser.eval(function, value).getValue()));
        }
    }
}
