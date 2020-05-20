package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.Point;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EvaluateController {
    @FXML
    private FunctionComboBoxController functionComboEvaluateController;

    @FXML
    private TextField valueFunction;

    @FXML
    private Label resultFunction;

    @FXML
    private void executeEvaluation() {
        String function = functionComboEvaluateController.getFunction();
        Point value = new Point("x", Double.parseDouble(valueFunction.getCharacters().toString()));

        resultFunction.setText(Double.toString(Parser.eval(function, value).getValue()));
    }
}
