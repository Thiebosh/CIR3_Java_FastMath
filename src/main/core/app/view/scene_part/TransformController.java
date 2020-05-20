package core.app.view.scene_part;

import core.app.view.scene_components.FunctionChoiceBoxController;
import javafx.fxml.FXML;

public class TransformController {
    @FXML
    private FunctionChoiceBoxController functionChoiceTransformController;

    @FXML
    private void action() {
        String functionChoice = (String) functionChoiceTransformController.getValue();
    }
}
