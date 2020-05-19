package core.app.view.scene_part;

import core.app.view.scene_components.FunctionChoiceBoxController;
import javafx.fxml.FXML;

public class TransformController {
    private static String functionChoice;

    public static void setFunctionChoice(final String choice) { functionChoice = choice; }

    @FXML
    private void action() {
        FunctionChoiceBoxController.requireFunctionChoiceUpdate();

    }
}
