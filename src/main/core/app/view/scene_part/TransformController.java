package core.app.view.scene_part;

import core.app.view.scene_components.FunctionComboBoxController;
import javafx.fxml.FXML;

public class TransformController {
    @FXML
    private FunctionComboBoxController functionComboTransformController;

    @FXML
    private void action() {
        String functionChoice = functionComboTransformController.getFunction();
    }
}
